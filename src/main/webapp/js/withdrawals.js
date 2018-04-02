/**
 * Created by Administrator on 2017/7/27.
 */
$(function () {
    // 获取数据
    repairExamine(1)
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
            status:"3",
            id:id
        }
        console.log(JSON.stringify(searchDate));
        $.ajax({
            type: "post",//读取方式
            url: "../api/withdrawals/updateWithdrawals",//通过url去读取json/数据
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
            status:"2",
            reason:reason,
            id:id
        }
        console.log(JSON.stringify(searchDate));
        if(reason){
            $.ajax({
                type: "post",//读取方式
                url: "../api/withdrawals/updateWithdrawals",//通过url去读取json/数据
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
    // 分页 首页
    $(".pageHome").on("click",function () {
        repairExamine(1);
    });
    //上一页
    $(".pageUpper").on("click",function () {
        var searchInput=$(".searchInput").val();
        var pagNumber=$("#pageFirstCont").text();
        if(pagNumber==1){
            $(".error").text("已是第一页");
            $(".error").fadeToggle(300,function () {
                $(".error").fadeToggle(3000)
            })
            return false
        }
        if(pagNumber !==null && pagNumber !==undefined && pagNumber!==false){
            repairExamine(parseInt(pagNumber)-1)
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
        if(parseInt(pagNumber) !==null || parseInt(pagNumber) !==undefined){
            repairExamine(parseInt(pagNumber)+1);
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
        }
        if(pagNumber){
            repairExamine(pagNumber);
        }
    });
    // input绑定事件
    $(".pagingInput").on("blur",function () {
        $(document).keydown(function(event){
            if(event.keyCode == 13){ //绑定回车
                $('.pagBtn').click(); //自动/触发登录按钮
            }
        });
    })
})
function repairExamine(pages){
    // var searchDate={
    //     currentPage:pages,
    // }
    // console.log(JSON.stringify(searchDate));
    $.ajax({
        url:"../api/withdrawals/queryWithdrawalsAdmin?currentPage="+pages,
        type:"get",
        contentType: "application/json",
        // data: JSON.stringify(searchDate),//是否向后台传参数
        success:function (data) {
            console.log(data)
            var str='';
            if(data.status==false){
                for(var i=0;i<data.body.length;i++){
                    str+='<tr>'
                    if(data.body[i].user.userName==null||data.body[i].user.userName==""){
                        str+=  ' <td>'+data.body[i].user.userFull+'</td> '
                    }else {
                        str+=  ' <td>'+data.body[i].user.userName+'</td> '
                    }
                    str+=  '<td>'+data.body[i].withdrawalsMoney+'</td>'
                    if(data.body[i].accountNumberType==1){
                        str+= '<td>微信</td>'
                    }

                    str+= '<td>'+data.body[i].accountNumber+'</td>'
                    str+=  '<td class="table-btn">'
                    str+=  '<button class="pass"  value="'+data.body[i].id+'" > 通过</button>'
                    str+=  '<button class="reject" value="'+data.body[i].id+'"> 驳回</button>'
                    str+=  '</td></tr>'
                }
                $(".pageTotal").text("共"+data.totalRow+"条");
                $("#pageFirstCont").text(data.currentPage);
                $(".pageCount").text("共"+data.totalPage+"页");
                $("tbody").append(str)
            }
        }
    })
}