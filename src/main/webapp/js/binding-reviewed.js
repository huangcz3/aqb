/**
 * Created by Administrator on 2017/7/27.
 */
$(function () {
    // 获取数据
    $.ajax({
        url:"../api/binding/adminBinding",
        type:"GET",
        success:function (data) {
            console.log(data)
            var str='';
            if(data.status==false){
                for(var i=0;i<data.body.length;i++){
                    if(data.body[i].depositStatus==0){
                        str+='<tr> <td>'+data.body[i].bindingNumber+'</td> <td>'+data.body[i].userName+'</td>' +
                            '<td>'+data.body[i].bindingCar+'</td><td>'+data.body[i].bindingIdcard+'</td>' +
                            '<td><p class="binding-p">' +
                            '<img src="'+data.body[i].idcardFront+'" class="binding-img" alt=""></p><p class="binding-p1">' +
                            '<img src="'+data.body[i].idcardBack+'" class="binding-img" alt=""></p></td>' +
                            '<td><p class="binding-p"><img src="'+data.body[i].travelFront+'" class="binding-img" alt=""></p><p class="binding-p1">' +
                            '<img src="'+data.body[i].travelBack+'" class="binding-img" alt=""> </p></td><td>未缴纳</td><td class="table-btn">' +
                            '<button class="pass"  value="'+data.body[i].id+'" > 通过</button><button class="reject" value="'+data.body[i].id+'"> 驳回</button></td></tr>'
                    }else  if(data.body[i].depositStatus==1){
                        str+='<tr> <td>'+data.body[i].bindingNumber+'</td> <td>'+data.body[i].userName+'</td>' +
                            '<td>'+data.body[i].bindingCar+'</td><td>'+data.body[i].bindingIdcard+'</td>' +
                            '<td><p class="binding-p">' +
                            '<img src="'+data.body[i].idcardFront+'" class="binding-img" alt=""></p><p class="binding-p1">' +
                            '<img src="'+data.body[i].idcardBack+'" class="binding-img" alt=""></p></td>' +
                            '<td><p class="binding-p"><img src="'+data.body[i].travelFront+'" class="binding-img" alt=""></p><p class="binding-p1">' +
                            '<img src="'+data.body[i].travelBack+'" class="binding-img" alt=""> </p></td><td>已缴纳</td><td class="table-btn">' +
                            '<button  value="'+data.body[i].id+'" class="pass"> 通过</button><button class="reject" value="'+data.body[i].id+'"> 驳回</button></td></tr>'
                    }
                }
                $("tbody").append(str)
            }
        }
    })
    // 大图预览图片
    $(document).on("click",".binding-img",function () {
        var srcLarge=$(this).attr("src");
        getImageWidth(srcLarge,function(w,h){
            console.log({width:w,height:h});
            if(w>1200||h>600){
                w=1000;
                h=500;
            }
            $(".preview").css({"width":w+"px","height":h+"px","margin-top":"-"+h/2+"px","margin-left":"-"+w/2+"px","margin-bottom":"-"+h/2+"px","margin-right":"-"+w/2+"px"})
        });
        $(".preview-pictures").fadeIn(300);
        $(".preview-img").attr("src",srcLarge)
    });
    $(".preview").on("click",function (e) {
        e.stopPropagation()
    });
    $(".preview-picturesClone").on("click",function () {
        $(this).parent().parent().fadeOut(300);
        $(".reject-text").val("");
    });
    $(".preview-pictures").on("click",function () {
        $(".preview-pictures").fadeOut(300)
    });
    // 填写未通过原因
    $(document).on("click",".reject",function () {
        var varId=$(this).val();
        $(".reject-prompt").fadeIn(300);
        $(".reject-btn").val(varId);
    })
    // 审核通过
    $(document).on("click",".pass",function () {
        var id=$(this).val();
        var searchDate={
            status:"1",
            id:id
        }
        console.log(JSON.stringify(searchDate));
        $.ajax({
            type: "post",//读取方式
            url: "../api/binding/examineBinding",//通过url去读取json/数据
            contentType: "application/json",
            data: JSON.stringify(searchDate),//是否向后台传参数
            success: function (date) {//result变量去接收json数据
                if (date.status == false) {
                    $(".error").text(date.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(3000)
                    })
                    window.location.reload();
                } else {
                    $(".error").text(date.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(3000)
                    })
                }
            },//success
        });
    })
    // 审核不通过
    $(".reject-btn").on("click",function () {
        var reason=$(".reject-text").val();
        var id=$(this).val();
        var searchDate={
            status:"0",
            reason:reason,
            id:id
        }
        console.log(JSON.stringify(searchDate));
        if(reason){
            $.ajax({
                type: "post",//读取方式
                url: "../api/binding/examineBindingerror",//通过url去读取json/数据
                contentType: "application/json",
                data: JSON.stringify(searchDate),//是否向后台传参数
                success: function (date) {//result变量去接收json数据
                    if (date.status == false) {
                        $(".error").text(date.message);
                        $(".error").fadeToggle(300,function () {
                            $(".error").fadeToggle(3000)
                        })
                        $(this).parent().parent().fadeOut(300)
                        window.location.reload();
                    } else {
                        $(".error").text(date.message);
                        $(".error").fadeToggle(300,function () {
                            $(".error").fadeToggle(3000)
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
})
// 计算图片宽高
function getImageWidth(url,callback){
    var img = new Image();
    img.src = url;
    // 如果图片被缓存，则直接返回缓存数据
    if(img.complete){
        callback(img.width, img.height);
    }else{
        // 完全加载完毕的事件
        img.onload = function(){
            callback(img.width, img.height);
        }
    }
}