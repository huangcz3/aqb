package com.aqb.cn.action;

import com.aqb.cn.annotation.AdminLogin;
import com.aqb.cn.bean.*;
import com.aqb.cn.common.*;
import com.aqb.cn.mapper.AdverDateMapper;
import com.aqb.cn.mapper.DeliveryMapper;
import com.aqb.cn.mapper.PriceMapper;
import com.aqb.cn.mapper.ProportionMapper;
import com.aqb.cn.pojo.*;
import com.aqb.cn.service.*;
import com.aqb.cn.utils.*;
import com.aqb.cn.utils.getSession.MySessionContext;
import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by Administrator on 2017/5/18.
 */
@Controller
public class AdvertisementAction {

    private final Log logger = LogFactory.getLog(AdvertisementAction.class);

    @Autowired
    private AdvertisementService advertisementService;
    @Autowired
    private PriceMapper priceMapper;
    @Autowired
    private DeliveryMapper deliveryMapper;
    @Autowired
    private AdverDateMapper adverDateMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private YueService yueService;
    @Autowired
    private JifenService jifenService;
    @Autowired
    private ProportionMapper proportionMapper;
    @Autowired
    private NewPayService newPayService;
    @Autowired
    private NewNoticeService newNoticeService;
    @Autowired
    private DaxiaoquanService daxiaoquanService;





    /**
     * 车辆列表查询
     * APP
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/advertisement/getCars", method = RequestMethod.POST)
    public Object getCars(HttpServletRequest request,@RequestBody Coordinate coordinate) {
        Set set = new HashSet();
        //获取redis中保存的车辆坐标集合
        List<Position> positions = null;
        try {
            positions = GetPositionList.getPositionList();
        }catch (Exception e){
            System.out.println("获取在线设备时异常");
        }
        if(positions == null || positions.size() == 0){
            return new Response(Status.SUCCESS,"没有车辆");
        }
        System.out.println("获取到的车辆："+positions.size());
//        String coords = "";
//        for(int i=0;i<positions.size();i++){
//            Position position = positions.get(i);
//            if(i==0){
//                coords = coords + String.valueOf(position.getLongitude())+","+coords + String.valueOf(position.getLatitude());
//            }else {
//                coords = coords +";"+String.valueOf(position.getLongitude())+","+coords + String.valueOf(position.getLatitude());
//            }
//        }
//        //利用百度地图的坐标偏移，计算设备的坐标
//        String ss=HttpRequest.sendGet("http://api.map.baidu.com/geoconv/v1/",
//                "coords="+coords+"&from=1&to=5&ak=idLNAFXGGoKT81I02g5w5hX9r0G34xwW&mcode=B8:D4:1C:BE:B1:8E:66:A2:EB:01:2A:36:64:33:D8:D6:0F:D4:CB:E5;com.anquanbao.dmlr");
//        System.out.println(ss);
//        Gson gs = new Gson();
//        GsonMap gsonMap = gs.fromJson(ss, GsonMap.class);
//        System.out.println(gsonMap.getStatus());
//        if(gsonMap.getStatus() == 0){
//            System.out.println(gsonMap.getResult());
//            for(GsonXY gsonXY : gsonMap.getResult()){
//                System.out.println(gsonXY.getX());
//                System.out.println(gsonXY.getY());
//            }
//        }

        for(Position position : positions){
//            System.out.println("*************");
//            System.out.println("转换之前的坐标：" + position.getLatitude());
//            System.out.println("转换之前的坐标：" + position.getLongitude());
//            System.out.println("*************");
            String coords = String.valueOf(position.getLongitude())+","+String.valueOf(position.getLatitude());
            //利用百度地图的坐标偏移，计算设备的坐标
            String ss=HttpRequest.sendGet("http://api.map.baidu.com/geoconv/v1/",
                    "coords=" + coords + "&from=1&to=5&ak=idLNAFXGGoKT81I02g5w5hX9r0G34xwW&mcode=B8:D4:1C:BE:B1:8E:66:A2:EB:01:2A:36:64:33:D8:D6:0F:D4:CB:E5;com.anquanbao.dmlr");
            System.out.println(ss);
            Gson gs = new Gson();
            GsonMap gsonMap = gs.fromJson(ss, GsonMap.class);
            System.out.println(gsonMap.getStatus());
            if(gsonMap.getStatus() == 0){
                System.out.println(gsonMap.getResult());
                for(GsonXY gsonXY : gsonMap.getResult()){
//                    System.out.println("转换之后的坐标：" + gsonXY.getX());
//                    System.out.println("转换之后的坐标：" + gsonXY.getY());
//                    System.out.println("*************");
                    position.setLongitude(gsonXY.getX());
                    position.setLatitude(gsonXY.getY());
                }
            }
            //计算车辆位置与，圈的圆心位置的距离
            double s = GPSDistance.GetDistance(coordinate.getLatitude(), coordinate.getLongitude(),
                    position.getLatitude(), position.getLongitude());
            //根据距离判断车是否在圈内
            if(coordinate.getRadius() > s){
                //在圈内的车，加入set集合
                set.add(position);
            }
        }


        return new Response(Status.SUCCESS,"成功",set);
    }

    /**
     * 在线设备查询
     * 后台管理使用
     *
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/advertisement/getCarsAdmin", method = RequestMethod.GET)
    public Object getCarsAdmin(HttpServletRequest request) {
        List<Position> positions = new ArrayList<>();
        try {
            positions = GetPositionList.getPositionList();
        }catch (Exception e){
            System.out.println("获取在线设备时异常");
        }
        if(positions == null || positions.size() == 0){
            return new Response(Status.SUCCESS,"没有车辆");
        }
        for(Position position : positions){
            if(!position.getHasPos()){
                position.setLatitude(0);
                position.setLongitude(0);
                position.setHeight(0);
            }
        }

        return new Response(Status.SUCCESS,"成功",positions);
    }

    @ResponseBody
    @RequestMapping(value = "/api/advertisement/pay", method = RequestMethod.GET)
    public Object pay(HttpServletRequest request) {

        return null;
    }






    /**
     * APP发广告.
     * 广告发送完毕，需要设置该广告的定时任务(现在不设置定时任务)
     * 定时任务触发时，需要判断车辆位置是否在圈内
     * 把在圈内的车辆id，存入set集合中
     * 同时调用Node给出的接口，将广告信息发送给硬件
     *
     * @return state : 0 -- 成功, 1--失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/advertisement/addAdvertisement", method = RequestMethod.POST)
    public Object addAdvertisement(HttpServletRequest request,@RequestBody Request req
                                   //@RequestParam("userPay") String userPay
                                    ) {
        int status;
        String message = "";
        //获取session
        if(req.getSessionId() == null || req.getSessionId().equals("")){
            return new Response(Status.ERROR, "sessionId不能为空");
        }
        MySessionContext myc= MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(req.getSessionId());
        if(httpSession == null){
            return new Response(14,"登录失效");
        }
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        User obj = ActionUtil.getCurrentUser(httpSession);
        if(obj == null){
            return new Response(14,"登录失效");
        }
        User user = userService.get(obj.getId());
        //判断支付密码
        if (!user.getUserPay().equals(EncryptionUtil.encrypt(req.getUserPay()))){
            return new Response(Status.ERROR,"支付密码错误");
        }

        //查出用户总余额
        List<Yue> yues = yueService.queryYueByUserId(obj.getId());//
        float myAssets_yue = 0;
        for (Yue yue:yues){
            //true为收入，false为支出
            if (!yue.getYueIncomeOut()){
                float f = yue.getYueSubtotal();
                yue.setYueSubtotal(-f);
            }
            myAssets_yue = myAssets_yue + yue.getYueSubtotal();
        }
        //查出用户总积分
        List<Jifen> jifens = jifenService.queryJifenByUserId(obj.getId());//查出该userId的积分表
        float myAssets_jifen = 0;
        for (Jifen jifen:jifens) {
            //true为收入，false为支出
            if (!jifen.getJifenIncomeOut()) {
                float i = jifen.getJifenSubtotal();
                jifen.setJifenSubtotal(-i);
            }
            float j = jifen.getJifenSubtotal();
            myAssets_jifen = myAssets_jifen + j;
        }

        //判断参数的正确性
        Advertisement advertisement = req.getAdvertisement();
        if(advertisement.getAdvertisementContent() == null || advertisement.getAdvertisementContent().equals("")){
            return new Response(Status.ERROR, "广告内容不能为空");
        }
        if(advertisement.getJingdu() == null || advertisement.getJingdu() <= 0 ||
                advertisement.getWeidu() == null || advertisement.getWeidu() <= 0){
            return new Response(Status.ERROR, "经纬度信息不正确");
        }
        //判断余额够不够daquanMoneyNumber neiquanMoneyNumber:输入金额 先判断支付方式
        //daquanMoneyStatus大圈支付方式(1--金额，2--积分)
        //neiquanMoneyStatus内圈支付方式 金额1
        Integer daquanMoneyStatus = advertisement.getDaquanMoneyStatus();
        Integer neiquanMoneyStatus = advertisement.getNeiquanMoneyStatus();
        Float daquanMoneyNumber = advertisement.getDaquanMoneyNumber();
        Float neiquanMoneyNumber = advertisement.getNeiquanMoneyNumber();
        if (daquanMoneyStatus == 1){
            Float cost = null;
            if (neiquanMoneyNumber == null || neiquanMoneyNumber.equals("")){
                cost = daquanMoneyNumber;
            }else if (daquanMoneyNumber == null || daquanMoneyNumber.equals("")){
                cost = neiquanMoneyNumber;
            }else {
                cost = daquanMoneyNumber+neiquanMoneyNumber;//余额总花费
            }
            if (cost > myAssets_yue){
                return new Response(Status.ERROR,"余额不足，请充值");
            }
            //当大圈为金额时，需要将金额转积分
            List<Proportion> proportions = proportionMapper.selectByStatus(2);
//            Float jf = (float)0;//转换后的积分
//            for(Proportion proportion : proportions){
//                je = je + ((int)(jf / proportion.getProportionFront()) * proportion.getProportionAfter());
//                jf = jf % proportion.getProportionFront();
//                if(jf == 0){
//                    break;
//                }
//            }
            //转换后的积分
            Float jf = proportions.get(0).getProportionAfter() * advertisement.getDaquanMoneyNumber();
            advertisement.setDaquanJifen(jf);
        }
        if (daquanMoneyStatus == 2){
            if (daquanMoneyNumber > myAssets_jifen){
                return new Response(Status.ERROR,"积分不足，请充值");
            }
            if (neiquanMoneyNumber > myAssets_yue){
                return new Response(Status.ERROR,"余额不足，请充值");
            }
            //当大圈为积分时
            advertisement.setDaquanJifen(advertisement.getDaquanMoneyNumber());
            advertisement.setDaquanMoneyNumber((float)0);
        }

        advertisement.setId(UUIDCreator.getUUID());
        advertisement.setUserId(obj.getId());
        advertisement.setStatus(1);
        advertisement.setFoundDate(new Date());
        advertisement.setShStatus(1);
        advertisement.setPayStatus(2);
        advertisement.setRfStatus(1);

        Price price = priceMapper.selectJiage();//查询系统当前投放时间的广告单价
        //判断大圈
        if(advertisement.getDaquan() != null && advertisement.getDaquan() > 0) {
            advertisement.setDaquanStatus(1);//大圈投放状态（1--未投放完毕，2--已投放完毕）
            if (advertisement.getDaquanTiaoshu() != null && advertisement.getDaquanTiaoshu() > 0) {//手动输入的条数
                //如果手动录入了条数，则添加此时系统的默认单价
                advertisement.setDaquanDanjia(price.getPriceJiage());
            }
        }
        //判断内圈
        if(advertisement.getNeiquan() != null && advertisement.getNeiquan() > 0) {
            advertisement.setNeiquan((float)(advertisement.getNeiquan() / 1000.0));
            advertisement.setNeiquanStatus(1);//内圈投放状态（1--未投放完毕，2--已投放完毕）
            if (advertisement.getNeiquanTiaoshu() != null && advertisement.getNeiquanTiaoshu() > 0) {//手动输入的条数
                //如果手动录入了条数，则添加此时系统的默认单价
                advertisement.setNeiquanDanjia(advertisement.getNeiquanMoneyNumber() / (float)advertisement.getNeiquanTiaoshu());
                if(advertisement.getNeiquanMoneyNumber() / (float)advertisement.getNeiquanTiaoshu() < 1){
                    return new Response(Status.ERROR, "内圈单价不能小于1元");
                }
            }
        }

        //添加到数据库
        List<AdverDate> adverDates = req.getAdverDates();//时间段参数，APP必须以时间先后有顺序的传入
        if(adverDates == null || adverDates.size() == 0){
            return new Response(Status.ERROR, "投放时间段不能为空");
        }
        for(AdverDate adverDate : adverDates){
            System.out.println("StartDateString===" + adverDate.getStartDateString());
            System.out.println("EndDateString===" + adverDate.getEndDateString());
            adverDate.setEndDate(TimeToString.StrToTime(adverDate.getEndDateString()));//前端传过来的值，返这传的，所以这边返过来赋值
            adverDate.setStartDate(TimeToString.StrToTime(adverDate.getStartDateString()));//前端传过来的值，返这传的，所以这边返过来赋值
        }
        //验证参数的正确性

        status = advertisementService.addAdvertisement(advertisement, adverDates);
        if (status == Status.SUCCESS) {
            message = "添加广告成功";
            //扣除广告费用，添加记录至yue jifen数据库
            if (daquanMoneyStatus == 1){
                Float cost = null;
                if (neiquanMoneyNumber == null || neiquanMoneyNumber.equals("")){
                    cost = daquanMoneyNumber;
                }else if (daquanMoneyNumber == null || daquanMoneyNumber.equals("")){
                    cost = neiquanMoneyNumber;
                }else {
                    cost = daquanMoneyNumber+neiquanMoneyNumber;//余额总花费
                }
                Yue yue_db = new Yue();
                yue_db.setId(UUIDCreator.getUUID());
                yue_db.setUserId(obj.getId());
                yue_db.setYueCategory(4);//4-安全宝广告
                yue_db.setYueSubtotal(cost);//花费
                yue_db.setYueStatus(0);
                yue_db.setYueIncomeOut(false);
                yue_db.setYueFoudDate(new Date());
                yueService.add(yue_db);
                //添加支付通知记录
                NewPay newPay = new NewPay();
                newPay.setId(UUIDCreator.getUUID());
                newPay.setUserId(obj.getId());
                newPay.setPayType(1);//支付方式(1--余额支付，2--积分支付，3--微信支付，4--支付宝支付)
                newPay.setPayMoney(cost);
                newPay.setPayName("发广告支付成功");
                newPay.setPayNumber(advertisement.getId());
                newPay.setStatus(1);//状态(1--未读，2--已读）
                newPay.setFoundDate(new Date());
                newPayService.add(newPay);
            }
            //大圈扣除积分，内圈扣除余额
            if (daquanMoneyStatus == 2){
                if(advertisement.getNeiquan() != null){
                    Yue yue_db = new Yue();
                    yue_db.setId(UUIDCreator.getUUID());
                    yue_db.setUserId(obj.getId());
                    yue_db.setYueCategory(4);//4-安全宝广告
                    yue_db.setYueSubtotal(neiquanMoneyNumber);//花费
                    yue_db.setYueStatus(0);
                    yue_db.setYueIncomeOut(false);
                    yue_db.setYueFoudDate(new Date());
                    yueService.add(yue_db);
                    //添加支付通知记录
                    NewPay newPay = new NewPay();
                    newPay.setId(UUIDCreator.getUUID());
                    newPay.setUserId(obj.getId());
                    newPay.setPayType(1);//支付方式(1--余额支付，2--积分支付，3--微信支付，4--支付宝支付)
                    newPay.setPayMoney(neiquanMoneyNumber);
                    newPay.setPayName("发广告支付成功");
                    newPay.setPayNumber(advertisement.getId());
                    newPay.setStatus(1);//状态(1--未读，2--已读）
                    newPay.setFoundDate(new Date());
                    newPayService.add(newPay);
                }
                Jifen jifen_db = new Jifen();
                jifen_db.setId(UUIDCreator.getUUID());
                jifen_db.setUserId(obj.getId());
                jifen_db.setJifenCategory(10);//10-安全宝广告
                jifen_db.setJifenIncomeOut(false);//收入
                jifen_db.setJifenSubtotal(daquanMoneyNumber);
                jifen_db.setJifenStatus(0);
                jifen_db.setJifenFoudDate(new Date());
                jifenService.add(jifen_db);
                //添加支付通知记录
                NewPay newPay2 = new NewPay();
                newPay2.setId(UUIDCreator.getUUID());
                newPay2.setUserId(obj.getId());
                newPay2.setPayType(2);//支付方式(1--余额支付，2--积分支付，3--微信支付，4--支付宝支付)
                newPay2.setPayMoney(daquanMoneyNumber);
                newPay2.setPayName("发广告支付成功");
                newPay2.setPayNumber(advertisement.getId());
                newPay2.setStatus(1);//状态(1--未读，2--已读）
                newPay2.setFoundDate(new Date());
                newPayService.add(newPay2);
            }

            return new Response(status, message, advertisement.getId());
        }if(status == Status.EXISTS){
            message = "广告已经存在";
            return new Response(status, message);
        }
        message = "添加广告失败";
        return new Response(status, message);
    }


    /**
     * 删除广告
     * @param request
     * @param advertisement
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
//    @ResponseBody
//    @RequestMapping(value = "/api/advertisement/deleteAdvertisement", method = RequestMethod.POST)
    public Object deleteAdvertisement(HttpServletRequest request, @RequestBody Advertisement advertisement) {
        int status;
        String message = "";
        if(advertisement.getId() == null || advertisement.getId().equals("")){
            return new Response(Status.ERROR, "id不能为空");
        }
        status = advertisementService.delete(advertisement);
        if(status == Status.NOT_EXISTS){
            message = "广告不存在";
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
     * 修改广告
     * @param request
     * @param advertisement advertisement.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 广告不存在, 1 -- 修改失败
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/advertisement/updateAdvertisement", method = RequestMethod.POST)
    public Object updateAdvertisement(HttpServletRequest request, @RequestBody Advertisement advertisement) {
        int status = Status.ERROR;
        String message = "";
        if(advertisement.getId() == null || advertisement.getId().equals("")){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = advertisementService.update(advertisement);
        if(status == Status.SUCCESS){
            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "广告不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 广告列表查询
     * 查询自己的广告列表
     * APP
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/advertisement/queryByUserId", method = RequestMethod.GET)
    public Object queryByUserId(HttpServletRequest request, @RequestParam("sessionId")String sessionId) {
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

        List<Advertisement> advertisements = advertisementService.queryByUserId(obj.getId());

        return new Response(Status.SUCCESS,"成功",advertisements);
    }

    /**
     * (APP查询地图上的圈)
     * APP
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/advertisement/queryAdvertisement", method = RequestMethod.POST)
    public Object queryAdvertisement(HttpServletRequest request, @RequestBody Coordinate coordinate) {
        List<Advertisement> advertisements = advertisementService.queryAdvertisement();
        Set set = new HashSet();
        List<Daxiaoquan> daquans = daxiaoquanService.queryDaxiaoquanStatus(2, 1);//查询大圈的颜色设置
        List<Daxiaoquan> neiquans = daxiaoquanService.queryDaxiaoquanStatus(2,2);//查询内圈的颜色设置
        //查询系统当前投放时间的广告单价
        Price price = priceMapper.selectJiage();
        for(Advertisement advertisement : advertisements){
            double s = GPSDistance.GetDistance(coordinate.getLatitude(), coordinate.getLongitude(),
                    advertisement.getWeidu(), advertisement.getJingdu());
            if(coordinate.getRadius() > s){
                //判断大圈的颜色
                Float danjia2;//实际投放广告应扣除的单价
                if(advertisement.getDaquanTiaoshu() != null && advertisement.getDaquanDanjia() != null){
                    Float danjia1 = advertisement.getDaquanJifen() / advertisement.getDaquanTiaoshu();//计算用户手动录入的单价=大圈总积分/手动录入的条数
                    //计算实际投放广告应扣除的单价
                                    /*
                                    计算公式：
                                    用户发广告时刻的系统默认单价 : 用户手动录入的单价 = 用户实际投放广告时的系统默认单价 : 实际投放广告应扣除的单价
                                    */
                    danjia2 = (danjia1 * price.getPriceJiage()) / advertisement.getDaquanDanjia();
                    //根据当前实际投放广告的单价，判断圈的颜色
                    for(Daxiaoquan daxiaoquan : daquans){
                        if(danjia2 > daxiaoquan.getDaxiaoquanCanshu()){
                            advertisement.setDaquanColour(daxiaoquan.getDaxiaoquanYanse());//设置大圈颜色
                        }
                    }
                    if(advertisement.getDaquanColour() == null){
                        advertisement.setDaquanColour(daquans.get(daquans.size()-1).getDaxiaoquanYanse());//设置大圈颜色
                    }
                }else{
                    danjia2 = price.getPriceJiage();//用户果如没有手动录入条数，实际投放广告应扣除的单价 = 实际投放广告时的系统默认单价
                    advertisement.setDaquanColour(daquans.get(0).getDaxiaoquanYanse());//默认单价使用默认颜色
                }
                //判断内圈的颜色
                if(advertisement.getNeiquan() != null){
                    danjia2=advertisement.getNeiquanMoneyNumber() / (float)advertisement.getNeiquanTiaoshu();
                    if(danjia2 == 1){
                        advertisement.setNeiquanColour(neiquans.get(0).getDaxiaoquanYanse());//默认单价使用默认颜色
                    }else {
                        //根据当前实际投放广告的单价，判断圈的颜色
                        for(Daxiaoquan daxiaoquan : neiquans){
                            if(danjia2 > daxiaoquan.getDaxiaoquanCanshu()){
                                advertisement.setNeiquanColour(daxiaoquan.getDaxiaoquanYanse());//设置大圈颜色
                            }
                        }
                        if(advertisement.getDaquanColour() == null){
                            advertisement.setNeiquanColour(daquans.get(daquans.size()-1).getDaxiaoquanYanse());//设置大圈颜色
                        }
                    }
                }
                //在圈内的车，加入set集合
                set.add(advertisement);
            }
        }
        return new Response(Status.SUCCESS,"成功",set);
    }

    /**
     * 后台广告列表查询
     * 后台广告审核时使用
     * 审核状态(1--审核中，2--已驳回，3--已通过）
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/advertisement/adminAdvertisement", method = RequestMethod.GET)
    public Object adminAdvertisement(HttpServletRequest request) {
        List<Advertisement> advertisements = advertisementService.adminAdvertisement(1);
        for(Advertisement advertisement : advertisements){
            User user = userService.get(advertisement.getUserId());
            advertisement.setUserId(user.getUserName());
        }
        return new Response(Status.SUCCESS,"成功",advertisements);
    }

    /**
     * 驳回待修改重新提交订单列表
     * 后台
     * shStatus 审核状态(1--审核中，2--已驳回，3--已通过）
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/advertisement/adminAdvertisementshStatus", method = RequestMethod.GET)
    public Object adminAdvertisementshStatus(HttpServletRequest request,
                                             @RequestParam(value = "currentPage", required = false) Long currentPage,
                                             @RequestParam(value = "maxRow", required = false) Long maxRow) {
        QueryBase query = new QueryBase();
        if (currentPage != null) {
            query.setCurrentPage(currentPage);
        }
        if(maxRow != null){
            query.setMaxRow(maxRow);
        }
        Map map = query.getParameters();
        map.put("shStatus",2);//审核状态(1--审核中，2--已驳回，3--已通过）
        map.put("rfStatus",1);//退款状态（1--正常，2--已退款）
        map.put("status",1);//状态(1--已暂停，2--已开始，3--已投放完毕）
        advertisementService.query(query);
        return new Response(Status.SUCCESS,query.getTotalRow(),query.getTotalPage(),query.getResults(),query.getCurrentPage());
    }

    /**
     * 投放中的订单
     * 后台
     * shStatus 审核状态(1--审核中，2--已驳回，3--已通过）
     * rfStatus 退款状态（1--正常，2--已退款）
     * status 状态(1--已暂停，2--已开始，3--已投放完毕）
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/advertisement/adminAdvertisementstatus", method = RequestMethod.GET)
    public Object adminAdvertisementstatus(HttpServletRequest request,
                                           @RequestParam(value = "currentPage", required = false) Long currentPage,
                                           @RequestParam(value = "maxRow", required = false) Long maxRow) {
        QueryBase query = new QueryBase();
        if (currentPage != null) {
            query.setCurrentPage(currentPage);
        }
        if(maxRow != null){
            query.setMaxRow(maxRow);
        }
        Map map = query.getParameters();
        map.put("shStatus",3);//审核状态(1--审核中，2--已驳回，3--已通过）
        map.put("rfStatus",1);//退款状态（1--正常，2--已退款）
        map.put("aStatus",3);//
        advertisementService.query(query);
        return new Response(Status.SUCCESS,query.getTotalRow(),query.getTotalPage(),query.getResults(),query.getCurrentPage());
    }

    /**
     * 退费订单列表
     * 后台
     * shStatus 审核状态(1--审核中，2--已驳回，3--已通过）
     * rfStatus 退款状态（1--正常，2--已退款）
     * status 状态(1--已暂停，2--已开始，3--已投放完毕）
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/advertisement/adminAdvertisementstatus3", method = RequestMethod.GET)
    public Object adminAdvertisementstatus3(HttpServletRequest request,
                                            @RequestParam(value = "currentPage", required = false) Long currentPage,
                                            @RequestParam(value = "maxRow", required = false) Long maxRow) {
        QueryBase query = new QueryBase();
        if (currentPage != null) {
            query.setCurrentPage(currentPage);
        }
        if(maxRow != null){
            query.setMaxRow(maxRow);
        }
        Map map = query.getParameters();
        map.put("rfStatus",2);//退款状态（1--正常，2--已退款）
        advertisementService.query(query);
        return new Response(Status.SUCCESS,query.getTotalRow(),query.getTotalPage(),query.getResults(),query.getCurrentPage());
    }

    /**
     * 已完成列表
     * 后台
     * shStatus 审核状态(1--审核中，2--已驳回，3--已通过）
     * rfStatus 退款状态（1--正常，2--已退款）
     * status 状态(1--已暂停，2--已开始，3--已投放完毕）
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/advertisement/adminAdvertisementrfStatus", method = RequestMethod.GET)
    public Object adminAdvertisementrfStatus(HttpServletRequest request,
                                             @RequestParam(value = "currentPage", required = false) Long currentPage,
                                             @RequestParam(value = "maxRow", required = false) Long maxRow) {
        QueryBase query = new QueryBase();
        if (currentPage != null) {
            query.setCurrentPage(currentPage);
        }
        if(maxRow != null){
            query.setMaxRow(maxRow);
        }
        Map map = query.getParameters();
        map.put("shStatus",3);//审核状态(1--审核中，2--已驳回，3--已通过）
        map.put("rfStatus",1);//退款状态（1--正常，2--已退款）
        map.put("status",3);//状态(1--已暂停，2--已开始，3--已投放完毕）
        advertisementService.query(query);
        return new Response(Status.SUCCESS,query.getTotalRow(),query.getTotalPage(),query.getResults(),query.getCurrentPage());
    }


    /**
     * 广告审核
     * 后台管理接口
     * shstatus 驳回--2，通过--3
     * @param request
     * @param advertisement advertisement.id
     * @return status: 0 -- 成功, 1 -- 失败
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/advertisement/examineAdvertisement", method = RequestMethod.POST)
    public Object examineAdvertisement(HttpServletRequest request, @RequestBody Advertisement advertisement) {
        int status = Status.ERROR;
        String message = "";
        if(advertisement.getId() == null || advertisement.getId().equals("")){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        if(advertisement.getShStatus() == null){
            message = "审核状态不能为空";
            return new Response(Status.ERROR, message);
        }

        Advertisement advertisement1 = new Advertisement();
        advertisement1.setId(advertisement.getId());
        if(advertisement.getShStatus() == 2){
            advertisement1.setShStatus(advertisement.getShStatus());
            advertisement1.setBhContent(advertisement.getBhContent());
        }else if(advertisement.getShStatus() == 3){
            advertisement1.setShStatus(advertisement.getShStatus());
            advertisement1.setStatus(2);
        }else {
            return new Response(Status.ERROR, "审核状态错误");
        }

        status = advertisementService.update(advertisement1);
        if(status == Status.SUCCESS){
            message = "修改成功";
            //添加通知消息
            NewNotice newNotice = new NewNotice();
            newNotice.setId(UUIDCreator.getUUID());
            newNotice.setUserId(advertisementService.get(advertisement.getId()).getUserId());
            newNotice.setStatus(1);
            newNotice.setFoundDate(new Date());
            newNotice.setNoticeTitle("广告审核");
            if(advertisement1.getShStatus() == 3){
                newNotice.setNoticeType(1);//类型(1--通过，2--不通过)
                newNotice.setNoticeContent("审核已经通过");
            }else {
                newNotice.setNoticeType(2);//类型(1--通过，2--不通过)
                newNotice.setNoticeContent("审核未通过");
            }
            newNoticeService.add(newNotice);

        }else if(status == Status.NOT_EXISTS){
            message = "广告不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 退费并取消订单
     * 后台管理接口
     * shstatus 驳回--2，通过--3
     * @param request
     * @param advertisement advertisement.id
     * @return status: 0 -- 成功, 1 -- 失败
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/advertisement/refund", method = RequestMethod.POST)
    public Object refund(HttpServletRequest request, @RequestBody Advertisement advertisement) {
        int status = Status.ERROR;
        String message = "";
        if(advertisement.getId() == null || advertisement.getId().equals("")){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        Advertisement advertisement_db = advertisementService.get(advertisement.getId());
        if(advertisement_db.getRfStatus() != 1){
            return new Response(Status.ERROR, "退款状态非正常");
        }

        Advertisement advertisement1 = new Advertisement();
        advertisement1.setId(advertisement.getId());
        advertisement1.setRfStatus(2);

        status = advertisementService.update(advertisement1);
        if(status == Status.SUCCESS){
            //判断需要退费的这条广告是否已经投放过，如果投放过，需要在退还金额中减去已经投放的金额
            Float daquanMoney = deliveryMapper.sumByDeliveryMoney(advertisement.getId(),1);//统计大圈投放的积分
            Float neiquanMoney = deliveryMapper.sumByDeliveryMoney(advertisement.getId(), 2);//统计内圈投放的金额

            //判断用户使用的是金额还是积分
            if(advertisement_db.getDaquanMoneyStatus() == 1){//判断大圈的金额状态
                //大圈退费给用户
                Yue yue = new Yue();
                yue.setId(UUIDCreator.getUUID());
                yue.setUserId(advertisement_db.getUserId());
                yue.setYueFoudDate(new Date());
                yue.setYueStatus(0);
                //退费的额度，等于总额度减去已经投放了的额度
                if(daquanMoney == null){
                    yue.setYueSubtotal(advertisement_db.getDaquanMoneyNumber());
                }else {
                    float syjf = advertisement_db.getDaquanJifen()-daquanMoney;//总积分 - 已投积分 = 剩余积分
                    //计算剩余金额，公式：总金额 / 剩余金额 = 总积分 / 剩余积分
                    float syje = (advertisement_db.getDaquanMoneyNumber() * syjf) / advertisement_db.getDaquanJifen();
                    yue.setYueSubtotal(syje);
                }
                yue.setYueCategory(9);//9--大圈广告退费
                yue.setYueIncomeOut(true);//0-支出，1-收入
                yueService.add(yue);
            }
            if(advertisement_db.getDaquanMoneyStatus() == 2) {//判断大圈的金额状态
                //大圈退积分给用户
                Jifen jifen = new Jifen();
                jifen.setId(UUIDCreator.getUUID());
                jifen.setUserId(advertisement_db.getUserId());
                jifen.setJifenFoudDate(new Date());
                jifen.setJifenStatus(0);
                if(daquanMoney == null){
                    jifen.setJifenSubtotal(advertisement_db.getDaquanJifen());
                }else {
                    float syjf = advertisement_db.getDaquanJifen()-daquanMoney;//总积分 - 已投积分 = 剩余积分
//                    Float mon = daquanMoney;
//                    //金额转积分
//                    List<Proportion> proportions = proportionMapper.selectByStatus(2);
//                    Float je = new Float(0);//转换后的积分
//                    for(Proportion proportion : proportions){
//                        je = je + ((int)(mon / proportion.getProportionFront()) * proportion.getProportionAfter());
//                        mon = mon % proportion.getProportionFront();
//                        if(mon == 0){
//                            break;
//                        }
//                    }
//                    jifen.setJifenSubtotal(advertisement_db.getDaquanJifen()-(int)(float)je);
                    jifen.setJifenSubtotal(syjf);
                }
                jifen.setJifenCategory(12);//12-广告退还的积分
                jifen.setJifenIncomeOut(true);//0-支出，1-收入
                jifenService.add(jifen);
            }
            //内圈退费给用户
            if(advertisement_db.getNeiquan() != null){
                Yue yue2 = new Yue();
                yue2.setId(UUIDCreator.getUUID());
                yue2.setUserId(advertisement_db.getUserId());
                yue2.setYueFoudDate(new Date());
                yue2.setYueStatus(0);
                if(neiquanMoney == null){
                    yue2.setYueSubtotal(advertisement_db.getNeiquanMoneyNumber());
                }else {
                    yue2.setYueSubtotal(advertisement_db.getNeiquanMoneyNumber()-neiquanMoney);
                }
                yue2.setYueCategory(10);//9--大圈广告退费
                yue2.setYueIncomeOut(true);//0-支出，1-收入
                yueService.add(yue2);
            }

            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "广告不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 广告进行中、暂定的切换
     * APP
     */
    @ResponseBody
    @RequestMapping(value = "/api/advertisement/updateAdvertisementStatus", method = RequestMethod.POST)
    public Object updateAdvertisementStatus(HttpServletRequest request, @RequestBody Request req) {
        int status = Status.ERROR;
        String message = "";
        //获取session
        if(req.getSessionId() == null || req.getSessionId().equals("")){
            return new Response(Status.ERROR, "sessionId不能为空");
        }
        MySessionContext myc= MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(req.getSessionId());
        if(httpSession == null){
            return new Response(14,"sessionId失效");
        }
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        User obj = ActionUtil.getCurrentUser(httpSession);
        if(obj == null){
            return new Response(14,"sessionId失效");
        }
        Advertisement advertisement = req.getAdvertisement();
        if(advertisement.getId() == null || advertisement.getId().equals("")){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        Advertisement advertisement_db = advertisementService.get(advertisement.getId());
        if(advertisement_db.getPayStatus() != 2){
            return new Response(Status.ERROR, "未支付");
        }
        if(advertisement_db.getShStatus() != 3){
            return new Response(Status.ERROR, "未通过审核");
        }
        if(advertisement_db.getStatus() == 3){
            return new Response(Status.ERROR, "已投放完毕");
        }

        Advertisement advertisement1 = new Advertisement();
        advertisement1.setId(advertisement_db.getId());
        if(advertisement.getStatus() == 1){
            advertisement1.setStatus(2);
        }else if (advertisement.getStatus() == 2){
            advertisement1.setStatus(1);
        }else {
            return new Response(Status.ERROR, "status错误");
        }

        status = advertisementService.update(advertisement1);
        if(status == Status.SUCCESS){
            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "广告不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 广告被驳回，重新修改广告内容
     * APP
     */
    @ResponseBody
    @RequestMapping(value = "/api/advertisement/updateAdvertisementContent", method = RequestMethod.POST)
    public Object updateAdvertisementContent(HttpServletRequest request, @RequestBody Request req) {
        int status = Status.ERROR;
        String message = "";
        //获取session
        if(req.getSessionId() == null || req.getSessionId().equals("")){
            return new Response(Status.ERROR, "sessionId不能为空");
        }
        MySessionContext myc= MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(req.getSessionId());
        if(httpSession == null){
            return new Response(14,"sessionId失效");
        }
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        User obj = ActionUtil.getCurrentUser(httpSession);
        if(obj == null){
            return new Response(14,"sessionId失效");
        }
        Advertisement advertisement = req.getAdvertisement();
        if(advertisement.getId() == null || advertisement.getId().equals("")){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        if(advertisement.getAdvertisementContent() == null || advertisement.getAdvertisementContent().equals("")){
            message = "广告内容不能为空";
            return new Response(Status.ERROR, message);
        }
        Advertisement advertisement_db = advertisementService.get(advertisement.getId());
        //判断，只有审核状态为驳回，退款状态正常，才能
        if(advertisement_db.getShStatus() != 2){
            return new Response(Status.ERROR, "非驳回状态的数据");
        }
        if(advertisement_db.getRfStatus() != 1){
            return new Response(Status.ERROR, "退款状态非正常");
        }
        advertisement.setShStatus(1);

        status = advertisementService.update(advertisement);
        if(status == Status.SUCCESS){
            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "广告不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 广告详情查询
     * APP
     * @param request
     * @param req
     * @return status: 0 -- 成功, 1 -- 失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/advertisement/getByAdvertisement", method = RequestMethod.POST)
    public Object getByAdvertisement(HttpServletRequest request, @RequestBody Request req) {
        int status = Status.ERROR;
        String message = "";
        //获取session
        if(req.getSessionId() == null || req.getSessionId().equals("")){
            return new Response(Status.ERROR, "sessionId不能为空");
        }
        MySessionContext myc= MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(req.getSessionId());
        if(httpSession == null){
            return new Response(14,"sessionId失效");
        }
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        User obj = ActionUtil.getCurrentUser(httpSession);
        if(obj == null){
            return new Response(14,"sessionId失效");
        }

        if(req.getId() == null || req.getId().equals("")){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }

        Advertisement advertisement = advertisementService.get(req.getId());
        if(advertisement == null){
            return new Response(Status.ERROR, "id错误");
        }
        AdverGet adverGet = new AdverGet();
        adverGet.setContent(advertisement.getAdvertisementContent());
        adverGet.setFoundDate(advertisement.getFoundDate());
        //判断大圈是金额还是积分
        if(advertisement.getDaquanMoneyStatus() == 1){//结算方式(1--金额，2--积分)
            adverGet.setDaquanMoney(advertisement.getDaquanMoneyNumber());
            adverGet.setDaquanJifen(0);
        }
        if(advertisement.getDaquanMoneyStatus() == 2){//结算方式(1--金额，2--积分)
            adverGet.setDaquanJifen(advertisement.getDaquanJifen());
            adverGet.setDaquanMoney(0);
        }

        adverGet.setDaquanMoney(advertisement.getDaquanMoneyNumber());
        Price price = priceMapper.selectJiage();//查询系统当前投放时间的广告单价
        //判断大圈是否手动输入条数
        if(advertisement.getDaquanTiaoshu() == null || advertisement.getDaquanTiaoshu() <= 0){//未手动输入条数
            adverGet.setDaquanNumber((int)(advertisement.getDaquanMoneyNumber()/price.getPriceJiage())+1);//总金额/系统默认单价
            adverGet.setDaquanDanjia(price.getPriceJiage());
        }else {
            adverGet.setDaquanNumber(advertisement.getDaquanTiaoshu());//手动修改了条数，则为手动修改的条数
            adverGet.setDaquanDanjia(advertisement.getDaquanMoneyNumber() / advertisement.getDaquanTiaoshu());//总金额/手动修改的条数
        }
        //统计大圈已投放条数
        int number = deliveryMapper.sumByDeliveryNumber(advertisement.getId(), 1);//1-大圈，2-小圈
        adverGet.setDaquants(number);

        adverGet.setNeiquanMoney(advertisement.getNeiquanMoneyNumber());
        //判断小圈是否手动输入条数
        if(advertisement.getNeiquan() != null){
            if(advertisement.getNeiquanTiaoshu() == null || advertisement.getNeiquanTiaoshu() <= 0){//未手动输入条数
                adverGet.setNeiquanNumber((int) (advertisement.getNeiquanMoneyNumber() / price.getPriceJiage()) + 1);//总金额/系统默认单价
                adverGet.setNeiquanDanjia(price.getPriceJiage());
            }else {
                adverGet.setNeiquanNumber(advertisement.getNeiquanTiaoshu());//手动修改了条数，则为手动修改的条数
                adverGet.setNeiquanDanjia(advertisement.getNeiquanMoneyNumber() / advertisement.getNeiquanTiaoshu());//总金额/手动修改的条数
            }
            //统计小圈已投放条数
            int number1 = deliveryMapper.sumByDeliveryNumber(advertisement.getId(),2);//1-大圈，2-小圈
            adverGet.setNeiquants(number1);
        }else {
            adverGet.setNeiquanNumber(0);
            adverGet.setNeiquanDanjia((float) 0);
            adverGet.setNeiquants(0);
        }
        //查询投放时间段
        List<AdverDate> adverDates = adverDateMapper.selectByAdverId(advertisement.getId());
        for(AdverDate adverDate : adverDates){
            //时间格式转换
            adverDate.setStartDateString(TimeToString.TimeToStr(adverDate.getStartDate()));
            adverDate.setEndDateString(TimeToString.TimeToStr(adverDate.getEndDate()));
        }
        adverGet.setAdverDates(adverDates);

        return new Response(Status.SUCCESS, "",adverGet);
    }

}
