package com.aqb.cn.utils.ApplicationContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 1.创建一个类并让其实现org.springframework.context.
 * ApplicationContextAware接口来让Spring在启动的时候为我们注入ApplicationContext对象.
 */
public class MyApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;//声明一个静态变量保存

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        MyApplicationContextUtil.context=context;
    }

    public static ApplicationContext getContext(){
        return context;
    }
}
