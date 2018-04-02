package com.aqb.cn.action;

import com.aqb.cn.bean.Binding;
import com.aqb.cn.bean.Delivery;
import com.aqb.cn.bean.User;
import com.aqb.cn.common.ActionUtil;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.BindingMapper;
import com.aqb.cn.service.DeliveryService;
import com.aqb.cn.utils.Request;
import com.aqb.cn.utils.getSession.MySessionContext;
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
 * Created by Administrator on 2017/7/15.
 */
@Controller
public class DeliveryAction {

    private final Log logger = LogFactory.getLog(DeliveryAction.class);

    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private BindingMapper bindingMapper;

    /**
     * 新增投放记录
     * @param delivery 投放记录信息
     * @param httpSession
     * @return state : 0 -- 成功, 6--投放记录已存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/delivery/adddelivery", method = RequestMethod.POST)
    public Object adddelivery(HttpServletRequest request,@RequestBody Delivery delivery, HttpSession httpSession) {
        int status;
        String message = "";
//        if(delivery.getdeliveryCode() == null || delivery.getdeliveryCode() == ""){
//            return new Response(Status.ERROR, "不能为空");
//        }

        status = deliveryService.add(delivery);
        if (status == Status.SUCCESS) {
            message = "添加投放记录成功";
            return new Response(status, message, delivery.getId());
        }if(status == Status.EXISTS){
            message = "投放记录已经存在";
            return new Response(status, message);
        }
        message = "添加投放记录失败";
        return new Response(status, message);
    }


    /**
     * 删除投放记录
     * @param request
     * @param delivery
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/delivery/deletedelivery", method = RequestMethod.POST)
    public Object deletedelivery(HttpServletRequest request, @RequestBody Delivery delivery) {
        int status;
        String message = "";
        if(delivery.getId() == null || delivery.getId().equals("")){
            return new Response(Status.ERROR, "id不能为空");
        }
        status = deliveryService.delete(delivery);
        if(status == Status.NOT_EXISTS){
            message = "投放记录不存在";
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
     * 修改投放记录
     * @param request
     * @param delivery delivery.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 投放记录不存在, 1 -- 修改失败
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/delivery/updatedelivery", method = RequestMethod.POST)
    public Object updatedelivery(HttpServletRequest request, @RequestBody Delivery delivery) {
        int status = Status.ERROR;
        String message = "";
        if(delivery.getId() == null){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = deliveryService.update(delivery);
        if(status == Status.SUCCESS){
            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "投放记录不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 红包列表查询
     * APP端使用
     *  查询自己未领取的红包
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/delivery/queryRed", method = RequestMethod.POST)
    public Object queryRed(HttpServletRequest request,@RequestBody Request req) {
        //获取登录时的sessionId
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

        //查询用户是否绑定设备
        Binding binding = bindingMapper.queryByUserId(obj.getId());
        if(binding == null){
            return new Response(Status.SUCCESS, "没有绑定设备");
        }

        List<Delivery> deliverys = deliveryService.queryRed(binding.getBindingNumber());
        return new Response(Status.SUCCESS,deliverys);
    }

    /**
     * 一键领取红包
     * APP端使用
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/delivery/updateRed", method = RequestMethod.POST)
    public Object updateRed(HttpServletRequest request,@RequestBody Request req) {
        //获取登录时的sessionId
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

        //获取未领取的红包
        List<Delivery> deliverys = req.getDeliverys();
        if(deliverys != null && deliverys.size() > 0){
            int status = deliveryService.updateRed(deliverys, obj.getId());
            if(status == Status.SUCCESS){
                return new Response(Status.SUCCESS,"成功");
            }
            return new Response(Status.ERROR,"失败");
        }
        return new Response(Status.ERROR,"没有可领取的红包");
    }


}
