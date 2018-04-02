package com.aqb.cn.pojo;

/**
 * Created by Administrator on 2017/8/3.
 */
public class NoticeAndPay {

    private boolean NoticeStatus;//是否存在通知消息

    private boolean PayStatus;//是否存在支付消息

    public boolean isNoticeStatus() {
        return NoticeStatus;
    }

    public void setNoticeStatus(boolean noticeStatus) {
        NoticeStatus = noticeStatus;
    }

    public boolean isPayStatus() {
        return PayStatus;
    }

    public void setPayStatus(boolean payStatus) {
        PayStatus = payStatus;
    }
}
