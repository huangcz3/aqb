package com.aqb.cn.action;

import com.aqb.cn.bean.Cof;
import com.aqb.cn.bean.CofPic;
import com.aqb.cn.bean.User;
import com.aqb.cn.common.ActionUtil;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.CofMapper;
import com.aqb.cn.mapper.CofPicMapper;
import com.aqb.cn.service.CofService;
import com.aqb.cn.utils.Request;
import com.aqb.cn.utils.UUIDCreator;
import com.aqb.cn.utils.getSession.MySessionContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.context.support.UiApplicationContextUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/27.
 */
@Controller
public class CofAction {

    private final Log logger = LogFactory.getLog(CofAction.class);

    @Autowired
    private CofService cofService;
    @Autowired
    private CofPicMapper cofPicMapper;

    /**
     * 新增朋友圈
     *
     * @param req 朋友圈信息
     * @return state : 0 -- 成功, 6--朋友圈已存在
     */
    @ResponseBody
    @RequestMapping(value = "/api/cof/addCof", method = RequestMethod.POST)
    public Object addCof(HttpServletRequest request, @RequestBody Request req) {
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


        //从Request中获取朋友圈参数
        Cof cof = req.getCof();
        cof.setId(UUIDCreator.getUUID());
        cof.setUserId(obj.getId());
        cof.setFoundDate(new Date());


        status = cofService.add(cof);
        if (status == Status.SUCCESS) {
            //添加朋友圈图片
            String[] strings = req.getStrings();
            for (int i=0;i<strings.length;i++) {
                CofPic cofPic = new CofPic();
                cofPic.setId(UUIDCreator.getUUID());
                cofPic.setCofId(cof.getId());
                cofPic.setCofPicAddress(strings[i]);
                cofPic.setCofPicSaddress(strings[i] + "s");
                cofPic.setFoundDate(new Date());
                cofPicMapper.insertSelective(cofPic);
            }
            message = "添加朋友圈成功";
            return new Response(status, message, cof.getId());
        }
        if (status == Status.EXISTS) {
            message = "朋友圈已经存在";
            return new Response(status, message);
        }
        message = "添加朋友圈失败";
        return new Response(status, message);
    }


    /**
     * 删除朋友圈
     *
     * @param request
     * @param req
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
    @ResponseBody
    @RequestMapping(value = "/api/cof/deleteCof", method = RequestMethod.POST)
    public Object deleteCof(HttpServletRequest request, @RequestBody Request req) {
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


        Cof cof = req.getCof();
        if (cof.getId() == null || cof.getId().equals("")) {
            return new Response(Status.ERROR, "id不能为空");
        }
        status = cofService.delete(cof);
        if (status == Status.NOT_EXISTS) {
            message = "朋友圈不存在";
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
     * 修改朋友圈
     *
     * @param request
     * @param req
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 朋友圈不存在, 1 -- 修改失败
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/cof/updatecof", method = RequestMethod.POST)
    public Object updatecof(HttpServletRequest request, @RequestBody Request req) {
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
        Cof cof = req.getCof();
        cof.setUserId(obj.getId());
        cof.setId(null);

        if (cof.getId() == null) {
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = cofService.update(cof);
        if (status == Status.SUCCESS) {
            message = "修改成功";
        } else if (status == Status.NOT_EXISTS) {
            message = "朋友圈不存在，修改失败";
        } else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 朋友圈列表查询
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/cof/querycof", method = RequestMethod.GET)
    public Object querycof(HttpServletRequest request,
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
        Map map = query.getParameters();
        map.put("userId",obj.getId());


        cofService.query(query);

        return new Response(Status.SUCCESS, query.getTotalRow(), query.getTotalPage(), query.getResults(), query.getCurrentPage());
    }

    /**
     * 朋友圈(朋友空间)动态查询
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/cof/querycof_space", method = RequestMethod.GET)
    public Object querycof_space(HttpServletRequest request,
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
        Map map = query.getParameters();
        map.put("userId",obj.getId());

        cofService.querycof_space(query);

        return new Response(Status.SUCCESS, query.getTotalRow(), query.getTotalPage(), query.getResults(), query.getCurrentPage());
    }
}