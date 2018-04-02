package com.aqb.cn.action;

import com.aqb.cn.annotation.AdminLogin;
import com.aqb.cn.bean.Daxiaoquan;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.service.DaxiaoquanService;
import com.aqb.cn.utils.UUIDCreator;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/17.
 */
@Controller
public class DaxiaoquanAction {

    private final Log logger = LogFactory.getLog(DaxiaoquanAction.class);

    @Autowired
    private DaxiaoquanService daxiaoquanService;

    /**
     * 新增大小圈设置
     * 后台
     * @param daxiaoquan 大小圈设置信息
     * @return state : 0 -- 成功, 6--大小圈设置已存在
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/daxiaoquan/addDaxiaoquan", method = RequestMethod.POST)
    public Object addDaxiaoquan(HttpServletRequest request,@RequestBody Daxiaoquan daxiaoquan) {
        int status;
        String message = "";
        if(daxiaoquan.getDaxiaoquanCanshu() == null || daxiaoquan.getDaxiaoquanCanshu() <= 0 ||
                daxiaoquan.getXulie() == null || daxiaoquan.getDaxiaoquanYanse() == null ||
                daxiaoquan.getDaxiaoquanYanse().equals("")){
            return new Response(Status.ERROR, "参数错误");
        }
        daxiaoquan.setId(UUIDCreator.getUUID());
        if(daxiaoquan.getXulie() == 1){//1--大圈，2--内圈
            daxiaoquan.setDaxiaoquanName("大圈颜色");
        }
        if(daxiaoquan.getXulie() == 2){//1--大圈，2--内圈
            daxiaoquan.setDaxiaoquanName("内圈颜色");
        }
        daxiaoquan.setFoundDate(new Date());
        daxiaoquan.setStatus(2);//状态(1--大小圈,2--广告颜色）

        status = daxiaoquanService.add(daxiaoquan);
        if (status == Status.SUCCESS) {
            message = "添加大小圈设置成功";
            return new Response(status, message, daxiaoquan.getId());
        }if(status == Status.EXISTS){
            message = "大小圈设置已经存在";
            return new Response(status, message);
        }
        message = "添加大小圈设置失败";
        return new Response(status, message);
    }


    /**
     * 删除大小圈设置
     * @param request
     * @param daxiaoquan
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/daxiaoquan/deleteDaxiaoquan", method = RequestMethod.POST)
    public Object deleteDaxiaoquan(HttpServletRequest request, @RequestBody Daxiaoquan daxiaoquan) {
        int status;
        String message = "";
        if(daxiaoquan.getId() == null || daxiaoquan.getId().equals("")){
            return new Response(Status.ERROR, "id不能为空");
        }
        status = daxiaoquanService.delete(daxiaoquan);
        if(status == Status.NOT_EXISTS){
            message = "大小圈设置不存在";
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
     * 修改大小圈设置
     * 后台
     * @param request
     * @param daxiaoquan daxiaoquan.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 大小圈设置不存在, 1 -- 修改失败
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/daxiaoquan/updateDaxiaoquan", method = RequestMethod.POST)
    public Object updateDaxiaoquan(HttpServletRequest request, @RequestBody Daxiaoquan daxiaoquan) {
        int status = Status.ERROR;
        String message = "";
        if(daxiaoquan.getId() == null || daxiaoquan.getId().equals("")){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = daxiaoquanService.update(daxiaoquan);
        if(status == Status.SUCCESS){
            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "大小圈设置不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 大小圈设置列表查询
     * 后台
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/daxiaoquan/queryDaxiaoquan", method = RequestMethod.GET)
    public Object queryDaxiaoquan(HttpServletRequest request) {
        List<Daxiaoquan> daxiaoquans = daxiaoquanService.queryDaxiaoquan(1);//查询大小圈设置
        List<Daxiaoquan> daxiaoquans2 = daxiaoquanService.queryDaxiaoquan(2);//查询广告颜色
        Map map = new HashMap<>();
        map.put("one",daxiaoquans);
        map.put("two",daxiaoquans2);
        return new Response(Status.SUCCESS,map);
    }

    /**
     * 获取内圈的金额和积分开启限额
     * APP
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/daxiaoquan/getDaxiaoquan", method = RequestMethod.GET)
    public Object getDaxiaoquan(HttpServletRequest request) {
        List<Daxiaoquan> daxiaoquans = daxiaoquanService.queryDaxiaoquan(1);//查询大小圈设置
//        List<Daxiaoquan> daxiaoquans2 = daxiaoquanService.queryDaxiaoquan(2);//查询广告颜色
//        Map map = new HashMap<>();
//        map.put("1",daxiaoquans);
//        map.put("2",daxiaoquans2);
        return new Response(Status.SUCCESS,daxiaoquans);
    }


}
