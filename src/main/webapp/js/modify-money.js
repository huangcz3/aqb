/**
 * Created by Administrator on 2017/7/29.
 */
$(function () {
    $.ajax({
        type: "GET",//读取方式
        url: "../api/deposit/selectDepositNumber",//通过url去读取json/数据
        contentType: "application/json",
        success: function (date) {//result变量去接收json数据
            console.log(date);
            var str='';
            if (date.status == false) {
                var time = date.body.foundDate;
                var timeout = getDate(time);//时间戳转换
                str+='<tr><td><input type="text" value="'+date.body.money+'" class="moneyInput"> </td><td>'+timeout+'</td><td><button class="reject">修改</button></td></tr>'
                $("tbody").append(str)
            } else {
                $(".error").text(data.message);
                $(".error").fadeToggle(300,function () {
                    $(".error").fadeToggle(2000)
                    window.location.reload();
                })
            }
        },//success
    });
    $(document).on("click",".reject",function () {
        var money=$(".moneyInput").val()
        $.ajax({
            type: "post",//读取方式
            url:"../api/deposit/updateDepositNumber?money="+parseInt(money),//通过url去读取json/数据
            contentType: "application/json",
            success: function (data) {//result变量去接收json数据
                if (data.status == 1) {
                    $(".error").text(data.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(2000)
                    })
                    window.location.reload();
                } else {
                    $(".error").text(data.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(3000)
                    })
                }
            },//success
        });
    })
    function getDate(tm){
        var tt=new Date(tm).toLocaleString();
        return tt;
    }
})