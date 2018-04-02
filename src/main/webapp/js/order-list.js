/**
 * Created by Administrator on 2017/7/28.
 */
$(function () {
    // 驳回待修改重新提交订单列表
    $(".reject-modify").on("click",function () {
        $.ajax({
            type: "GET",//读取方式
            url: "../api/advertisement/adminAdvertisementshStatus",//通过url去读取json/数据
            contentType: "application/json",
            success: function (data) {//result变量去接收json数据
                $("tbody").html("")
                if (data.status == false) {
                    $(".error").text(data.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(1000)
                    })
                    var str='';
                    for(var i=0;i<data.body.length;i++){
                        str+='<tr><td>'+data.body[i].user.userName+'</td><td>'+data.body[i].advertisementContent+'</td>' +
                            '<td>驳回</td><td class="table-btn"><button class="reject" value="'+data.body[i].id+'" name="2">退费</button></td></tr>'
                    }
                    $(".pageTotal").text("共"+data.totalRow+"条")
                    $("#pageFirstCont").text(data.currentPage);
                    $(".pageCount").text("共"+data.totalPage+"页");
                    $(".pagingBox").attr("sate","../api/advertisement/adminAdvertisementshStatus?currentPage=")
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
    })
    // 投放中订单列表
    $(".delivery").on("click",function () {
        $.ajax({
            type: "GET",//读取方式
            url: "../api/advertisement/adminAdvertisementstatus",//通过url去读取json/数据
            contentType: "application/json",
            success: function (data) {//result变量去接收json数据
                $("tbody").html("")
                if (data.status == false) {
                    $(".error").text(data.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(1000)
                    })
                    var str='';
                    for(var i=0;i<data.body.length;i++){
                        str+='<tr><td>'+data.body[i].user.userName+'</td><td>'+data.body[i].advertisementContent+'</td>' +
                            '<td>投放中</td><td class="table-btn"><button class="reject" value="'+data.body[i].id+'" name="1">退费</button></td></tr>'
                    }
                    $(".pageTotal").text("共"+data.totalRow+"条")
                    $("#pageFirstCont").text(data.currentPage);
                    $(".pageCount").text("共"+data.totalPage+"页")
                    $(".pagingBox").attr("sate","../api/advertisement/adminAdvertisementstatus?currentPage=")
                    $(".pagingInput").attr("name","1");
                    $("tbody").append(str);
                } else {
                    $(".error").text(data.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(3000)
                    })
                }
                console.log(data)
            },//success
        });
    })
    // 退费并取消订单
    $(document).on("click",".reject",function () {
        var id=$(this).val();
        var name=$(this).attr("name");
        console.log(name)
        var searchDate={
            id:id
        }
        console.log(JSON.stringify(searchDate));
        $.ajax({
            type: "post",//读取方式
            url: "../api/advertisement/refund",//通过url去读取json/数据
            contentType: "application/json",
            data: JSON.stringify(searchDate),//是否向后台传参数
            success: function (date) {//result变量去接收json数据
                if (date.status == false) {
                    $(".error").text(date.message);
                    if(name==1){
                        $('.delivery').trigger("click");
                    }else if(name==2){
                        $('.reject-modify').trigger("click");
                    }
                } else {
                    $(".error").text(date.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(2000)
                    })
                }
            },//success
        });
    })
    // 退费订单列表
    $(".back").on("click",function () {
        $.ajax({
            type: "GET",//读取方式
            url: "../api/advertisement/adminAdvertisementstatus3",//通过url去读取json/数据
            contentType: "application/json",
            success: function (data) {//result变量去接收json数据
                $("tbody").html("")
                if (data.status == false) {
                    $(".error").text(data.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(1000)
                    })
                    var str='';
                    for(var i=0;i<data.body.length;i++){
                        str+='<tr><td>'+data.body[i].user.userName+'</td><td>'+data.body[i].advertisementContent+'</td>' +
                            '<td>退费</td><td class="table-btn"> 退费成功</td></tr>'
                    }
                    $(".pageTotal").text("共"+data.totalRow+"条");
                    $("#pageFirstCont").text(data.currentPage);
                    $(".pageCount").text("共"+data.totalPage+"页");
                    $(".pagingBox").attr("sate","../api/advertisement/adminAdvertisementstatus3?currentPage=")
                    $(".pagingInput").attr("name","2");
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
    })
    // 已完成订单列表
    $(".complete").on("click",function () {
        $.ajax({
            type: "GET",//读取方式
            url: "../api/advertisement/adminAdvertisementrfStatus",//通过url去读取json/数据
            contentType: "application/json",
            success: function (data) {//result变量去接收json数据
                $("tbody").html("")
                if (data.status == false) {
                    $(".error").text(data.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(1000)
                    })
                    var str='';
                    for(var i=0;i<data.body.length;i++){
                        str+='<tr><td>'+data.body[i].user.userName+'</td><td>'+data.body[i].advertisementContent+'</td>' +
                            '<td>已完成</td><td class="table-btn"> 投放完成</td></tr>'
                    }
                    $(".pageTotal").text("共"+data.totalRow+"条");
                    $("#pageFirstCont").text(data.currentPage);
                    $(".pageCount").text("共"+data.totalPage+"页");
                    $(".pagingBox").attr("sate","../api/advertisement/adminAdvertisementrfStatus?currentPage=")
                    $(".pagingInput").attr("name","3");
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
    })
    // 分页 首页
    $(".pageHome").on("click",function () {
        pages(1);
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
            pages(parseInt(pagNumber)-1);
        }
    });
    //下一页
    $(".pageNext").on("click",function () {
        var pagNumber=$("#pageFirstCont").text();
        if(pagNumber !==null || pagNumber !==undefined){
            pages(parseInt(pagNumber)+1);
        }else {
            alert(pagNumber)
        }
    });
    //跳转至
    $(".pagBtn").on("click",function () {
        var pageCount=$(".pageCount").text();
            pageCount=pageCount.replace(/[^0-9]/ig,"")
         console.log(pageCount)
        var pagNumber=$(".pagingInput").val();
        if(pagNumber>pageCount){
            $(".error").text("超过总页数");
            $(".error").fadeToggle(300,function () {
                $(".error").fadeToggle(3000)
            })
            return false
        }
        if(pagNumber){
            pages(pagNumber);
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
    // 分页函数
    function pages(pag) {
        var names=$(".pagingInput").attr("name");//0----驳回 1---投放中 2----退费订单 3---已完成
        var path=$(".pagingBox").attr("sate");
        $.ajax({
            type: "GET",//读取方式
            url: path+pag,//通过url去读取json/数据
            contentType: "application/json",
            success: function (data) {//result变量去接收json数据
                $("tbody").html("");
                var str='';
                if (data.status == false) {
                    for(var i=0;i<data.body.length;i++){
                        if(names==0){
                                $("tbody").append('<tr><td>'+data.body[i].user.userName+'</td><td>'+data.body[i].advertisementContent+'</td>' +
                                    '<td>驳回</td><td class="table-btn"><button class="reject" value="'+data.body[i].id+'" name="2">退费</button></td></tr>')
                        }else if(names==1){
                            $("tbody").append('<tr><td>'+data.body[i].user.userName+'</td><td>'+data.body[i].advertisementContent+'</td>' +
                                '<td>驳回</td><td class="table-btn"><button class="reject" value="'+data.body[i].id+'" name="2">退费</button></td></tr>')
                        }else if(names==2){
                            $("tbody").append('<tr><td>'+data.body[i].user.userName+'</td><td>'+data.body[i].advertisementContent+'</td>' +
                                '<td>退费</td><td class="table-btn"> 退费成功</td></tr>')
                        }else {
                            $("tbody").append('<tr><td>'+data.body[i].user.userName+'</td><td>'+data.body[i].advertisementContent+'</td>' +
                                '<td>已完成</td><td class="table-btn"> 投放完成</td></tr>')
                        }
                    }
                    $(".pageTotal").text("共"+data.totalRow+"条");
                    $("#pageFirstCont").text(data.currentPage);
                    $(".pageCount").text("共"+data.totalPage+"页");
                } else {
                    $(".error").text(data.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(3000)
                    })
                }
            },//success
        });
    }

})