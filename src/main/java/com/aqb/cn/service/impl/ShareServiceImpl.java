package com.aqb.cn.service.impl;

import com.aqb.cn.bean.ShareAward;
import com.aqb.cn.bean.ShareComment;
import com.aqb.cn.bean.ShareRule;
import com.aqb.cn.bean.User;
import com.aqb.cn.mapper.ShareMapper;
import com.aqb.cn.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by XD on 2017/8/5.
 */
@Service
@Transactional
public class ShareServiceImpl implements ShareService {

    @Autowired(required = false)
    private ShareMapper shareMapper;


    //管理员新建邀请分享规则
    @Override
    public int addShareComment(ShareComment shareComment) {
        //将其他规则状态改为不可用
        shareMapper.updateShareCommentState();
        //添加新规则
        int i = shareMapper.addShareComment(shareComment);

        return i;
    }

    //进入页面时查询正在使用的规则说明
    @Override
    public ShareComment findShareComment() {
        return shareMapper.findShareComment();
    }

    //进入该管理页面查询出正在使用的分享规则
    @Override
    public ShareRule findShareRule() {
        return shareMapper.findShareRule();
    }
    //管理员新建分享规则
    @Override
    public int addShareRule(ShareRule shareRule) {
        //将其他规则状态改为不可用
        shareMapper.updateShareRuleState();
        //添加新规则
        int i = shareMapper.addShareRule(shareRule);

        return i;
    }

    //管理员进入页面时分页查询奖励排行榜
    @Override
    public Map findShareAwardSort() {
        return shareMapper.findShareAwardSort();
    }

    //管理员进入页面时分页查询奖励排行榜
    @Override
    public List findShareRankingPaging(int pageIndex) {

        Map lmap = new LinkedHashMap();
        lmap.put("fields","u.user_name,u.user_full,u.user_nick,count(sa2.user_id),sum(sa2.integral),sum(sa2.money)");
        lmap.put("tables","user u,share_award sa2");
        lmap.put("where","u.id in (SELECT DISTINCT(sa.user_id) FROM share_award sa WHERE sa.award_state not in (0) AND sa.ranking_state not in (0)) AND u.id = sa2.user_id");
        lmap.put("groupby","sa2.user_id");
        lmap.put("orderby","count(sa2.user_id) DESC ,sum(sa2.integral) DESC ,sum(sa2.money) DESC");
        lmap.put("pageindex",pageIndex);
        lmap.put("pageSize",10);
        lmap.put("sumfields","integral,money");




        List list1 = shareMapper.findShareRankingPaging(lmap);
        Map map2 = shareMapper.findStatistics();


        List list = new ArrayList();
        list.add(list1);
        list.add(map2);


        return list;
    }

    //管理员清空排行状态
    @Override
    public int updateShare(ShareAward shareAward) {
        return shareMapper.updateShare(shareAward);
    }


    //查询接受分享的人是否已经注册过了
    @Override
    public User findUserByShareTel(ShareAward shareAward) {
        return shareMapper.findUserByShareTel(shareAward);
    }

    //查询接受分享的人是否已接受过分享了
    @Override
    public ShareAward findSAByShareTel(ShareAward shareAward) {
        return shareMapper.findSAByShareTel(shareAward);
    }

    //将信息添加到分享记录表
    @Override
    public int addShareAward(ShareAward shareAward) {
        return shareMapper.addShareAward(shareAward);
    }

    //查询接受分享人是否已注册，如果已注册，就给两人奖励
    @Override
    public Map addAward(ShareAward shareAward,ShareRule shareRule) {
        //查询接受分享人是否已注册
        User user = shareMapper.findUserByShareTel(shareAward);
        Map map = new HashMap();

        if(user==null){
            map.put("message","手机号注册成功才算分享成功哦");
            map.put("state","0");
            return map;
        }else if(shareAward.getUserId().equals(user.getId())){
            map.put("message","自己不能邀请自己");
            map.put("state","0");
            return map;
        }else {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String time1 = sdf.format(new Date());
//            Date time2 = TimeToString.StrToDate(time1);
//
//            //如果已注册，就根据奖励规则给两人奖励
//            Jifen jifen1 = new Jifen();//分享人
//            Jifen jifen2 = new Jifen();//接受分享的人
//            jifen1.setId(UUIDCreator.getUUID());
//            jifen2.setId(UUIDCreator.getUUID());
//            jifen1.setUserId(shareAward.getUserId());
//            jifen2.setUserId(user.getId());
//            //判断分享规则
//            if(shareRule.getShareRule()==1){//规则为积分
//                jifen1.setJifenCategory(15);
//                jifen2.setJifenCategory(15);
//                jifen1.setJifenSubtotal(new Float(shareAward.getIntegral()));
//                jifen2.setJifenSubtotal(new Float(shareAward.getIntegral()));
//            }else if(shareRule.getShareRule()==2){//规则为金额
//                jifen1.setJifenCategory(16);
//                jifen2.setJifenCategory(16);
//                jifen1.setJifenSubtotal(new Float(shareAward.getMoney()));
//                jifen2.setJifenSubtotal(new Float(shareAward.getMoney()));
//            }
//            jifen1.setJifenIncomeOut(true);
//            jifen2.setJifenIncomeOut(true);
//            jifen1.setJifenFoudDate(time2);
//            jifen2.setJifenFoudDate(time2);
//
//
//
//            //向积分记录表添加信息
//            int i = shareMapper.addJifen(jifen1);
//            int j = shareMapper.addJifen(jifen2);
//
//            //将此条分享信息状态修改为已发放奖励
//            shareAward.setShareTime(new Timestamp(new Date().getTime()));
//            System.out.println(shareAward);
//            int k = shareMapper.updateAwardState(shareAward);
//
//            System.out.println(i+":"+j+":"+k);
            map.put("message","已注册用户不能再接受邀请");
            map.put("state","1");

            return map;
        }
    }

    //查询个人邀请排行榜
    @Override
    public Map findShareByUserId(User user) {

        List<Map> list1 = shareMapper.findShareByUserId(user);
        System.out.println(list1);
        int count = list1.size();
        int integrals = 0;
        Double moneys = 0.0;

        for (Map map:list1){
            for (Object key:map.keySet()){
                if(key.toString().equals("integral")){
                    integrals += Integer.parseInt(map.get(key).toString());
                }
                if(key.toString().equals("money")){
                    moneys += Double.parseDouble(map.get(key).toString());
                }
            }

        }
        Map map = new LinkedHashMap();
        map.put("count",count);
        map.put("integrals",integrals);
        map.put("moneys",moneys);

        Map map1 = new LinkedHashMap();
        map1.put("list",list1);
        map1.put("map",map);



        return map1;
    }

    @Override
    public List<Map> findShareRanking() {
        return shareMapper.findShareRanking();
    }


}
