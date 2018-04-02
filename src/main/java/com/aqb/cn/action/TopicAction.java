package com.aqb.cn.action;

import com.aqb.cn.annotation.AdminLogin;
import com.aqb.cn.bean.Topic;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.service.BarrageService;
import com.aqb.cn.service.TopicService;
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
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/15.
 */
@Controller
public class TopicAction {

    private final Log logger = LogFactory.getLog(TopicAction.class);

    @Autowired
    private TopicService topicService;
    @Autowired
    private BarrageService barrageService;

    /**
     * 新增话题
     * 后台
     * @param topic 话题信息
     * @return state : 0 -- 成功, 6--话题已存在
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/topic/addTopic", method = RequestMethod.POST)
    public Object addTopic(HttpServletRequest request,@RequestBody Topic topic) {
        int status;
        String message = "";
        if(topic.getTopicTitle() == null || topic.getTopicTitle().equals("") ||
                topic.getTopicContent() == null || topic.getTopicContent().equals("")){
            return new Response(Status.ERROR, "标题或内容不能为空");
        }
        topic.setId(UUIDCreator.getUUID());
        topic.setFoundDate(new Date());
        status = topicService.add(topic);
        if (status == Status.SUCCESS) {
            message = "添加话题成功";
            return new Response(status, message, topic.getId());
        }if(status == Status.EXISTS){
            message = "话题已经存在";
            return new Response(status, message);
        }
        message = "添加话题失败";
        return new Response(status, message);
    }


    /**
     * 删除话题
     * 后台
     * @param request
     * @param topic
     * @return status :0 -- 成功 ,1 -- 失败, 7 -- 不存在
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/topic/deleteTopic", method = RequestMethod.POST)
    public Object deletetopic(HttpServletRequest request, @RequestBody Topic topic) {
        int status;
        String message = "";
        if(topic.getId() == null || topic.getId().equals("")){
            return new Response(Status.ERROR, "id不能为空");
        }
        status = topicService.delete(topic);
        if(status == Status.NOT_EXISTS){
            message = "话题不存在";
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
     * 修改话题
     * 后台
     * @param request
     * @param topic topic.id
     * @return status: 0 -- 成功, 10 -- 传参错误, 7 -- 话题不存在, 1 -- 修改失败
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/topic/updateTopic", method = RequestMethod.POST)
    public Object updateTopic(HttpServletRequest request, @RequestBody Topic topic) {
        int status = Status.ERROR;
        String message = "";
        if(topic.getId() == null || topic.getId().equals("")){
            message = "传参错误，id不能为空";
            return new Response(Status.ERROR, message);
        }
        status = topicService.update(topic);
        if(status == Status.SUCCESS){
            message = "修改成功";
        }else if(status == Status.NOT_EXISTS){
            message = "话题不存在，修改失败";
        }else {
            message = "修改失败";
        }
        return new Response(status, message);
    }

    /**
     * 话题列表查询
     * 后台
     * @param request
     * @return
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/topic/queryTopic", method = RequestMethod.GET)
    public Object queryTopic(HttpServletRequest request) {
        List<Topic> topics = topicService.queryTopic();
        return new Response(Status.SUCCESS,topics);
    }
    /**
     * 话题列表查询
     * app
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/topic/queryTopicApp", method = RequestMethod.GET)
    public Object queryTopicApp(HttpServletRequest request) {
        List<Topic> topics = topicService.queryTopic();
        return new Response(Status.SUCCESS,topics);
    }
}
