package com.aqb.cn.action;

import com.aqb.cn.annotation.AdminLogin;
import com.aqb.cn.bean.NewNotice;
import com.aqb.cn.bean.Repair;
import com.aqb.cn.bean.RepairComment;
import com.aqb.cn.bean.User;
import com.aqb.cn.common.ActionUtil;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.service.NewNoticeService;
import com.aqb.cn.service.RepairCommentService;
import com.aqb.cn.utils.ImageUtil;
import com.aqb.cn.utils.Request;
import com.aqb.cn.utils.UUIDCreator;
import com.aqb.cn.utils.getSession.MySessionContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/28.
 */
@Controller
public class RepairCommentAction {

    private final Log logger = LogFactory.getLog(RepairCommentAction.class);

    @Autowired(required = true)
    private RepairCommentService repairCommentService;
    @Autowired(required = true)
    private NewNoticeService newNoticeService;


/*
     * 新增修车修车圈评论
     *
     * @param req 修车修车圈评论信息
     * @return state : 0 -- 成功, 6--修车修车圈评论已存在
 */

    @ResponseBody
    @RequestMapping(value = "/api/repairComment/addrepairComment", method = RequestMethod.POST)
    public Object addrepairComment(HttpServletRequest request, @RequestBody Request req) {
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

        String repairId = req.getRepair().getId();//修车圈店铺的id
        //从Request中获取修车修车圈评论参数
        RepairComment repairComment = req.getRepairComment();
        repairComment.setId(UUIDCreator.getUUID());
        repairComment.setUserId(obj.getId());//评论用户id
        repairComment.setRepairId(repairId);//修车圈店铺的id
        repairComment.setFoundDate(new Date());

        //评论图片1
        if(repairComment.getPicture1()!=null){
            String commentPic1 = UUIDCreator.getUUID();
            try {
                ImageUtil.generateImage(repairComment.getPicture1(), "/home/java/aqb/pic/" + commentPic1 + ".jpg");
            } catch (Exception e) {
                e.printStackTrace();
            }
            repairComment.setPicture1("http://47.94.45.159:8080/aqb/pic/" + commentPic1 + ".jpg");
        }
        //评论图片2
        if (repairComment.getPicture2()!=null){
            String commentPic2 = UUIDCreator.getUUID();
            try {
                ImageUtil.generateImage(repairComment.getPicture2(), "/home/java/aqb/pic/" + commentPic2 + ".jpg");
            } catch (Exception e) {
                e.printStackTrace();
            }
            repairComment.setPicture2("http://47.94.45.159:8080/aqb/pic/" + commentPic2 + ".jpg");
        }

        if (repairComment.getPicture3() != null){
            String commentPic3 = UUIDCreator.getUUID();
            try {
                ImageUtil.generateImage(repairComment.getPicture3(), "/home/java/aqb/pic/" + commentPic3 + ".jpg");
            } catch (Exception e) {
                e.printStackTrace();
            }
            repairComment.setPicture3("http://47.94.45.159:8080/aqb/pic/" + commentPic3 + ".jpg");
        }


//        if (repairComment.getRepairId()==null||repairComment.getRepairId().equals("")) {
//            return new Response(Status.ERROR,"修车圈店铺的id不能为空");
//        }

        status = repairCommentService.add(repairComment);
        if (status == Status.SUCCESS) {
            message = "添加修车圈评论成功";
            return new Response(status, message, repairComment.getId());
        }
        if (status == Status.EXISTS) {
            message = "修车圈评论已经存在";
            return new Response(status, message);
        }
        message = "添加修车圈评论失败";
        return new Response(status, message);
    }




/*
     * 修改修车修车圈评论
     *
     * @param request
     * @param repairComment     repairComment.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 修车修车圈评论不存在, 1 -- 修改失败
*/

//    @ResponseBody
//    @RequestMapping(value = "/api/repairComment/updaterepairComment", method = RequestMethod.POST)
    public Object updaterepairComment(HttpServletRequest request, @RequestBody RepairComment repairComment) {
        int status = Status.ERROR;
        String message = "";
        if (repairComment.getId() == null) {
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = repairCommentService.update(repairComment);
        if (status == Status.SUCCESS) {
            message = "修改成功";
        } else if (status == Status.NOT_EXISTS) {
            message = "修车圈评论不存在，修改失败";
        } else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

//    /**
//     * 上传评论图片
//     *
//     * @param request
//     * @param
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "/api/repairComment/uploadCommentpic", method = RequestMethod.POST)
//    public Object uploadCommentpic(HttpServletRequest request,
//                             //@RequestParam("sessionId")String sessionId,
//                             //@RequestParam("file")MultipartFile multipartFile,
//                             @RequestBody Request req) {
//        int status;
//        String message = "";
//
//        //判断用户的session是否正常
//        //获取session
//        if (req.getSessionId() == null || req.getSessionId().equals("")) {
//            return new Response(Status.ERROR, "sessionId不能为空");
//        }
//        MySessionContext myc = MySessionContext.getInstance();
//        HttpSession httpSession = myc.getSession(req.getSessionId());
//        if (httpSession == null) {
//            return new Response(14, "sessionId失效");
//        }
//        //登录时，用户信息已经保存在session中，在session中取出用户信息
//        User obj = ActionUtil.getCurrentUser(httpSession);
//        if (obj == null) {
//            return new Response(14, "sessionId失效");
//        }
//
//        RepairComment repairComment = req.getRepairComment();
//
////        User user = req.getUser();
////        user.setId(obj.getId());
////
////        User user1 = userService.get(obj.getId());
//
//        //评论图片
//        String commentPic = UUIDCreator.getUUID();
//        try {
//            ImageUtil.generateImage(user.getUserHead(), "/home/java/aqb/commentPic/" + commentPic + ".jpg");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        user.setUserHead("http://47.94.45.159:8080/aqb/commentPic/" + commentPic + ".jpg");
//
//        user1.setUserHead(user.getUserHead());
//        status = userService.update(user1);
//        return new Response(status, "上传成功");
//    }

/*
     * 修车修车圈评论列表查询(待审核) 后台
     *
     * @param currentPage
     * @para maxRow
     * @return
*/
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/repairComment/queryrepairComment", method = RequestMethod.GET)
    public Object queryrepairComment(HttpServletRequest request,
                                  @RequestParam(value = "currentPage", required = false) Long currentPage,
                                  @RequestParam(value = "maxRow", required = false) Long maxRow) {

        QueryBase query = new QueryBase();
        if (currentPage != null) {
            query.setCurrentPage(currentPage);
        }
        if (maxRow != null) {
            query.setMaxRow(maxRow);
        }
        repairCommentService.queryRC(query);
        return new Response(Status.SUCCESS, query.getTotalRow(), query.getTotalPage(), query.getResults(), query.getCurrentPage());
    }
  /*
     * 修车圈评论审核(通过)
     *
     * @param request
     * @param repairComment     repairComment.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 修车修车圈评论不存在, 1 -- 修改失败
*/
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/repairComment/examineRC", method = RequestMethod.POST)
    public Object examineRC(HttpServletRequest request, @RequestBody RepairComment repairComment) {
        int status = Status.ERROR;
        String message = "";
        String noticeContent = "审核已经通过";
        if(repairComment.getId() == null || repairComment.getId().equals("")){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        if(repairComment.getStatus() == null){
            message = "审核状态你不能为空";
            return new Response(Status.ERROR, message);
        }
        status = repairCommentService.update(repairComment);
        if(status == Status.SUCCESS){
            message = "修改成功";
            //添加通知消息
            NewNotice newNotice = new NewNotice();
            newNotice.setId(UUIDCreator.getUUID());
            newNotice.setUserId(repairComment.getUserId());
            newNotice.setStatus(1);
            newNotice.setFoundDate(new Date());
            newNotice.setNoticeTitle("修车圈用户评价");
            newNotice.setNoticeType(1);//类型(1--通过，2--不通过)
            newNotice.setNoticeContent(noticeContent);
            newNoticeService.add(newNotice);
        }else if(status == Status.NOT_EXISTS){
            message = "评论不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }
    /*
     * 修车圈评论审核(通过)
     *
     * @param request
     * @param repairComment     repairComment.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 修车修车圈评论不存在, 1 -- 修改失败
*/
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/repairComment/examineRCerror", method = RequestMethod.POST)
    public Object examineRCerror(HttpServletRequest request, @RequestBody RepairComment repairComment) {
        int status = Status.ERROR;
        String message = "";
        String noticeContent = "审核未通过";
        if(repairComment.getId() == null || repairComment.getId().equals("")){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        if(repairComment.getStatus() == null){
            message = "审核状态你不能为空";
            return new Response(Status.ERROR, message);
        }
        status = repairCommentService.update(repairComment);
        if(status == Status.SUCCESS){
            message = "修改成功";
            //添加通知消息
            NewNotice newNotice = new NewNotice();
            newNotice.setId(UUIDCreator.getUUID());
            newNotice.setUserId(repairComment.getUserId());
            newNotice.setStatus(1);
            newNotice.setFoundDate(new Date());
            newNotice.setNoticeTitle("修车圈用户评价");
            newNotice.setNoticeType(2);//类型(1--通过，2--不通过)
            newNotice.setNoticeContent(noticeContent + repairComment.getReason());
            newNoticeService.add(newNotice);
        }else if(status == Status.NOT_EXISTS){
            message = "评论不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /*
     * 修车圈评论列表 搜索评价 后台
     *
     * @param currentPage 当前页
     * @para maxRow
     * @para repairCommentContent 评价内容
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/repairComment/searchRCByComment", method = RequestMethod.GET)
    public Object searchRCByComment(HttpServletRequest request,
                                     @RequestParam(value = "currentPage", required = false) Long currentPage,
                                     @RequestParam(value = "maxRow", required = false) Long maxRow,
                                     @RequestParam(value = "repairCommentContent", required = false) String repairCommentContent) {

        QueryBase query = new QueryBase();
        if (currentPage != null) {
            query.setCurrentPage(currentPage);
        }
        if (maxRow != null) {
            query.setMaxRow(maxRow);
        }
        Map map = query.getParameters();
        map.put("repairCommentContent",repairCommentContent);
        repairCommentService.searchRCByComment(query);
        return new Response(Status.SUCCESS, query.getTotalRow(), query.getTotalPage(), query.getResults(), query.getCurrentPage());
    }

    /*
     * 修车圈评论列表 搜索商家 后台
     *
     *
     * @param currentPage 当前页
     * @para maxRow
     * @para repairName 商家名称
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/repairComment/searchRCByRepairName", method = RequestMethod.GET)
    public Object searchRCByRepairName(HttpServletRequest request,
                                     @RequestParam(value = "currentPage", required = false) Long currentPage,
                                     @RequestParam(value = "maxRow", required = false) Long maxRow,
                                     @RequestParam(value = "repairName", required = false) String repairName) {

        QueryBase query = new QueryBase();
        if (currentPage != null) {
            query.setCurrentPage(currentPage);
        }
        if (maxRow != null) {
            query.setMaxRow(maxRow);
        }
        Map map = query.getParameters();
        map.put("repairName",repairName);
        repairCommentService.searchRCByRepairName(query);
        return new Response(Status.SUCCESS, query.getTotalRow(), query.getTotalPage(), query.getResults(), query.getCurrentPage());
    }

    /*
     * 修车圈评论列表 搜索用户 后台
     *
     *
     * @param currentPage 当前页
     * @para maxRow
     * @para userName 手机号
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/repairComment/searchRCByUserName", method = RequestMethod.GET)
    public Object searchRCByUserName(HttpServletRequest request,
                                     @RequestParam(value = "currentPage", required = false) Long currentPage,
                                     @RequestParam(value = "maxRow", required = false) Long maxRow,
                                     @RequestParam(value = "userName", required = false) String userName) {

        QueryBase query = new QueryBase();
        if (currentPage != null) {
            query.setCurrentPage(currentPage);
        }
        if (maxRow != null) {
            query.setMaxRow(maxRow);
        }
        Map map = query.getParameters();
        map.put("userName",userName);
        repairCommentService.searchRCByUserName(query);
        return new Response(Status.SUCCESS, query.getTotalRow(), query.getTotalPage(), query.getResults(), query.getCurrentPage());
    }

    /*
     * 删除单条修车圈评论 后台
     *
     * @param repairComment
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
    */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/repairComment/deleterepairComment", method = RequestMethod.POST)
    public Object deleterepairComment(HttpServletRequest request, @RequestBody RepairComment repairComment) {
        int status;
        String message = "";
        if (repairComment.getId() == null || repairComment.getId().equals("")) {
            return new Response(Status.ERROR, "id不能为空");
        }

        status = repairCommentService.delete(repairComment);
        if (status == Status.NOT_EXISTS) {
            message = "修车圈评论不存在";
        }
        if (status == Status.ERROR) {
            message = "删除失败";
        }
        if (status == Status.SUCCESS) {
            message = "删除成功";
        }
        return new Response(status, message);
    }

    /*
     * 删除搜索出来的所有评论 后台
     *
     * @param repairComment
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
    */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/repairComment/deleteAll", method = RequestMethod.POST)
    public Object deleteAll(HttpServletRequest request, @RequestBody String[] strings) {
        int status = Status.ERROR;
        String message = "";
        if (strings == null){
            return new Response(Status.ERROR, "id不能为空");
        }
        for (String id:strings){
            RepairComment repairComment = repairCommentService.get(id);
            status = repairCommentService.delete(repairComment);
        }

        if (status == Status.NOT_EXISTS) {
            message = "修车圈评论不存在";
        }
        if (status == Status.ERROR) {
            message = "删除失败";
        }
        if (status == Status.SUCCESS) {
            message = "删除成功";
        }
        return new Response(status, message);
    }

    /*
     * 删除商家所有评论 后台
     *
     * @param repairComment
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
    */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/repairComment/deleteByRepair", method = RequestMethod.POST)
    public Object deleteByRepair(HttpServletRequest request, @RequestBody Repair repair) {
        int status = Status.ERROR;
        String message = "";
        if (repair.getId() == null || repair.getId().equals("")) {
            return new Response(Status.ERROR, "id不能为空");
        }
        List<RepairComment> repairCommentList =  repairCommentService.queryByRepairId(repair.getId());
        for (RepairComment repairComment:repairCommentList){
            status = repairCommentService.delete(repairComment);
        }
        if (status == Status.NOT_EXISTS) {
            message = "修车圈评论不存在";
        }
        if (status == Status.ERROR) {
            message = "删除失败";
        }
        if (status == Status.SUCCESS) {
            message = "删除成功";
        }
        return new Response(status, message);
    }

    /*
     * 删除用户所有评论 后台
     *
     * @param repairComment
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
    */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/repairComment/deleteByUser", method = RequestMethod.POST)
    public Object deleteByUser(HttpServletRequest request, @RequestBody User user) {
        int status = Status.ERROR;
        String message = "";
        if (user.getId() == null || user.getId().equals("")) {
            return new Response(Status.ERROR, "id不能为空");
        }
        List<RepairComment> repairCommentList =  repairCommentService.queryByUserId(user.getId());
        for (RepairComment repairComment:repairCommentList){
            status = repairCommentService.delete(repairComment);
        }
        if (status == Status.NOT_EXISTS) {
            message = "修车圈评论不存在";
        }
        if (status == Status.ERROR) {
            message = "删除失败";
        }
        if (status == Status.SUCCESS) {
            message = "删除成功";
        }
        return new Response(status, message);
    }

}
