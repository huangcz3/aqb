package com.aqb.cn.action;

import com.aqb.cn.annotation.AdminLogin;
import com.aqb.cn.bean.Price;
import com.aqb.cn.bean.Proportion;
import com.aqb.cn.bean.User;
import com.aqb.cn.common.ActionUtil;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.service.PriceService;
import com.aqb.cn.service.ProportionService;
import com.aqb.cn.utils.TimeToString;
import com.aqb.cn.utils.getSession.MySessionContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Administrator on 2017/5/17.
 */
@Controller
public class PriceAction {

    private final Log logger = LogFactory.getLog(PriceAction.class);

    @Autowired
    private PriceService priceService;
    @Autowired
    private ProportionService proportionService;


    /**
     * 根据投放金额
     * 计算当前时间的可投放条数
     * money 用户输入的投放金额
     * moneyStatus 状态（1--金额，2--积分）
     * sessionId 用户登录的sessionId
     * APP
     */
    @ResponseBody
    @RequestMapping(value = "/api/price/queryByMoney", method = RequestMethod.GET)
    public Object queryByMoney(HttpServletRequest request,
                               @RequestParam("moneyStatus")Integer moneyStatus,
                               @RequestParam("money")Float money,
                               @RequestParam("sessionId")String sessionId) {
        //获取session
        if(sessionId == null || sessionId.equals("")){
            return new Response(Status.ERROR, "sessionId不能为空");
        }
        MySessionContext myc= MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(sessionId);
        if(httpSession == null){
            return new Response(14,"sessionId失效");
        }
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        User obj = ActionUtil.getCurrentUser(httpSession);
        if(obj == null){
            return new Response(14,"sessionId失效");
        }

        if(money == null || money <= 0){
            return new Response(Status.ERROR,"money参数错误");
        }

        //查询系统当前时间默认单价 (默认单价 单位已经改为积分)
        Price price = priceService.selectJiage();
        if(price == null){
            return new Response(Status.ERROR,"系统默认单价异常");
        }
        //判断moneyStatus
        if(moneyStatus == 1){//当用户使用金额的时候，需要把金额转换为积分
            List<Proportion> proportions = proportionService.selectByStatus(2);//查询金额转积分的记录
            if(proportions.size() != 1 || proportions.get(0).getProportionAfter() <= 0){
                return new Response(Status.ERROR,"金额转积分的记录不正确");
            }
            Float jf = money * proportions.get(0).getProportionAfter();

            int ts = (int)(jf / price.getPriceJiage()) + 1;//计算条数
            return new Response(Status.SUCCESS,ts);
        }else if(moneyStatus == 2){

            int ts = (int)(money / price.getPriceJiage()) + 1;//计算条数
            return new Response(Status.SUCCESS,ts);

        }else{
            return new Response(Status.ERROR,"moneyStatus参数错误");
        }
    }

    /**
     * 新增投放价格
     * @param price 投放价格信息
     * @param httpSession
     * @return state : 0 -- 成功, 6--投放价格已存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/price/addPrice", method = RequestMethod.POST)
    public Object addprice(HttpServletRequest request,@RequestBody Price price, HttpSession httpSession) {
        int status;
        String message = "";
//        if(price.getpriceTitle() == null || price.getpriceTitle().equals("") ||
//                price.getpriceContent() == null || price.getpriceContent().equals("")){
//            return new Response(Status.ERROR, "标题或内容不能为空");
//        }

        status = priceService.add(price);
        if (status == Status.SUCCESS) {
            message = "添加投放价格成功";
            return new Response(status, message, price.getId());
        }if(status == Status.EXISTS){
            message = "投放价格已经存在";
            return new Response(status, message);
        }
        message = "添加投放价格失败";
        return new Response(status, message);
    }


    /**
     * 删除投放价格
     * @param request
     * @param price
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/price/deletePrice", method = RequestMethod.POST)
    public Object deletePrice(HttpServletRequest request, @RequestBody Price price) {
        int status;
        String message = "";
        if(price.getId() == null || price.getId().equals("")){
            return new Response(Status.ERROR, "id不能为空");
        }
        status = priceService.delete(price);
        if(status == Status.NOT_EXISTS){
            message = "投放价格不存在";
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
     * 修改投放价格
     * @param request
     * @param price price.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 投放价格不存在, 1 -- 修改失败
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/price/updatePrice", method = RequestMethod.POST)
    public Object updatePrice(HttpServletRequest request, @RequestBody Price price) {
        int status = Status.ERROR;
        String message = "";
        if(price.getId() == null || price.getId().equals("")){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = priceService.update(price);
        if(status == Status.SUCCESS){
            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "投放价格不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 投放价格列表查询
     * 后台
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/price/queryPrice", method = RequestMethod.GET)
    public Object queryPrice(HttpServletRequest request,@RequestParam("priceCity")String priceCity) {
        List<Price> prices = priceService.queryPrice(priceCity);
        for(Price price : prices){
            price.setPriceStartString(TimeToString.TimeToStr(price.getPriceStart()));
            price.setPriceEndString(TimeToString.TimeToStr(price.getPriceEnd()));
        }
        return new Response(Status.SUCCESS,prices);
    }

}
