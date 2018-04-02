/**
 * Created by Administrator on 2017/8/5.
 */
// ajax数据获取列表
$(function () {
    repairExamine(1);
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
        var userid=$(this).val();
        var name=$(this).attr("name");
        $(".reject-prompt").fadeIn(300);
        $(".reject-btn").val(userid);
        $(".reject-btn").attr("name",name);
    })
    // 审核通过
    $(document).on("click",".pass",function () {
        var userid=$(this).val();
        var id=$(this).attr("name");
        var searchDate={
            status:"1",
            id:id,//商家的id
            userId:userid//用户的id
        }
        console.log(JSON.stringify(searchDate));
        $.ajax({
            type: "post",//读取方式
            url: "../api/repairComment/examineRC",//通过url去读取json/数据
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
        var id=$(this).val();//用户的id
        var idName=$(this).attr("name");//评价的商户
        var searchDate={
            status:"0",
            id:idName,//
            userId:id,//用户的id
            reason:reason
        }
        console.log(JSON.stringify(searchDate));
        if(reason){
            $.ajax({
                type: "post",//读取方式
                url: "../api/repairComment/examineRCerror",//通过url去读取json/数据
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
    function repairExamine(pages){
        // var searchDate={
        //     currentPage:pages,
        // }
        // console.log(JSON.stringify(searchDate));
        $.ajax({
            type: "get",//读取方式
            url: "../api/repairComment/queryrepairComment?currentPage="+pages,//通过url去读取json/数据
            contentType: "application/json",
            // data: JSON.stringify(searchDate),//是否向后台传参数
            success: function (data) {//result变量去接收json数据
                // $("tbody").html("");
                if (data.status == false) {
                    var str='';
                    for(var i=0;i<data.body.length;i++){
                        str+='<tr><td>'+data.body[i].userName+'</td><td>'+data.body[i].repairName+'</td><td>'+data.body[i].repairCommentContent+'</td><td class="examine">'
                        if(data.body[i].picture1 !==null){
                            str+=  '<img src="'+data.body[i].picture1+'" alt="" class="binding-img">'
                        }
                        if(data.body[i].picture2 !==null){
                            str+=  '<img src="'+data.body[i].picture2+'" alt="" class="binding-img">'
                        }
                        if(data.body[i].picture3 !==null){
                            str+=  '<img src="'+data.body[i].picture3+'" alt="" class="binding-img">'
                        }
                        str+= '</td><td>'+data.body[i].repairCommentMoney+'</td> <td class="table-btn">'
                        str+= '<button class="pass" style="margin-bottom: 15px" value="'+data.body[i].userId+'" name="'+data.body[i].id+'">通过</button>'
                        str+= '<button class="reject" value="'+data.body[i].userId+'" name="'+data.body[i].id+'">驳回</button>'
                        str+= '</td> </tr>'
                    }
                    $(".pageTotal").text("共"+data.totalRow+"条");
                    $("#pageFirstCont").text(data.currentPage);
                    $(".pageCount").text("共"+data.totalPage+"页");
                    $(".pagingBox").attr("sate","../api/deposit/searchUser");
                    $(".pagingInput").attr("name","0");
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
    }
})