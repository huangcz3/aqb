package com.aqb.cn.action;

import com.aqb.cn.annotation.AdminLogin;
import com.aqb.cn.bean.ProfitPlatform;
import com.aqb.cn.common.Response;
import com.aqb.cn.common.Status;
import com.aqb.cn.pojo.Year;
import com.aqb.cn.service.ProfitPlatformService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Administrator on 2017/8/10.
 */
@Controller
public class ProfitPlatformAction {

    private final Log logger = LogFactory.getLog(ProfitPlatformAction.class);

    @Autowired
    private ProfitPlatformService profitPlatformService;
    

    /**
     * 年收益总和
     * 后台
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/profitPlatform/getProfitPlatformYear", method = RequestMethod.GET)
    public Object getProfitPlatformYear(HttpServletRequest request,
                                      @RequestParam("year")String year) {
        Float sum1 = profitPlatformService.sumMoneyByYear(1, year);//统计一年的积分
        Float sum2 = profitPlatformService.sumMoneyByYear(2, year);//统计一年的金额
        if(sum1 == null){
            sum1 = (float)0;
        }
        if(sum2 == null){
            sum2 = (float)0;
        }
        Map map = new HashMap<>();
        map.put("sum1",sum1);
        map.put("sum2",sum2);
        return new Response(Status.SUCCESS,map);
    }

    /**
     * 月收益总和
     * 后台
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/profitPlatform/getProfitPlatformMonth", method = RequestMethod.GET)
    public Object getProfitPlatformMonth(HttpServletRequest request,
                                      @RequestParam("month")String month) {
        Float sum1 = profitPlatformService.sumMoneyByMonth(1, month);//统计一个月的积分
        Float sum2 = profitPlatformService.sumMoneyByMonth(2, month);//统计一个月的金额
        if(sum1 == null){
            sum1 = (float)0;
        }
        if(sum2 == null){
            sum2 = (float)0;
        }
        Map map = new HashMap<>();
        map.put("sum1",sum1);
        map.put("sum2",sum2);
        return new Response(Status.SUCCESS,map);
    }

    /**
     * 年收益记录
     * 一年中每个月的记录
     * 后台
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/profitPlatform/getProfitPlatformYears", method = RequestMethod.GET)
    public Object getProfitPlatformYears(HttpServletRequest request,
                                         @RequestParam("year")String year) {
        String[] strings = new String[12];
        strings[0] = year + "-01";
        strings[1] = year + "-02";
        strings[2] = year + "-03";
        strings[3] = year + "-04";
        strings[4] = year + "-05";
        strings[5] = year + "-06";
        strings[6] = year + "-07";
        strings[7] = year + "-08";
        strings[8] = year + "-09";
        strings[9] = year + "-10";
        strings[10] = year + "-11";
        strings[11] = year + "-12";
        List<Year> years = new ArrayList<>(12);
        for(int i=0;i<strings.length;i++){
            Float sum1 = profitPlatformService.sumMoneyByMonth(1, strings[i]);//统计一个月的积分
            Float sum2 = profitPlatformService.sumMoneyByMonth(2, strings[i]);//统计一个月的金额
            if(sum1 == null){
                sum1 = (float)0;
            }
            if(sum2 == null){
                sum2 = (float)0;
            }
            Year y = new Year();
            y.setYear(strings[i]);
            y.setSum1(sum1);
            y.setSum2(sum2);
            years.add(y);
        }
        return new Response(Status.SUCCESS,years);
    }

    /**
     * 月收益记录
     * 一个月中每天的记录
     * 后台
     */
    @AdminLogin
    @ResponseBody
    @RequestMapping(value = "/api/profitPlatform/getProfitPlatformMonths", method = RequestMethod.GET)
    public Object getProfitPlatformMonths(HttpServletRequest request,
                                         @RequestParam("month")String month) {
        int year = Integer.valueOf(month.substring(0,4));
        int mon = 0;
        if(Integer.valueOf(month.substring(5,6)) == 0){
            mon = Integer.valueOf(month.substring(6,7));
        }
        if(Integer.valueOf(month.substring(5,6)) == 1){
            mon = Integer.valueOf(month.substring(5,7));
        }
        Calendar c = Calendar.getInstance();
        c.set(year, mon, 0); //输入类型为int类型
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);//得到天数
        String[] strings = new String[dayOfMonth];
        for(int i=0;i<dayOfMonth;i++){
            String day;
            if(i < 9){
                day = "-0"+String.valueOf(i+1);
            }else{
                day = "-"+String.valueOf(i+1);
            }
            strings[i] = month + day;
        }
        List<Year> years = new ArrayList<>(dayOfMonth);
        for(int i=0;i<strings.length;i++){
            Float sum1 = profitPlatformService.sumMoneyByDay(1, strings[i]);//统计一天的积分
            Float sum2 = profitPlatformService.sumMoneyByDay(2, strings[i]);//统计一天的金额
            if(sum1 == null){
                sum1 = (float)0;
            }
            if(sum2 == null){
                sum2 = (float)0;
            }
            Year y = new Year();
            y.setYear(strings[i]);
            y.setSum1(sum1);
            y.setSum2(sum2);
            years.add(y);
        }
        return new Response(Status.SUCCESS,years);
    }

}
