package com.aqb.cn.pojo;

import com.aqb.cn.bean.Redpacket;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */
public class MyAssets_redpacket {

    List<Redpacket> redpackets;//红包

    Float redpacket_Assets;//红包资产

    public List<Redpacket> getRedpackets() {
        return redpackets;
    }

    public void setRedpackets(List<Redpacket> redpackets) {
        this.redpackets = redpackets;
    }

    public Float getRedpacket_Assets() {
        return redpacket_Assets;
    }

    public void setRedpacket_Assets(Float redpacket_Assets) {
        this.redpacket_Assets = redpacket_Assets;
    }
}
