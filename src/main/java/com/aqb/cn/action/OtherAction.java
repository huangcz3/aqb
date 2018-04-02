package com.aqb.cn.action;

import com.aqb.cn.bean.Other;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.service.OtherService;
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
import java.util.List;

/**
 * Created by Administrator on 2017/5/11.
 */
@Controller
public class OtherAction {

    private final Log logger = LogFactory.getLog(OtherAction.class);

    @Autowired
    private OtherService otherService;

    /**
     * 新增其他设置
     * @param other 其他设置信息
     * @return state : 0 -- 成功, 6--其他设置已存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/other/addOther", method = RequestMethod.POST)
    public Object addOther(HttpServletRequest request,@RequestBody Other other) {
        int status;
        String message = "";
//        if(other.getotherCode() == null || other.getotherCode() == ""){
//            return new Response(Status.ERROR, "不能为空");
//        }

        status = otherService.add(other);
        if (status == Status.SUCCESS) {
            message = "添加其他设置成功";
            return new Response(status, message, other.getId());
        }if(status == Status.EXISTS){
            message = "其他设置已经存在";
            return new Response(status, message);
        }
        message = "添加其他设置失败";
        return new Response(status, message);
    }


    /**
     * 删除其他设置
     * @param request
     * @param other
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/other/deleteother", method = RequestMethod.POST)
    public Object deleteother(HttpServletRequest request, @RequestBody Other other) {
        int status;
        String message = "";
        if(other.getId() == null || other.getId().equals("")){
            return new Response(Status.ERROR, "id不能为空");
        }
        status = otherService.delete(other);
        if(status == Status.NOT_EXISTS){
            message = "其他设置不存在";
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
     * 修改其他设置
     * @param request
     * @param other other.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 其他设置不存在, 1 -- 修改失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/other/updateOther", method = RequestMethod.POST)
    public Object updateOther(HttpServletRequest request, @RequestBody Other other) {
        int status = Status.ERROR;
        String message = "";
        if(other.getId() == null){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = otherService.update(other);
        if(status == Status.SUCCESS){
            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "其他设置不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 其他设置列表查询
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/other/queryOther", method = RequestMethod.GET)
    public Object queryOther(HttpServletRequest request) {
        List<Other> others = otherService.queryOther();
        return new Response(Status.SUCCESS,others);
    }


}
