package com.aqb.cn.action;

import com.aqb.cn.annotation.AdminLogin;
import com.aqb.cn.bean.Divided;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.service.DividedService;
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

/**
 * Created by Administrator on 2017/5/11.
 */
@Controller
public class DividedAction {

    private final Log logger = LogFactory.getLog(DividedAction.class);

    @Autowired
    private DividedService dividedService;

    /**
     * 新增车主分成比例
     * @param divided 车主分成比例信息
     * @param httpSession
     * @return state : 0 -- 成功, 6--车主分成比例已存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/divided/adddivided", method = RequestMethod.POST)
    public Object adddivided(HttpServletRequest request,@RequestBody Divided divided, HttpSession httpSession) {
        int status;
        String message = "";
//        if(divided.getdividedCode() == null || divided.getdividedCode() == ""){
//            return new Response(Status.ERROR, "不能为空");
//        }

        status = dividedService.add(divided);
        if (status == Status.SUCCESS) {
            message = "添加车主分成比例成功";
            return new Response(status, message, divided.getId());
        }if(status == Status.EXISTS){
            message = "车主分成比例已经存在";
            return new Response(status, message);
        }
        message = "添加车主分成比例失败";
        return new Response(status, message);
    }


    /**
     * 删除车主分成比例
     * @param request
     * @param divided
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/divided/deletedivided", method = RequestMethod.POST)
    public Object deletedivided(HttpServletRequest request, @RequestBody Divided divided) {
        int status;
        String message = "";
        if(divided.getId() == null || divided.getId().equals("")){
            return new Response(Status.ERROR, "id不能为空");
        }
        status = dividedService.delete(divided);
        if(status == Status.NOT_EXISTS){
            message = "车主分成比例不存在";
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
     * 修改车主分成比例
     * @param request
     * @param divided divided.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 车主分成比例不存在, 1 -- 修改失败
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/divided/updateDivided", method = RequestMethod.POST)
    public Object updateDivided(HttpServletRequest request, @RequestBody Divided divided) {
        int status = Status.ERROR;
        String message = ""; 
        status = dividedService.update(divided);
        if(status == Status.SUCCESS){
            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "车主分成比例不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 车主分成比例列表查询
     *
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/divided/queryDivided", method = RequestMethod.GET)
    public Object queryDivided(HttpServletRequest request) {
        Divided divided = dividedService.queryDivided();
        return new Response(Status.SUCCESS,divided);
    }


}
