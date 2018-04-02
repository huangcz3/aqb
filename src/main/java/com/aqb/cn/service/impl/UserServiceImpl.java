package com.aqb.cn.service.impl;

import com.aqb.cn.bean.User;
import com.aqb.cn.common.EncryptionUtil;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.UserMapper;
import com.aqb.cn.service.UserService;
import com.aqb.cn.utils.Request;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/9.
 */
@Service
public class UserServiceImpl implements UserService {

    private Log logger = LogFactory.getLog(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Override
    public int add(User user) {
        User user1 = userMapper.selectByPrimaryKey(user.getId());
        User user2 = userMapper.queryByName(user.getUserName());
        if(user1 == null && user2 == null){
            if (userMapper.insertSelective(user) > 0) {
                logger.debug("添加用户" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
         }
        return Status.EXISTS;
        //return userMapper.insertSelective(obj);
    }

    @Override
    public int update(User user) {
        User user2 = userMapper.selectByPrimaryKey(user.getId());
        if (user2 == null) {
            logger.warn("尝试更新用户" + user.getId() + " ,但是用户不存在");
            return Status.NOT_EXISTS;
        }
        if (userMapper.updateByPrimaryKeySelective(user) > 0) {
            logger.debug("更新成功" + user2.getUserName());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(User user) {
        User user1 = userMapper.selectByPrimaryKey(user.getId());
        if (user1 == null) {
            logger.debug("尝试删除用户，但是用户不存在");
            return Status.NOT_EXISTS;
        }
        if (userMapper.deleteByPrimaryKey(user.getId())> 0 ){
            logger.debug("删除用户成功");
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public User get(String id) {
//        User user = userMapper.selectByPrimaryKey(id);
//        if (user == null){
//            logger.debug("尝试得到用户，但是用户不存在");
//            return null;
//        }
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
        queryBase.setResults(userMapper.queryUser(queryBase));
        queryBase.setTotalRow(userMapper.countUser(queryBase));
    }

    @Override
    public int login(User user) {
        User user2 = userMapper.queryByName(user.getUserName());
        if (user2 == null) {
            return Status.NOT_EXISTS;
        }
        String encryptuserPass = EncryptionUtil.encrypt(user.getUserPass());

        if(!encryptuserPass.equals(user2.getUserPass())){
            return Status.PASSWD_NOT_MATCH;
        }
        return Status.SUCCESS;
    }

    @Override
    public User queryByName(String name) {
        return userMapper.queryByName(name);
    }

    @Override
    public User queryByCode1(String userCode1) {
        return userMapper.queryByCode1(userCode1);
    }

    @Override
    public User queryByCode2(String userCode2) {
        return userMapper.queryByCode2(userCode2);
    }

    @Override
    public User queryByCode3(String userCode3) {
        return userMapper.queryByCode3(userCode3);
    }

    @Override
    public List<User> queryUser(Request req) {
        return userMapper.queryUser1(req);
    }

    @Override
    public long countUser(Request req) {
        return userMapper.countUser1(req);
    }

    @Override
    public List<User> queryUsermohu(QueryBase queryBase) {
        queryBase.setResults(userMapper.queryUsermohu(queryBase));
        return null;
    }

    @Override
    public List<User> queryUser2(Map map) {
        return userMapper.queryUser2(map);
    }

    @Override
    public Map findC() {
        return userMapper.findC();
    }

    @Override
    public List<Map> queryUser3(Map map) {
        return userMapper.queryUser3(map);
    }
}
