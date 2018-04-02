package com.aqb.cn.action;

import com.aqb.cn.annotation.AdminLogin;
import com.aqb.cn.bean.AdverPlatform;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.service.AdverPlatformService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/28.
 */
@Controller
public class AdverPlatformAction {

    private final Log logger = LogFactory.getLog(AdverPlatformAction.class);

    @Autowired
    private AdverPlatformService adverPlatformService;

    /**
     * 新增公益广告
     * @param adverPlatform 公益广告信息
     * @param httpSession
     * @return state : 0 -- 成功, 6--公益广告已存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/adverPlatform/addadverPlatform", method = RequestMethod.POST)
    public Object addadverPlatform(HttpServletRequest request,@RequestBody AdverPlatform adverPlatform, HttpSession httpSession) {
        int status;
        String message = "";
//        if(adverPlatform.getadverPlatformCode() == null || adverPlatform.getadverPlatformCode() == ""){
//            return new Response(Status.ERROR, "不能为空");
//        }

        status = adverPlatformService.add(adverPlatform);
        if (status == Status.SUCCESS) {
            message = "添加公益广告成功";
            return new Response(status, message, adverPlatform.getId());
        }if(status == Status.EXISTS){
            message = "公益广告已经存在";
            return new Response(status, message);
        }
        message = "添加公益广告失败";
        return new Response(status, message);
    }


    /**
     * 删除公益广告
     * @param request
     * @param adverPlatform
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/adverPlatform/deleteadverPlatform", method = RequestMethod.POST)
    public Object deleteadverPlatform(HttpServletRequest request, @RequestBody AdverPlatform adverPlatform) {
        int status;
        String message = "";
        if(adverPlatform.getId() == null || adverPlatform.getId().equals("")){
            return new Response(Status.ERROR, "id不能为空");
        }
        status = adverPlatformService.delete(adverPlatform);
        if(status == Status.NOT_EXISTS){
            message = "公益广告不存在";
        }
        if(status == Status.ERROR){
            message = "删除失败";
        }
        if(status == Status.SUCCESS){
            message = "删除成功";
        }
        return new Response(status,message);
    }

    /**
     * 修改公益广告
     * @param request
     * @param adverPlatform adverPlatform.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 公益广告不存在, 1 -- 修改失败
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/adverPlatform/updateAdverPlatform", method = RequestMethod.POST)
    public Object updateAdverPlatform(HttpServletRequest request, @RequestBody AdverPlatform adverPlatform) {
        int status = Status.ERROR;
        String message = "";
        if(adverPlatform.getId() == null){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        adverPlatform.setFoundDate(new Date());
        status = adverPlatformService.update(adverPlatform);
        if(status == Status.SUCCESS){
            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "公益广告不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 公益广告列表查询
     *
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/adverPlatform/queryAdverPlatform", method = RequestMethod.GET)
    public Object queryAdverPlatform(HttpServletRequest request) {
        AdverPlatform adverPlatform = adverPlatformService.get("ASHGVDUGYSADQWUG1287EGUAGDYGASDA");
        return new Response(Status.SUCCESS,adverPlatform);
    }

}
