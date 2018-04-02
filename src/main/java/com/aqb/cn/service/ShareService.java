package com.aqb.cn.service;

import com.aqb.cn.bean.ShareAward;
import com.aqb.cn.bean.ShareComment;
import com.aqb.cn.bean.ShareRule;
import com.aqb.cn.bean.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by XD on 2017/8/5.
 */
@Transactional
public interface ShareService {

    //管理员新建邀请分享规则说明
    public int addShareComment(ShareComment shareComment);

    //进入页面时查询正在使用的规则说明
    public ShareComment findShareComment();

    //进入该管理页面查询出正在使用的分享规则
    public ShareRule findShareRule();

    //管理员新建分享规则
    public int addShareRule(ShareRule shareRule);

    //管理员进入页面时分页查询奖励排行榜
    public List findShareRankingPaging(int pageIndex);

    public Map findShareAwardSort();

    //管理员清空排行状态
    public int updateShare(ShareAward shareAward);

    //查询接受分享的人是否已经注册过了
    public User findUserByShareTel(ShareAward shareAward);

    //查询接受分享的人是否已接受过分享了
    public ShareAward findSAByShareTel(ShareAward shareAward);

    //将信息添加到分享记录表
    public int addShareAward(ShareAward shareAward);

    //查询接受分享人是否已注册，如果已注册，就给两人奖励
    public Map addAward(ShareAward shareAward,ShareRule shareRule);

    //查询个人邀请排行榜
    public Map findShareByUserId(User user);

    //查询奖励排行榜
    public List<Map> findShareRanking();
}
