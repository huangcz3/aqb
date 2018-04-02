/**
 * Created by Administrator on 2017/8/7.
 */
$(function () {
    tbodyAjax(1);
    //分享奖励
    $.ajax({
        type: "post",//读取方式
        url: "../api/share/findShareRule",//通过url去读取json/数据
        contentType: "application/json",
        // data: JSON.stringify(searchDate),//是否向后台传参数
        success: function (date) {//result变量去接收json数据
            if(date.status==false){
                $(".jifen").val(date.body.integral);
                $(".money").val(date.body.money);
                if(date.body.shareRule==1){
                    $(".share-setJifen").show();
                    $(".share-setMoney").hide();
                    $(".share-setSelect option[value='1']").attr("selected",true)

                }else if(date.body.shareRule==2){
                    $(".share-setJifen").hide();
                    $(".share-setMoney").show();
                    $(".share-setSelect option[value='2']").attr("selected",true)
                }
            }else {
                $(".error").text(date.message);
                $(".error").fadeToggle(300,function () {
                    $(".error").fadeToggle(300)
                })
            }
            console.log(date)
        },//success
    });
    // 查询规则
    ruleAjax();
    // 点击富文本出现
    $(".share-modify").on("click",function () {
        $(".shaer-alert").fadeIn(300)
        $(".panel-body").html($(".share-ruleCont-P").html())
    })
    // 关闭富文本的编辑窗
    $(".preview-picturesClone").on("click",function () {
        $(".shaer-alert").fadeOut(300)
    })
    // 提交富文本
    $(".share-alertModify").on("click",function () {
        $(".btn-codeview").click();
        var rule=$(".note-codable").val()
        var searchDate={
            rule:rule
        };
        console.log(JSON.stringify(searchDate));
        $.ajax({
            type: "post",//读取方式
            url: "../api/share/addShareComment",//通过url去读取json/数据
            contentType: "application/json",
            data: JSON.stringify(searchDate),//是否向后台传参数
            success: function (date) {//result变量去接收json数据
                $(".btn-codeview").click();
                $(".shaer-alert").fadeOut(300);
                ruleAjax();
                $(".error").text(date.message);
                $(".error").fadeToggle(300,function () {
                    $(".error").fadeToggle(300)
                })
            },//success
        });
    })
$(".share-setSelect").on("change",function () {
    var ststus=$(".share-setSelect option:selected").val();
    if(ststus==1){
       $(".share-setJifen").show()
       $(".share-setMoney").hide()
    }else {
        $(".share-setJifen").hide()
        $(".share-setMoney").show()
    }
})
$(".jifenBtn").on("click",function () {
    var ststus=$(".share-setSelect option:selected").val();
    var jifen=$(".jifen").val();
    var money=$(".money").val("");
    subAjax(ststus,jifen,money)
})
    $(".moneyBtn").on("click",function () {
        var ststus=$(".share-setSelect option:selected").val();
        var jifen=$(".jifen").val("");
        var money=$(".money").val();
        subAjax(ststus,jifen,money)
    })
    // 查询规则
    function ruleAjax() {
        $.ajax({
            type: "post",//读取方式
            url: "../api/share/findShareComment",//通过url去读取json/数据
            contentType: "application/json",
            success: function (data) {//result变量去接收json数据
                $(".share-ruleCont-P").html((data.rule).replace("\"",""));
                console.log(data)
            },//success
        });
    } // 分页 首页
    $(".pageHome").on("click",function () {
        tbodyAjax(1,"");
    });
    //上一页
    $(".pageUpper").on("click",function () {
        var pagNumber=$("#pageFirstCont").text();
        if(pagNumber<=1){
            $(".error").text("已是第一页");
            $(".error").fadeToggle(300,function () {
                $(".error").fadeToggle(3000)
            })
            return false
        }
        tbodyAjax(parseInt(pagNumber)-1,"");
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
        tbodyAjax(parseInt(pagNumber)+1,"");

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
            tbodyAjax(pagNumber);
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

    // 查询排行榜
    function tbodyAjax(pages) {
        var searchDate={
            page:pages
        };
        $.ajax({
            type: "post",//读取方式
            url: "../api/share/findShareAwardSort",//通过url去读取json/数据
            contentType: "application/json",
            data: JSON.stringify(searchDate),//是否向后台传参数
            success: function (data) {//result变量去接收json数据
                var str='';
               if(data.status==false){
                   for(var i=0;i<data.body.length;i++){
                       str+='<tr><td>'+(i+1)+'</td>'
                       if(data.body[i].user_name){
                           str+= '<td>'+data.body[i].user_name+'</td>'
                       }else if(data.body[i].user_full){
                           str+= '<td>'+data.body[i].user_full+'</td>'
                       }else if(data.body[i].user_nick){
                           str+= '<td>'+data.body[i].user_nick+'</td>'
                       }else {
                           str+= '<td>&nbsp</td>'
                       }
                       str+= '<td>'+data.body[i].count+'</td>'
                       str+= '<td>'+data.body[i].integrals+'个</td>'
                       str+= '<td>'+data.body[i].moneys+'元</td>'
                       str+= '</tr>'
                   }
                   $("tbody").append(str)
                   $(".pageTotal").text("共"+data.totalRow+"条");
                   $("#pageFirstCont").text(data.currentPage);
                   $(".pageCount").text("共"+data.totalPage+"页");
               }
                console.log(data)
            },//success
        });
    }
    // 提交奖励方式的
    function subAjax(shareRule,integral,money) {
        var searchDate={
            shareRule:shareRule,
            integral:integral,
            money:money
        };
        console.log(JSON.stringify(searchDate));
        $.ajax({
            type: "post",//读取方式
            url: "../api/share/addShareRule",//通过url去读取json/数据
            contentType: "application/json",
            data: JSON.stringify(searchDate),//是否向后台传参数
            success: function (date) {//result变量去接收json数据
                $(".error").text(date.message);
                $(".error").fadeToggle(300,function () {
                    $(".error").fadeToggle(300)
                })
            },//success
        });
    }
})
