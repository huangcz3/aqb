/**
 * Created by Administrator on 2017/8/11.
 */
$(function () {
    var u = navigator.userAgent;
    var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
    var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
    // alert('是否是Android：'+isAndroid);
    // alert('是否是iOS：'+isiOS);
    if(isAndroid){
        $(".share-promptBtnPass").attr("href","javascript:;")
    }
    if(isiOS){
        $(".share-promptBtnPass").attr("href","javascript:;")
    }
    $(".share-btns").on("click",function () {
        var phone=$(".share-input").val()
        var ipone=/^0{0,1}(13[0-9]|15[1-9]|18[1-9])[0-9]{8}$/;
        console.log(phone);
        if(!ipone.test(phone)||phone==""){
            $(".share-input").css("border-color","red")
            console.log(phone)
        }else {
            $(".share-promptwait").fadeIn(300);
            $(".share-input").css("border-color","#434343");
            var Request=new UrlSearch(); //实例化
            var userIds=Request.userId;
            console.log(userIds)
            var searchDate={
                shareTel:phone,
                userId:userIds
            };
            console.log(JSON.stringify(searchDate));
            $.ajax({
                type: "post",//读取方式
                url: "../api/share/addShareAward",//通过url去读取json/数据
                contentType: "application/json",
                data: JSON.stringify(searchDate),//是否向后台传参数
                success: function (date) {//result变量去接收json数据
                    $(".share-promptwait").fadeOut(300);
                    if(date.state==1){
                        $(".share-promptPass").fadeIn(300);
                    }else{
                        $(".share-promptFail").fadeIn(300);
                        $("#error").text(date.body.message);
                    }
                },//success
            });
        }
    })
    $(".share-promptBtnFail").on("click",function () {
        $(".share-promptFail").fadeOut(300)
    })
})
// 获取地址id
function UrlSearch(){
    var name,value;
    var str=location.href; //取得整个地址栏
    var num=str.indexOf("?")
    str=str.substr(num+1); //取得所有参数   stringvar.substr(start [, length ]
    var arr=str.split("&"); //各个参数放到数组里
    for(var i=0;i < arr.length;i++){
        num=arr[i].indexOf("=");
        if(num>0){
            name=arr[i].substring(0,num);
            value=arr[i].substr(num+1);
            this[name]=value;
        }
    }
}