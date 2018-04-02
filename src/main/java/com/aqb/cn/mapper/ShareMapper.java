package com.aqb.cn.mapper;

import com.aqb.cn.bean.*;
import org.apache.ibatis.annotations.Options;

import java.util.List;
import java.util.Map;

/**
 * Created by XD on 2017/8/5.
 */
public interface ShareMapper {

    //将其他规则状态改为不可用
    public void updateShareCommentState();

    //新建规则
    public int addShareComment(ShareComment shareComment);

    //进入页面时查询正在使用的规则
    public ShareComment findShareComment();

    //进入该管理页面查询出正在使用的分享规则
    public ShareRule findShareRule();

    //管理员新建规则前将其他规则禁用
    public void updateShareRuleState();

    //管理员新建规则
    public int addShareRule(ShareRule shareRule);

    //管理员进入页面时分页查询奖励排行榜
    public Map findShareAwardSort();

    @Options(timeout = 1000,flushCache = true)
    public List<Map> findShareRankingPaging(Map map);

    @Options(timeout = 1000,flushCache = true)
    public Map findStatistics();

    //管理员清空排行状态
    public int updateShare(ShareAward shareAward);

    //查询接受分享的人是否已接受过分享了
    public ShareAward findSAByShareTel(ShareAward shareAward);

    //将信息添加到分享记录表
    public int addShareAward(ShareAward shareAward);

    //查询接受分享人是否已注册
    public User findUserByShareTel(ShareAward shareAward);

    //添加积分
    public int addJifen(Jifen jifen);


    //将此条分享信息状态修改为已发放奖励
    public int updateAwardState(ShareAward shareAward);



    //查询个人邀请排行榜
    public List<Map> findShareByUserId(User user);

    //查询奖励排行榜
    public List<Map> findShareRanking();
}
