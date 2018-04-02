package com.aqb.cn.aspectj;

import com.aqb.cn.bean.Admin;
import com.aqb.cn.common.ActionUtil;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Aspect
public class UserLoginAspectj {

    private Log logger = LogFactory.getLog(UserLoginAspectj.class);

    @Resource(name="sessionOutTimeResponse")  //在Spring-MVC文件中有注解
        Response sessionOutTimeResponse;
    @Resource(name="sessionRepeatResponse")  //在Spring-MVC文件中有注解
        Response sessionRepeatResponse;
    @Resource(name="sessionQxResponse")  //在Spring-MVC文件中有注解
            Response sessionQxResponse;

    @Around("@annotation(com.aqb.cn.annotation.AdminLogin)")
     public Object checkUserAuthorized(ProceedingJoinPoint point) throws Throwable{
//        HttpServletRequest request1=(HttpServletRequest) point.getArgs()[0];
//        Admin admin1= ActionUtil.getCurrentAdmin(request1);
//        System.out.println(admin1);
        try{
            HttpServletRequest request=(HttpServletRequest) point.getArgs()[0];
            Admin admin= ActionUtil.getCurrentAdmin(request);
            if(admin != null){
                return point.proceed();
            }
        }catch(Exception e){
            e.printStackTrace();
            return new Response(Status.ERROR,"系统错误");
        }
        return sessionOutTimeResponse;
    }



}
