package com.aqb.cn.action;

import com.aqb.cn.annotation.AdminLogin;
import com.aqb.cn.bean.Admin;
import com.aqb.cn.bean.AdminModule;
import com.aqb.cn.common.ActionUtil;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.service.AdminModuleService;
import com.aqb.cn.service.AdminService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/10.
 */
@Controller
public class AdminAction {

    /**
     * 员工和Session绑定关系
     */
    public static final Map<String, HttpSession> ADMIN_SESSION = new HashMap<String, HttpSession>();

    /**
     * seeionId和员工的绑定关系
     */
    public static final Map<String, String> SESSIONID_ADMIN = new HashMap<String, String>();

    private final Log logger = LogFactory.getLog(AdminAction.class);

    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminModuleService adminModuleService;


    /**
     * 新增员工
     *
     * @param admin       员工信息
     * @param httpSession
     * @return state : 0 -- 成功, 6--员工已存在
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/admin/addAdmin", method = RequestMethod.POST)
    public Object addAdmin(HttpServletRequest request, @RequestBody Admin admin, HttpSession httpSession) {
        int status;
        String message = "";
        if (admin.getAdminName() == null || admin.getAdminName().equals("") ||
                admin.getAdminPass() == null || admin.getAdminPass().equals("") ||
                admin.getAdminNumber() == null || admin.getAdminNumber().equals("")
                ) {
            return new Response(Status.ERROR, "信息不完整");
        }

        status = adminService.add(admin);
        if (status == Status.SUCCESS) {
            message = "添加员工成功";
            return new Response(status, message, admin.getId());
        }
        if (status == Status.EXISTS) {
            message = "用户名或工号已经存在";
            return new Response(status, message);
        }
        message = "添加员工失败";
        return new Response(status, message);
    }


    /**
     * 删除员工
     *
     * @param request
     * @param admin
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/admin/deleteAdmin", method = RequestMethod.POST)
    public Object deleteAdmin(HttpServletRequest request, @RequestBody Admin admin) {
        int status;
        String message = "";
        if (admin.getId() == null || admin.getId().equals("")) {
            return new Response(Status.ERROR, "id不能为空");
        }
        status = adminService.delete(admin);
        if (status == Status.NOT_EXISTS) {
            message = "员工不存在";
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
     * 修改员工
     *
     * @param request
     * @param admin   admin.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 员工不存在, 1 -- 修改失败
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/admin/updateadmin", method = RequestMethod.POST)
    public Object updateadmin(HttpServletRequest request, @RequestBody Admin admin) {
        int status = Status.ERROR;
        String message = "";
        if (admin.getId() == null) {
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = adminService.update(admin);
        if (status == Status.SUCCESS) {
            message = "修改成功";
        } else if (status == Status.NOT_EXISTS) {
            message = "员工不存在，修改失败";
        } else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 员工列表查询
     *
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/admin/queryAdmin", method = RequestMethod.GET)
    public Object queryAdmin(HttpServletRequest request) {
        QueryBase query = new QueryBase();
        query.setPageSize(10l);
        adminService.query(query);
        return new Response(Status.SUCCESS, query.getTotalRow(), query.getTotalPage(), query.getResults());
    }

    /**
     * 查询指定员工权限
     *
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/admin/queryAppointAdmin", method = RequestMethod.GET)
    public Object queryAppointAdmin(HttpServletRequest request, @RequestParam("id") String id) {
        List<AdminModule> adminModules = adminModuleService.queryByAdminId(id);
        return new Response(Status.SUCCESS, adminModules);
    }

    /**
     * 修改指定员工权限
     *
     * @param request
     * @param adminModules 需要包含id和status
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 员工不存在, 1 -- 修改失败
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/admin/updateAppointAdmin", method = RequestMethod.POST)
    public Object updateAppointAdmin(HttpServletRequest request, @RequestBody List<AdminModule> adminModules) {
        int status = Status.ERROR;
        String message = "";

        status = adminModuleService.updateAppointAdmin(adminModules);
        if (status == Status.SUCCESS) {
            message = "修改成功";
        } else {
            message = "修改失败";
        }
        return new Response(status, message);
    }


    /**
     * 员工端登录
     *
     * @param request
     * @param admin       员工
     * @param httpSession
     * @return status 0--成功, 10--验证码错误, 7 --客户不存在,9-- 密码不正确
     */
    @ResponseBody
    @RequestMapping(value = "/api/admin/login", method = RequestMethod.POST)
    public Object login(HttpServletRequest request,
                        @RequestBody Admin admin,
                        HttpSession httpSession) {
        int status;
        String message = "";
        String name = admin.getAdminName();
        status = adminService.login(admin);
        if (status == Status.SUCCESS) {
            Admin admin1 = adminService.queryByName(name);
            ActionUtil.setCurrentAdmin(request, admin1);// 用户信息存session
            admin1.setAdminPass(""); // 返回把密码置空

            //处理用户登录(保持同一时间同一账号只能在一处登录)
            adminLoginHandle(request, name);
            //添加用户与HttpSession的绑定
            ADMIN_SESSION.put(name, httpSession);
            //添加sessionId和用户的绑定
            SESSIONID_ADMIN.put(httpSession.getId(), name);

            httpSession.setAttribute("adminName", name);

            message = "登录成功";

            return new Response(status, message, admin1);
        }
        if (status == Status.NOT_EXISTS) {
            message = "用户不存在";
            return new Response(status, message);
        }
        if (status == Status.PASSWD_NOT_MATCH) {
            message = "密码错误";
            return new Response(status, message);
        }
        return new Response(status, message);
    }

    /**
     * Description:用户登录时的处理
     *
     * @param request
     * @see
     */
    private static void adminLoginHandle(HttpServletRequest request, String adminName) {

        //删除当前登录用户已绑定的HttpSession
        HttpSession session = ADMIN_SESSION.get(adminName);
        try {
            if (session != null && session.getAttribute("adminName").equals(adminName)) {
                //删除已登录的sessionId绑定的用户
                SESSIONID_ADMIN.remove(session.getId());
                ADMIN_SESSION.remove(adminName);
                session.removeAttribute(ActionUtil.SESSION_Admin);
            }
        }catch (IllegalStateException ise){

        }
    }

}
