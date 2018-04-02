/**
 * Created by Administrator on 2017/8/10.
 */
$(function () {
    var year = document.getElementById("year");
    var my = new Date();
    var endYear = my.getFullYear();// 获取当前年份
    for (var i = 2000; i <= endYear; i++) {
        year.options.add(new Option(i, i));
    }
    $("#year option[value='"+endYear+"']").attr("selected", "selected");
    yearHe(endYear);//获取的当年的信息
    yearSee(endYear)
    $("#year").on("change",function () {
        var yearSelect=$("#year option:selected").val();
        console.log(yearSelect)
        yearHe(yearSelect);
        yearSee(yearSelect);
    })
    $(".commit-month").on("change",function () {
        var yearSelect=$(".commit-month option:selected").val();
        monthHe(yearSelect)
        monthSee(yearSelect);
    })
    function yearHe(endYear) {
        $.ajax({
            type: "get",//读取方式
            url: "../api/profitPlatform/getProfitPlatformYear?year="+endYear,//通过url去读取json/数据
            contentType: "application/json",
            // data: JSON.stringify(searchDate),//是否向后台传参数
            success: function (date) {//result变量去接收json数据
                console.log(date)
                if(date.status==false){
                    $(".commit-money").text('金额'+date.body.sum1+'元')
                    $(".commit-jifen").text('积分'+date.body.sum2+'个')
                }else {
                    $(".error").text(date.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(3000)
                    })
                }
            },//success
        });
    }
    function yearSee(endYear) {
        $.ajax({
            type: "get",//读取方式
            url: "../api/profitPlatform/getProfitPlatformYears?year="+endYear,//通过url去读取json/数据
            contentType: "application/json",
            // data: JSON.stringify(searchDate),//是否向后台传参数
            success: function (data) {//result变量去接收json数据
                $(".commit-yearSee").empty();
                $(".commit-month").empty();
                if(data.status==false){

                  console.log(data);
                    var str='';
                    var opt='';
                    for(var i=0;i<data.body.length;i++){
                        str+='<tr><td>'+data.body[i].year+'</td>' +
                            '<td>'+data.body[i].sum1+'</td>' +
                            '<td>'+data.body[i].sum2+'</td></tr>'
                        opt+='<option value="'+data.body[i].year+'">'+data.body[i].year+'</option>'
                    }
                    $(".commit-yearSee").append(str);
                    $(".commit-month").append(opt).change();
                }else {
                    $(".error").text(data.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(3000)
                    })
                }
            },//success
        });
    }
    function monthHe(month) {
        $.ajax({
            type: "get",//读取方式
            url: "../api/profitPlatform/getProfitPlatformMonth?month="+month,//通过url去读取json/数据
            contentType: "application/json",
            // data: JSON.stringify(searchDate),//是否向后台传参数
            success: function (date) {//result变量去接收json数据
                console.log(date)
                if(date.status==false){
                    $(".commit-monthMoney").text('金额'+date.body.sum1+'元')
                    $(".commit-monthJifen").text('积分'+date.body.sum2+'个')
                }else {
                    $(".error").text(date.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(3000)
                    })
                }
            },//success
        });
    }
    function monthSee(month) {
        $.ajax({
            type: "get",//读取方式
            url: "../api/profitPlatform/getProfitPlatformMonths?month="+month,//通过url去读取json/数据
            contentType: "application/json",
            // data: JSON.stringify(searchDate),//是否向后台传参数
            success: function (data) {//result变量去接收json数据
                $(".commit-monthSee").empty()
                if(data.status==false){
                    console.log(data);
                    var str='';
                    for(var i=0;i<data.body.length;i++){
                        str+='<tr><td>'+data.body[i].year+'</td>' +
                            '<td>'+data.body[i].sum1+'</td>' +
                            '<td>'+data.body[i].sum2+'</td></tr>'
                    }
                    $(".commit-monthSee").append(str)
                }else {
                    $(".error").text(data.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(3000)
                    })
                }
            },//success
        });
    }
})