package com.aqb.cn.action;

import com.aqb.cn.annotation.AdminLogin;
import com.aqb.cn.bean.*;
import com.aqb.cn.common.ActionUtil;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.service.ShareService;
import com.aqb.cn.utils.Request;
import com.aqb.cn.utils.TimeToString;
import com.aqb.cn.utils.getSession.MySessionContext;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.ws.rs.PathParam;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aqb.cn.common.ActionUtil.SESSION_Admin;

/**
 * Created by XD on 2017/8/5.
 */
@Controller
public class ShareAction {

    @Autowired(required = false)
    private ShareService shareService;


    //管理员新建邀请分享规则说明
    @AdminLogin
    @RequestMapping(value = "/api/share/addShareComment",method = RequestMethod.POST)
    @ResponseBody
    public Object addShareComment(@RequestBody ShareComment shareComment, HttpSession session){
        System.out.println("=============================================进入请求addShareComment============================================");
        System.out.println(shareComment);

        Admin admin = (Admin) session.getAttribute(SESSION_Admin);

        Timestamp timestamp = new Timestamp(new Date().getTime());

//        shareComment.setAdminId(admin.getId());//获取管理员ID
        shareComment.setAdminId("123456");//测试 写死
        shareComment.setTime(timestamp);

        int i = shareService.addShareComment(shareComment);

        String message = "";
        Map map = new HashMap();
        if(i==1){
            map.put("message","添加成功");
        }else if(i==0){
            map.put("message","添加失败");
        }else {
            map.put("message","未知错误");
        }
        return map;

    }

    //进入该管理页面是查询正在使用的分享规则说明
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/share/findShareComment",method = RequestMethod.POST)
    public Object findShareComment(){
        System.out.println("=============================================进入请求findShareComment============================================");
        ShareComment shareComment = shareService.findShareComment();
        System.out.println(shareComment);
        return shareComment;
    }

    //进入该管理员页面查询正在使用的额分享规则
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/share/findShareRule",method = RequestMethod.POST)
    public Object findShareRule(){
        System.out.println("=============================================进入请求findShareRule============================================");

        ShareRule shareRule = shareService.findShareRule();
        System.out.println(shareRule);
        return new Response(Status.SUCCESS,shareRule);
    }

    //管理员新建分享规则
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/share/addShareRule",method = RequestMethod.POST)
    public Object addShareRule(@RequestBody ShareRule shareRule,HttpSession session){
        System.out.println("=============================================进入请求addShareRule============================================");
        System.out.println(shareRule);

        Admin admin = (Admin) session.getAttribute(SESSION_Admin);

//        shareRule.setAdminId(admin.getId());//获取管理员ID
        shareRule.setAdminId("123456");//测试 写死

        int i = shareService.addShareRule(shareRule);

        if(i==1){
            return new Response(Status.SUCCESS,"添加成功");
        }else{
            return new Response(Status.SUCCESS,"添加失败");
        }

    }

    //管理员进入页面分页查询奖励排行榜
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/share/findShareAwardSort",method = RequestMethod.POST)
    public Object findShareAwardSort(@RequestBody Map map){
        int pageIndex = 0;
        for(Object key:map.keySet()){
            if("page".equals(key.toString())){
                pageIndex = Integer.parseInt(map.get(key)+"");
            }
        }

        if(pageIndex==0){
            pageIndex = 1;
        }
        List list = shareService.findShareRankingPaging(pageIndex);
        System.out.println("=================================排行榜========================================");
        System.out.println(list);
        Long totalPage = 0l;
        Long totalRow = 0l;
        Long currentPage = new Long(pageIndex);

        Map map1 = (Map) list.get(1);
        for (Object key:map1.keySet()){
            if(key.toString().equals("@pagecount")){
                totalPage = (Long) map1.get(key);
            }
            if(key.toString().equals("@totalcount")){
                totalRow = (Long) map1.get(key);
            }
        }



        return new Response(Status.SUCCESS,totalRow,totalPage, list.get(0),currentPage);
    }

    //管理员清除当前时间以前的排行榜数据
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/share/updateShare",method = RequestMethod.POST)
    public Object updateShare(HttpSession session){

        Admin admin = (Admin) session.getAttribute(SESSION_Admin);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(new Date());

        Date date = TimeToString.StrToDate(str);



        ShareAward shareAward = new ShareAward();
//        shareAward.setAdminId(admin.getId());
        shareAward.setAdminId("123456");//测试 写死
        shareAward.setTime(date);

        int i = shareService.updateShare(shareAward);



        return new Response(Status.SUCCESS,"成功将"+i+"条数据列入不参与排名");
//        return date;
    }


    //用户分享成功时，添加分享信息
    @ResponseBody
    @RequestMapping(value = "/api/share/addShareAward",method = RequestMethod.POST)
    public Object addShareAward(@RequestBody ShareAward shareAward){
        System.out.println("============================进入请求===================================");
        System.out.println(shareAward);
        //查询接受分享的人是否已经注册过了
        User user = shareService.findUserByShareTel(shareAward);


        Map map = new HashMap();
        if(user!=null){
            map.put("message","已注册用户不能再接受邀请");
            map.put("state","0");

            return new Response(Status.SUCCESS,map);

        }else if(user==null){
            map.put("message","手机号注册成功才算分享成功哦");
            map.put("state","0");
        }else if(shareAward.getUserId().equals(user.getId())){
            map.put("message","自己不能邀请自己");
            map.put("state","0");
        }


        //查询接受分享的人是否已接受过分享了，如果已经接受过分享了，返回分享失败信息
        ShareAward shareAward1 = shareService.findSAByShareTel(shareAward);
        if(shareAward1!=null){
            Map map2 = new HashMap();
            map2.put("message","已经接受过分享了");
            map2.put("state","0");
            return new Response(Status.SUCCESS,map2);
        }

        //查询当前奖励规则
        ShareRule shareRule = shareService.findShareRule();
        System.out.println(shareRule);

        //当前系统时间
        Timestamp timestamp = new Timestamp(new Date().getTime());

//        shareAward.setIntegral(shareRule.getIntegral());
//        shareAward.setMoney(shareRule.getMoney());
        shareAward.setShareTime(timestamp);

        System.out.println("==============================================================================================");
        System.out.println(shareAward);

        //将信息添加到分享记录表
        int i = shareService.addShareAward(shareAward);
//        //查询接受分享人是否已注册，如果已注册，就给两人奖励
//        Map map = shareService.addAward(shareAward,shareRule);




        System.out.println(map);
        return new Response(Status.SUCCESS,map);

    }


    //查询个人分享记录
    @ResponseBody
    @RequestMapping(value = "/api/share/findShareByUserId",method = RequestMethod.POST)
    public Object findShareByUserId(@RequestBody Request req){
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        //User user = ActionUtil.getCurrentUser(request);

        if (req.getSessionId() == null || req.getSessionId().equals("")) {
            return new Response(Status.ERROR, "sessionId不能为空");
        }
        MySessionContext myc = MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(req.getSessionId());
        if (httpSession == null) {
            return new Response(14, "sessionId失效");
        }
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        User user = ActionUtil.getCurrentUser(httpSession);

        System.out.println("============================进入请求findShareByUserId==================================");
        Map map= shareService.findShareByUserId(user);
        System.out.println("========================================查询结果=================================================");
        return new Response(Status.SUCCESS,map);
    }

    //查询分享排行榜
    @ResponseBody
    @RequestMapping(value = "/api/share/findShareranking",method = RequestMethod.POST)
    public Object findShareranking(@RequestBody Request req){
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        //User user = ActionUtil.getCurrentUser(request);

        if (req.getSessionId() == null || req.getSessionId().equals("")) {
            return new Response(Status.ERROR, "sessionId不能为空");
        }
        MySessionContext myc = MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(req.getSessionId());
        if (httpSession == null) {
            return new Response(14, "sessionId失效");
        }


        List list = shareService.findShareRanking();

        System.out.println(list);


        return new Response(Status.SUCCESS,list);
    }



}
