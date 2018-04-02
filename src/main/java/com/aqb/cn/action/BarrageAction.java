package com.aqb.cn.action;

import com.aqb.cn.annotation.AdminLogin;
import com.aqb.cn.bean.*;
import com.aqb.cn.common.ActionUtil;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.service.*;
import com.aqb.cn.utils.Request;
import com.aqb.cn.utils.TimeToString;
import com.aqb.cn.utils.UUIDCreator;
import com.aqb.cn.utils.getSession.MySessionContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/26.
 */
@Controller
public class BarrageAction {

    private final Log logger = LogFactory.getLog(BarrageAction.class);

    @Autowired
    private BarrageService barrageService;
    @Autowired
    private BarrageLikeService barrageLikeService;
    @Autowired
    private BarrageUnlikeService barrageUnlikeService;
    @Autowired
    private BarrageDeleteTimeService barrageDeleteTimeService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserVipService userVipService;
    @Autowired
    private ShieldService shieldService;
    @Autowired
    private VipService vipService;

    /**
     * 新增弹幕
     * APP
     * @param req
     * @return state : 0 -- 成功, 6--弹幕已存在
     */
    @ResponseBody
    @RequestMapping(value = "/api/barrage/addBarrage", method = RequestMethod.POST)
    public Object addBarrage(HttpServletRequest request,@RequestBody Request req) {
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

        Barrage barrage = req.getBarrage();
        //判断发送的弹幕是否 包含 屏蔽关键词
        List<Shield> shields = shieldService.selectShieldList();//查询所有已经启用的屏蔽关键词
        for(Shield shield : shields){
            if(barrage.getBarrageContent().contains(shield.getShieldGuanjianci())){
                return new Response(Status.ERROR,"包含敏感词汇");
            }
        }

        UserVip myVip = userVipService.selectUserVipUse(obj.getId());
        if (myVip == null){
            return new Response(Status.ERROR,"您还未购买会员，无法发送弹幕");
        }
        Vip vip = vipService.get(myVip.getVipId());
        //弹幕颜色（0-白色，1-蓝色，2-红色，3-黄色）
        if (barrage.getBarrageColor() == 1 && !vip.getBlue()){
            return new Response(Status.ERROR,"您的会员等级不够，不能选择该弹幕颜色");
        }
        if (barrage.getBarrageColor() == 2 && !vip.getRed()){
            return new Response(Status.ERROR,"您的会员等级不够，不能选择该弹幕颜色");
        }
        if (barrage.getBarrageColor() == 3 && !vip.getYellow()){
            return new Response(Status.ERROR,"您的会员等级不够，不能选择该弹幕颜色");
        }
        //弹幕类型（0-滚动弹幕，1-顶部弹幕，2-底部弹幕）
        if (barrage.getBarrageType() == 0 && !vip.getSend()){
            return new Response(Status.ERROR,"您的会员等级不够，不能选择该弹幕类型");
        }
        if (barrage.getBarrageType() == 1 && !vip.getTop()){
            return new Response(Status.ERROR,"您的会员等级不够，不能选择该弹幕类型");
        }
        if (barrage.getBarrageType() == 2 && !vip.getBottom()){
            return new Response(Status.ERROR,"您的会员等级不够，不能选择该弹幕类型");
        }

        barrage.setId(UUIDCreator.getUUID());
        barrage.setUserId(obj.getId());
        barrage.setbLike(0);
        barrage.setbUnlike(0);
        Date date = new Date();
        barrage.setFoundTime(TimeToString.StrToTime(TimeToString.TimeToStr(date)));
        barrage.setFoundDate(date);
        //话题为空时，设置为默认值，默认值为“其他话题”
        if (barrage.getTopicId()==null||barrage.getTopicId().equals("")){
            barrage.setTopicId("F7E3E255A97541B396DC5D159A8BE075");
        }

        barrage.setStatus(0);

        status = barrageService.add(barrage);
        if (status == Status.SUCCESS) {
            message = "添加弹幕成功";
            return new Response(status, message, barrage.getId());
        }if(status == Status.EXISTS){
            message = "弹幕已经存在";
            return new Response(status, message);
        }
        message = "添加弹幕失败";
        return new Response(status, message);
    }

    /**
     * 修改弹幕
     * 后台
     *
     * @param request
     * @param barrage barrage.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 弹幕不存在, 1 -- 修改失败
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/barrage/updatebarrage", method = RequestMethod.POST)
    public Object updatebarrage(HttpServletRequest request, @RequestBody Barrage barrage) {
        int status = Status.ERROR;
        String message = "";
        if(barrage.getId() == null){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = barrageService.update(barrage);
        if(status == Status.SUCCESS){
            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "弹幕不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 弹幕列表查询（模糊查询）
     *  后台
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/barrage/queryBarrage", method = RequestMethod.GET)
    public Object queryBarrage(HttpServletRequest request,
                               @RequestParam(value = "currentPage", required = false) Long currentPage,
                               @RequestParam(value = "maxRow", required = false) Long maxRow,
                               @RequestParam(value = "userFull", required = false)String userFull,
                               @RequestParam(value = "topicId", required = false)String topicId){
        QueryBase query = new QueryBase();
        if (currentPage != null) {
            query.setCurrentPage(currentPage);
        }
        if(maxRow != null){
            query.setMaxRow(maxRow);
        }
        Map map = query.getParameters();
        map.put("userFull", userFull);
        map.put("topicId", topicId);
        //List<BarrageAndUser> barrages = barrageMapper.queryBarrage(query);
        barrageService.query(query);
        return new Response(Status.SUCCESS,query.getTotalRow(),query.getTotalPage(),query.getResults(),query.getCurrentPage());
    }

    /**
     * 删除单条弹幕（后台）
     * @param request
     * @param barrage
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/barrage/deleteBarrage", method = RequestMethod.POST)
    public Object deleteBarrage(HttpServletRequest request, @RequestBody Barrage barrage) {
        int status;
        String message = "";
        if(barrage.getId() == null || barrage.getId().equals("")){
            return new Response(Status.ERROR, "id不能为空");
        }
        status = barrageService.delete(barrage);
        if(status == Status.NOT_EXISTS){
            message = "弹幕不存在";
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
     * 删除该话题下的所有弹幕（后台）
     * @param request
     * @param  topic
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/barrage/deleteTopicBarrage", method = RequestMethod.POST)
    public Object deleteTopicBarrage(HttpServletRequest request, @RequestBody Topic topic) {
        int status = Status.ERROR;
        String message = "";
        if(topic.getId() == null || topic.getId().equals("")){
            return new Response(Status.ERROR, "id不能为空");
        }
        List<Barrage> barrages = barrageService.queryByTopicId(topic.getId());
        for (Barrage barrage:barrages){
            status = barrageService.delete(barrage);
        }
        if(status == Status.NOT_EXISTS){
            message = "弹幕不存在";
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
     * 查询每个话题定时自动删除的时间 后台
     *
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/barrage/queryBarrageDeleteTime", method = RequestMethod.GET)
    public Object queryBarrageDeleteTime(HttpServletRequest request){
        List<BarrageDeleteTime> barrageDeleteTimeList = barrageDeleteTimeService.queryBarrageDeleteTime();
        return new Response(Status.SUCCESS,barrageDeleteTimeList);
    }

    /**
     * 查询已启用的定时自动删除的时间 后台
     *
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/barrage/queryInUseBarrageDeleteTime", method = RequestMethod.GET)
    public Object queryInUseBarrageDeleteTime(HttpServletRequest request){
        BarrageDeleteTime barrageDeleteTime = barrageDeleteTimeService.selectInUse();
        return new Response(Status.SUCCESS,barrageDeleteTime);
    }

    /**
     * 设置每个话题定时自动删除的时间 后台
     *
     * @param barrageDeleteTime
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/barrage/setBarrageDeleteTime", method = RequestMethod.POST)
    public Object setBarrageDeleteTime(HttpServletRequest request,@RequestBody BarrageDeleteTime barrageDeleteTime) throws ParseException {
//        if (barrageDeleteTime.getCycle() == "")
        barrageDeleteTimeService.useOtherBarrageDeleteTime(barrageDeleteTime.getId());
        return new Response(Status.SUCCESS,"设置成功");
    }


    /**
     * 查询一定时间段弹幕列表（首页）
     * APP
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/barrage/homepageBarrage", method = RequestMethod.GET)
    public Object homepageBarrage(HttpServletRequest request){


        Date date = new Date();
        String str_before = TimeToString.Bef_ThirtySeconds(date);
        String str_after = TimeToString.After_ThirtySeconds(date);


        //List<Barrage> barrages = barrageService.queryBarrageByTime(date1,date2);
        List<Barrage> barrages = barrageService.queryBarrageByTime(str_before,str_after);
        //判断发送的弹幕是否 包含 屏蔽关键词
        List<Shield> shields = shieldService.selectShieldList();//查询所有已经启用的屏蔽关键词
        for(int i = 0; i< barrages.size(); i++){
            Barrage barrage = barrages.get(i);
            for(Shield shield : shields){
                String s = "";
                if(barrage.getBarrageContent().contains(shield.getShieldGuanjianci())){
                    barrages.remove(i);
                    i--;
                    break;
                }
            }
        }

        for (Barrage barrage:barrages){
            User user = userService.get(barrage.getUserId());
            barrage.setUser(user);

        }

        return new Response(Status.SUCCESS,barrages);

    }

    /**
     * 查询一定时间段弹幕列表
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/barrage/queryBarrageByTime", method = RequestMethod.GET)
    public Object queryBarrageByTime(HttpServletRequest request,
                                     @RequestParam("sessionId")String sessionId
                               //@RequestParam(value = "currentPage", required = false) Long currentPage,
                               //@RequestParam(value = "maxRow", required = false) Long maxRow,
                                    // @RequestParam(value = "foundDate") Date foundDate
                                    ){
        //判断用户是否登录
        if (sessionId == null || sessionId.equals("")){
            return new Response(Status.ERROR,"sessionId不能为空");
        }
        MySessionContext myc = MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(sessionId);
        if (httpSession == null){
            return new Response(14,"sessionId无效");
        }
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        User obj = ActionUtil.getCurrentUser(httpSession);
        if (obj == null) {
            return new Response(14,"sessionId无效");
        }

        Date date = new Date();
//      String str = TimeToString.TimeToStr(date);//将当前时间转换为字符串
        String str_before = TimeToString.Bef_ThirtySeconds(date);
        String str_after = TimeToString.After_ThirtySeconds(date);
//      Date date1 = TimeToString.StrToTime(str_before);
//      Date date2 = TimeToString.StrToTime(str_after);


        //List<Barrage> barrages = barrageService.queryBarrageByTime(date1,date2);
        List<Barrage> barrages = barrageService.queryBarrageByTime(str_before,str_after);
        //判断发送的弹幕是否 包含 屏蔽关键词
        List<Shield> shields = shieldService.selectShieldList();//查询所有已经启用的屏蔽关键词
        for(int i = 0; i< barrages.size(); i++){
            Barrage barrage = barrages.get(i);
            for(Shield shield : shields){
                if(barrage.getBarrageContent().contains(shield.getShieldGuanjianci())){
                    barrages.remove(i);
                    i--;
                    break;
                }
            }
        }

        for (Barrage barrage:barrages){
            User user = userService.get(barrage.getUserId());
            barrage.setUser(user);
            //查看这一条弹幕是否已经获得自己点赞 和 点差
            //点赞的状态，是否能点赞，true-可以点赞，false-不能点赞
            //点差的状态，是否能点差，true-可以点差，false-不能点差
            BarrageLike barrageLike = barrageLikeService.queryByUserIdBarrageId(obj.getId(),barrage.getId());
            BarrageUnlike barrageUnlike = barrageUnlikeService.queryByUserIdBarrageId(obj.getId(),barrage.getId());
            if (barrageLike == null && barrageUnlike == null ){
                barrage.setbLike_status(true);
                barrage.setbUnlike_status(true);
            }
            if (barrageLike != null && barrageUnlike == null ){
                barrage.setbLike_status(false);
                barrage.setbUnlike_status(true);
            }
            if (barrageLike == null && barrageUnlike!=null){
                barrage.setbLike_status(true);
                barrage.setbUnlike_status(false);
            }

        }

        return new Response(Status.SUCCESS,barrages);

    }

    /**
     * 新增点赞
     *
     * @param req 点赞信息
     * @return state : 0 -- 成功, 6--点赞已存在
     */
    @ResponseBody
    @RequestMapping(value = "/api/barrage/addbarrageLike", method = RequestMethod.POST)
    public Object addbarrageLike(HttpServletRequest request, @RequestBody Request req) {
        int status = Status.ERROR;
        String message = "点赞失败";

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
        //从Request中获取点赞参数
        BarrageLike barrageLike = req.getBarrageLike();
        //Integer i = barrage.getbLike();
        Barrage barrage = barrageService.get(barrageLike.getBarrageId());
        barrageLike.setId(UUIDCreator.getUUID());
        barrageLike.setUserId(obj.getId());
        barrageLike.setFoundDate(new Date());

        //查看这一条弹幕是否已经获得自己点赞 和 点差
        //点赞的状态，是否能点赞，true-可以点赞，false-不能点赞
        //点差的状态，是否能点差，true-可以点差，false-不能点差
        BarrageLike barrageLike1 = barrageLikeService.queryByUserIdBarrageId(obj.getId(),barrage.getId());
        BarrageUnlike barrageUnlike = barrageUnlikeService.queryByUserIdBarrageId(obj.getId(),barrage.getId());
        if (barrageLike1 == null && barrageUnlike == null ){
            //点赞表增加一条数据
            status = barrageLikeService.add(barrageLike);
            barrage.setbLike(barrage.getbLike() + 1);
            barrageService.update(barrage);
            message = "点赞成功";
        }

        if (barrageLike1 == null && barrageUnlike!=null){
//            barrage.setbLike_status(true);
//            barrage.setbUnlike_status(false);
            //点赞表增加一条数据，点差表删除一条数据并更新弹幕表
            status = barrageLikeService.add(barrageLike);
            barrageUnlikeService.delete(barrageUnlike);
            barrage.setbLike(barrage.getbLike() + 1);
            barrage.setbUnlike(barrage.getbUnlike()-1);
            barrageService.update(barrage);
            message = "点赞成功";
        }

        return new Response(status, message);
    }

    /**
     * 新增点差
     *
     * @param req 点赞信息
     * @return state : 0 -- 成功, 6--点赞已存在
     */
    @ResponseBody
    @RequestMapping(value = "/api/barrage/addbarrageUnlike", method = RequestMethod.POST)
    public Object addbarrageUnlike(HttpServletRequest request, @RequestBody Request req) {
        int status = Status.ERROR;
        String message = "点差失败";

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
        //从Request中获取点赞参数
        BarrageUnlike barrageUnlike = req.getBarrageUnlike();
        //Integer i = barrage.getbLike();
        Barrage barrage = barrageService.get(barrageUnlike.getBarrageId());
        barrageUnlike.setId(UUIDCreator.getUUID());
        barrageUnlike.setUserId(obj.getId());
        barrageUnlike.setFoundDate(new Date());

        //查看这一条弹幕是否已经获得自己点赞 和 点差
        //点赞的状态，是否能点赞，true-可以点赞，false-不能点赞
        //点差的状态，是否能点差，true-可以点差，false-不能点差
        BarrageLike barrageLike1 = barrageLikeService.queryByUserIdBarrageId(obj.getId(),barrage.getId());
        BarrageUnlike barrageUnlike1 = barrageUnlikeService.queryByUserIdBarrageId(obj.getId(),barrage.getId());
        if (barrageLike1 == null && barrageUnlike1 == null ){
            //点差表增加一条数据
            status = barrageUnlikeService.add(barrageUnlike);
            barrage.setbUnlike(barrage.getbUnlike() + 1);
            barrageService.update(barrage);
            message = "点差成功";
        }

        if (barrageLike1 != null && barrageUnlike1 == null) {
//            barrage.setbLike_status(false);
//            barrage.setbUnlike_status(true);
            //点差表增加一条数据，点赞表删除一条数据并更新弹幕表
            status = barrageUnlikeService.add(barrageUnlike);
            barrageLikeService.delete(barrageLike1);
            barrage.setbUnlike(barrage.getbUnlike() + 1);
            barrage.setbLike(barrage.getbLike() - 1);
            barrageService.update(barrage);
            message = "点差成功";
        }
        return new Response(status, message);
    }
}
