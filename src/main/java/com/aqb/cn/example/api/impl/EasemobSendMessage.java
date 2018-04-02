package com.aqb.cn.example.api.impl;

import com.aqb.cn.example.api.SendMessageAPI;
import com.aqb.cn.example.comm.OrgInfo;
import com.aqb.cn.example.comm.ResponseHandler;
import com.aqb.cn.example.comm.EasemobAPI;
import com.aqb.cn.example.comm.TokenUtil;
import io.swagger.client.ApiException;
import io.swagger.client.api.MessagesApi;
import io.swagger.client.model.Msg;

public class EasemobSendMessage implements SendMessageAPI {
    private ResponseHandler responseHandler = new ResponseHandler();
    private MessagesApi api = new MessagesApi();
    @Override
    public Object sendMessage(final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameMessagesPost(OrgInfo.ORG_NAME,OrgInfo.APP_NAME,TokenUtil.getAccessToken(), (Msg) payload);
            }
        });
    }
}
