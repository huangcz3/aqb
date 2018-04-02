package com.aqb.cn.example.comm;

import com.google.gson.Gson;
import io.swagger.client.ApiException;
import io.swagger.client.api.AuthenticationApi;
import io.swagger.client.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Created by easemob on 2017/3/14.
 */
public class TokenUtil {
    public static String GRANT_TYPE = "client_credentials";
    private static String CLIENT_ID = "YXA6SkAB4H25EeetGj0rvqZe3w";
    private static String CLIENT_SECRET = "YXA6NRrhRh43aFOeWZZaoQp_BANiuDI";
    private static Token BODY;
    private static AuthenticationApi API = new AuthenticationApi();
    private static String ACCESS_TOKEN;
    private static Double EXPIREDAT = -1D;
    private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    /**
     * get token from server
     */
    static {
//        InputStream inputStream = TokenUtil.class.getClassLoader().getResourceAsStream("config.properties");
//        Properties prop = new Properties();
//        try {
//            prop.load(inputStream);
//        } catch (IOException e) {
//            logger.error(e.getMessage());
//        }
//        GRANT_TYPE = prop.getProperty("client_credentials");
//        CLIENT_ID = prop.getProperty("YXA6MgUooHEKEee9tPeAc44A5Q");
//        CLIENT_SECRET = prop.getProperty("YXA6DJcDRlzG0FX0atPA1p_NRYdVE-s");
        BODY = new Token().clientId(CLIENT_ID).grantType(GRANT_TYPE).clientSecret(CLIENT_SECRET);
    }

    public static void initTokenByProp() {
        String resp = null;
        try {
            resp = API.orgNameAppNameTokenPost(OrgInfo.ORG_NAME, OrgInfo.APP_NAME, BODY);
        } catch (ApiException e) {
            logger.error(e.getMessage());
        }
        Gson gson = new Gson();
        Map map = gson.fromJson(resp, Map.class);
        ACCESS_TOKEN = " Bearer " + map.get("access_token");
        EXPIREDAT = System.currentTimeMillis() + (Double) map.get("expires_in");
    }

    /**
     * get Token from memory
     *
     * @return
     */
    public static String getAccessToken() {
        if (ACCESS_TOKEN == null || isExpired()) {
            initTokenByProp();
        }
        return ACCESS_TOKEN;
    }

    private static Boolean isExpired() {
        return System.currentTimeMillis() > EXPIREDAT;
    }

}

