package com.aqb.cn.action;

import com.aqb.cn.annotation.AdminLogin;
import com.aqb.cn.bean.Parameter;
import com.aqb.cn.bean.PopAdv;
import com.aqb.cn.common.FileUtil;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.pojo.PopAndPara;
import com.aqb.cn.service.ParameterService;
import com.aqb.cn.service.PopAdvService;
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

/**
 * Created by Administrator on 2017/8/4/0004.
 */
@Controller
public class PopAdvAction {

    private final Log logger = LogFactory.getLog(PopAdvAction.class);

    @Autowired
    private PopAdvService popAdvService;
    @Autowired
    private ParameterService parameterService;

    /**
     * 添加弹窗广告（后台管理）
     *
     * @param multipartFile 文件
     * @param webPage       网页链接
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/popAdv/addPopAdv", method = RequestMethod.POST)
    public Object addPopAdv(HttpServletRequest request,
                            @RequestParam("file") MultipartFile multipartFile,
                            @RequestParam("webPage") String webPage) {
        PopAdv popAdv = new PopAdv();
        popAdv.setId(UUIDCreator.getUUID());
        popAdv.setWebpage(webPage);

        if (null == multipartFile) {
            return new Response(Status.ERROR, "请上传图片");
        }
        //保存文件
        String fileName = multipartFile.getOriginalFilename(),
                headUUID = UUIDCreator.getUUID(),
                saveName = headUUID + "." + FileUtil.getExtensions(fileName),
                //path = "/home/java/aqb/other/" + saveName;
                path = "E:/test/" + saveName;
        //判断图片格式
        if (!FileUtil.isPicture(fileName)){
            return new Response(Status.ERROR, "图片格式有误，请上传bmp、gif、jpe、jpeg、jpg、png格式");
        }
        logger.debug(path);
        System.out.println(path);
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            multipartFile.transferTo(file);

            String URL = "http://47.94.45.159:8080/aqb/other/" + saveName;
            //添加到数据库
            popAdv.setPicStr(URL);
            popAdv.setFoundDate(new Date());
            popAdvService.add(popAdv);
            //上传图片成功，返回URL
            return new Response(Status.SUCCESS, "添加成功", popAdv);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("存储文件失败");
            return new Response(Status.ERROR, "存储文件时出现异常");
        }
    }

    /**
     * 修改弹窗广告
     *
     * @param multipartFile
     * @param
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 弹窗广告设置不存在, 1 -- 修改失败
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/popAdv/updatepopAdv", method = RequestMethod.POST)
    public Object updatepopAdv(HttpServletRequest request,
                               @RequestParam("id") String id,
                               @RequestParam(value = "file", required = false) MultipartFile multipartFile,
                               @RequestParam(value = "webPage",required = false) String webPage,
                               @RequestParam(value = "status",required = false) String status) {
        //System.out.println(multipartFile.getOriginalFilename());
        System.out.println("**********************************");
        System.out.println(id);
        int result = Status.ERROR;
        String message = "";
        PopAdv popAdv = new PopAdv();
        popAdv.setId(id);
        popAdv.setWebpage(webPage);
        popAdv.setStatus(Integer.valueOf(status));


        if (popAdv.getId() == null) {
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        if (null != multipartFile) {
            //保存文件
            System.out.println(multipartFile.getOriginalFilename());
            String fileName = multipartFile.getOriginalFilename(),
                    headUUID = UUIDCreator.getUUID(),
                    saveName = headUUID + "." + FileUtil.getExtensions(fileName),
                    //path = "/home/java/aqb/other/" + saveName;
                    path = "E:/test/" + saveName;
            //判断图片格式
            if (!FileUtil.isPicture(fileName)){
                return new Response(Status.ERROR, "图片格式有误，请上传bmp、gif、jpe、jpeg、jpg、png格式");
            }
            File file = new File(path);
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                multipartFile.transferTo(file);
                String URL = "http://47.94.45.159:8080/aqb/other/" + saveName;
                popAdv.setPicStr(URL);
            } catch (IOException e) {
                logger.info("存储文件失败");
                return new Response(Status.ERROR, "存储文件时出现异常");
            }
            PopAdv popAdv_db = popAdvService.get(popAdv.getId());
            if (popAdv_db != null) {
                String oldPic = popAdv_db.getPicStr();
                if (oldPic != null) {
                    FileUtil.deleteFile(oldPic);
                }
            }
        }
        result = popAdvService.update(popAdv);
        if (result == Status.SUCCESS) {
            message = "修改成功";
        } else if (result == Status.NOT_EXISTS) {
            message = "弹窗广告设置不存在，修改失败";
        } else {
            message = "修改失败";
        }
        return new Response(result, message);
    }


    /**
     * 开启/关闭 弹窗广告
     * 后台
     * @param parameter 0-关闭弹窗，1-开启弹窗
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 弹窗广告设置不存在, 1 -- 修改失败
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/parameter/popOnOff", method = RequestMethod.POST)
    public Object popOnOff(HttpServletRequest request, @RequestBody Parameter parameter) {
        int status = Status.ERROR;
        String message = "";
        //Integer s = parameter.getStatus();//0-关闭弹窗，1-开启弹窗
        if (parameter.getId() == null) {
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = parameterService.update(parameter);
        if (status == Status.SUCCESS) {
            message = "修改成功";
        } else if (status == Status.NOT_EXISTS) {
            message = "弹窗广告设置不存在，修改失败";
        } else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 弹窗广告列表查询
     * 后台
     * @param
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/popAdv/querypopAdv", method = RequestMethod.GET)
    public Object querypopAdv() {
        PopAndPara popAndPara = popAdvService.queryPopAdv();
        return new Response(Status.SUCCESS, popAndPara);
    }

    /**
     * 弹窗广告列表查询
     * app端
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/popAdv/appPopAdv", method = RequestMethod.GET)
    public Object appPopAdv() {
        PopAndPara popAndPara = popAdvService.queryappPopAdv();
        return new Response(Status.SUCCESS, popAndPara);
    }


//    /**
//     * 新增弹窗广告
//     * 后台
//     *
//     * @param popAdv 弹窗广告设置信息
//     * @param
//     * @return state : 0 -- 成功, 6--弹窗广告设置已存在
//     */
//    @ResponseBody
//    @RequestMapping(value = "/api/popAdv/addpopAdv", method = RequestMethod.POST)
//    public Object addpopAdv(HttpServletRequest request, @RequestBody PopAdv popAdv) {
//        int status;
//        String message = "";
//
//        if (popAdv.getPicStr() == null || popAdv.getPicStr().equals("")) {
//            return new Response(Status.ERROR, "图片为空");
//        }
//        if (popAdv.getWebpage() == null || popAdv.getWebpage().equals("")) {
//            return new Response(Status.ERROR, "网页链接为空");
//        }
//        popAdv.setId(UUIDCreator.getUUID());
//        popAdv.setFoundDate(new Date());
//        status = popAdvService.add(popAdv);
//        if (status == Status.SUCCESS) {
//            message = "添加弹窗广告设置成功";
//            return new Response(status, message, popAdv.getId());
//        }
//        if (status == Status.EXISTS) {
//            message = "弹窗广告设置已经存在";
//            return new Response(status, message);
//        }
//        message = "添加弹窗广告设置失败";
//        return new Response(status, message);
//    }


//    /**
//     * 删除弹窗广告设置
//     *
//     * @param request
//     * @param popAdv
//     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
//     */
////    @ResponseBody
////    @RequestMapping(value = "/api/popAdv/deletepopAdv", method = RequestMethod.POST)
//    public Object deletepopAdv(HttpServletRequest request, @RequestBody PopAdv popAdv) {
//        int status;
//        String message = "";
//        if (popAdv.getId() == null || popAdv.getId().equals("")) {
//            return new Response(Status.ERROR, "id不能为空");
//        }
//        status = popAdvService.delete(popAdv);
//        if (status == Status.NOT_EXISTS) {
//            message = "弹窗广告设置不存在";
//        }
//        if (status == Status.ERROR) {
//            message = "删除失败";
//        }
//        if (status == Status.SUCCESS) {
//            message = "删除成功";
//        }
//        return new Response(status, message);
//    }


}
