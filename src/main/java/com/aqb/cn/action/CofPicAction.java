package com.aqb.cn.action;

import com.aqb.cn.bean.CofPic;
import com.aqb.cn.bean.User;
import com.aqb.cn.common.*;
import com.aqb.cn.service.CofPicService;
import com.aqb.cn.utils.Request;
import com.aqb.cn.utils.UUIDCreator;
import com.aqb.cn.utils.getSession.MySessionContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/27.
 */
@Controller
public class CofPicAction {
    private final Log logger = LogFactory.getLog(CofPicAction.class);

    @Autowired
    private CofPicService cofPicService;

    /**
     * 新增图片
     *
     * @param req 图片信息
     * @return state : 0 -- 成功, 6--图片已存在
     */
    @ResponseBody
    @RequestMapping(value = "/api/cofPic/addcofPic", method = RequestMethod.POST)
    public Object addcofPic(HttpServletRequest request, @RequestBody Request req) {
        int status;
        String message = "";

        //判断用户的session是否正常
        //获取session
        if (req.getSessionId() == null || req.getSessionId().equals("")) {
            return new Response(Status.ERROR, "sessionId不能为空");
        }
        MySessionContext myc = MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(req.getSessionId());
        if (httpSession == null) {
            return new Response(14, "sessionId失效");
        }
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        User obj = ActionUtil.getCurrentUser(httpSession);
        if (obj == null) {
            return new Response(14, "sessionId失效");
        }


        //从Request中获取图片参数
        CofPic cofPic = req.getCofPic();
        cofPic.setId(UUIDCreator.getUUID());
        cofPic.setFoundDate(new Date());
        String[] strings = req.getStrings();
        for (String str:strings) {
            cofPic.setCofPicAddress(str);
            cofPic.setCofPicSaddress(str + "s");
        }
        if (cofPic.getCofId() == null || cofPic.getCofId().equals("")){
            return new Response(Status.ERROR,"cofId不能为空");
        }
        status = cofPicService.add(cofPic);
        if (status == Status.SUCCESS) {
            message = "添加图片成功";
            return new Response(status, message, cofPic.getId());
        }
        if (status == Status.EXISTS) {
            message = "图片已经存在";
            return new Response(status, message);
        }
        message = "添加图片失败";
        return new Response(status, message);
    }
    /**
     * 上传系列轮播图
     * @param request
     * @param multipartFiles 文件
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/cofPic/uploadPackCofPic", method = RequestMethod.POST)
    public Object uploadPackCofPic(HttpServletRequest request,@RequestParam("file") MultipartFile[] multipartFiles) {
        if(multipartFiles != null && multipartFiles.length > 0){
            String[] string1 = new String[multipartFiles.length];
            for (int i = 0; i <multipartFiles.length; i++) {
                //保存文件
                String fileName = multipartFiles[i].getOriginalFilename(),
                        headUUID = UUIDCreator.getUUID(),
                        saveName = headUUID + "." + FileUtil.getExtensions(fileName),
                        //                        address = PicAction.PICTURE_UPLOAD_PATH + File.separator + saveName,
                        //                    path = ShouyeLunboAction.HOME_shouyelunbotu + saveName;
                        path = "E:/" + saveName;

                File file = new File(path);
                try {
                    if(!file.exists()){
                        file.createNewFile();
                    }
                    multipartFiles[i].transferTo(file);

                    //产生缩略图
                    //                ImageUtil r = new ImageUtil();
                    //                String filepath = IllustrationUrlAction.HOME_headPhoto;
                    //                String filename = saveName;
                    //                BufferedImage im = r.getImage(filepath+filename);
                    //                r.writeHighQuality(r.zoomImage(im), filepath +"s_"+ filename);//为防止覆盖原图片,加s_区分是压缩以后的图片

                    String URL = "" + saveName;
                    string1[i] = URL;

                }catch (IOException e){
                    logger.info("存储文件失败");
                    return new Response(Status.ERROR,"存储文件时出现异常");
                }
            }
            //上传图片成功，返回URL
            return new Response(Status.SUCCESS,"上传成功",string1);
        }
        return new Response(Status.ERROR,"文件不能为空");
    }



    /**
     * 删除图片
     *
     * @param request
     * @param req
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
    @ResponseBody
    @RequestMapping(value = "/api/cofPic/deletecofPic", method = RequestMethod.POST)
    public Object deletecofPic(HttpServletRequest request, @RequestBody Request req) {
        int status;
        String message = "";
        if (req.getSessionId() == null || req.getSessionId().equals("")) {
            return new Response(Status.ERROR, "sessionId不能为空");
        }
        MySessionContext myc = MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(req.getSessionId());
        if (httpSession == null) {
            return new Response(14, "sessionId失效");
        }
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        User obj = ActionUtil.getCurrentUser(httpSession);
        if (obj == null) {
            return new Response(14, "sessionId失效");
        }
        CofPic cofPic = req.getCofPic();

        if (cofPic.getId() == null || cofPic.getId().equals("")) {
            return new Response(Status.ERROR, "id不能为空");
        }
        status = cofPicService.delete(cofPic);
        if (status == Status.NOT_EXISTS) {
            message = "图片不存在";
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
     * 修改图片
     *
     * @param request
     * @param req     cofPic.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 图片不存在, 1 -- 修改失败
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/cofPic/updatecofPic", method = RequestMethod.POST)
    public Object updatecofPic(HttpServletRequest request, @RequestBody Request req) {
        int status = Status.ERROR;
        String message = "";
        if (req.getSessionId() == null || req.getSessionId().equals("")) {
            return new Response(Status.ERROR, "sessionId不能为空");
        }
        MySessionContext myc = MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(req.getSessionId());
        if (httpSession == null) {
            return new Response(14, "sessionId失效");
        }
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        User obj = ActionUtil.getCurrentUser(httpSession);
        if (obj == null) {
            return new Response(14, "sessionId失效");
        }
        CofPic cofPic = req.getCofPic();
        cofPic.setCofId(null);
        cofPic.setId(null);

        if (cofPic.getId() == null) {
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = cofPicService.update(cofPic);
        if (status == Status.SUCCESS) {
            message = "修改成功";
        } else if (status == Status.NOT_EXISTS) {
            message = "图片不存在，修改失败";
        } else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 图片列表查询
     *
     * @param request
     * @return
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/cofPic/querycofPic", method = RequestMethod.GET)
    public Object querycofPic(HttpServletRequest request,
                                   @RequestParam(value = "currentPage", required = false) Long currentPage,
                                   @RequestParam(value = "maxRow", required = false) Long maxRow,
                                   @RequestParam("sessionId") String sessionId) {

        //判断用户的session是否正常
        //获取session
        if (sessionId == null || sessionId.equals("")) {
            return new Response(Status.ERROR, "sessionId不能为空");
        }
        MySessionContext myc = MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(sessionId);
        if (httpSession == null) {
            return new Response(14, "sessionId失效");
        }
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        User obj = ActionUtil.getCurrentUser(httpSession);
        if (obj == null) {
            return new Response(14, "sessionId失效");
        }

        QueryBase query = new QueryBase();
        if (currentPage != null) {
            query.setCurrentPage(currentPage);
        }
        if (maxRow != null) {
            query.setMaxRow(maxRow);
        }
        cofPicService.query(query);

        return new Response(Status.SUCCESS, query.getTotalRow(), query.getTotalPage(), query.getResults(), query.getCurrentPage());
    }
}
