package com.aqb.cn.action;

import com.aqb.cn.annotation.AdminLogin;
import com.aqb.cn.bean.Repair;
import com.aqb.cn.bean.RepairComment;
import com.aqb.cn.common.FileUtil;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.service.RepairCommentService;
import com.aqb.cn.service.RepairService;
import com.aqb.cn.utils.UUIDCreator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 修车
 * Created by Administrator on 2017/6/28.
 */
@Controller
public class RepairAction {

    private final Log logger = LogFactory.getLog(RepairAction.class);

    @Autowired
    private RepairService repairService;
    @Autowired
    private RepairCommentService repairCommentService;


//    /**
//     * 新增修车圈
//     *
//     * @param req 修车圈信息
//     * @return state : 0 -- 成功, 6--修车圈已存在
//     */
//    @ResponseBody
//    @RequestMapping(value = "/api/repair/addrepair", method = RequestMethod.POST)
//    public Object addrepair(HttpServletRequest request, @RequestBody Request req) {
//        int status;
//        String message = "";
//
//        //判断用户的session是否正常
//        //获取session
//        if (req.getSessionId() == null || req.getSessionId().equals("")) {
//            return new Response(Status.ERROR, "sessionId不能为空");
//        }
//        MySessionContext myc = MySessionContext.getInstance();
//        HttpSession httpSession = myc.getSession(req.getSessionId());
//        if (httpSession == null) {
//            return new Response(14, "sessionId失效");
//        }
//        //登录时，用户信息已经保存在session中，在session中取出用户信息
//        User obj = ActionUtil.getCurrentUser(httpSession);
//        if (obj == null) {
//            return new Response(14, "sessionId失效");
//        }
//
//
//        //从Request中获取修车圈参数
//        Repair repair = req.getRepair();
//        repair.setFoundDate(new Date());
//
//        status = repairService.add(repair);
//        if (status == Status.SUCCESS) {
//            message = "添加修车圈成功";
//            return new Response(status, message, repair.getId());
//        }
//        if (status == Status.EXISTS) {
//            message = "修车圈已经存在";
//            return new Response(status, message);
//        }
//        message = "添加修车圈失败";
//        return new Response(status, message);
//    }





    /**
     * 修车圈列表查询
     *
     *  status //状态表示查询的类型 1-查询全部，2-查询修车，3-查询保养，4-查询洗车
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/repair/queryrepair", method = RequestMethod.GET)
    public Object queryrepair(HttpServletRequest request,
                           @RequestParam(value = "currentPage", required = false) Long currentPage,
                           @RequestParam(value = "maxRow", required = false) Long maxRow,
                              @RequestParam("status")Integer status,
                              @RequestParam("longitude")Double longitude,
                              @RequestParam("latitude")Double latitude) {

        QueryBase query = new QueryBase();
        if (currentPage != null) {
            query.setCurrentPage(currentPage);
        }
        if (maxRow != null) {
            query.setMaxRow(maxRow);
        }
        Map map = query.getParameters();
        map.put("status", status);
        map.put("longitude",longitude);
        map.put("latitude",latitude);
        repairService.query(query);
        return new Response(Status.SUCCESS, query.getTotalRow(), query.getTotalPage(), query.getResults(), query.getCurrentPage());
    }



    /**
     * 上传或修改商家封面图
     *
     * @param id 商家id
     * @param multipartFile 商家封面图
     * @return result : 0 -- 成功, 6--商家已存在
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/repair/uploadCover", method = RequestMethod.POST)
    public Object uploadCover(HttpServletRequest request,
                               @RequestParam(value = "id",required = false) String id,
                               @RequestParam(value = "file",required = false) MultipartFile multipartFile) {

        int result = Status.ERROR;
        String message = "";
        String URL = "";
//        if (id==null||id.equals("")){
//            return new Response(result,"id不能为空");
//        }
        //上传封面图
        if (null != multipartFile) {
            //保存文件
            String fileName = multipartFile.getOriginalFilename(),
                    headUUID = UUIDCreator.getUUID(),
                    saveName = headUUID + "." + FileUtil.getExtensions(fileName),
                    path = "/home/java/aqb/shopCover/" + saveName;
                    //path = "E:/test/" + saveName;
            if (!FileUtil.isPicture(fileName)) {
                return new Response(Status.ERROR, "图片格式有误，请上传bmp、gif、jpe、jpeg、jpg、png格式");
            }
            logger.debug(path);
            File file = new File(path);
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                multipartFile.transferTo(file);
                URL = "http://47.94.45.159:8080/aqb/shopCover/" + saveName;
                //添加到数据库
                //删除旧的封面图
                if (id != null){
                    Repair repair_db = repairService.get(id);
                    if (repair_db != null){
                        String oldCover = repair_db.getRepairCover();
                        if (oldCover != null && oldCover.equals("")){
                            FileUtil.deleteFile(oldCover);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.info("存储文件失败");
                return new Response(Status.ERROR, "存储文件时出现异常");
            }
        }
        return new Response(Status.SUCCESS,message,URL);
    }

    /**
     * 新增商家
     *
     * @param repair 商家
     * @return result : 0 -- 成功, 6--商家已存在
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/repair/addseller", method = RequestMethod.POST)
    public Object addseller(HttpServletRequest request, @RequestBody Repair repair) {
        int result = Status.ERROR;
        String message = "";
        //参数放入repair
        repair.setId(UUIDCreator.getUUID());
        repair.setFoundDate(new Date());
        result = repairService.add(repair);
        if (result == Status.SUCCESS) {
            message = "添加商家成功";
            return new Response(result, message, repair.getId());
        }
        if (result == Status.EXISTS) {
            message = "商家已经存在";
            return new Response(result, message);
        }
        message = "添加商家失败";
        return new Response(result, message);
    }

    /**
     * 删除商家 后台
     * 还需要删除该商家下的所有的评论
     * @param request
     * @param repair
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/repair/deleteseller", method = RequestMethod.POST)
    public Object deleteseller(HttpServletRequest request, @RequestBody Repair repair) {
        int status;
        String message = "";
        if (repair.getId() == null || repair.getId().equals("")) {
            return new Response(Status.ERROR, "id不能为空");
        }
        List<RepairComment> repairCommentList =  repairCommentService.queryByRepairId(repair.getId());
        for (RepairComment repairComment:repairCommentList){
            repairCommentService.delete(repairComment);
        }
        status = repairService.delete(repair);
        if (status == Status.NOT_EXISTS) {
            message = "修车圈不存在";
        }
        if (status == Status.ERROR) {
            message = "删除失败";
        }
        if (status == Status.SUCCESS) {
            message = "删除成功";
        }
        return new Response(status, message);
    }


    /**
     * 修改商家
     *
     * @param repair 商家
     * @return result : 0 -- 成功, 6--商家已存在
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/repair/updateseller", method = RequestMethod.POST)
    public Object updateseller(HttpServletRequest request,@RequestBody Repair repair) {

        int status = Status.ERROR;
        String message = "";
        if (repair.getId() ==null||repair.getId().equals("")){
            return new Response(status,"id不能为空");
        }
        status = repairService.update(repair);
        if (status == Status.SUCCESS) {
            message = "修改商家成功";
            return new Response(status, message, repair.getId());
        }
        if (status == Status.NOT_EXISTS) {
            message = "商家不存在";
            return new Response(status, message);
        }
        message = "修改商家失败";
        return new Response(status, message);
    }


    /**
     * 商家列表
     *
     *  status //状态表示查询的类型 1-查询全部，2-查询修车，3-查询保养，4-查询洗车
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/repair/queryShop", method = RequestMethod.GET)
    public Object queryShop(HttpServletRequest request,
                              @RequestParam(value = "currentPage", required = false) Long currentPage,
                              @RequestParam(value = "maxRow", required = false) Long maxRow) {

        QueryBase query = new QueryBase();
        if (currentPage != null) {
            query.setCurrentPage(currentPage);
        }
        if (maxRow != null) {
            query.setMaxRow(maxRow);
        }

        repairService.queryShop(query);
        return new Response(Status.SUCCESS, query.getTotalRow(), query.getTotalPage(), query.getResults(), query.getCurrentPage());
    }

    /**
     * 指定商家详情
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/repair/queryById", method = RequestMethod.GET)
    public Object queryById(HttpServletRequest request, @RequestParam(value = "id") String id) {

        Repair repair = repairService.queryById(id);
        return new Response(Status.SUCCESS,repair);
    }




}
