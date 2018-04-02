package com.aqb.cn.service.impl;

import com.aqb.cn.bean.*;
import com.aqb.cn.common.QueryBase;
import com.aqb.cn.common.Status;
import com.aqb.cn.mapper.*;
import com.aqb.cn.pojo.CofAndCofCommentAndCofFabulousAndCofPic;
import com.aqb.cn.pojo.CofAndCofPic;
import com.aqb.cn.service.CofService;
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
public class CofServiceImpl implements CofService {

    private Log logger = LogFactory.getLog(CofServiceImpl.class);

    @Autowired
    private CofMapper cofMapper;
    @Autowired
    private CofCommentMapper cofCommentMapper;
    @Autowired
    private CofFabulousMapper cofFabulousMapper;
    @Autowired
    private CofPicMapper cofPicMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public int add(Cof cof) {
        Cof cof_db = cofMapper.selectByPrimaryKey(cof.getId());
        if(cof_db == null) {
            if (cofMapper.insertSelective(cof) > 0) {
                logger.debug("添加朋友圈" + "成功");
                return Status.SUCCESS;
            }
            return Status.ERROR;
        }
        return Status.EXISTS;
    }

    @Override
    public int update(Cof cof) {
        Cof cof2 =cofMapper.selectByPrimaryKey(cof.getId());
        if(cof2 == null){
            logger.warn("尝试更新朋友圈"  + " ,但是朋友圈不存在");
            return Status.NOT_EXISTS;
        }
        if (cofMapper.updateByPrimaryKeySelective(cof) > 0) {
            logger.debug("更新朋友圈成功" + cof2.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public int delete(Cof cof) {
        Cof cof2 = cofMapper.selectByPrimaryKey(cof.getId());
        if (cof2 == null) {
            logger.warn("尝试删除朋友圈，但是朋友圈不存在");
            return Status.NOT_EXISTS;
        }
        if (cofMapper.deleteByPrimaryKey(cof.getId()) > 0 ) {
            logger.debug("删除朋友圈成功" + cof.getId());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    @Override
    public Cof get(String id) {
        return cofMapper.selectByPrimaryKey(id);
    }

    @Override
    public void query(QueryBase queryBase) {
        List<Cof> cofs = cofMapper.queryCof(queryBase);
        List<CofAndCofPic> cofAndCofPics = new ArrayList<CofAndCofPic>();
        for (Cof cof:cofs){
            CofAndCofPic cofAndCofPic = new CofAndCofPic();
            List<CofPic> cofPics = cofPicMapper.selectByCofId(cof.getId());
            User user = userMapper.selectByPrimaryKey(cof.getUserId());
            cofAndCofPic.setUser(user);
            cofAndCofPic.setCof(cof);
            cofAndCofPic.setCofPics(cofPics);
            cofAndCofPics.add(cofAndCofPic);
        }

        queryBase.setResults(cofAndCofPics);
        queryBase.setTotalRow(cofMapper.countCof(queryBase));

    }

    @Override
    public void querycof_space(QueryBase queryBase) {
        List<Cof> cofs = cofMapper.queryCof_space(queryBase);

        List<CofAndCofCommentAndCofFabulousAndCofPic> cofAndCofCommentAndCofFabulouses = new ArrayList<CofAndCofCommentAndCofFabulousAndCofPic>();

        for(Cof cof:cofs){
            CofAndCofCommentAndCofFabulousAndCofPic cofAndCofCommentAndCofFabulous = new CofAndCofCommentAndCofFabulousAndCofPic();

            List<CofComment> cofComments = cofCommentMapper.selectByCofId(cof.getId());

            List<CofFabulous> cofFabulouses = cofFabulousMapper.selectByCofId(cof.getId());

            List<CofPic> cofPics = cofPicMapper.selectByCofId(cof.getId());

            User user = userMapper.selectByPrimaryKey(cof.getUserId());

            cofAndCofCommentAndCofFabulous.setUser(user);

            cofAndCofCommentAndCofFabulous.setCof(cof);

            cofAndCofCommentAndCofFabulous.setCofComments(cofComments);

            cofAndCofCommentAndCofFabulous.setCofFabulouses(cofFabulouses);

            cofAndCofCommentAndCofFabulous.setCofPics(cofPics);

            cofAndCofCommentAndCofFabulouses.add(cofAndCofCommentAndCofFabulous);
        }

        queryBase.setResults(cofAndCofCommentAndCofFabulouses);

        queryBase.setTotalRow(cofMapper.countCof_space(queryBase));
    }
}
