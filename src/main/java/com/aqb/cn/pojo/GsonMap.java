package com.aqb.cn.pojo;

import java.util.List;

/**
 * Created by Administrator on 2017/8/14.
 */
public class GsonMap {

    private Integer status;

    private List<GsonXY> result;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<GsonXY> getResult() {
        return result;
    }

    public void setResult(List<GsonXY> result) {
        this.result = result;
    }
}
