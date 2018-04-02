package com.aqb.cn.pojo;

/**
 * Created by Administrator on 2017/5/27.
 */
public class UserAndCode{

    private String userName;

    private String userPass;

    private String code;

    private String codeSessionId;//验证码的sessionid

    public String getCodeSessionId() {
        return codeSessionId;
    }

    public void setCodeSessionId(String codeSessionId) {
        this.codeSessionId = codeSessionId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}