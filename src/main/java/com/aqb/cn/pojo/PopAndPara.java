package com.aqb.cn.pojo;

import com.aqb.cn.bean.PopAdv;

import java.util.List;

/**
 * Created by Administrator on 2017/8/4/0004.
 */
public class PopAndPara {
    private String paraId; //参数的id

    private boolean onOff_para;//开关的参数

    private List<PopAdv> popAdv;//弹窗广告

    public String getParaId() {
        return paraId;
    }

    public void setParaId(String paraId) {
        this.paraId = paraId;
    }

    public boolean isOnOff_para() {
        return onOff_para;
    }

    public void setOnOff_para(boolean onOff_para) {
        this.onOff_para = onOff_para;
    }

    public List<PopAdv> getPopAdv() {
        return popAdv;
    }

    public void setPopAdv(List<PopAdv> popAdv) {
        this.popAdv = popAdv;
    }
}
