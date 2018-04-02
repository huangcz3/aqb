$(function () {
   $.ajax({
       url:"../api/advertisement/adminAdvertisement",
       type:"GET",
       success:function (data) {
           var items=data.body;
           var length=items.length;
            console.log(items)

           if(length>0){
               for(var i=0;i<length;i++){
                var time = items[i].foundDate;
                function getDate(tm){
                  var tt=new Date(tm).toLocaleString();
                  return tt;
                }
               var timeout = getDate(time);//时间戳转换
  $("tbody").append("<tr><td class=''>"+items[i].userId+"</td><td class=''>"+items[i].advertisementContent+"</td><td class=''>"+timeout+"</td><td ><button class='pass' name='"+items[i].id+"'>通过</button></td><td ><button class='delete' name='"+items[i].id+"' value='"+items[i].id+"'>驳回</button></td></tr>");
               }

           }
       }
   })
});

$(document).on("click",".pass",function () {
    var id=$(this).attr("name");
        var upexam={
            "id":id,
            "shStatus":3
        }
        $.ajax({
            url:"../api/advertisement/examineAdvertisement",
            type:"POST",
            contentType:"application/json",
            data:JSON.stringify(upexam),
            success:function (data) {
                $(".error").text(data.message);
                $(".error").fadeToggle(300,function () {
                    $(".error").fadeToggle(3000)
                })
               window.history.go(0);
            }
        })
});
// 填写未通过原因
$(document).on("click",".delete",function () {
    var varId=$(this).val();
    $(".reject-prompt").fadeIn(300);
    $(".reject-btn").val(varId);
})
// 审核不通过
$(".reject-btn").on("click",function () {
    var reason=$(".reject-text").val();
    var id=$(this).val();
    if(reason){
        var searchDate={
            shStatus:"2",
            bhContent:reason,
            id:id
        }
        console.log(JSON.stringify(searchDate));
        $.ajax({
            type: "post",//读取方式
            url: "../api/advertisement/examineAdvertisement",//通过url去读取json/数据
            contentType: "application/json",
            data: JSON.stringify(searchDate),//是否向后台传参数
            success: function (date) {//result变量去接收json数据
                if (date.status == false) {
                    $(".error").text(date.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(3000);
                    })
                    $(this).parent().parent().fadeOut(300);
                    window.location.reload();
                } else {
                    $(".error").text(date.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(3000);
                    })
                }
            },//success
        });
    }else {
        $(".error").text("请填写未通过的原因");
        $(".error").fadeToggle(300,function () {
            $(".error").fadeToggle(3000)
        })
    }
})
$(".preview-picturesClone").on("click",function () {
    $(this).parent().parent().fadeOut(300)
    $(".reject-text").val("");
});