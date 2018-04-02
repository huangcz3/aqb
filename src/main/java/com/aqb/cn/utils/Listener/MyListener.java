package com.aqb.cn.utils.Listener;

import com.aqb.cn.bean.*;
import com.aqb.cn.mapper.*;
import com.aqb.cn.pojo.Position;
import com.aqb.cn.utils.ApplicationContext.MyApplicationContextUtil;
import com.aqb.cn.utils.GPSDistance;
import com.aqb.cn.utils.GetPositionList;
import com.aqb.cn.utils.UUIDCreator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Listener的方式在后台执行一线程
 *
 * @author Champion.Wong
 *
 */
public class MyListener implements ServletContextListener {
    private MyThread myThread;

    public void contextDestroyed(ServletContextEvent e) {
        if (myThread != null && myThread.isInterrupted()) {
            myThread.interrupt();
        }
    }

    public void contextInitialized(ServletContextEvent e) {
        String str = null;
        if (str == null && myThread == null) {
            myThread = new MyThread();
            myThread.start(); // servlet 上下文初始化时启动 socket
        }
    }
}

/**
 * 自定义一个 Class 线程类继承自线程类，重写 run() 方法，用于从后台获取并处理数据
 *
 * @author Champion.Wong
 *
 */
class MyThread extends Thread {
    public void run() {
        //从 ApplicationContext 中获取bean
        AdvertisementMapper advertisementMapper = (AdvertisementMapper) MyApplicationContextUtil.getContext().getBean("advertisementMapper");
        DeliveryMapper deliveryMapper = (DeliveryMapper) MyApplicationContextUtil.getContext().getBean("deliveryMapper");
        BindingMapper bindingMapper = (BindingMapper) MyApplicationContextUtil.getContext().getBean("bindingMapper");
        PriceMapper priceMapper = (PriceMapper) MyApplicationContextUtil.getContext().getBean("priceMapper");
        AdverPlatformMapper adverPlatformMapper = (AdverPlatformMapper) MyApplicationContextUtil.getContext().getBean("adverPlatformMapper");
        DeliveryPlatformMapper deliveryPlatformMapper = (DeliveryPlatformMapper) MyApplicationContextUtil.getContext().getBean("deliveryPlatformMapper");
        DividedMapper dividedMapper = (DividedMapper) MyApplicationContextUtil.getContext().getBean("dividedMapper");
        ProportionMapper proportionMapper = (ProportionMapper) MyApplicationContextUtil.getContext().getBean("proportionMapper");
        RedpacketMapper redpacketMapper = (RedpacketMapper) MyApplicationContextUtil.getContext().getBean("redpacketMapper");
        JifenMapper jifenMapper = (JifenMapper) MyApplicationContextUtil.getContext().getBean("jifenMapper");
        ParameterMapper parameterMapper = (ParameterMapper) MyApplicationContextUtil.getContext().getBean("parameterMapper");
        ProfitPlatformMapper profitPlatformMapper = (ProfitPlatformMapper) MyApplicationContextUtil.getContext().getBean("profitPlatformMapper");
        ProfitUserMapper profitUserMapper = (ProfitUserMapper) MyApplicationContextUtil.getContext().getBean("profitUserMapper");
        UserMapper userMapper = (UserMapper) MyApplicationContextUtil.getContext().getBean("userMapper");
        ShareAwardMapper shareAwardMapper = (ShareAwardMapper) MyApplicationContextUtil.getContext().getBean("shareAwardMapper");
        ShareRewardMapper shareRewardMapper = (ShareRewardMapper) MyApplicationContextUtil.getContext().getBean("shareRewardMapper");


        while (!this.isInterrupted()) {// 线程未中断执行循环
            try {
                Thread.sleep(30 * 1000); //每隔 30 秒执行一次
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//           ------------------ 开始执行 ---------------------------
//            System.out.println("____FUCK TIME:" + System.currentTimeMillis());
            Date date = new Date();
            System.out.println("____FUCK TIME:" + date);
            //获取redis中保存的车辆坐标集合
            List<Position> positions = null;
            try {
                positions = GetPositionList.getPositionList();
            }catch (Exception e){
                System.out.println("获取在线设备时异常");
                continue;
            }

            if(positions == null || positions.size() == 0){
                System.out.println("未获取到在线设备");
                continue;
            }

            //查询数据库 在当前时间可以发送广告的圈
            List<Advertisement> advertisements = advertisementMapper.queryAdvertisementList();
            if(advertisements == null || advertisements.size() == 0) {
                System.out.println("当前可发的广告："+advertisements.size());
            }

            for(Position position : positions){
                //判断上线的设备中，设备是否符合投放广告的条件
                List<Binding> bindings = bindingMapper.selectByBindingNumber(position.getDevNum());//查询设备的绑定情况
                if(bindings == null || bindings.size() == 0){
                    System.out.println("设备未绑定：" + position.getDevNum());
                    continue;
                }

                //把在线的每一辆车，都拿来和所有圈进行比较，以确定该车在几个圈内,把车辆所在的圈，放入set中
                List<Advertisement> list = new ArrayList();
                for(Advertisement advertisement : advertisements){
                    //计算车辆位置与，圈的圆心位置的距离
                    double s = GPSDistance.GetDistance(advertisement.getWeidu()-0.00374531687912, advertisement.getJingdu()-0.008774687519
                            ,
                            position.getLatitude(), position.getLongitude());
                    //根据距离判断车是否在大圈内
                    if(advertisement.getDaquan() > s){
                        //判断是否在小圈内
                        if(advertisement.getNeiquan() != null && advertisement.getNeiquan() > s &&
                                advertisement.getNeiquanStatus() == 1){//判断内圈投放状态 1--未投放完毕，2--已投放完毕
                            //添加一个在小圈内的标记,1--在大圈内，2--在小圈内
                            advertisement.setKey(2);
                            list.add(advertisement);
                        }else{
                            if(advertisement.getDaquanStatus() == 1){//判断大圈投放状态 1--未投放完毕，2--已投放完毕
                                advertisement.setKey(1);
                                list.add(advertisement);
                            }
                        }
                    }
                }
                //判断是否在圈内
                if(list.size() > 0){
                    System.out.println("设备在圈内："+list.size() + " *** 设备号："+position.getDevNum());
                    //向车辆投放广告，如果set中有多个圈，则查看数据库，根据投放规则进行顺序投放
                    Advertisement advertisement = null;
                    //判断
                    if(list.size() == 1){   //车辆在一个圈中的情况
                        advertisement = list.get(0);
                    }else{                  //车辆在多个圈中的情况
                        int limit = list.size()-1;
                        //当车辆在不止一个圈内时，查询该车辆最近 list.size()-1 次的投放记录,以排除之前的投放目标，从而确定该次的投放目标
                        List<Delivery> deliverys = deliveryMapper.selectByDevNum(position.getDevNum(),limit);
                        //排除list中之前投放过的圈
                        for (int i = 0; i <list.size() ; i++) {
                            Advertisement advertisement1 = list.get(i);
                            for(Delivery delivery : deliverys){
                                if(advertisement1.getId().equals(delivery.getAdvertisementId())){
                                    list.remove(advertisement1);
                                    i--;
                                    break;
                                }
                            }
                        }
                        //剩余之前未投放过的圈，选择单价高的进行投放
                        advertisement = list.get(0);
//                        if(list.size() > 1){
//                            for(Advertisement advertisement1 : list){
//                                //比较单价
//                                float danjia1 = 0;
//                                float danjia2 = 0;
//                                if(advertisement.getKey() == 1){
//                                    if(advertisement.getDaquanDanjia() != null){
//                                        danjia1 = advertisement.getDaquanDanjia();
//                                    }
//                                }else if(advertisement.getKey() == 2){
//                                    if(advertisement.getNeiquanDanjia() != null){
//                                        danjia1 = advertisement.getNeiquanDanjia();
//                                    }
//                                }
//                                if(advertisement1.getKey() == 1){
//                                    if(advertisement1.getDaquanDanjia() != null){
//                                        danjia2 = advertisement1.getDaquanDanjia();
//                                    }
//                                }else if(advertisement1.getKey() == 2){
//                                    if(advertisement1.getNeiquanDanjia() != null){
//                                        danjia2 = advertisement1.getNeiquanDanjia();
//                                    }
//                                }
//                                if(danjia1 < danjia2){
//                                    advertisement = advertisement1;
//                                }
//                            }
//                        }
                    }

                    //投放广告
                    String s = position.getDevNum();
                    String sendMsg="msg="+advertisement.getAdvertisementContent()+"&list=["+s+"]";
                    String response="";
                    try{
                        response = GetPositionList.sendPost("http://127.0.0.1:8888/aqb/tests/sendMsg",sendMsg);
                        if(response.equals("e")){
                            System.out.println("给列表用户发送消息时异常");
                            continue;
                        }
                    }catch (Exception e){
                        System.out.println("给列表用户发送消息时异常");
                        e.printStackTrace();
                        continue;
                    }
                    //解析接收到的json响应
                    JsonParser parser=new JsonParser();
                    JsonObject object=(JsonObject) parser.parse(response);
                    JsonElement status = object.get("status");
                    System.out.println("getAsInt()===" + status.getAsInt());
                    if(status.getAsInt() == 1){
                        System.out.println("发送失败");
                        continue;
                    }
                    if(status.getAsInt() == 0){
                        System.out.println("发送成功");
                        //添加广告投放表数据
                        Delivery delivery = new Delivery();
                        delivery.setId(UUIDCreator.getUUID());
                        delivery.setAdvertisementId(advertisement.getId());
                        delivery.setUserId(s);
                        //查询系统当前投放时间的广告单价
                        Price price = priceMapper.selectJiage();
                        if(price == null){
                            System.out.println("系统默认单价异常");
                            continue;
                        }
                        if(advertisement.getKey() == 1){//判断大圈投放或小圈投放,1-大圈，2-小圈
                            //判断到是大圈投放，先生成大圈的红包数据
                            delivery.setRedPacketType(2);//红包的类型（1--金额，2--积分）
                            delivery.setRedPacketStatus(0);//红包的状态（0--已领取，1--未领取）
//                            delivery.setRedPacketMoney(new Float(1.0));//红包的额度
                            delivery.setStatus(1);//(1--大圈投放，2--内圈投放)
                            Float priceJiage = price.getPriceJiage();//用户实际投放广告时的系统默认单价
                            float daquanMoney = advertisement.getDaquanJifen();//大圈总积分
//                            if(advertisement.getDaquanMoneyStatus() == 1){
//                                daquanMoney = advertisement.getDaquanJifen();//大圈总积分
//                            }else if(advertisement.getDaquanMoneyStatus() == 2){
//                                //之前是按照上面的方法兑换，现在不需要兑换，如果是金额，在发广告时就已经兑换好了。
//                                daquanMoney = advertisement.getDaquanJifen();//大圈总积分
//                            }else{
//                                System.out.println("广告的金额类型不正确");
//                                continue;
//                            }
                            Float daquanDeliveryMoney = deliveryMapper.sumByDeliveryMoney(advertisement.getId(),1);//统计大圈已经投放的金额
                            if(daquanDeliveryMoney == null){
                                daquanDeliveryMoney = new Float(0.0);
                            }
                            //比较总金额与已投放金额
                            if(daquanMoney > daquanDeliveryMoney){
                                Float danjia2;//实际投放广告应扣除的单价
                                //判断用户是否手动录入了条数  advertisement.getDaquanDanjia()==用户发广告时刻的系统默认单价
                                if(advertisement.getDaquanTiaoshu() != null && advertisement.getDaquanDanjia() != null){
                                    Float danjia1 = daquanMoney / advertisement.getDaquanTiaoshu();//计算用户手动录入的单价
                                    //计算实际投放广告应扣除的单价
                                    /*
                                    计算公式：
                                    用户发广告时刻的系统默认单价 : 用户手动录入的单价 = 用户实际投放广告时的系统默认单价 : 实际投放广告应扣除的单价
                                    */
                                    danjia2 = (danjia1 * priceJiage) / advertisement.getDaquanDanjia();
                                }else{
                                    danjia2 = priceJiage;//用户果如没有手动录入条数，实际投放广告应扣除的单价 = 实际投放广告时的系统默认单价
                                }
                                delivery.setDeliveryMoney(danjia2);
                                //查询车主分成比例
                                Divided divided = dividedMapper.selectByPrimaryKey("KDSJHFIO8943HIUJHDSF9978H9HHSUIH");
                                Float mon = danjia2 * divided.getDividedBili();//计算出车主应得的积分。
                                Float plat = danjia2 - mon;        //平台应得的积分
                                ProfitPlatform profitPlatform = new ProfitPlatform();
                                profitPlatform.setId(UUIDCreator.getUUID());
                                profitPlatform.setFoundDate(date);
                                profitPlatform.setStatus(1);
                                profitPlatform.setAdvertisementId(advertisement.getId());
                                profitPlatform.setEquipmentNumber(s);
                                profitPlatform.setMoney(plat);
                                profitPlatformMapper.insertSelective(profitPlatform);//添加平台收益记录到数据库
                                //查询车主是否有推荐人，如果有推荐人，是否单独设置了推荐人奖励。根据这些设置来为推荐人分成
                                User user = userMapper.selectByPrimaryKey(bindings.get(0).getUserId());
                                if(user != null && user.getUserName() != null && !user.getUserName().equals("")){
                                    List<com.aqb.cn.bean.a.ShareAward> shareAward = shareAwardMapper.selectByShareTel(user.getUserName());
                                    if(shareAward != null && shareAward.size() > 0 && shareAward.get(0).getUserId() != null && !shareAward.get(0).getUserId().equals("")){
                                        ProfitUser profitUser = new ProfitUser();
                                        //有推荐人，查询是否单独设置了推荐人奖励
                                        ShareReward shareReward = shareRewardMapper.queryByUserId(shareAward.get(0).getUserId());
                                        if(shareReward != null && shareReward.getRewardPercent() != null && shareReward.getRewardPercent() > 0){
                                            //有单独设置
                                            Float money = shareReward.getRewardPercent() * mon;//计算出车主的推荐人应得的积分。
                                            profitUser.setMoney(money);
                                        }else{
                                            //无单独设置，按照默认设置计算
                                            ShareReward shareReward_db = shareRewardMapper.selectByPrimaryKey("VUYASFDUYFU216F8E12UVE1UGY2E8712");
                                            if(shareReward_db == null){
                                                System.out.println("数据库推荐人奖励默认设置数据出错");
                                            }else{
                                                Float money = shareReward_db.getRewardPercent() * mon;//计算出车主的推荐人应得的积分。
                                                profitUser.setMoney(money);
                                            }
                                        }
                                        profitUser.setId(UUIDCreator.getUUID());
                                        profitUser.setStatus(1);
                                        profitUser.setFoundDate(date);
                                        profitUser.setEquipmentNumber(s);
                                        profitUser.setUserId(shareAward.get(0).getUserId());
                                        profitUser.setAdvertisementId(advertisement.getId());
                                        profitUserMapper.insertSelective(profitUser);//推荐人奖励录入数据库
                                    }
                                }
                                delivery.setRedPacketMoney(mon);//红包的额度
                            }else{
                                //正常情况不会进入这里，如果进入，说明广告的状态不对
                                System.out.println("实际投放金额已经超过总金额!");
                            }
                        }else if(advertisement.getKey() == 2){
                            //判断到是内圈投放，先生成内圈的红包数据
                            delivery.setRedPacketType(1);//红包的类型（1--金额，2--积分）
                            delivery.setRedPacketStatus(0);//红包的状态（0--已领取，1--未领取）
//                            delivery.setRedPacketMoney(new Float(1.0));//红包的额度
                            delivery.setStatus(2);//(1--大圈投放，2--内圈投放)
//                            Float priceJiage = price.getPriceJiage();//用户实际投放广告时的系统默认单价
                            float neiquanMoney = advertisement.getNeiquanMoneyNumber();//内圈总金额
//                            if(advertisement.getNeiquanMoneyStatus() == 1){
//                                neiquanMoney = advertisement.getNeiquanMoneyNumber();//内圈总金额
//                            }else if(advertisement.getNeiquanMoneyStatus() == 2){
//                                //如果是积分，需要从数据库获取 积分和现金的兑换比例，把积分转换为等值的现金
//
//                            }else{
//                                System.out.println("广告的金额类型不正确");
//                                continue;
//                            }
                            Float neiquanDeliveryMoney = deliveryMapper.sumByDeliveryMoney(advertisement.getId(),2);//统计内圈已经投放的金额
                            if(neiquanDeliveryMoney == null){
                                neiquanDeliveryMoney = new Float(0.0);
                            }
                            //比较总金额与已投放金额
                            if(neiquanMoney > neiquanDeliveryMoney){
                                Float danjia2;//实际投放广告应扣除的单价
//                                //判断用户是否手动录入了条数  advertisement.getNeiquanDanjia()==用户发广告时刻的系统默认单价
//                                if(advertisement.getNeiquanTiaoshu() != null && advertisement.getNeiquanDanjia() != null){
//                                    Float danjia1 = neiquanMoney / advertisement.getNeiquanTiaoshu();//计算用户手动录入的单价
//                                    //计算实际投放广告应扣除的单价
//                                    /*
//                                    计算公式：
//                                    用户发广告时刻的系统默认单价 : 用户手动录入的单价 = 用户实际投放广告时的系统默认单价 : 实际投放广告应扣除的单价
//                                    */
//                                    danjia2 = (danjia1 * priceJiage) / advertisement.getNeiquanDanjia();
//                                }else{
//                                    danjia2 = priceJiage;//用户果如没有手动录入条数，实际投放广告应扣除的单价 = 实际投放广告时的系统默认单价
//                                }
                                danjia2=advertisement.getNeiquanMoneyNumber() / (float)advertisement.getNeiquanTiaoshu();
                                delivery.setDeliveryMoney(danjia2);
                                //查询车主分成比例
                                Divided divided = dividedMapper.selectByPrimaryKey("KDSJHFIO8943HIUJHDSF9978H9HHSUIH");
                                Float mon = danjia2 * divided.getDividedBili();//计算出车主应得的金额。
                                Float plat = danjia2 - mon;        //平台应得的金额
                                ProfitPlatform profitPlatform = new ProfitPlatform();
                                profitPlatform.setId(UUIDCreator.getUUID());
                                profitPlatform.setFoundDate(date);
                                profitPlatform.setStatus(2);
                                profitPlatform.setAdvertisementId(advertisement.getId());
                                profitPlatform.setEquipmentNumber(s);
                                profitPlatform.setMoney(plat);
                                profitPlatformMapper.insertSelective(profitPlatform);//添加平台收益记录到数据库
                                //查询车主是否有推荐人，如果有推荐人，是否单独设置了推荐人奖励。根据这些设置来为推荐人分成
                                User user = userMapper.selectByPrimaryKey(bindings.get(0).getUserId());
                                if(user != null && user.getUserName() != null && !user.getUserName().equals("")){
                                    List<com.aqb.cn.bean.a.ShareAward> shareAward = shareAwardMapper.selectByShareTel(user.getUserName());
                                    if(shareAward != null && shareAward.size() > 0 && shareAward.get(0).getUserId() != null && !shareAward.get(0).getUserId().equals("")){
                                        ProfitUser profitUser = new ProfitUser();
                                        //有推荐人，查询是否单独设置了推荐人奖励
                                        ShareReward shareReward = shareRewardMapper.queryByUserId(shareAward.get(0).getUserId());
                                        if(shareReward != null && shareReward.getRewardPercent() != null && shareReward.getRewardPercent() > 0){
                                            //有单独设置
                                            Float money = shareReward.getRewardPercent() * mon;//计算出车主的推荐人应得的积分。
                                            profitUser.setMoney(money);
                                        }else{
                                            //无单独设置，按照默认设置计算
                                            ShareReward shareReward_db = shareRewardMapper.selectByPrimaryKey("VUYASFDUYFU216F8E12UVE1UGY2E8712");
                                            if(shareReward_db == null){
                                                System.out.println("数据库推荐人奖励默认设置数据出错");
                                            }else{
                                                Float money = shareReward_db.getRewardPercent() * mon;//计算出车主的推荐人应得的金额。
                                                profitUser.setMoney(money);
                                            }
                                        }
                                        profitUser.setId(UUIDCreator.getUUID());
                                        profitUser.setStatus(2);
                                        profitUser.setFoundDate(date);
                                        profitUser.setEquipmentNumber(s);
                                        profitUser.setUserId(shareAward.get(0).getUserId());
                                        profitUser.setAdvertisementId(advertisement.getId());
                                        profitUserMapper.insertSelective(profitUser);//推荐人奖励录入数据库
                                    }
                                }
                                delivery.setRedPacketMoney(mon);//红包的额度
                            }else{
                                //正常情况不会进入这里，如果进入，说明广告的状态不对
                                System.out.println("实际投放金额已经超过总金额!");
                            }
                        }
                        delivery.setFoundDate(date);
                        if(deliveryMapper.insertSelective(delivery) > 0){//添加投放记录
                            System.out.println("投放记录添加成功");
                            //默认给用户领取红包
                            if(delivery.getRedPacketType() == 1){//现金红包,需要判断后台 内圈红包开关 参数是否开启
                                if(parameterMapper.selectByPrimaryKey(3).getStatus() == 1){//查询 内圈红包开关 参数
                                    Redpacket redpacket = new Redpacket();
                                    redpacket.setId(UUIDCreator.getUUID());
                                    redpacket.setUserId(bindings.get(0).getUserId());
                                    redpacket.setRedFoundDate(date);
                                    redpacket.setRedStatus(0);
                                    redpacket.setRedIncomeOut(true);
                                    redpacket.setRedCategory(2);//2--广告红包
                                    redpacket.setRedSubtotal(delivery.getRedPacketMoney());
                                    redpacketMapper.insertSelective(redpacket);//添加红包
                                }else {//如果没有开启 内圈红包开关 ，则把红包金额兑换成积分，加入积分表
                                    List<Proportion> proportions = proportionMapper.selectByStatus(3);//3--内圈红包与积分比例
                                    Jifen jifen = new Jifen();
                                    jifen.setId(UUIDCreator.getUUID());
                                    jifen.setJifenFoudDate(date);
                                    jifen.setUserId(bindings.get(0).getUserId());
                                    jifen.setJifenStatus(0);
                                    jifen.setJifenIncomeOut(true);
                                    jifen.setJifenCategory(7);//7--广告积分
                                    jifen.setJifenSubtotal(delivery.getRedPacketMoney() * proportions.get(0).getProportionAfter());
                                    jifenMapper.insertSelective(jifen);
                                }
                            }else if(delivery.getRedPacketType() == 2){//积分红包
                                Jifen jifen = new Jifen();
                                jifen.setId(UUIDCreator.getUUID());
                                jifen.setJifenFoudDate(date);
                                jifen.setUserId(bindings.get(0).getUserId());
                                jifen.setJifenStatus(0);
                                jifen.setJifenIncomeOut(true);
                                jifen.setJifenCategory(7);
                                jifen.setJifenSubtotal(delivery.getRedPacketMoney()+1);
                                jifenMapper.insertSelective(jifen);
                            }

                            //投放记录添加成功，需要判断广告金额是否已经被投放完毕，投放完毕的，需要修改广告的投放状态
                            if(advertisement.getKey() == 1) {//判断大圈投放或小圈投放,1-大圈，2-小圈
                                Float daquanDeliveryMoney = deliveryMapper.sumByDeliveryMoney(advertisement.getId(),1);//统计大圈已经投放的金额
                                if(daquanDeliveryMoney == null){
                                    daquanDeliveryMoney = new Float(0.0);
                                }
                                //比较总积分与已投放积分
                                if(advertisement.getDaquanJifen() <= daquanDeliveryMoney){
                                    //修改大圈投放状态
                                    Advertisement advertisement1 = new Advertisement();
                                    advertisement1.setId(advertisement.getId());
                                    advertisement1.setDaquanStatus(2);//1--未投放完毕，2--已投放完毕
                                    if(advertisement.getNeiquanStatus() == 2){//同时判断内圈的投放状态，如果都已投放完毕，则更改整条广告的状态
                                        advertisement1.setStatus(3);//1--已暂停，2--已开始，3--已投放完毕
                                    }
                                    advertisementMapper.updateByPrimaryKeySelective(advertisement1);
                                }
                            }else if(advertisement.getKey() == 2) {//判断大圈投放或小圈投放,1-大圈，2-小圈
                                Float neiquanDeliveryMoney = deliveryMapper.sumByDeliveryMoney(advertisement.getId(),2);//统计内圈已经投放的金额
                                if(neiquanDeliveryMoney == null){
                                    neiquanDeliveryMoney = new Float(0.0);
                                }
                                //比较总金额与已投放金额
                                if(advertisement.getNeiquanMoneyNumber() <= neiquanDeliveryMoney){
                                    //修改内圈投放状态
                                    Advertisement advertisement1 = new Advertisement();
                                    advertisement1.setId(advertisement.getId());
                                    advertisement1.setNeiquanStatus(2);//1--未投放完毕，2--已投放完毕
                                    if(advertisement.getDaquanStatus() == 2){//同时判断大圈的投放状态，如果都已投放完毕，则更改整条广告的状态
                                        advertisement1.setStatus(3);//1--已暂停，2--已开始，3--已投放完毕
                                    }
                                    advertisementMapper.updateByPrimaryKeySelective(advertisement1);
                                }
                            }

                        }

                    }else {
                        System.out.println("发送失败");
                    }


                }else {
                    System.out.println("设备不在圈内：" + position.getDevNum());
                    //不在圈内，发送平台广告和公益广告，通过GPS定位查看该设备所在城市，根据不同城市的查询后台设置的不同的公益广告或平台广告
                    //查询出要发送的平台广告
                    AdverPlatform adverPlatform = adverPlatformMapper.selectByPrimaryKey("ASHGVDUGYSADQWUG1287EGUAGDYGASDA");
                    //先查询数据库，确定后台是否开启公益广告发送的功能。
                    if(adverPlatform.getStatus() == 0){//状态（0--已关闭，1--已开启）
                        continue;
                    }
                    //投放广告
                    String s = position.getDevNum();
                    String sendMsg = "msg=" + adverPlatform.getAdver() + "&list=[" + s + "]";
                    String response = "";
                    try {
                        response = GetPositionList.sendPost("http://127.0.0.1:8888/aqb/tests/sendMsg", sendMsg);
                        if (response.equals("e")) {
                            System.out.println("给列表用户发送消息时异常");
                            continue;
                        }
                    } catch (Exception e) {
                        System.out.println("给列表用户发送消息时异常");
                        e.printStackTrace();
                        continue;
                    }
                    //解析接收到的json响应
                    JsonParser parser = new JsonParser();
                    JsonObject object = (JsonObject) parser.parse(response);
                    JsonElement status = object.get("status");
                    System.out.println("getAsInt()===" + status.getAsInt());
                    if(status.getAsInt() == 1){
                        System.out.println("发送失败");
                        continue;
                    }
                    if (status.getAsInt() == 0) {
                        System.out.println("公益广告发送成功");
                        //添加平台广告投放表数据
                        DeliveryPlatform deliveryPlatform = new DeliveryPlatform();
                        deliveryPlatform.setId(UUIDCreator.getUUID());
                        deliveryPlatform.setFoundDate(date);
                        deliveryPlatform.setStatus(0);
                        deliveryPlatform.setUserId(s);
                        deliveryPlatform.setAdverPlatformId(adverPlatform.getId());
                        deliveryPlatform.setAdverPlatformContent(adverPlatform.getAdver());
                        deliveryPlatform.setAdverPlatformScore(Float.valueOf(adverPlatform.getCity()));
                        if (position.getHasPos()) {//判断是否收到GPS
                            deliveryPlatform.setLongitude(position.getLongitude());
                            deliveryPlatform.setLatitude(position.getLatitude());
                        }
                        deliveryPlatformMapper.insertSelective(deliveryPlatform);//添加到数据库
                        //默认帮用户领取公益广告收到的积分
                        Jifen jifen = new Jifen();
                        jifen.setId(UUIDCreator.getUUID());
                        jifen.setJifenFoudDate(date);
                        jifen.setUserId(bindings.get(0).getUserId());
                        jifen.setJifenStatus(0);
                        jifen.setJifenIncomeOut(true);
                        jifen.setJifenCategory(14);//14-公益广告积分
                        jifen.setJifenSubtotal(Float.valueOf(adverPlatform.getCity()));
                        jifenMapper.insertSelective(jifen);

                    }
                }
            }

        }
    }
}
