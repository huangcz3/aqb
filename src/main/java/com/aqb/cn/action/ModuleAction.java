package com.aqb.cn.action;

import com.aqb.cn.bean.Module;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.service.ModuleService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Administrator on 2017/3/11.
 */
@Controller
public class ModuleAction {

    private final Log logger = LogFactory.getLog(ModuleAction.class);

    @Autowired
    private ModuleService moduleService;

    /**
     * 新增模块
     * @param module 模块信息
     * @param httpSession
     * @return state : 0 -- 成功, 6--模块已存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/module/addModule", method = RequestMethod.POST)
    public Object addModule(HttpServletRequest request,@RequestBody Module module, HttpSession httpSession) {
        int status;
        String message = "";
        if(module.getModuleCode() == null || module.getModuleCode() == ""){
            return new Response(Status.ERROR, "不能为空");
        }

        status = moduleService.add(module);
        if (status == Status.SUCCESS) {
            message = "添加模块成功";
            return new Response(status, message, module.getId());
        }if(status == Status.EXISTS){
            message = "模块已经存在";
            return new Response(status, message);
        }
        message = "添加模块失败";
        return new Response(status, message);
    }


    /**
     * 删除模块
     * @param request
     * @param module
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/module/deleteModule", method = RequestMethod.POST)
    public Object deleteModule(HttpServletRequest request, @RequestBody Module module) {
        int status;
        String message = "";
        if(module.getId() == null || module.getId().equals("")){
            return new Response(Status.ERROR, "id不能为空");
        }
        status = moduleService.delete(module);
        if(status == Status.NOT_EXISTS){
            message = "模块不存在";
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
     * 修改模块
     * @param request
     * @param module module.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 模块不存在, 1 -- 修改失败
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/module/updateModule", method = RequestMethod.POST)
    public Object updateModule(HttpServletRequest request, @RequestBody Module module) {
        int status = Status.ERROR;
        String message = "";
        if(module.getId() == null){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = moduleService.update(module);
        if(status == Status.SUCCESS){
            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "模块不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 模块列表查询
     *
     * @param request
     * @return
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/module/queryModulePri", method = RequestMethod.GET)
    public Object queryModulePri(HttpServletRequest request) {
        return new Response(Status.SUCCESS);
    }
    
}
