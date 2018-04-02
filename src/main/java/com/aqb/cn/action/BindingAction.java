package com.aqb.cn.action;

import com.aqb.cn.annotation.AdminLogin;
import com.aqb.cn.bean.*;
import com.aqb.cn.common.ActionUtil;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.pojo.BindingContent;
import com.aqb.cn.service.*;
import com.aqb.cn.utils.ImageUtil;
import com.aqb.cn.utils.Request;
import com.aqb.cn.utils.UUIDCreator;
import com.aqb.cn.utils.getSession.MySessionContext;
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
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/5.
 */
@Controller
public class BindingAction {

    private final Log logger = LogFactory.getLog(BindingAction.class);

    @Autowired
    private BindingService bindingService;
    @Autowired
    private BindingNumberService bindingNumberService;
    @Autowired
    private UserService userService;
    @Autowired
    private DepositService depositService;
    @Autowired
    private NewNoticeService newNoticeService;


    /**
     * 判断用户的身份证号和真实姓名是否为空
     *
     * @param request
     * @return status : 0 --成功， 7 -- 用户不存在
     * body  : 用户数据json格式
     */
    @ResponseBody
    @RequestMapping(value = "/api/binding/judge_userCarduserFull", method = RequestMethod.GET)
    public Object judge_userCarduserFull(HttpServletRequest request,
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
        if (user.getUserName() == null || user.getUserName().equals("")){
            return new Response(Status.SUCCESS,"",false);
        }
        if (user.getUserFull() == null || user.getUserFull().equals("")){
            return new Response(Status.SUCCESS,"请完善真实姓名信息",false);
        }
        if (user.getUserCard() == null || user.getUserCard().equals("")){
            return new Response(Status.SUCCESS,"请完善身份证号码信息",false);
        }

        return new Response(Status.SUCCESS,"",true);

    }

    /*
     * 新增绑定设备
     * 安卓  绑定设备接口
     * @param req 绑定设备信息
     * @return state : 0 -- 成功, 6--绑定设备已存在
 */
    @ResponseBody
    @RequestMapping(value = "/api/binding/addbinding", method = RequestMethod.POST)
    public Object addbinding(HttpServletRequest request, @RequestBody Request req) {
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
        User user = userService.get(obj.getId());
        //从Request中获取绑定设备参数
        Binding binding = req.getBinding();
        binding.setUserId(obj.getId());
        binding.setId(UUIDCreator.getUUID());
        binding.setFoundDate(new Date());
        binding.setBindingIdcard(user.getUserCard());
        if (binding.getBindingNumber() == null || binding.getBindingNumber().equals("")){
            return new Response(Status.ERROR,"设备号不能为空");
        }
        if (binding.getBindingCar() == null || binding.getBindingCar().equals("")){
            return new Response(Status.ERROR,"车牌号不能为空");
        }
        if (binding.getUserId() == null || binding.getUserId().equals("")){
            return new Response(Status.ERROR,"UserId不能为空");
        }
        if (binding.getIdcardFront() == null || binding.getIdcardFront().equals("")){
            return new Response(Status.ERROR,"身份证不能为空");
        }
        if (binding.getIdcardBack() == null || binding.getIdcardBack().equals("")){
            return new Response(Status.ERROR,"身份证不能为空");
        }
        if (binding.getTravelFront() == null || binding.getTravelFront().equals("")){
            return new Response(Status.ERROR,"行驶证不能为空");
        }
        if (binding.getTravelBack() == null || binding.getTravelBack().equals("")){
            return new Response(Status.ERROR,"行驶证不能为空");
        }
        //用户已成功绑定一个设备，数据库有记录且审核通过
        List<Binding> bindings = bindingService.queryByUserId2(obj.getId());
        for (Binding binding1:bindings){
            if (binding1!=null && binding1.getStatus()==2){
                return new Response(Status.ERROR,"该用户只可绑定一个设备");
            }
            if (binding1!=null && binding1.getStatus()==1){
                return new Response(Status.ERROR,"该用户只可绑定一个设备");
            }
        }
        //验证设备号的正确性
        BindingNumber bindingNumber = bindingNumberService.selectByDeviceNumber(binding.getBindingNumber());
        if(bindingNumber == null){
            return new Response(Status.ERROR,"设备号不存在");
        }
        if(bindingNumber.getStatus() == 2){
            return new Response(Status.ERROR,"设备已绑定");
        }

        //身份证背面
        String idcardBack = UUIDCreator.getUUID();
        try {
            ImageUtil.generateImage(binding.getIdcardBack(),"/home/java/aqb/pic/"+idcardBack+".jpg");
        }catch (Exception e){
            e.printStackTrace();
        }
        binding.setIdcardBack("http://47.94.45.159:8080/aqb/pic/"+idcardBack+".jpg");

        //身份证正面
        String idcardFront = UUIDCreator.getUUID();
        try {

            ImageUtil.generateImage(binding.getIdcardFront(),"/home/java/aqb/pic/"+idcardFront+".jpg");
        }catch (Exception e){
            e.printStackTrace();
        }
        binding.setIdcardFront("http://47.94.45.159:8080/aqb/pic/" + idcardFront + ".jpg");

        //行驶证正面
        String travelFront = UUIDCreator.getUUID();
        try {
            ImageUtil.generateImage(binding.getTravelFront(),"/home/java/aqb/pic/"+travelFront+".jpg");
        }catch (Exception e){
            e.printStackTrace();
        }
        binding.setTravelFront("http://47.94.45.159:8080/aqb/pic/" + travelFront + ".jpg");

        //行驶证背面
        String travelBack = UUIDCreator.getUUID();
        try {
            ImageUtil.generateImage(binding.getTravelBack(),"/home/java/aqb/pic/"+travelBack+".jpg");
        }catch (Exception e){
            e.printStackTrace();
        }
        binding.setTravelBack("http://47.94.45.159:8080/aqb/pic/" + travelBack + ".jpg");
        binding.setStatus(2);//状态（0--审核不通过，1--审核不通过，2-审核中，3-已解绑，4-解绑审核中），开始都为审核中

        //查看是否缴纳押金即押金表是否有该用户的记录
        Deposit deposit = depositService.queryDepositByUserId(obj.getId());
        if (deposit == null || deposit.equals("")){
            binding.setDepositStatus(0);//押金状态（0-未缴纳，1-已缴纳）
        }else {
            binding.setDepositStatus(1);//押金状态（0-未缴纳，1-已缴纳）
        }
        status = bindingService.add(binding);
        if (status == Status.SUCCESS) {
            message = "绑定设备成功";
            //修改设备表对应设备的状态
            BindingNumber bindingNumber_db = new BindingNumber();
            bindingNumber_db.setId(bindingNumber.getId());
            bindingNumber_db.setStatus(2);
            if (bindingNumberService.update(bindingNumber_db) == 0){
                return new Response(status, message, binding.getDepositStatus());
            }
        }
        if (status == Status.EXISTS) {
            message = "绑定设备已经存在";
            return new Response(status, message);
        }
        message = "绑定设备失败";
        return new Response(status, message);
    }

    /**
     * 上传图片
     * IOS传文件流
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/binding/uploadPicFileByte", method = RequestMethod.POST)
    public Object uploadHeadFileByte( @RequestParam("sessionId") String sessionId,
                                      @RequestParam("number") String number,
                                      @RequestParam MultipartFile imageName
    )
            throws IOException, FileUploadException {


        System.out.println("================================进入上传图片请求====================================");
        System.out.println("================================获取用户信息====================================");

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
            File file = new File("/home/java/aqb/pic/");
            String name = UUIDCreator.getUUID()+imageName.getOriginalFilename().substring(imageName.getOriginalFilename().lastIndexOf("."));

            FileCopyUtils.copy(
                    imageName.getBytes(), new FileOutputStream(
                            new File(file + "/" + name)));

            //把图片访问路径保存到session
            httpSession.setAttribute("pic"+number,"http://47.94.45.159:8080/aqb/pic/"+name);

            return new Response(Status.SUCCESS,"uploadFile - ok");
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(Status.ERROR,"uploadFile - failed");
        }
    }

    /*
     * 新增绑定设备
     * IOS  使用该接口绑定设备
     * @param req 绑定设备信息
     * @return state : 0 -- 成功, 6--绑定设备已存在
 */

    @ResponseBody
    @RequestMapping(value = "/api/binding/addbindingIOS", method = RequestMethod.POST)
    public Object addbindingIOS(HttpServletRequest request, @RequestBody Request req) {
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
        User user = userService.get(obj.getId());
        //从Request中获取绑定设备参数
        Binding binding = req.getBinding();
        binding.setUserId(obj.getId());
        binding.setId(UUIDCreator.getUUID());
        binding.setFoundDate(new Date());
        binding.setBindingIdcard(user.getUserCard());
        if (binding.getBindingNumber() == null || binding.getBindingNumber().equals("")){
            return new Response(Status.ERROR,"设备号不能为空");
        }
        if (binding.getBindingCar() == null || binding.getBindingCar().equals("")){
            return new Response(Status.ERROR,"车牌号不能为空");
        }
        if (binding.getUserId() == null || binding.getUserId().equals("")){
            return new Response(Status.ERROR,"UserId不能为空");
        }

        //用户已成功绑定一个设备，数据库有记录且审核通过
        List<Binding> bindings = bindingService.queryByUserId2(obj.getId());
        for (Binding binding1:bindings){
            if (binding1!=null && binding1.getStatus()==2){
                return new Response(Status.ERROR,"该用户只可绑定一个设备");
            }
            if (binding1!=null && binding1.getStatus()==1){
                return new Response(Status.ERROR,"该用户只可绑定一个设备");
            }
        }
        //验证设备号的正确性
        BindingNumber bindingNumber = bindingNumberService.selectByDeviceNumber(binding.getBindingNumber());
        if(bindingNumber == null){
            return new Response(Status.ERROR,"设备号不存在");
        }
        if(bindingNumber.getStatus() == 2){
            return new Response(Status.ERROR,"设备已绑定");
        }

        if(httpSession.getAttribute("pic1") == null ||
                httpSession.getAttribute("pic2") == null ||
                httpSession.getAttribute("pic3") == null ||
                httpSession.getAttribute("pic4") == null){
            return new Response(Status.ERROR,"图片不能为空");
        }

        //身份证正面
        String idcardFront = (String)httpSession.getAttribute("pic1");
        binding.setIdcardFront(idcardFront);
        //身份证背面
        String idcardBack = (String)httpSession.getAttribute("pic2");
        binding.setIdcardBack(idcardBack);
        //行驶证正面
        String travelFront = (String)httpSession.getAttribute("pic3");
        binding.setTravelFront(travelFront);
        //行驶证背面
        String travelBack = (String)httpSession.getAttribute("pic4");
        binding.setTravelBack(travelBack);

        binding.setStatus(2);//状态（0--审核不通过，1--审核不通过，2-审核中，3-已解绑，4-解绑审核中），开始都为审核中

        //查看是否缴纳押金即押金表是否有该用户的记录
        Deposit deposit = depositService.queryDepositByUserId(obj.getId());
        if (deposit == null || deposit.equals("")){
            binding.setDepositStatus(0);//押金状态（0-未缴纳，1-已缴纳）
        }else {
            binding.setDepositStatus(1);//押金状态（0-未缴纳，1-已缴纳）
        }
        status = bindingService.add(binding);
        if (status == Status.SUCCESS) {
            message = "绑定设备成功";
            //修改设备表对应设备的状态
            BindingNumber bindingNumber_db = new BindingNumber();
            bindingNumber_db.setId(bindingNumber.getId());
            bindingNumber_db.setStatus(2);
            if (bindingNumberService.update(bindingNumber_db) == 0){
                return new Response(status, message, binding.getDepositStatus());
            }
        }
        if (status == Status.EXISTS) {
            message = "绑定设备已经存在";
            return new Response(status, message);
        }
        message = "绑定设备失败";
        return new Response(status, message);
    }

    /*
    * 申请解除绑定
    *
    * @param request
    * @param binding
    * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
    */
    @ResponseBody
    @RequestMapping(value = "/api/binding/applyUnbinding", method = RequestMethod.POST)
    public Object applyUnbinding(HttpServletRequest request, @RequestBody Request req) {
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
        Binding binding = req.getBinding();
        if (binding.getBindingNumber().equals("")){
            return new Response(Status.ERROR,"设备号不能为空");
        }
        if (binding.getRelieve() == null || binding.getRelieve().equals("")){
            return new Response(Status.ERROR,"请填写解绑理由");
        }


        //BindingNumber bindingNumber = bindingNumberService.selectByDeviceNumber(binding.getBindingNumber());
        Binding binding_db = bindingService.selectByBN(binding.getBindingNumber());//通过设备号 查询 成功绑定且缴纳押金的设备

        //更新binding表
        binding_db.setStatus(4);//状态（0--审核不通过，1--审核通过，2-审核中,3-已解绑，4-解绑审核中）
        binding_db.setRelieve(binding.getRelieve());
        status = bindingService.update(binding_db);
        if (status == Status.NOT_EXISTS) {
            message = "绑定设备不存在";
        }
        if (status == Status.ERROR) {
            message = "申请解绑失败";
        }
        if (status == Status.SUCCESS) {
            message = "申请解绑成功";
        }
        return new Response(status, message);
    }

/*
     * 修改绑定设备
     *
     * @param request
     * @param binding     binding.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 绑定设备不存在, 1 -- 修改失败
*/
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/binding/updatebinding", method = RequestMethod.POST)
    public Object updatebinding(HttpServletRequest request, @RequestBody Binding binding) {
        int status = Status.ERROR;
        String message = "";
        if (binding.getId() == null) {
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = bindingService.update(binding);
        if (status == Status.SUCCESS) {
            message = "修改成功";
        } else if (status == Status.NOT_EXISTS) {
            message = "绑定设备不存在，修改失败";
        } else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /*
     * 解除绑定设备
     * 后台
     * @param request
     * @param binding     binding.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 绑定设备不存在, 1 -- 修改失败
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/binding/updateRelieveBinding", method = RequestMethod.POST)
    public Object updateRelieveBinding(HttpServletRequest request, @RequestBody Binding binding) {
        int status = Status.ERROR;
        String message = "";
        if (binding.getId() == null) {
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        BindingNumber bindingNumber_db = bindingNumberService.get(binding.getId());
        Binding binding_db = bindingService.queryByBindingNumber(bindingNumber_db.getDeviceNumber());
        if(binding_db == null){
            return new Response(Status.ERROR, "id不存在");
        }
        if(binding_db.getDepositStatus() != 1){//押金状态（0-未缴纳，1-已缴纳）
            return new Response(Status.ERROR, "未缴纳押金，无法解绑");
        }
        if(binding_db.getStatus() != 1){//状态（0--审核不通过，1--审核通过，2-审核中,3-已解绑）
            return new Response(Status.ERROR, "审核未通过，无法解绑");
        }
        binding_db.setStatus(3);
        binding_db.setRelieve(binding.getRelieve());//修改解绑原因
        status = bindingService.update(binding_db);
        if (status == Status.SUCCESS) {
            message = "解绑成功";
            BindingNumber bindingNumber = bindingNumberService.selectByDeviceNumber(binding_db.getBindingNumber());
            bindingNumber.setStatus(0);
            bindingNumberService.update(bindingNumber);
        } else if (status == Status.NOT_EXISTS) {
            message = "绑定设备不存在，解绑失败";
        } else {
            message = "解绑失败";
        }
        return new Response(status, message);
    }

    /*
     * 后台绑定设备
     * 后台
     * @param request
     * @param binding     binding.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 绑定设备不存在, 1 -- 修改失败
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/binding/updateBindingAdmin", method = RequestMethod.POST)
    public Object updateBindingAdmin(HttpServletRequest request, @RequestBody Binding binding) {
        int status = Status.ERROR;
        String message = "";
        if (binding.getBindingNumber() == null) {
            message = "传参错误，设备号不能为空";
            return new Response(Status.ERROR, message);
        }
        BindingNumber bindingNumber = bindingNumberService.selectByDeviceNumber(binding.getBindingNumber());
        if(bindingNumber == null || bindingNumber.getStatus() != 0){
            return new Response(Status.ERROR, "该设备无法绑定");
        }
        if (binding.getUserId() == null) {
            message = "传参错误，手机号不能为空";
            return new Response(Status.ERROR, message);
        }
        User user = userService.queryByName(binding.getUserId());
        if(user == null || user.getUserName() == null){
            return new Response(Status.ERROR, "手机号错误，无法绑定");
        }
        binding.setUserId(user.getId());
        binding.setId(UUIDCreator.getUUID());
        binding.setFoundDate(new Date());
        binding.setDepositStatus(1);//押金状态（0-未缴纳，1-已缴纳）
        binding.setStatus(1);//状态（0--审核不通过，1--审核通过，2-审核中,3-已解绑）

        status = bindingService.add(binding);
        if (status == Status.SUCCESS) {
            message = "绑定成功";
            bindingNumber.setStatus(2);
            bindingNumberService.update(bindingNumber);//修改设备表中的状态
            return new Response(status, message);
        }
        if (status == Status.EXISTS) {
            message = "绑定设备已经存在";
            return new Response(status, message);
        }
        return new Response(status, "绑定失败");
    }

    /**
     * 设备绑定列表查询
     * 后台
     * bindingNumber 设备号
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/binding/queryBindingAdmin", method = RequestMethod.GET)
    public Object queryBindingAdmin(HttpServletRequest request,
                                           @RequestParam(value = "currentPage", required = false) Long currentPage,
                                           @RequestParam(value = "maxRow", required = false) Long maxRow,
                                           @RequestParam(value = "bindingNumber", required = false) String bindingNumber) {
        QueryBase query = new QueryBase();
        if (currentPage != null) {
            query.setCurrentPage(currentPage);
        }
        if(maxRow != null){
            query.setMaxRow(maxRow);
        }
        Map map = query.getParameters();
        if(bindingNumber != null){
            map.put("bindingNumber",bindingNumber);
        }
        bindingService.queryBindingAdmin(query);
        return new Response(Status.SUCCESS,query.getTotalRow(),query.getTotalPage(),query.getResults(),query.getCurrentPage());
    }


/*
     * 选择绑定设备
     *
     * @param request
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
*/

    @ResponseBody
    @RequestMapping(value = "/api/binding/selectbinding", method = RequestMethod.POST)
    public Object selectbinding(HttpServletRequest request, @RequestBody Request req) {
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
        //Deposit deposit = depositService.queryDepositByUserId(obj.getId());
        BindingContent bindingContent = new BindingContent();
        Binding binding = bindingService.queryByUserId1(obj.getId());//查询成功绑定的设备 status = 1
        Binding binding3 = bindingService.queryByUserId3(obj.getId());//查询审核中的绑定设备 status = 2
        List<Binding> bindings = bindingService.queryByUserId4(obj.getId());//查询未通过审核的设备 status = 0
        List<Binding> binding_relieveList = bindingService.queryRelieveByUserId(obj.getId());//查询解绑审核中的绑定设备 status = 4
        List<Binding> binding_unRelieveList = bindingService.queryUnRelieveByUserId(obj.getId());//查询解绑审核不通过的绑定设备 status = 5

        if (binding != null){
            //绑定完成后展示成功绑定的设备内容
            bindingContent.setCarInfo(binding.getBindingCar());
            bindingContent.setBindingNumber(binding.getBindingNumber());
            bindingContent.setUserFull(user.getUserFull());
            bindingContent.setUserCard(user.getUserCard());
            bindingContent.setAuditStatus(binding.getStatus());
            bindingContent.setDepositStatus(binding.getDepositStatus());
            bindingContent.setReason(binding.getReason());
            bindingContent.setRelieve("");
        }else if (binding3 != null){
            //绑定完成后展示审核内容
            bindingContent.setCarInfo(binding3.getBindingCar());
            bindingContent.setBindingNumber(binding3.getBindingNumber());
            bindingContent.setUserFull(user.getUserFull());
            bindingContent.setUserCard(user.getUserCard());
            bindingContent.setAuditStatus(binding3.getStatus());
            bindingContent.setDepositStatus(binding3.getDepositStatus());
            bindingContent.setReason(binding3.getReason());
            bindingContent.setRelieve("");
        }else if(binding_relieveList.size() > 0) {
            Binding binding_relieve = binding_relieveList.get(0);
            if(binding_relieve != null){
                //绑定完成后展示审核内容
                bindingContent.setCarInfo(binding_relieve.getBindingCar());
                bindingContent.setBindingNumber(binding_relieve.getBindingNumber());
                bindingContent.setUserFull(user.getUserFull());
                bindingContent.setUserCard(user.getUserCard());
                bindingContent.setAuditStatus(binding_relieve.getStatus());
                bindingContent.setDepositStatus(binding_relieve.getDepositStatus());
                bindingContent.setReason("");
                bindingContent.setRelieve(binding_relieve.getRelieve());
            }
        }else if(binding_unRelieveList.size() > 0) {
            Binding binding_UnRelieve = binding_unRelieveList.get(0);
            if(binding_UnRelieve != null){
                //绑定完成后展示审核内容
                bindingContent.setCarInfo(binding_UnRelieve.getBindingCar());
                bindingContent.setBindingNumber(binding_UnRelieve.getBindingNumber());
                bindingContent.setUserFull(user.getUserFull());
                bindingContent.setUserCard(user.getUserCard());
                bindingContent.setAuditStatus(binding_UnRelieve.getStatus());
                bindingContent.setDepositStatus(binding_UnRelieve.getDepositStatus());
                bindingContent.setReason(binding_UnRelieve.getReason());
                bindingContent.setRelieve(binding_UnRelieve.getRelieve());
                //更改状态为已成功绑定设备
                binding_UnRelieve.setStatus(1);
                bindingService.update(binding_UnRelieve);
            }
        }
        else if (bindings.size() > 0){
            if (bindings.get(0) != null){
                //绑定完成后展示未通过内容
                bindingContent.setCarInfo(bindings.get(0).getBindingCar());
                bindingContent.setBindingNumber(bindings.get(0).getBindingNumber());
                bindingContent.setUserFull(user.getUserFull());
                bindingContent.setUserCard(user.getUserCard());
                bindingContent.setAuditStatus(bindings.get(0).getStatus());
                bindingContent.setDepositStatus(bindings.get(0).getDepositStatus());
                bindingContent.setReason(bindings.get(0).getReason());
                bindingContent.setRelieve("");
            }
        }
        else{
            return new Response(Status.ERROR);
        }
        return new Response(Status.SUCCESS,"成功",bindingContent);
    }

    /**
     * 后台绑定设备列表查询
     * 后台绑定设备审核时使用
     *
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/binding/adminBinding", method = RequestMethod.GET)
    public Object adminBinding(HttpServletRequest request) {
        List<Binding> bindings = bindingService.adminBinding();
        for (Binding binding : bindings) {
            User user = userService.get(binding.getUserId());
            binding.setUserName(user.getUserName());//设置手机号
        }
        return new Response(Status.SUCCESS,"成功",bindings);
    }

    /**
     * 设备绑定审核(成功)
     * 后台管理接口
     * @param request
     * @param binding
     * @return status: 0 -- 成功, 1 -- 失败
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/binding/examineBinding", method = RequestMethod.POST)
    public Object examineBinding(HttpServletRequest request, @RequestBody Binding binding) {
        int status = Status.ERROR;
        String message = "";
        if(binding.getId() == null || binding.getId().equals("")){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        if(binding.getStatus() == null){
            message = "审核状态你不能为空";
            return new Response(Status.ERROR, message);
        }
        Binding binding1 = new Binding();
        binding1.setId(binding.getId());
        binding1.setStatus(binding.getStatus());
        status = bindingService.update(binding1);
        if(status == Status.SUCCESS){
            message = "修改成功";
            //添加通知消息
            NewNotice newNotice = new NewNotice();
            newNotice.setId(UUIDCreator.getUUID());
            newNotice.setUserId(bindingService.get(binding.getId()).getUserId());
            newNotice.setStatus(1);
            newNotice.setFoundDate(new Date());
            newNotice.setNoticeTitle("绑定设备审核");
            newNotice.setNoticeType(1);//类型(1--通过，2--不通过)
            newNotice.setNoticeContent("审核已经通过");
            newNoticeService.add(newNotice);
        }else if(status == Status.NOT_EXISTS){
            message = "设备不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 设备绑定审核(失败)
     * 后台管理接口
     * @param request
     * @param binding
     * @return status: 0 -- 成功, 1 -- 失败
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/binding/examineBindingerror", method = RequestMethod.POST)
    public Object examineBindingerror(HttpServletRequest request, @RequestBody Binding binding) {
        int status = Status.ERROR;
        String message = "";
        if(binding.getId() == null || binding.getId().equals("")){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        if(binding.getStatus() == null){
            message = "审核状态你不能为空";
            return new Response(Status.ERROR, message);
        }
        Binding binding1 = new Binding();
        binding1.setId(binding.getId());
        binding1.setStatus(binding.getStatus());
        binding1.setReason(binding.getReason());
        status = bindingService.update(binding1);
        //审核失败，设备号表bindingNumber中状态status设置为0，状态（0--未绑定，2--已绑定）
        Binding binding_db = bindingService.get(binding.getId());
        BindingNumber bindingNumber = bindingNumberService.selectByDeviceNumber(binding_db.getBindingNumber());
        bindingNumber.setStatus(0);
        bindingNumberService.update(bindingNumber);
        if(status == Status.SUCCESS){
            message = "修改成功";
            //添加通知消息
            NewNotice newNotice = new NewNotice();
            newNotice.setId(UUIDCreator.getUUID());
            newNotice.setUserId(bindingService.get(binding.getId()).getUserId());
            newNotice.setStatus(1);
            newNotice.setFoundDate(new Date());
            newNotice.setNoticeTitle("绑定设备审核");
            newNotice.setNoticeType(2);//类型(1--通过，2--不通过)
            newNotice.setNoticeContent("审核不通过");
            newNoticeService.add(newNotice);
        }else if(status == Status.NOT_EXISTS){
            message = "设备不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 解除绑定审核列表
     * 后台
     *
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/binding/unboundAudit", method = RequestMethod.GET)
    public Object unboundAudit(HttpServletRequest request) {
        List<Binding> bindings = bindingService.unboundAuditList();
        for (Binding binding : bindings) {
            User user = userService.get(binding.getUserId());
            binding.setIdcardBack("");
            binding.setIdcardFront("");
            binding.setTravelBack("");
            binding.setTravelFront("");
            binding.setUserName(user.getUserName());//设置手机号
        }
        return new Response(Status.SUCCESS,"成功",bindings);
    }

    /**
     * 设备解绑审核(成功)
     * 后台
     * @param request
     * @param binding
     * @return status: 3-已解绑，4-解绑审核中，5-解绑审核不通过
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/binding/unboundAuditSuccess", method = RequestMethod.POST)
    public Object unboundAuditSuccess(HttpServletRequest request, @RequestBody Binding binding) {
        int status = Status.ERROR;
        String message = "";
        if(binding.getId() == null || binding.getId().equals("")){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        Binding binding_db = bindingService.get(binding.getId());
        if (binding_db.getStatus() == 3){
            return new Response(Status.ERROR, "您已解绑该设备");
        }
        binding.setStatus(3);// 3-已解绑，4-解绑审核中，5-解绑审核不通过
        status = bindingService.update(binding);
        if(status == Status.SUCCESS){
            message = "解绑成功";
            BindingNumber bindingNumber = bindingNumberService.selectByDeviceNumber(binding_db.getBindingNumber());
            bindingNumber.setStatus(0);
            bindingNumberService.update(bindingNumber);
            //添加通知消息
            NewNotice newNotice = new NewNotice();
            newNotice.setId(UUIDCreator.getUUID());
            newNotice.setUserId(bindingService.get(binding.getId()).getUserId());
            newNotice.setStatus(1);//状态(1--未读，2--已读）
            newNotice.setFoundDate(new Date());
            newNotice.setNoticeTitle("解除绑定设备审核");
            newNotice.setNoticeType(1);//类型(1--通过，2--不通过)
            newNotice.setNoticeContent("审核已经通过");
            newNoticeService.add(newNotice);
        }else if(status == Status.NOT_EXISTS){
            message = "设备不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }
    /**
     * 设备绑定审核(失败)
     * 后台管理接口
     * @param request
     * @param binding
     * @return status: 3-已解绑，4-解绑审核中，5-解绑审核不通过
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/binding/unboundAudiError", method = RequestMethod.POST)
    public Object unboundAudiError(HttpServletRequest request, @RequestBody Binding binding) {
        int status = Status.ERROR;
        String message = "";
        if(binding.getId() == null || binding.getId().equals("")){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        Binding binding_db = bindingService.get(binding.getId());
        if (binding_db.getStatus() == 5){
            return new Response(Status.ERROR, "该设备解绑失败，请重新申请解绑");
        }
        binding.setStatus(5);// 3-已解绑，4-解绑审核中，5-解绑审核不通过
        status = bindingService.update(binding);
        if(status == Status.SUCCESS){
            message = "解绑失败";
            //添加通知消息
            NewNotice newNotice = new NewNotice();
            newNotice.setId(UUIDCreator.getUUID());
            newNotice.setUserId(bindingService.get(binding.getId()).getUserId());
            newNotice.setStatus(1);
            newNotice.setFoundDate(new Date());
            newNotice.setNoticeTitle("解除绑定设备审核");
            newNotice.setNoticeType(2);//类型(1--通过，2--不通过)
            newNotice.setNoticeContent("审核不通过");
            newNoticeService.add(newNotice);
        }else if(status == Status.NOT_EXISTS){
            message = "设备不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }
}
