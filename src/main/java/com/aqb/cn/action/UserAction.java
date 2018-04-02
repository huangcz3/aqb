package com.aqb.cn.action;

import com.aqb.cn.bean.*;
import com.aqb.cn.common.*;
import com.aqb.cn.example.CreateUser;
import com.aqb.cn.mapper.ShareAwardMapper;
import com.aqb.cn.mapper.ShareRuleMapper;
import com.aqb.cn.pojo.Login;
import com.aqb.cn.pojo.UpdatePass;
import com.aqb.cn.pojo.UserAndCode;
import com.aqb.cn.service.*;
import com.aqb.cn.utils.ImageUtil;
import com.aqb.cn.utils.Request;
import com.aqb.cn.utils.UUIDCreator;
import com.aqb.cn.utils.getSession.MySessionContext;
import com.aqb.cn.utils.sms.SendMessageUtill;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/27.
 */
@Controller
public class UserAction {

    /**
     * 用户和Session绑定关系
     */
    public static final Map<String, HttpSession> USER_SESSION = new HashMap<String, HttpSession>();

    /**
     * seeionId和用户的绑定关系
     */
    public static final Map<String, String> SESSIONID_USER = new HashMap<String, String>();

    private final Log logger = LogFactory.getLog(UserAction.class);

    @Autowired
    private UserService userService;
    @Autowired
    private UserVipService userVipService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private SignService signService;
    @Autowired
    private BindingService bindingService;
    @Autowired
    private JifenService jifenService;
    @Autowired
    private YueService yueService;

    @Autowired
    private ShareAwardMapper shareAwardMapper;
    @Autowired
    private ShareRuleMapper shareRuleMapper;


    /**
     * 获取短信验证码
     * 将验证码保存于session中，返回sessionId
     *
     * @return status: 0 -- 成功, 1 -- 修改失败, 14 -- 登录失效
     */
    @ResponseBody
    @RequestMapping(value = "/api/user/getCode", method = RequestMethod.GET)
    public Object getCode(HttpServletRequest request, @RequestParam("userName") String userName) {
        int status;
        String message = "";
        //User user = userService.queryByName(userName);
        //发送验证码到用户手机
        HashMap<String, String> m = SendMessageUtill.getIdentifying_code(userName);  //应用发送短信接口
        String result = m.get("status");            //获取到状态的值，即成功与否
        String msg = m.get("message");              //获取到message值
        String mobile_code = m.get("mobile_code");  //获取到mobile_code值（验证码）

        if (result != null && result.equals("Success")) {
            System.out.println("发送成功");
            logger.info("发送的验证码:" + mobile_code);  //打印日志
            HttpSession session = request.getSession(); //设置session
            session.setAttribute("userName", userName);//将手机号保存在session中
            session.setAttribute("code", mobile_code); //将短信验证码放到session中保存
            session.setMaxInactiveInterval(60 * 5);//保存时间 暂时设定为5分钟
            return new Response(Status.SUCCESS, "发送成功", session.getId());
        } else {
            System.out.println("短信发送失败");
            return new Response(Status.ERROR, "短信发送失败");
        }
    }


    /**
     * 新增用户
     * 注册
     * (使用验证码)
     *
     * @return state : 0 -- 成功, 10 -- 验证码错误, 6--用户已存在
     */
    @ResponseBody
    @RequestMapping(value = "/api/user/addUserCode", method = RequestMethod.POST)
    public Object addUserCode(HttpServletRequest request, @RequestBody UserAndCode user) {
        int status;
        String message = "";
        //获取codeSession
        if (user.getCodeSessionId() == null || user.getCodeSessionId().equals("")) {
            return new Response(Status.ERROR, "getCodeSessionId不能为空");
        }
        MySessionContext myc = MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(user.getCodeSessionId());
        if (httpSession == null) {
            return new Response(Status.ERROR, "验证码失效");
        }
        //获取保存在session中的验证码和手机号
        String code = null;
        String userName1 = null;
        try {
            code = (String) httpSession.getAttribute("code");
            userName1 = (String) httpSession.getAttribute("userName");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user.getUserName() == null ||
                user.getUserPass() == null ||
                user.getUserName().equals("") ||
                user.getUserPass().equals("")
                ) {
            return new Response(Status.ERROR, "请填写完整的用户信息");
        }
        //判断验证码是否正确
        if (code == null || user.getCode() == null || user.getCode().equals("") || !(code.equals(user.getCode()))) {
            message = "验证码错误";
            status = Status.CODE_NOT_MATCH;
            return new Response(status, message);
        }
        //判断传进来的手机号和session中保存的手机号是否一致
        if (!user.getUserName().equals(userName1)) {
            return new Response(Status.ERROR, "手机号错误");
        }

        User user1 = userService.queryByName(user.getUserName());
        if (user1 != null) {
            return new Response(Status.ERROR, "手机号已存在");
        }

        User user_db = new User();
        if (user.getUserPass().length() < 6) {
            return new Response(Status.ERROR,"密码长度至少为6位");
        }
        user_db.setId(UUIDCreator.getUUID());
        user_db.setUserName(user.getUserName());
        user_db.setUserPass(EncryptionUtil.encrypt(user.getUserPass()));
        //user_db.setUserPay(EncryptionUtil.encrypt("123456"));//支付密码默认为123456
        user_db.setStatus(0);
        user_db.setFoundDate(new Date());

        //注册用户的环信账号
        io.swagger.client.model.User userIM = new io.swagger.client.model.User();
        userIM.setUsername(EncryptionUtil.encrypt(user_db.getId()));//用户的id进行MD5，作为环信的IM账号
        userIM.setPassword("e10adc3949ba59abbe56e057f20f883e");//123456DM5作为登录环信的默认密码
        int activated = new CreateUser().createUser(userIM);
        if(activated == Status.SUCCESS){
            user_db.setImUserName(userIM.getUsername());
        }else{
            //如果失败，重新添加一次
            user_db.setId(UUIDCreator.getUUID());
            userIM.setUsername(EncryptionUtil.encrypt(user_db.getId()));
            int activated2 = new CreateUser().createUser(userIM);
            if(activated2 == Status.SUCCESS) {
                user_db.setImUserName(userIM.getUsername());
            }
        }

        status = userService.add(user_db);
        taskService.insertNewbieTask(user_db.getId());//添加新手任务
        //判断用户是否为邀请用户，如果是，则给邀请人双方发放邀请奖励
        List<com.aqb.cn.bean.a.ShareAward> shareAwards = shareAwardMapper.selectByShareTelStatus(user_db.getUserName());
        if(shareAwards != null && shareAwards.size() > 0){
            //查询奖励
            com.aqb.cn.bean.a.ShareRule shareRule = shareRuleMapper.selectByState(1);
            if(shareRule != null){
                if(shareRule.getShareRule() == 1){//积分奖励
                    Jifen jifen = new Jifen();
                    jifen.setId(UUIDCreator.getUUID());
                    jifen.setUserId(shareAwards.get(0).getUserId());
                    jifen.setJifenCategory(3);//3-邀请好友
                    jifen.setJifenFoudDate(new Date());
                    jifen.setJifenIncomeOut(true);
                    jifen.setJifenSubtotal((float) shareRule.getIntegral());
                    jifen.setJifenStatus(0);
                    jifenService.add(jifen);//奖励邀请人
                    Jifen jifen2 = new Jifen();
                    jifen2.setId(UUIDCreator.getUUID());
                    jifen2.setUserId(user_db.getId());
                    jifen2.setJifenCategory(3);//3-邀请好友
                    jifen2.setJifenFoudDate(new Date());
                    jifen2.setJifenIncomeOut(true);
                    jifen2.setJifenSubtotal((float) shareRule.getIntegral());
                    jifen2.setJifenStatus(0);
                    jifenService.add(jifen2);//奖励被邀请人
                    //更改数据库的状态
                    for(com.aqb.cn.bean.a.ShareAward shareAward : shareAwards){
                        shareAward.setAwardState(1);
                        shareAward.setIntegral(shareRule.getIntegral());
                        shareAwardMapper.updateByPrimaryKeySelective(shareAward);//更改已经领取的状态
                    }
                }
                if(shareRule.getShareRule() == 2){//金额奖励
                    Yue yue = new Yue();
                    yue.setId(UUIDCreator.getUUID());
                    yue.setUserId(shareAwards.get(0).getUserId());
                    yue.setYueFoudDate(new Date());
                    yue.setYueStatus(0);
                    yue.setYueCategory(12);//12--邀请好友
                    yue.setYueIncomeOut(true);
                    yue.setYueSubtotal(shareRule.getMoney().floatValue());
                    yueService.add(yue);//奖励邀请人
                    Yue yue2 = new Yue();
                    yue2.setId(UUIDCreator.getUUID());
                    yue2.setUserId(user_db.getId());
                    yue2.setYueFoudDate(new Date());
                    yue2.setYueStatus(0);
                    yue2.setYueCategory(12);//12--邀请好友
                    yue2.setYueIncomeOut(true);
                    yue2.setYueSubtotal(shareRule.getMoney().floatValue());
                    yueService.add(yue2);//奖励被邀请人
                    //更改数据库的状态
                    for(com.aqb.cn.bean.a.ShareAward shareAward : shareAwards){
                        shareAward.setAwardState(1);
                        shareAward.setMoney(shareRule.getMoney());
                        shareAwardMapper.updateByPrimaryKeySelective(shareAward);//更改已经领取的状态
                    }
                }
            }
        }
        httpSession.removeAttribute("code");//使用完成后销毁验证码
        if (status == Status.SUCCESS) {
            message = "添加用户成功";
            return new Response(status, message, user_db.getUserName());
        }
        if (status == Status.EXISTS) {
            message = "用户已经存在";
            return new Response(status, message);
        }
        message = "添加用户失败";
        return new Response(status, message);
    }

    /**
     * 密码登录
     *
     * @param request
     * @param user        用户
     * @param httpSession
     * @return status 0--成功, 10--验证码错误, 7 --客户不存在,9-- 密码不正确
     */
    @ResponseBody
    @RequestMapping(value = "/api/user/login", method = RequestMethod.POST)
    public Object login(HttpServletRequest request,  HttpSession httpSession,@RequestBody User user) {
        int status;
        String message = "";
        String userName = user.getUserName();
        status = userService.login(user);//登录
        System.out.println("userName:" + user.getUserName());
        if (status == Status.SUCCESS) {
            User user1 = userService.queryByName(userName);
            //ActionUtil.setCurrentUser(request, user1);// 用户信息存session
            httpSession = ActionUtil.getUserSession(request, user1);// 用户信息存session并返回session
            user1.setUserPass(""); // 返回把密码置空

            //处理用户登录(保持同一时间同一账号只能在一处登录)
            userLoginHandle(request,userName);
            //添加用户与HttpSession的绑定
            USER_SESSION.put(userName, httpSession);
            //添加sessionId和用户的绑定
            SESSIONID_USER.put(httpSession.getId(), userName);

            httpSession.setAttribute("userName", userName);
            httpSession.removeAttribute("userMsg");

            message = "登录成功";
            Login login = new Login();
            login.setSessionId(request.getSession().getId());
            login.setUsername(user1.getImUserName());
            login.setPassword("e10adc3949ba59abbe56e057f20f883e");//环信的登录密码为默认的
            return new Response(status, message, login);
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
    private static void userLoginHandle(HttpServletRequest request,String userName){

        //获取当前登录的用户，是否有绑定的session。如果有，则表示用户已经处于登录状态
        HttpSession session = USER_SESSION.get(userName);
        try{
            if (session != null && session.getAttribute("userName").equals(userName)) {
                //顶掉当前登录的用户
                SESSIONID_USER.remove(session.getId());
                USER_SESSION.remove(userName);
                session.removeAttribute(ActionUtil.SESSION_USER);
            }
        }catch (IllegalStateException ise){

        }
    }

    /**
     * 短信验证码登录
     *
     * @param request
     * @param userAndCode 手机号和短信验证码
     * @return status 0--成功, 10--验证码错误, 7 --客户不存在,9-- 密码不正确
     */
    @ResponseBody
    @RequestMapping(value = "/api/user/login_sms", method = RequestMethod.POST)
    public Object login_sms(HttpServletRequest request,
                            @RequestBody UserAndCode userAndCode) {
        int status;
        String message = "";
        //获取codeSession
        if (userAndCode.getCodeSessionId() == null || userAndCode.getCodeSessionId().equals("")) {
            return new Response(Status.ERROR, "getCodeSessionId不能为空");
        }
        MySessionContext myc = MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(userAndCode.getCodeSessionId());
        if (httpSession == null) {
            return new Response(Status.ERROR, "验证码失效");
        }

        //获取保存在session中的验证码和手机号
        String code = null;
        String userName = null;
        try {
            code = (String) httpSession.getAttribute("code");//验证码
            userName = (String) httpSession.getAttribute("userName");//手机号
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userAndCode.getUserName() == null || userAndCode.getUserName().equals("")) {
            return new Response(Status.ERROR, "请输入手机号");
        }

        //判断验证码是否正确（判断session获取的验证码和传进来的验证码）
        if (code == null || userAndCode.getCode() == null || userAndCode.getCode().equals("") || !(code.equals(userAndCode.getCode()))) {
            message = "验证码错误";
            status = Status.CODE_NOT_MATCH;
            return new Response(status, message);
        }
        //判断传进来的手机号和session中保存的手机号是否一致
        if (!userAndCode.getUserName().equals(userName)) {
            return new Response(Status.ERROR, "手机号错误");
        }

        //String name = userAndCode.getUserName();
        User user = new User();
        user.setUserName(userAndCode.getUserName());

        User user_db = userService.queryByName(user.getUserName());//查询数据库中是否存在该用户

        //手机号是否注册
        if (user_db == null) {
            user.setId(UUIDCreator.getUUID());
            //user.setUserPass(EncryptionUtil.encrypt("123456"));//用户未注册，登录密码默认为123456
            //user.setUserPay(EncryptionUtil.encrypt("123456"));//支付密码默认为123456
            user.setStatus(0);
            user.setFoundDate(new Date());
            user.setLoginMode(4);

            //注册用户的环信账号
            io.swagger.client.model.User userIM = new io.swagger.client.model.User();
            userIM.setUsername(EncryptionUtil.encrypt(user.getId()));//用户的id进行MD5，作为环信的IM账号
            userIM.setPassword("e10adc3949ba59abbe56e057f20f883e");//123456DM5作为登录环信的默认密码
            int activated = new CreateUser().createUser(userIM);
            if(activated == Status.SUCCESS){
                user.setImUserName(userIM.getUsername());
            }else{
                //如果失败，重新添加一次
                user.setId(UUIDCreator.getUUID());
                userIM.setUsername(EncryptionUtil.encrypt(user.getId()));
                int activated2 = new CreateUser().createUser(userIM);
                if(activated2 == Status.SUCCESS) {
                    user.setImUserName(userIM.getUsername());
                }
            }
            userService.add(user);
            taskService.insertNewbieTask(user.getId());//添加新手任务
            user_db = userService.get(user.getId());
        }
//        User user_db = new User();
//        ActionUtil.setCurrentUser(request,user_db);
        ActionUtil.setCurrentUser(request, user_db);// 用户信息存session
        message = "登录成功";

        httpSession.removeAttribute("code");//使用完成后销毁验证码
        Login login = new Login();
        login.setSessionId(request.getSession().getId());
        login.setUsername(user_db.getImUserName());
        login.setPassword("e10adc3949ba59abbe56e057f20f883e");//环信的登录密码为默认的
        //ActionUtil.setCurrentUser(request,user);
        return new Response(Status.SUCCESS, message, login);
    }


    /**
     * 微信第三方登录
     *
     * @param request
     * @param map        用户信息map
     * @return status 0--成功
     */
    @ResponseBody
    @RequestMapping(value = "/api/user/login_weixin", method = RequestMethod.POST)
    public Object login_weixin(HttpServletRequest request,
                               //@RequestBody User user,
                               @RequestBody HashMap<String,Object> map) {
        int status;
        String message = "";
        // String str = null;//存放微信登录返回的openid
        User user = new User();//将微信用户信息存放在该user对象中
        for (Map.Entry<String,Object> m :map.entrySet()){
            if (m.getKey().equals("openid")){
                user.setUserCode1((String) m.getValue());
            }
            if (m.getKey().equals("nickname")){
                user.setUserNick((String) m.getValue());
            }
//            if (m.getKey().equals("sex")){
//                user.setUserSex((String) m.getValue());
//            }
            if (m.getKey().equals("headimgurl")) {
                user.setUserHead((String) m.getValue());
            }
        }
        User user_db = userService.queryByCode1(user.getUserCode1());//查询数据库是否存在
        if (user_db == null){
            //新注册一个用户且code1为openid且登录方式loginMode=1，新建四个任务
            user.setId(UUIDCreator.getUUID());
            user.setStatus(0);
            user.setFoundDate(new Date());
            user.setLoginMode(1);//微信登录

            //注册用户的环信账号
            io.swagger.client.model.User userIM = new io.swagger.client.model.User();
            userIM.setUsername(EncryptionUtil.encrypt(user.getId()));//用户的id进行MD5，作为环信的IM账号
            userIM.setPassword("e10adc3949ba59abbe56e057f20f883e");//123456DM5作为登录环信的默认密码
            int activated = new CreateUser().createUser(userIM);
            if(activated == Status.SUCCESS){
                user.setImUserName(userIM.getUsername());
            }else{
                //如果失败，重新添加一次
                user.setId(UUIDCreator.getUUID());
                userIM.setUsername(EncryptionUtil.encrypt(user.getId()));
                int activated2 = new CreateUser().createUser(userIM);
                if(activated2 == Status.SUCCESS) {
                    user.setImUserName(userIM.getUsername());
                }
            }
            userService.add(user);
            taskService.insertNewbieTask(user.getId());//添加新手任务
            user_db = userService.get(user.getId());
        }
        ActionUtil.setCurrentUser(request, user_db);// 用户信息存session
        message = "登录成功";
        Login login = new Login();
        login.setSessionId(request.getSession().getId());
        login.setUsername(user_db.getImUserName());
        login.setPassword("e10adc3949ba59abbe56e057f20f883e");//环信的登录密码为默认的
        return new Response(Status.SUCCESS, message, login);
    }

    /**
     * qq第三方登录
     *
     * @param request
     * @param map        用户信息map
     * @return status 0--成功
     */
    @ResponseBody
    @RequestMapping(value = "/api/user/login_qq", method = RequestMethod.POST)
    public Object login_qq(HttpServletRequest request,
                           //@RequestBody User user,
                           @RequestBody HashMap<String,Object> map) {
        int status;
        String message = "";
        // String str = null;//存放qq登录返回的openid
        User user = new User();//将qq用户信息存放在该user对象中
        for (Map.Entry<String,Object> m :map.entrySet()){
            if (m.getKey().equals("openid")){
                user.setUserCode2((String) m.getValue());
            }
            if (m.getKey().equals("nickname")){
                user.setUserNick((String) m.getValue());
            }
            if (m.getKey().equals("figureurl")) {
                user.setUserHead((String) m.getValue());
            }
        }
        User user_db = userService.queryByCode2(user.getUserCode2());//查询数据库是否存在
        if (user_db == null){
            //新注册一个用户且code1为openid且登录方式loginMode=2，新建四个任务
            user.setId(UUIDCreator.getUUID());
            user.setStatus(0);
            user.setFoundDate(new Date());
            user.setLoginMode(2);//qq登录

            //注册用户的环信账号
            io.swagger.client.model.User userIM = new io.swagger.client.model.User();
            userIM.setUsername(EncryptionUtil.encrypt(user.getId()));//用户的id进行MD5，作为环信的IM账号
            userIM.setPassword("e10adc3949ba59abbe56e057f20f883e");//123456DM5作为登录环信的默认密码
            int activated = new CreateUser().createUser(userIM);
            if(activated == Status.SUCCESS){
                user.setImUserName(userIM.getUsername());
            }else{
                //如果失败，重新添加一次
                user.setId(UUIDCreator.getUUID());
                userIM.setUsername(EncryptionUtil.encrypt(user.getId()));
                int activated2 = new CreateUser().createUser(userIM);
                if(activated2 == Status.SUCCESS) {
                    user.setImUserName(userIM.getUsername());
                }
            }
            userService.add(user);
            taskService.insertNewbieTask(user.getId());//添加新手任务
            user_db = userService.get(user.getId());
        }
        ActionUtil.setCurrentUser(request, user_db);// 用户信息存session
        message = "登陆成功";
        Login login = new Login();
        login.setSessionId(request.getSession().getId());
        login.setUsername(user_db.getImUserName());
        login.setPassword("e10adc3949ba59abbe56e057f20f883e");//环信的登录密码为默认的
        return new Response(Status.SUCCESS, message, login);
    }

    /**
     * 微博第三方登录
     *
     * @param request
     * @param map        用户信息map
     * @return status 0--成功
     */
    @ResponseBody
    @RequestMapping(value = "/api/user/login_weibo", method = RequestMethod.POST)
    public Object login_weibo(HttpServletRequest request,
                              //@RequestBody User user,
                              @RequestBody HashMap<String,Object> map) {
        int status;
        String message = "";
        // String str = null;//存放微博登录返回的idstr
        User user = new User();//将微博用户信息存放在该user对象中
        for (Map.Entry<String,Object> m :map.entrySet()){
            if (m.getKey().equals("idstr")){
                user.setUserCode3((String) m.getValue());
            }
            if (m.getKey().equals("screen_name")){
                user.setUserNick((String) m.getValue());
            }
            if (m.getKey().equals("avatar_large")) {
                user.setUserHead((String) m.getValue());
            }
        }
        User user_db = userService.queryByCode3(user.getUserCode3());//查询数据库是否存在
        if (user_db == null){
            //新注册一个用户且code1为openid且登录方式loginMode=3，新建四个任务
            user.setId(UUIDCreator.getUUID());
            user.setStatus(0);
            user.setFoundDate(new Date());
            user.setLoginMode(3);//微博登录

            //注册用户的环信账号

            io.swagger.client.model.User userIM = new io.swagger.client.model.User();
            userIM.setUsername(EncryptionUtil.encrypt(user.getId()));//用户的id进行MD5，作为环信的IM账号
            userIM.setPassword("e10adc3949ba59abbe56e057f20f883e");//123456DM5作为登录环信的默认密码
            int activated = new CreateUser().createUser(userIM);
            if(activated == Status.SUCCESS){
                user.setImUserName(userIM.getUsername());
            }else{
                //如果失败，重新添加一次
                user.setId(UUIDCreator.getUUID());
                userIM.setUsername(EncryptionUtil.encrypt(user.getId()));
                int activated2 = new CreateUser().createUser(userIM);
                if(activated2 == Status.SUCCESS) {
                    user.setImUserName(userIM.getUsername());
                }
            }
            userService.add(user);
            taskService.insertNewbieTask(user.getId());//添加新手任务
            user_db = userService.get(user.getId());
        }
        ActionUtil.setCurrentUser(request, user_db);// 用户信息存session
        message = "登陆成功";
        Login login = new Login();
        login.setSessionId(request.getSession().getId());
        login.setUsername(user_db.getImUserName());
        login.setPassword("e10adc3949ba59abbe56e057f20f883e");//环信的登录密码为默认的
        return new Response(Status.SUCCESS, message, login);
    }

    /**
     * 用户判断自己的密码是否为空
     *
     * @param request
     * @return status : 0 --成功， 7 -- 用户不存在
     * body  : 用户数据json格式
     */
    @ResponseBody
    @RequestMapping(value = "/api/user/judgeuserPass", method = RequestMethod.GET)
    public Object judgeuserPass(HttpServletRequest request,
                                @RequestParam("sessionId") String sessionId) {
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        //User user = ActionUtil.getCurrentUser(request);

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
        User user = userService.get(obj.getId());
        if (user.getUserPass() == null || user.getUserPass().equals("")){
            return new Response(Status.SUCCESS,"",false);
        }

        return new Response(Status.SUCCESS,"",true);

    }

    /**
     * 用户判断自己的支付密码是否为空
     *
     * @param request
     * @return status : 0 --成功， 7 -- 用户不存在
     * body  : 用户数据json格式
     */
//    @UserAuthorized
    @ResponseBody
    @RequestMapping(value = "/api/user/judgeuserPay", method = RequestMethod.GET)
    public Object judgeuserPay(HttpServletRequest request,
                               @RequestParam("sessionId") String sessionId) {
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        //User user = ActionUtil.getCurrentUser(request);

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
        User user = userService.get(obj.getId());
        if (user.getUserPay() == null || user.getUserPay().equals("")){
            return new Response(Status.SUCCESS,"",false);
        }

        return new Response(Status.SUCCESS,"",true);

    }


    /**
     * 用户查询自己的详情
     * 所有user登录的用户都使用此接口
     *
     * @param request
     * @return status : 0 --成功， 7 -- 用户不存在
     * body  : 用户数据json格式
     */
//    @UserAuthorized
    @ResponseBody
    @RequestMapping(value = "/api/user/getUser", method = RequestMethod.GET)
    public Object getUser(HttpServletRequest request,
                          @RequestParam("sessionId") String sessionId) {
        int status;
        String message = "";
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        //User user = ActionUtil.getCurrentUser(request);

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
        List<Task> tasks = taskService.queryByUserId(obj.getId());
        Task task_sign = tasks.get(0);//签到任务
        Task task_binding = tasks.get(1);//绑定设备任务
        Task task_userdata = tasks.get(2);//完善个人资料任务
        if (signService.querytodaySign(obj.getId()) == null){
            task_sign.setStatus(0);//未完成今日签到
        }else {
            task_sign.setStatus(1);//已完成签到任务
        }
        if (bindingService.queryByUserId1(obj.getId()) == null||bindingService.queryByUserId1(obj.getId()).equals("")){
            task_binding.setStatus(0);//未完成绑定设备任务
        }else {
            task_binding.setStatus(1);//已完成绑定设备任务
        }
        User user_db = userService.get(obj.getId());
        if (user_db.getUserFull() == null||user_db.getUserFull().equals("") //真实姓名
                ||user_db.getUserNick()==null||user_db.getUserNick().equals("")//昵称
                ||user_db.getUserSex()==null||user_db.getUserSex().equals("")//性别
                ||user_db.getUserAge() == null||user_db.getUserAge().equals("") //年龄
                ||user_db.getUserConstellation()==null||user_db.getUserConstellation().equals("")  //星座
                ||user_db.getUserCard()==null||user_db.getUserCard().equals("")           //身份证
                ||user_db.getUserName()==null||user_db.getUserName().equals("")           //手机号
                ||user_db.getUserEmail()==null||user_db.getUserEmail().equals("")          //电子邮箱
                ||user_db.getUserAddress()==null||user_db.getUserAddress().equals("")        //默认地址
                ||user_db.getUserPay()==null||user_db.getUserPay().equals("")            //支付密码
                ||user_db.getUserPass()==null||user_db.getUserPass().equals("")){          //登录密码
            task_userdata.setStatus(0);
        }else {
            task_userdata.setStatus(1);
        }


        UserVip userVip = userVipService.selectUserVipUse(obj.getId());
        User user = userService.get(obj.getId());
        user.setUserPass("");
        user.setUserPay("");
        user.setTasks(tasks);
        if (userVip == null){
            user.setVipGrade(0);
        }else {
            user.setVipGrade(userVip.getVipGrade());
        }
        return new Response(Status.SUCCESS, user);
    }


    /**
     * 用户查询额外任务
     * 所有user登录的用户都使用此接口
     *
     * @param request
     * @return status : 0 --成功， 7 -- 用户不存在
     * body  : 用户数据json格式
     */
//    @UserAuthorized
    @ResponseBody
    @RequestMapping(value = "/api/user/getOthertasks", method = RequestMethod.GET)
    public Object getOthertasks(HttpServletRequest request,
                                @RequestParam("sessionId") String sessionId) {
        int status;
        String message = "";
        //登录时，用户信息已经保存在session中，在session中取出用户信息
        //User user = ActionUtil.getCurrentUser(request);

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
        User user = userService.get(obj.getId());
        List<Task> tasks = taskService.queryByUserId(obj.getId());

        tasks.remove(0);
        tasks.remove(0);
        tasks.remove(0);//移除前三个基础任务

        for (int i = 0;i < tasks.size();i++){
            tasks.get(i).setUrl("https://jx.tmall.com/?ali_trackid=2:mm_112628724_11936611_53308360:1500090512_2k4_2059896139");
        }

        user.setUserPass("");
        user.setUserPay("");
        user.setTasks(tasks);
        return new Response(Status.SUCCESS, user);
    }



    /**
     * 用户修改自己的信息
     *
     * @param request
     * @return status: 0 -- 成功, 1 -- 修改失败, 14 -- 登录失效
     */
    @ResponseBody
    @RequestMapping(value = "/api/user/updateUser", method = RequestMethod.POST)
    public Object updateUser(HttpServletRequest request,
                             //@RequestParam UserAndCode userAndCode,
                             @RequestBody Request req) {
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
        //User obj = ActionUtil.getCurrentUser(request);
        User user = req.getUser();
        user.setId(obj.getId());
        //user.setUserName(null);//手机号
        user.setUserPass(null);//登录密码
        //user.setUserCard(null);//身份证
        user.setUserPay(null);//支付密码

        User user1 = userService.get(obj.getId());
        UserAndCode userAndCode = req.getUserAndCode();

        //判断是否修改了手机号（也就是登录用户名），如果修改了手机号，需要验证输入的验证码是否正确
        if (user1.getUserName() == null && !userAndCode.getUserName().equals("")) {
            //获取codeSession
            if (userAndCode.getCodeSessionId() == null || userAndCode.getCodeSessionId().equals("")) {
                return new Response(Status.ERROR, "getCodeSessionId不能为空");
            }
            MySessionContext myc1 = MySessionContext.getInstance();
            HttpSession httpSession1 = myc1.getSession(userAndCode.getCodeSessionId());
            if (httpSession1 == null) {
                return new Response(Status.ERROR, "验证码失效");
            }
            //获取保存在session中的验证码和手机号
            String code = null;
            String userName = null;
            try {
                code = (String) httpSession1.getAttribute("code");//验证码
                userName = (String) httpSession1.getAttribute("userName");//手机号
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (userAndCode.getUserName() == null || userAndCode.getUserName().equals("")) {
                return new Response(Status.ERROR, "请输入手机号");
            }

            //判断验证码是否正确（判断session获取的验证码和传进来的验证码）
            if (code == null || userAndCode.getCode() == null || userAndCode.getCode().equals("") || !(code.equals(userAndCode.getCode()))) {
                message = "验证码错误";
                status = Status.CODE_NOT_MATCH;
                return new Response(status, message);
            }
            //判断传进来的手机号和session中保存的手机号是否一致
            if (!userAndCode.getUserName().equals(userName)) {
                return new Response(Status.ERROR, "手机号错误");
            }
            User user2 = userService.queryByName(userName);
            if (user2 != null) {
                return new Response(Status.ERROR, "手机号已注册");
            }
            user.setUserName(userName);
        }
        status = userService.update(user);
        //判断用户是否为邀请用户，如果是，则给邀请人双方发放邀请奖励
        List<com.aqb.cn.bean.a.ShareAward> shareAwards = shareAwardMapper.selectByShareTelStatus(user.getUserName());
        if(shareAwards != null && shareAwards.size() > 0){
            //查询奖励
            com.aqb.cn.bean.a.ShareRule shareRule = shareRuleMapper.selectByState(1);
            if(shareRule != null){
                if(shareRule.getShareRule() == 1){//积分奖励
                    Jifen jifen = new Jifen();
                    jifen.setId(UUIDCreator.getUUID());
                    jifen.setUserId(shareAwards.get(0).getUserId());
                    jifen.setJifenCategory(3);//3-邀请好友
                    jifen.setJifenFoudDate(new Date());
                    jifen.setJifenIncomeOut(true);
                    jifen.setJifenSubtotal((float) shareRule.getIntegral());
                    jifen.setJifenStatus(0);
                    jifenService.add(jifen);//奖励邀请人
                    Jifen jifen2 = new Jifen();
                    jifen2.setId(UUIDCreator.getUUID());
                    jifen2.setUserId(user.getId());
                    jifen2.setJifenCategory(3);//3-邀请好友
                    jifen2.setJifenFoudDate(new Date());
                    jifen2.setJifenIncomeOut(true);
                    jifen2.setJifenSubtotal((float) shareRule.getIntegral());
                    jifen2.setJifenStatus(0);
                    jifenService.add(jifen2);//奖励被邀请人
                    //更改数据库的状态
                    for(com.aqb.cn.bean.a.ShareAward shareAward : shareAwards){
                        shareAward.setAwardState(1);
                        shareAward.setIntegral(shareRule.getIntegral());
                        shareAwardMapper.updateByPrimaryKeySelective(shareAward);//更改已经领取的状态
                    }
                }
                if(shareRule.getShareRule() == 2){//金额奖励
                    Yue yue = new Yue();
                    yue.setId(UUIDCreator.getUUID());
                    yue.setUserId(shareAwards.get(0).getUserId());
                    yue.setYueFoudDate(new Date());
                    yue.setYueStatus(0);
                    yue.setYueCategory(12);//12--邀请好友
                    yue.setYueIncomeOut(true);
                    yue.setYueSubtotal(shareRule.getMoney().floatValue());
                    yueService.add(yue);//奖励邀请人
                    Yue yue2 = new Yue();
                    yue2.setId(UUIDCreator.getUUID());
                    yue2.setUserId(user.getId());
                    yue2.setYueFoudDate(new Date());
                    yue2.setYueStatus(0);
                    yue2.setYueCategory(12);//12--邀请好友
                    yue2.setYueIncomeOut(true);
                    yue2.setYueSubtotal(shareRule.getMoney().floatValue());
                    yueService.add(yue2);//奖励被邀请人
                    //更改数据库的状态
                    for(com.aqb.cn.bean.a.ShareAward shareAward : shareAwards){
                        shareAward.setAwardState(1);
                        shareAward.setMoney(shareRule.getMoney());
                        shareAwardMapper.updateByPrimaryKeySelective(shareAward);//更改已经领取的状态
                    }
                }
            }
        }

        User user_db = userService.get(user.getId());
        if (user_db.getUserFull() == null||user_db.getUserFull().equals("") //真实姓名
                ||user_db.getUserNick()==null||user_db.getUserNick().equals("")//昵称
                ||user_db.getUserSex()==null||user_db.getUserSex().equals("")//性别
                ||user_db.getUserAge() == null||user_db.getUserAge().equals("") //年龄
                ||user_db.getUserConstellation()==null||user_db.getUserConstellation().equals("")  //星座
                ||user_db.getUserCard()==null||user_db.getUserCard().equals("")           //身份证
                ||user_db.getUserName()==null||user_db.getUserName().equals("")           //手机号
                ||user_db.getUserEmail()==null||user_db.getUserEmail().equals("")          //电子邮箱
                ||user_db.getUserAddress()==null||user_db.getUserAddress().equals("")        //默认地址
                ||user_db.getUserPay()==null||user_db.getUserPay().equals("")            //支付密码
                ||user_db.getUserPass()==null||user_db.getUserPass().equals("")){          //登录密码

        }else {
            //判断该用户完善个人资料任务是否完成
            List<Task> tasks = taskService.queryByUserId(user_db.getId());
            Task task = tasks.get(2);//完善个人资料任务
            if (task.getStatus() == 0){
                //完成完善个人资料任务即可获得积分奖励
                Jifen jifen = new Jifen();
                jifen.setId(UUIDCreator.getUUID());
                jifen.setUserId(user.getId());
                jifen.setJifenCategory(4);//类别：完善个人资料
                jifen.setJifenIncomeOut(true);//收入
                jifen.setJifenSubtotal((float)30);//小计：30积分
                jifen.setJifenFoudDate(new Date());
                jifenService.add(jifen);
                taskService.update(task);
            }
        }

        if (status == Status.SUCCESS) {
            message = "修改成功";
            return new Response(Status.SUCCESS, message);
        } else if (status == Status.NOT_EXISTS) {
            message = "用户不存在，修改失败";
            return new Response(Status.NOT_EXISTS, message);
        } else {
            message = "修改失败";
            return new Response(Status.ERROR, message);
        }
    }

    /**
     * 重置密码
     *
     * @param request
     * @param userAndCode 手机号和短信验证码
     * @return status 0--成功, 10--验证码错误, 7 --客户不存在,9-- 密码不正确
     */
    @ResponseBody
    @RequestMapping(value = "/api/user/reset_pass", method = RequestMethod.POST)
    public Object reset_pass(HttpServletRequest request,
                             @RequestBody UserAndCode userAndCode) {
        int status;
        String message = "";
        //获取codeSession
        if (userAndCode.getCodeSessionId() == null || userAndCode.getCodeSessionId().equals("")) {
            return new Response(Status.ERROR, "getCodeSessionId不能为空");
        }
        MySessionContext myc = MySessionContext.getInstance();
        HttpSession httpSession = myc.getSession(userAndCode.getCodeSessionId());
        if (httpSession == null) {
            return new Response(Status.ERROR, "验证码失效");
        }

        //获取保存在session中的验证码和手机号
        String code = null;
        String userName = null;
        try {
            code = (String) httpSession.getAttribute("code");//验证码
            userName = (String) httpSession.getAttribute("userName");//手机号
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userAndCode.getUserName() == null || userAndCode.getUserName().equals("")) {
            return new Response(Status.ERROR, "请输入手机号");
        }

        //判断验证码是否正确（判断session获取的验证码和传进来的验证码）
        if (code == null || userAndCode.getCode() == null || userAndCode.getCode().equals("") || !(code.equals(userAndCode.getCode()))) {
            message = "验证码错误";
            status = Status.CODE_NOT_MATCH;
            return new Response(status, message);
        }
        //判断传进来的手机号和session中保存的手机号是否一致
        if (!userAndCode.getUserName().equals(userName)) {
            return new Response(Status.ERROR, "手机号错误");
        }

        //String name = userAndCode.getUserName();
        //User user = new User();
        //user.setUserName(userAndCode.getUserName());
        User user_db = userService.queryByName(userAndCode.getUserName());//查询数据库中是否存在该用户
        //手机号是否注册
        if (user_db == null || user_db.equals("")) {
            return new Response(Status.ERROR,"手机号未注册");
        }
        user_db.setUserPass(EncryptionUtil.encrypt(userAndCode.getUserPass()));
        status = userService.update(user_db);

        httpSession.removeAttribute("code");//使用完成后销毁验证码

        if (status == Status.SUCCESS) {
            message = "修改成功";
            return new Response(Status.SUCCESS, message);
        } else if (status == Status.NOT_EXISTS) {
            message = "不存在，修改失败";
            return new Response(Status.NOT_EXISTS, message);
        } else {
            message = "修改失败";
            return new Response(Status.ERROR, message);
        }
    }

    /**
     * 用户设置自己的登录密码
     *
     * @param request
     * @return status: 0 -- 成功, 1 -- 修改失败, 6 -- 已存在, 14 -- 登录失效
     */
//    @UserAuthorized
    @ResponseBody
    @RequestMapping(value = "/api/user/insertUserPass", method = RequestMethod.POST)
    public Object insertUserPass(HttpServletRequest request, @RequestBody Request req) {
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

        User user = userService.get(obj.getId());
        if (user.getUserPass() != null && !user.getUserPass().equals("")) {
            return new Response(Status.EXISTS, "登录密码已存在");
        }
        String userPass = req.getUser().getUserPass();//req传进来的支付密码
        user.setUserPass(EncryptionUtil.encrypt(userPass));

        status = userService.update(user);
        if (status == Status.SUCCESS) {
            message = "设置成功";
            return new Response(Status.SUCCESS, message);
        } else if (status == Status.NOT_EXISTS) {
            message = "用户不存在，设置失败";
            return new Response(Status.NOT_EXISTS, message);
        } else {
            message = "设置失败";
            return new Response(Status.ERROR, message);
        }
    }


    /**
     * 用户修改自己的登录密码
     *
     * @param request
     * @return status: 0 -- 成功, 1 -- 修改失败, 14 -- 登录失效
     */
    @ResponseBody
    @RequestMapping(value = "/api/user/updateUserPass", method = RequestMethod.POST)
    public Object updateUserPass(HttpServletRequest request, @RequestBody Request req) {
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

        UpdatePass updatePass = req.getUpdatePass();

        User user = userService.get(obj.getId());

        if (!user.getUserPass().equals(EncryptionUtil.encrypt(updatePass.getOldPass()))) {
            return new Response(Status.ERROR, "原密码不正确");
        }
        if (updatePass.getNewPass() == null || updatePass.getNewPass().equals("")) {
            return new Response(Status.ERROR, "新密码不能为空");
        }
        User user2 = new User();
        user2.setId(user.getId());
        user2.setUserPass(EncryptionUtil.encrypt(updatePass.getNewPass()));
        status = userService.update(user2);
        if (status == Status.SUCCESS) {
            message = "修改成功";
            return new Response(Status.SUCCESS, message);
        } else if (status == Status.NOT_EXISTS) {
            message = "不存在，修改失败";
            return new Response(Status.NOT_EXISTS, message);
        } else {
            message = "修改失败";
            return new Response(Status.ERROR, message);
        }
    }

    /**
     * 用户设置自己的支付密码
     *
     * @param request
     * @return status: 0 -- 成功, 1 -- 修改失败, 6 -- 已存在, 14 -- 登录失效
     */
//    @UserAuthorized
    @ResponseBody
    @RequestMapping(value = "/api/user/insertUserPay", method = RequestMethod.POST)
    public Object insertUserPay(HttpServletRequest request, @RequestBody Request req) {
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

        User user = userService.get(obj.getId());
        if (user.getUserPay() != null && !user.getUserPay().equals("")) {
            return new Response(Status.EXISTS, "支付密码已存在");
        }
        String userPay = req.getUser().getUserPay();//req传进来的支付密码
        user.setUserPay(EncryptionUtil.encrypt(userPay));

        status = userService.update(user);
        if (status == Status.SUCCESS) {
            message = "设置成功";
            return new Response(Status.SUCCESS, message);
        } else if (status == Status.NOT_EXISTS) {
            message = "用户不存在，设置失败";
            return new Response(Status.NOT_EXISTS, message);
        } else {
            message = "设置失败";
            return new Response(Status.ERROR, message);
        }
    }

    /**
     * 用户修改自己的支付密码
     *
     * @param request
     * @return status: 0 -- 成功, 1 -- 修改失败, 7 -- 不存在,14 -- 登录失效
     */
    @ResponseBody
    @RequestMapping(value = "/api/user/updateUserPay", method = RequestMethod.POST)
    public Object updateUserPay(HttpServletRequest request, @RequestBody Request req) {
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
        User user = userService.get(obj.getId());

        if (user.getUserPay() == null || user.getUserPay().equals("")) {
            return new Response(Status.NOT_EXISTS, "不存在，修改失败");
        }

        UpdatePass updatePass = req.getUpdatePass();
        if (!user.getUserPay().equals(EncryptionUtil.encrypt(updatePass.getOldPass()))) {
            return new Response(Status.ERROR, "原密码不正确");
        }
        if (updatePass.getNewPass() == null || updatePass.getNewPass().equals("")) {
            return new Response(Status.ERROR, "新密码不能为空");
        }
        User user2 = new User();
        user2.setId(user.getId());
        user2.setUserPay(EncryptionUtil.encrypt(updatePass.getNewPass()));
        status = userService.update(user2);
        if (status == Status.SUCCESS) {
            message = "修改成功";
            return new Response(Status.SUCCESS, message);
        } else if (status == Status.NOT_EXISTS) {
            message = "不存在，修改失败";
            return new Response(Status.NOT_EXISTS, message);
        } else {
            message = "修改失败";
            return new Response(Status.ERROR, message);
        }
    }


    /**
     * 上传头像
     * 安卓传字符串
     * @param request
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/user/uploadHead", method = RequestMethod.POST)
    public Object uploadHead(HttpServletRequest request,
                             //@RequestParam("sessionId")String sessionId,
                             //@RequestParam("file")MultipartFile multipartFile,
                             @RequestBody Request req) {
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

        User user = req.getUser();
        user.setId(obj.getId());

        User user1 = userService.get(obj.getId());

        //头像
        String userHead = UUIDCreator.getUUID();
        try {
            ImageUtil.generateImage(user.getUserHead(), "/home/java/aqb/userHead/" + userHead + ".jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setUserHead("http://47.94.45.159:8080/aqb/userHead/" + userHead + ".jpg");

        user1.setUserHead(user.getUserHead());

        status = userService.update(user1);
        return new Response(status, "上传成功");
    }

    /**
     * 测试接口
     */
    @ResponseBody
    @RequestMapping(value = "/api/user/test", method = RequestMethod.GET)
    public Object test(){



        return "aa";
    }

    /**
     * 上传头像
     * IOS传文件流
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/user/uploadHeadFileByte", method = RequestMethod.POST)
    public Object uploadHeadFileByte( @RequestParam("sessionId") String sessionId,
            @RequestParam MultipartFile imageName
    )
            throws IOException, FileUploadException {


        System.out.println("================================进入上传图片请求====================================");
        System.out.println("================================获取用户信息====================================");

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
        User user = userService.get(obj.getId());
        System.out.println("================================图片上传====================================");

        try {
            File file = new File("/home/java/aqb/userHead/");
            String name = UUIDCreator.getUUID()+imageName.getOriginalFilename().substring(imageName.getOriginalFilename().lastIndexOf("."));

            FileCopyUtils.copy(
                    imageName.getBytes(), new FileOutputStream(
                            new File(file + "/" + name)));

            user.setUserHead("http://47.94.45.159:8080/aqb/userHead/"+name);
            int status = userService.update(user);
            return new Response(Status.SUCCESS,"uploadFile - ok");
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(Status.ERROR,"uploadFile - failed");
        }

        }


        /**
         * 上传头像
         * IOS传文件
         * @param request
         * @param
         * @return
         */
        @ResponseBody
        @RequestMapping(value = "/api/user/uploadHeadFile", method = RequestMethod.POST)
        public Object uploadHeadFile(HttpServletRequest request,
                @RequestParam("sessionId")String sessionId,
                MultipartFile multipartFile) {
            int status;
            String message = "";

            //判断用户的session是否正常
            //获取session
            if (sessionId == null || sessionId.equals("")) {
                return new Response(Status.ERROR, "sessionId不能为空");
            }
            MySessionContext myc = MySessionContext.getInstance();
            HttpSession httpSession = myc.getSession(sessionId);
            if (httpSession == null) {
                return new Response(14, "登录失效");
            }
            //登录时，用户信息已经保存在session中，在session中取出用户信息
            User obj = ActionUtil.getCurrentUser(httpSession);
            if (obj == null) {
                return new Response(14, "登录失效");
            }
            User user = userService.get(obj.getId());

            if(multipartFile != null){
                //保存文件
                String fileName = multipartFile.getOriginalFilename(),
                        headUUID = UUIDCreator.getUUID(),
                        saveName = headUUID + "." + FileUtil.getExtensions(fileName),
//                        address = PicAction.PICTURE_UPLOAD_PATH + File.separator + saveName,
//                    path = ShouyeLunboAction.HOME_shouyelunbotu + saveName;
                        path = "/home/java/aqb/userHead/" + saveName;

                File file = new File(path);
                try {
                    if(!file.exists()){
                        file.createNewFile();
                    }
                    multipartFile.transferTo(file);

                    String URL = "http://47.94.45.159:8080/aqb/userHead/" + saveName;
                    //更新数据库
                    User user1 = new User();
                    user1.setId(user.getId());
                    user1.setUserHead(URL);
                    userService.update(user1);
                    //上传图片成功，返回URL
                    return new Response(Status.SUCCESS,"上传成功",URL);

                }catch (IOException e){
                    e.printStackTrace();
                    logger.info("存储文件失败");
                    return new Response(Status.ERROR,"存储文件时出现异常");
                }
            }
            return new Response(Status.ERROR,"文件不能为空");
        }

        /**
         * 上传封面图
         *
         * @param request
         * @param
         * @return
         */
        @ResponseBody
        @RequestMapping(value = "/api/user/uploadCover", method = RequestMethod.POST)
        public Object uploadCover(HttpServletRequest request,
                @RequestBody Request req) {
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

            User user = req.getUser();
            user.setId(obj.getId());

            User user1 = userService.get(obj.getId());

            //头像
            String userCover = UUIDCreator.getUUID();
            try {
                ImageUtil.generateImage(user.getUserCover(), "/home/java/aqb/userCover/" + userCover + ".jpg");
            } catch (Exception e) {
                e.printStackTrace();
            }
            user.setUserCover("http://47.94.45.159:8080/aqb/userCover/" + userCover + ".jpg");

            user1.setUserCover(user.getUserCover());
            status = userService.update(user1);
            return new Response(status, "上传成功");
        }

        /**
         * 判断sessionId是否失效
         *
         * @param request
         * @param
         * @return
         */
        @ResponseBody
        @RequestMapping(value = "api/user/decide_sessionId", method = RequestMethod.GET)
        public Object decide_sessionId(HttpServletRequest request,
                @RequestParam("sessionId") String sessionId) {
            int status;
            String message = "";

            //判断用户的session是否正常
            //获取session
            if (sessionId == null || sessionId.equals("")) {
                return new Response(0, "sessionId不能为空",false);
            }
            MySessionContext myc = MySessionContext.getInstance();
            HttpSession httpSession = myc.getSession(sessionId);
            if (httpSession == null) {
                return new Response(0, "sessionId失效",true);
            }
            //登录时，用户信息已经保存在session中，在session中取出用户信息
            User obj = ActionUtil.getCurrentUser(httpSession);
            if (obj == null) {
                return new Response(0, "sessionId失效",true);
            }
            return new Response(Status.SUCCESS, "",false);
        }
    }