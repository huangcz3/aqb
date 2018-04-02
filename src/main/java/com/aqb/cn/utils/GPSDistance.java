package com.aqb.cn.utils;

/**
 * Created by Administrator on 2017/6/23.
 */
public class GPSDistance {

    public static void main(String[] args) {

        double lat1=28.12376123651,  lng1=112.1231231231;
        double lat2=30.75502,  lng2=103.91799;
        double getDistance = GetDistance(lat1, lng1, lat2, lng2);
        System.out.println(getDistance);

    }

    final static double EARTH_RADIUS = 6378.137;//地球半径

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 计算两个GPS点的距离
     * @param lat1 纬度
     * @param lng1 经度
     * @param lat2 纬度
     * @param lng2 经度
     * @return
     */
    public static double GetDistance(double lat1, double lng1, double lat2, double lng2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000.0;
        return s;
    }

}
