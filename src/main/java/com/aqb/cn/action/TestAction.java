package com.aqb.cn.action;

import com.aqb.cn.bean.User;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.utils.Timer.TimerPush;
import com.aqb.cn.utils.Timer.vip.VipTimer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.Timer;

/**
 * Created by Administrator on 2017/6/26.
 */
@Controller
public class TestAction {

    private String userName;

    /*
	 * 列表查询屏蔽关键词
	 */
    @ResponseBody
    @RequestMapping(value = "/api/test/test", method = RequestMethod.GET)
    public Object test(HttpServletRequest request){
        Timer timer;
        timer = new Timer();
        timer.schedule(new TimerPush("74BB02C0369146D9B2C919195E150844"), 1000);
        return new Response(Status.ERROR, "");
    }

    /*
	 * GET
	 */
    @ResponseBody
    @RequestMapping(value = "/api/get", method = RequestMethod.GET)
    public Object get(HttpServletRequest request){
        new VipTimer();
        return new Response(0, "测试成功");
    }

    /*
	 * POST
	 */
    @ResponseBody
    @RequestMapping(value = "/api/post", method = RequestMethod.POST)
    public Object post(HttpServletRequest request,@RequestBody User user){
        return new Response(0, "测试成功");
    }


    @RequestMapping(value = "test", method = RequestMethod.POST)
    public String fileUpLoad(String name, @RequestParam("file") CommonsMultipartFile file, HttpSession session) {
        if (file != null) {
            // 获取上传路径
            String realPath = "F:\\upload";
            // 获取文件名+路径
            String originalName = file.getOriginalFilename();
            // 文件名
            String fileName = originalName.substring(0, originalName.lastIndexOf("."));
            // 文件类型
            String fileType = originalName.substring(originalName.lastIndexOf("."));
            // 创建新的目标文件
            File targetFile = new File(realPath, fileName + new Date().getTime() + fileType);
            try {
                // 将上传的文件 写入新的目标文件
                file.getFileItem().write(targetFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            session.setAttribute("uploadMsg", "成功! 位置 => " + (realPath + "\\" + fileName + new Date().getTime() + fileType));
            name = targetFile.getAbsolutePath()+targetFile.getName();
        }
        return "test";
    }


}
