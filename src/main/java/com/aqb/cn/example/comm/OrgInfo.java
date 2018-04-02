package com.aqb.cn.example.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by easemob on 2017/3/31.
 */
public class OrgInfo {

    public static String ORG_NAME = "1189170810115328";
    public static String APP_NAME = "dmlr";
    public static final Logger logger = LoggerFactory.getLogger(OrgInfo.class);

//    static {
//        InputStream inputStream = OrgInfo.class.getClassLoader().getResourceAsStream("config.properties");
//        Properties prop = new Properties();
//        try {
//            prop.load(inputStream);
//        } catch (IOException e) {
//            logger.error(e.getMessage());
//        }
//        ORG_NAME = prop.getProperty("1158170725115993");
//        APP_NAME = prop.getProperty("mykefu");
//    }
}
