package com.aqb.cn.service.impl;

import com.aqb.cn.bean.*;
import com.aqb.cn.bean.CofPic;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.*;
import com.aqb.cn.mapper.CofPicMapper;
import com.aqb.cn.pojo.CofAndCofCommentAndCofFabulousAndCofPic;
import com.aqb.cn.service.CofPicService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/27.
 */
@Service
public class CofPicServiceImpl implements CofPicService {
    private Log logger = LogFactory.getLog(CofPicServiceImpl.class);

    @Autowired
    private CofPicMapper cofPicMapper;
    @Autowired
    private CofMapper cofMapper;
    @Autowired
    private CofCommentMapper cofCommentMapper;
    @Autowired
    private CofFabulousMapper cofFabulousMapper;


    @Override
    public int add(CofPic cofPic) {
        CofPic cofPic_db = cofPicMapper.selectByPrimaryKey(cofPic.getId());
        if(cofPic_db == null) {
            if (cofPicMapper.insertSelective(cofPic) > 0) {
                logger.debug("添加点赞" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(CofPic cofPic) {
        CofPic cofPic2 =cofPicMapper.selectByPrimaryKey(cofPic.getId());
        if(cofPic2 == null){
            logger.warn("尝试更新点赞"  + " ,但是点赞不存在");
            return Status.NOT_EXISTS;
        }
        if (cofPicMapper.updateByPrimaryKeySelective(cofPic) > 0) {
            logger.debug("更新点赞成功" + cofPic2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(CofPic cofPic) {
        CofPic cofPic2 = cofPicMapper.selectByPrimaryKey(cofPic.getId());
        if (cofPic2 == null) {
            logger.warn("尝试删除点赞，但是点赞不存在");
            return Status.NOT_EXISTS;
        }
        if (cofPicMapper.deleteByPrimaryKey(cofPic.getId()) > 0 ) {
            logger.debug("删除点赞成功" + cofPic.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public CofPic get(String id) {
        return cofPicMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
        List<Cof> cofs = cofMapper.queryCof_space(queryBase);

        List<CofAndCofCommentAndCofFabulousAndCofPic> cofAndCofCommentAndCofFabulouses = new ArrayList<CofAndCofCommentAndCofFabulousAndCofPic>();

        for(Cof cof:cofs){
            CofAndCofCommentAndCofFabulousAndCofPic cofAndCofCommentAndCofFabulous = new CofAndCofCommentAndCofFabulousAndCofPic();

            //List<CofComment> cofComments = cofCommentMapper.selectByCofId(cof.getId());

            //List<CofFabulous> cofFabulouses = cofFabulousMapper.selectByCofId(cof.getId());

            List<CofPic> cofPics = cofPicMapper.selectByCofId(cof.getId());

            //User user = userMapper.selectByPrimaryKey(cof.getUserId());

            //cofAndCofCommentAndCofFabulous.setUser(user);

            cofAndCofCommentAndCofFabulous.setCof(cof);

            //cofAndCofCommentAndCofFabulous.setCofComments(cofComments);

            //cofAndCofCommentAndCofFabulous.setCofFabulouses(cofFabulouses);

            cofAndCofCommentAndCofFabulous.setCofPics(cofPics);

            cofAndCofCommentAndCofFabulouses.add(cofAndCofCommentAndCofFabulous);
        }

        queryBase.setResults(cofAndCofCommentAndCofFabulouses);

        queryBase.setTotalRow(cofMapper.countCof_space(queryBase));



//        queryBase.setResults(cofPicMapper.queryCofPic(queryBase));
//        queryBase.setTotalRow(cofPicMapper.countCofPic(queryBase));
    }

}
