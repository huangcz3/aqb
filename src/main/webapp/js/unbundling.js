/**
 * Created by Administrator on 2017/7/29.
 */
$(function () {
    // 页面加载查询
    queryAjax("","");
    // 搜索框
    $(".search").on("click",function () {
        var serch=$(".searchInput").val();
        console.log(serch)
        queryAjax("1",serch)
    })
    // 分页 首页
    $(".pageHome").on("click",function () {
        queryAjax(1,"")
    });
    //上一页
    $(".pageUpper").on("click",function () {
        var pagNumber=$("#pageFirstCont").text();
        if(pagNumber==1){
            $(".error").text("已是第一页");
            $(".error").fadeToggle(300,function () {
                $(".error").fadeToggle(3000)
            })
            return false
        }
        if(pagNumber !==null && pagNumber !==undefined && pagNumber!==false){
            queryAjax(parseInt(pagNumber)-1,"");
        }
    });
    //下一页
    $(".pageNext").on("click",function () {
        var pagNumber=$("#pageFirstCont").text();
        var pageCount=$(".pageCount").text();
        pageCount=pageCount.replace(/[^0-9]/ig,"")
        if(parseInt(pagNumber)>=parseInt(pageCount)){
            $(".error").text("已经是最后一页了");
            $(".error").fadeToggle(300,function () {
                $(".error").fadeToggle(3000)
            })

            return false
        }
        if(pagNumber !==null || pagNumber !==undefined){
            queryAjax(parseInt(pagNumber)+1,"");
        }else {
            alert(pagNumber)
        }
    });
    //跳转至
    $(".pagBtn").on("click",function () {
        var pageCount=$(".pageCount").text();
        pageCount=pageCount.replace(/[^0-9]/ig,"")
        console.log(pageCount);
        var pagNumber=$(".pagingInput").val();
        console.log(pagNumber);
        if(parseInt(pagNumber)>parseInt(pageCount)){
            $(".error").text("超过总页数");
            $(".error").fadeToggle(300,function () {
                $(".error").fadeToggle(3000)
            })
            return false
        }else {
            if(pagNumber){
                queryAjax(pagNumber,"");
            }
        }

    });
    // 设备解除绑定
    $(document).on("click",".reject",function () {
        $(".reject-prompt").fadeIn(300)
        $(".reject-btn").val($(this).val())
    })
    $(".reject-btn").on("click",function () {
        var id= $(this).val();
        var relieve= $(".reject-text").val();
        var searchDate={
            id:id,
            relieve:relieve
        }
        console.log(JSON.stringify(searchDate));
        $.ajax({
            type: "post",//读取方式
            url: "../api/binding/updateRelieveBinding",//通过url去读取json/数据
            contentType: "application/json",
            data: JSON.stringify(searchDate),//是否向后台传参数
            success: function (date) {//result变量去接收json数据
                if (date.status == false) {
                    $(".error").text(date.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(1000)
                    })
                    $(".reject-prompt").fadeOut(300);
                    $(".reject-text").text("");
                    queryAjax("","");
                } else {
                    $(".error").text(date.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(2000)
                    })
                }
            },//success
        });
    })
    // 绑定设备
    $(document).on("click",".pass",function () {
        $(".bindingUser-box").fadeIn(300)
        $(".equipment").val($(this).val())
    })
    $(".pass-btn").on("click",function () {
        var userPhone=$(".userPhone").val();
        var equipment=$(".equipment").val();
        if(userPhone){
            var searchDate={
                bindingNumber:equipment,
                userId:userPhone
            }
            console.log(JSON.stringify(searchDate));
            $.ajax({
                type: "post",//读取方式
                url: "../api/binding/updateBindingAdmin",//通过url去读取json/数据
                contentType: "application/json",
                data: JSON.stringify(searchDate),//是否向后台传参数
                success: function (date) {//result变量去接收json数据
                    if (date.status == false) {
                        $(".error").text(date.message);
                        $(".error").fadeToggle(300,function () {
                            $(".error").fadeToggle(1000);
                        })
                        $(".bindingUser-box").fadeOut(300);
                        $(".userPhone").val("");
                        queryAjax("","");
                    } else {
                        $(".error").text(date.message);
                        $(".error").fadeToggle(300,function () {
                            $(".error").fadeToggle(2000)
                        })
                    }
                },//success
            });
        }
    })
    //关闭弹窗
    $(".preview-picturesClone").on("click",function () {
        $(this).parent().parent().fadeOut(300)
        $(".reject-text").val("");
    });
})

function queryAjax(currentPage,bindingNumber) {
    $.ajax({
        type: "GET",//读取方式
        url:"../api/binding/queryBindingAdmin?currentPage="+currentPage+"&bindingNumber="+bindingNumber,//通过url去读取json/数据
        contentType: "application/json",
        success: function (data) {//result变量去接收json数据
            $("tbody").html("");
            var str="";
            if (data.status == false) {
                    for(var i=0;i<data.body.length;i++){
                        str+="<tr><td>"+data.body[i].deviceNumber+"</td><td> "+data.body[i].batchNumber+"</td><td>"+data.body[i].batchDescribe+"</td>"
                        if(data.body[i].status==0){
                            str+="<td>未绑定</td><td><button class='pass unpass' value='"+data.body[i].deviceNumber+"'>绑定设备</button></td></tr>"
                        }else {
                            str+="<td>已绑定</td><td><button class='reject' value='"+data.body[i].id+"'>解除绑定</button></td></tr>"
                        }
                    }
                $(".pageTotal").text("共"+data.totalRow+"条");
                $("#pageFirstCont").text(data.currentPage);
                $(".pageCount").text("共"+data.totalPage+"页");
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