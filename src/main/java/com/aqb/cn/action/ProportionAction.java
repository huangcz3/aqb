package com.aqb.cn.action;

import com.aqb.cn.annotation.AdminLogin;
import com.aqb.cn.bean.Proportion;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.service.ProportionService;
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
 * Created by Administrator on 2017/5/15.
 */
@Controller
public class ProportionAction {

    private final Log logger = LogFactory.getLog(ProportionAction.class);

    @Autowired
    private ProportionService proportionService;

    /**
     * 新增兑换比例
     * @param proportion 兑换比例信息
     * @param httpSession
     * @return state : 0 -- 成功, 6--兑换比例已存在
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/proportion/addproportion", method = RequestMethod.POST)
    public Object addproportion(HttpServletRequest request,@RequestBody Proportion proportion, HttpSession httpSession) {
        int status;
        String message = "";
//        if(proportion.getproportionTitle() == null || proportion.getproportionTitle().equals("") ||
//                proportion.getproportionContent() == null || proportion.getproportionContent().equals("")){
//            return new Response(Status.ERROR, "标题或内容不能为空");
//        }

        status = proportionService.add(proportion);
        if (status == Status.SUCCESS) {
            message = "添加兑换比例成功";
            return new Response(status, message, proportion.getId());
        }if(status == Status.EXISTS){
            message = "兑换比例已经存在";
            return new Response(status, message);
        }
        message = "添加兑换比例失败";
        return new Response(status, message);
    }


    /**
     * 删除兑换比例
     * @param request
     * @param proportion
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/proportion/deleteproportion", method = RequestMethod.POST)
    public Object deleteproportion(HttpServletRequest request, @RequestBody Proportion proportion) {
        int status;
        String message = "";
        if(proportion.getId() == null || proportion.getId().equals("")){
            return new Response(Status.ERROR, "id不能为空");
        }
        status = proportionService.delete(proportion);
        if(status == Status.NOT_EXISTS){
            message = "兑换比例不存在";
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
     * 修改兑换比例
     * @param request
     * @param proportion proportion.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 兑换比例不存在, 1 -- 修改失败
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/proportion/updateproportion", method = RequestMethod.POST)
    public Object updateproportion(HttpServletRequest request, @RequestBody Proportion proportion) {
        int status = Status.ERROR;
        String message = "";
        if(proportion.getId() == null || proportion.getId().equals("")){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = proportionService.update(proportion);
        if(status == Status.SUCCESS){
            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "兑换比例不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 兑换比例列表查询
     *
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/proportion/queryProportion", method = RequestMethod.GET)
    public Object queryProportion(HttpServletRequest request) {
        List<Proportion> proportions = proportionService.queryProportion();
        return new Response(Status.SUCCESS,proportions);
    }
}
