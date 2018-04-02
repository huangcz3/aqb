/**
 * Created by Administrator on 2017/7/29.
 */
$(function () {
    $.ajax({
        type: "GET",//读取方式
        url:"../api/adverPlatform/queryAdverPlatform",//通过url去读取json/数据
        contentType: "application/json",
        success: function (data) {//result变量去接收json数据
            $("tbody").html("");
            var str="";
            if (data.status == false) {
                // console.log(data.body[0].foundDate)
                    var time = data.body.foundDate;
                    var timeout = getDate(time);//时间戳转换
                    console.log(timeout)
                    str+="<tr><td>"+timeout+"</td><td><p class='adv-p'>"+data.body.adver+"</p><textarea  placeholder='请输入广告内容' disabled  class='adv-sttText' >"+data.body.adver+"</textarea></td>"
                    if(data.body.status==1){
                        str+="<td>开启</td><td><button class='reject' value='"+data.body.id+"' name='"+data.body.status+"'>关闭</button><button class='modify' value='"+data.body.id+"'>编辑</button></td></tr>"
                    }else {
                        str+="<td>关闭</td><td><button class='pass' value='"+data.body.id+"'  name='"+data.body.status+"'>开启</button><button class='modify'value='"+data.body.id+"'>编辑</button></td></tr>"
                    }
                $("tbody").append(str)
            } else {
                $(".error").text(data.message);
                $(".error").fadeToggle(300,function () {
                    $(".error").fadeToggle(3000)
                })
            }
            console.log(data)
        },//success
    });
    // 关闭
    $(document).on("click",".reject",function () {
        var id= $(this).val();
        var textCont=$(".adv-sttText").text();
        console.log(textCont)
        modinfy(id,textCont,0)
    })
    // 开启
    $(document).on("click",".pass",function () {
        var id= $(this).val();
        var textCont=$(".adv-sttText").text();
        modinfy(id,textCont,1)

    })
    // 修改
    var flag=0;
    $(document).on("click",".modify",function () {
            $(this).prev().hide();
            $(this).text("提交");
            $(".adv-p").hide();
            $(".adv-sttText").attr("disabled",false);
          $(this).addClass("submit")
          $(this).removeClass("modify")

    })
    $(document).on("click",".submit",function () {
            var idname=$(this).prev().attr("name");
            var id= $(this).val();
            var textCont=$(".adv-sttText").val();
        console.log(textCont)
            if(idname==1){
                modinfy(id,textCont,1)

            }else {
                modinfy(id,textCont,0)
            }
    })
})
function modinfy(ids,advers,statuss) {
    var searchDate={
        id:ids,
        adver:advers,
        status:statuss
    }
    console.log(JSON.stringify(searchDate))
    $.ajax({
        type: "post",//读取方式
        url:"../api/adverPlatform/updateAdverPlatform",//通过url去读取json/数据
        contentType: "application/json",
        data: JSON.stringify(searchDate),//是否向后台传参数
        success: function (data) {//result变量去接收json数据
            if (data.status == false) {
                $(".error").text(data.message);
                $(".error").fadeToggle(300,function () {
                    $(".error").fadeToggle(2000)
                    window.location.reload();
                })

            } else {
                $(".error").text(data.message);
                $(".error").fadeToggle(300,function () {
                    $(".error").fadeToggle(3000)
                })
            }
        },//success
    });
}
function getDate(tm){
    var tt=new Date(tm).toLocaleString();
    return tt;
}