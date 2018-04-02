package com.aqb.cn.example;

import com.aqb.cn.common.Status;
import io.swagger.client.model.User;
import com.aqb.cn.example.api.impl.EasemobIMUsers;
import io.swagger.client.model.RegisterUsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by Administrator on 2017/8/4.
 */
public class CreateUser {

    private static final Logger logger = LoggerFactory.getLogger(CreateUser.class);
    private EasemobIMUsers easemobIMUsers = new EasemobIMUsers();

    public static void main(String[] args) {
        User user = new User();
        user.setUsername("fcea920f7412b5da7be0c");
        user.setPassword("e10adc3949ba59abbe56e057f20f883e");
        new CreateUser().createUser(user);
    }

    public int createUser(User user) {
        RegisterUsers users = new RegisterUsers();
        users.add(user);

        Object result = easemobIMUsers.createNewIMUserSingle(users);
//        logger.info(result.toString());
        if(result == null || result.equals("")){
            return Status.ERROR;
        }
        System.out.println(result.toString());
        return Status.SUCCESS;
    }

}
