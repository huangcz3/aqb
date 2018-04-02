package com.aqb.cn.action;

import com.aqb.cn.bean.Task;
import com.aqb.cn.bean.User;
import com.aqb.cn.common.ActionUtil;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.service.TaskService;
import com.aqb.cn.service.UserService;
import com.aqb.cn.utils.Request;
import com.aqb.cn.utils.UUIDCreator;
import com.aqb.cn.utils.getSession.MySessionContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/5.
 */
@Controller
public class TaskAction {

    private final Log logger = LogFactory.getLog(TaskAction.class);

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    /*
     * 新增用户任务
     *
     * @param req 用户任务信息
     * @return state : 0 -- 成功, 6--用户任务已存在
 */

    @ResponseBody
    @RequestMapping(value = "/api/task/addtask", method = RequestMethod.POST)
    public Object addtask(HttpServletRequest request, @RequestBody Request req) {
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


        //从Request中获取用户任务参数
        Task task = req.getTask();
        task.setUserId(obj.getId());
        task.setId(UUIDCreator.getUUID());
        task.setFoundDate(new Date());
        status = taskService.add(task);
        if (status == Status.SUCCESS) {
            message = "添加用户任务成功";
            return new Response(status, message, task.getId());
        }
        if (status == Status.EXISTS) {
            message = "用户任务已经存在";
            return new Response(status, message);
        }
        message = "添加用户任务失败";
        return new Response(status, message);
    }


/*
     * 删除用户任务
     *
     * @param request
     * @param task
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
*/

    @ResponseBody
    @RequestMapping(value = "/api/task/deletetask", method = RequestMethod.POST)
    public Object deletetask(HttpServletRequest request, @RequestBody Request req) {
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

        Task task = req.getTask();
        if (task.getId() == null || task.getId().equals("")) {
            return new Response(Status.ERROR, "id不能为空");
        }
//        if (task.getRepairId() == null || task.getRepairId().equals("")){
//            return new Response(Status.ERROR,"repairId不能为空");
//        }
        status = taskService.delete(task);
        if (status == Status.NOT_EXISTS) {
            message = "用户任务不存在";
        }
        if (status == Status.ERROR) {
            message = "删除失败";
        }
        if (status == Status.SUCCESS) {
            message = "删除成功";
        }
        return new Response(status, message);
    }

/*
     * 修改用户任务
     *
     * @param request
     * @param task     task.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 用户任务不存在, 1 -- 修改失败
*/

    //    @ResponseBody
//    @RequestMapping(value = "/api/task/updatetask", method = RequestMethod.POST)
    public Object updatetask(HttpServletRequest request, @RequestBody Task task) {
        int status = Status.ERROR;
        String message = "";
        if (task.getId() == null) {
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = taskService.update(task);
        if (status == Status.SUCCESS) {
            message = "修改成功";
        } else if (status == Status.NOT_EXISTS) {
            message = "用户任务不存在，修改失败";
        } else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

/*
     * 用户任务列表查询
     *
     * @param request
     * @return
*/

    @ResponseBody
    @RequestMapping(value = "/api/task/querytask", method = RequestMethod.GET)
    public Object querytask(HttpServletRequest request,
                               @RequestParam(value = "currentPage", required = false) Long currentPage,
                               @RequestParam(value = "maxRow", required = false) Long maxRow,
                               @RequestParam("sessionId")String sessionId) {
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
        taskService.query(query);

        return new Response(Status.SUCCESS, query.getTotalRow(), query.getTotalPage(), query.getResults(), query.getCurrentPage());
    }
}
