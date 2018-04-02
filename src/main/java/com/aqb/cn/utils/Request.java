package com.aqb.cn.utils;

import com.aqb.cn.bean.*;
import com.aqb.cn.pojo.Alipay;
import com.aqb.cn.pojo.UpdatePass;
import com.aqb.cn.pojo.UserAndCode;

import java.util.List;

/**
 * Created by Administrator on 2017/5/27.
 */
public class Request {

    private String sessionId; //sessionId

    private User user;//用户

    private String userPay;//用户支付密码

    private UserAndCode userAndCode;//短信验证码和用户

    private UserVip userVip;//用户会员

    private Integer vipGrade;//会员等级

    private Integer costStatus;//支付方式（1-余额，2-积分）

    private Dizhi dizhi;//地址

    private UpdatePass updatePass;//修改密码

    private Long currentPage;//查询页数

    private Long maxRow;//查询条数

    private Integer status;//订单状态(0--已取消,1--待付款，2--待发货，3--已发货，4--待评价）

    private String[] strings;//图片地址

    private String id;

    long limit;

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    private Float money;//充值金额

    private Advertisement advertisement;//广告

    private List<AdverDate> adverDates;//广告投放时间段

    private Binding binding;//绑定设备

    private BindingNumber bindingNumber;//绑定设备号

    private Sign sign;//签到

    private Barrage barrage;//弹幕

    private BarrageLike barrageLike;//弹幕点赞

    private BarrageUnlike barrageUnlike;//弹幕点差

    private Wallet wallet;//钱包

    private Yue yue;//余额

    private Jifen jifen;//积分

    private Task task;//用户任务

    private Repair repair;//修车圈

    private Redpacket redpacket;//红包

    private  List<Delivery> deliverys;//未领取的红包列表

    private Alipay alipay;//支付宝参数

    private String userName;//电话

    private String userFull;//用户名

    private Withdrawals withdrawals;//提现信息

    private NewNotice newNotice;//用户的通知消息

    private NewPay newPay;//用户的支付消息

    private UserFriends userFriends;//用户好友

    public UserFriends getUserFriends() {
        return userFriends;
    }

    public void setUserFriends(UserFriends userFriends) {
        this.userFriends = userFriends;
    }

    public NewPay getNewPay() {
        return newPay;
    }

    public void setNewPay(NewPay newPay) {
        this.newPay = newPay;
    }

    public NewNotice getNewNotice() {
        return newNotice;
    }

    public void setNewNotice(NewNotice newNotice) {
        this.newNotice = newNotice;
    }

    public Withdrawals getWithdrawals() {
        return withdrawals;
    }

    public void setWithdrawals(Withdrawals withdrawals) {
        this.withdrawals = withdrawals;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFull() {
        return userFull;
    }

    public void setUserFull(String userFull) {
        this.userFull = userFull;
    }

    public Alipay getAlipay() {
        return alipay;
    }

    public void setAlipay(Alipay alipay) {
        this.alipay = alipay;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public List<Delivery> getDeliverys() {
        return deliverys;
    }

    public void setDeliverys(List<Delivery> deliverys) {
        this.deliverys = deliverys;
    }

    public Request() {
    }

    public Repair getRepair() {
        return repair;
    }

    private RepairComment repairComment;//修车圈评论

    public String getUserPay() {
        return userPay;
    }

    public void setUserPay(String userPay) {
        this.userPay = userPay;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Barrage getBarrage() {
        return barrage;
    }

    public void setBarrage(Barrage barrage) {
        this.barrage = barrage;
    }

    public RepairComment getRepairComment() {
        return repairComment;
    }

    public void setRepairComment(RepairComment repairComment) {
        this.repairComment = repairComment;
    }

    public void setRepair(Repair repair) {
        this.repair = repair;
    }

    private Cof cof;//朋友圈

    public Cof getCof() {
        return cof;
    }

    public void setCof(Cof cof) {
        this.cof = cof;
    }

    private  CofFabulous cofFabulous;//朋友圈点赞

    public CofFabulous getCofFabulous() {
        return cofFabulous;
    }

    public void setCofFabulous(CofFabulous cofFabulous) {
        this.cofFabulous = cofFabulous;
    }

    private CofPic cofPic;//朋友圈图片

    public CofPic getCofPic() {
        return cofPic;
    }

    public void setCofPic(CofPic cofPic) {
        this.cofPic = cofPic;
    }

    private CofComment cofComment;//朋友圈评论

    public CofComment getCofComment() {
        return cofComment;
    }

    public void setCofComment(CofComment cofComment) {
        this.cofComment = cofComment;
    }

    public List<AdverDate> getAdverDates() {
        return adverDates;
    }

    public void setAdverDates(List<AdverDate> adverDates) {
        this.adverDates = adverDates;
    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UpdatePass getUpdatePass() {
        return updatePass;
    }

    public void setUpdatePass(UpdatePass updatePass) {
        this.updatePass = updatePass;
    }

    public Long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
    }

    public Long getMaxRow() {
        return maxRow;
    }

    public void setMaxRow(Long maxRow) {
        this.maxRow = maxRow;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String[] getStrings() {
        return strings;
    }

    public void setStrings(String[] strings) {
        this.strings = strings;
    }

    public Binding getBinding() {
        return binding;
    }

    public void setBinding(Binding binding) {
        this.binding = binding;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public UserAndCode getUserAndCode() {
        return userAndCode;
    }

    public void setUserAndCode(UserAndCode userAndCode) {
        this.userAndCode = userAndCode;
    }

    public Dizhi getDizhi() {
        return dizhi;
    }

    public void setDizhi(Dizhi dizhi) {
        this.dizhi = dizhi;
    }

    public BindingNumber getBindingNumber() {
        return bindingNumber;
    }

    public void setBindingNumber(BindingNumber bindingNumber) {
        this.bindingNumber = bindingNumber;
    }

    public Sign getSign() {
        return sign;
    }

    public void setSign(Sign sign) {
        this.sign = sign;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Yue getYue() {
        return yue;
    }

    public void setYue(Yue yue) {
        this.yue = yue;
    }

    public Jifen getJifen() {
        return jifen;
    }

    public void setJifen(Jifen jifen) {
        this.jifen = jifen;
    }

    public Redpacket getRedpacket() {
        return redpacket;
    }

    public void setRedpacket(Redpacket redpacket) {
        this.redpacket = redpacket;
    }

    public BarrageLike getBarrageLike() {
        return barrageLike;
    }

    public void setBarrageLike(BarrageLike barrageLike) {
        this.barrageLike = barrageLike;
    }

    public BarrageUnlike getBarrageUnlike() {
        return barrageUnlike;
    }

    public void setBarrageUnlike(BarrageUnlike barrageUnlike) {
        this.barrageUnlike = barrageUnlike;
    }

    public UserVip getUserVip() {
        return userVip;
    }

    public void setUserVip(UserVip userVip) {
        this.userVip = userVip;
    }

    public Integer getVipGrade() {
        return vipGrade;
    }

    public void setVipGrade(Integer vipGrade) {
        this.vipGrade = vipGrade;
    }

    public Integer getCostStatus() {
        return costStatus;
    }

    public void setCostStatus(Integer costStatus) {
        this.costStatus = costStatus;
    }
}
