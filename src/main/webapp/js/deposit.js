/**
 * Created by Administrator on 2017/7/27.
 */
$(function () {
    // 搜索
    depositSearch()
    $(".search").on("click",function () {
        depositSearch()
    })
    // 已交押金
    $(".query").on("click",function () {
        var searchDate={
            limit:1
        }
        console.log(JSON.stringify(searchDate));
        $.ajax({
            type: "post",//读取方式
            url: "../api/deposit/searchUser1",//通过url去读取json/数据
            contentType: "application/json",
            data: JSON.stringify(searchDate),//是否向后台传参数
            success: function (data) {//result变量去接收json数据
                $("tbody").html("");
                if (data.status == false) {
                    $(".error").text(data.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(3000)
                    })
                    var str='';
                    for(var i=0;i<data.body.length;i++){
                        str+=' <tr><td>'+data.body[i].userFull+'</td><td>'+data.body[i].userName+'</td>'
                        if(data.body[i].dizhi==null||data.body[i].dizhi==""){
                            str+='<td>未填写地址</td>'
                        }else{
                            str+='<td>'+data.body[i].dizhi.dizhiDiqu+data.body[i].dizhi.dizhiXiangxi+'</td>'
                        }
                        if(data.body[i].binding_status==1) {
                            str+='<td>已安装</td>'
                        }else {
                            str+='<td>未安装</td>'
                        }
                        if(data.body[i].deposit_status==1){
                            str+='<td>已交</td><td class="table-btn"><button class="reject" value="'+data.body[i].userName+'">退还押金</button></td></tr>'
                        }else {
                            str+='<td>未交</td><td class="table-btn"><button class="pass" value="'+data.body[i].userName+'">充值押金</button></td></tr>'
                        }
                    }
                    $(".pageTotal").text("共"+data.totalRow+"条");
                    $("#pageFirstCont").text(data.currentPage);
                    $(".pageCount").text("共"+data.totalPage+"页");
                    $(".pagingBox").attr("sate","../api/deposit/searchUser1");
                    $(".pagingInput").attr("name","1");
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
    $(document).on("click",".pass",function () {
        var id=$(this).val();
        var searchDate={
            userName:id
        }
        console.log(JSON.stringify(searchDate));
            $.ajax({
                type: "post",//读取方式
                url: "../api/deposit/rechargeDeposit",//通过url去读取json/数据
                contentType: "application/json",
                data: JSON.stringify(searchDate),//是否向后台传参数
                success: function (date) {//result变量去接收json数据
                    if (date.status == false) {
                        $(".error").text(date.message);
                        $(".error").fadeToggle(300,function () {
                            $(".error").fadeToggle(300)
                        })
                        $('.search').click()
                    } else {
                        $(".error").text(date.message);
                        $(".error").fadeToggle(300,function () {
                            $(".error").fadeToggle(300)
                        })
                    }
                },//success
            });
    })
    $(document).on("click",".reject",function () {
        $(".error").text("正在研发中");
        $(".error").fadeToggle(300,function () {
            $(".error").fadeToggle(3000)
        })
    })
    $(".searchInput").on("click",function () {
        $(document).keydown(function(event){
            if(event.keyCode == 13){ //绑定回车
                $('.search').click(); //自动/触发登录按钮
            }
        });
    });
    // 分页 首页
    $(".pageHome").on("click",function () {
        var names=$(".pagingInput").attr("name");//0----所有 1---已交押金
        var searchInput=$(".searchInput").val();
        if(names==0){
            pages(1,searchInput);
        }else {
            pages(1,"");
        }

    });
    //上一页
    $(".pageUpper").on("click",function () {
        var names=$(".pagingInput").attr("name");//0----所有 1---已交押金
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
                if(names==0){
                pages(parseInt(pagNumber)-1,searchInput);
                }else {
                    pages(parseInt(pagNumber)-1,"");
                }
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
        var pagNumber=$("#pageFirstCont").text();//获取当前页数
        var names=$(".pagingInput").attr("name");//0----所有 1---已交押金
        var searchInput=$(".searchInput").val();//获取输入框的值
        if(pagNumber !==null || pagNumber !==undefined){
            if(names==0){
                pages(parseInt(pagNumber)+1,searchInput);
            }else {
                pages(parseInt(pagNumber)+1,"");
            }
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
        var names=$(".pagingInput").attr("name");//0----所有 1---已交押金
        var searchInput=$(".searchInput").val();//获取输入框的值
        if(pagNumber){
            if(names==0){
                pages(pagNumber,searchInput);
            }else {
                pages(pagNumber,"");
            }
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
    function pages(pag,name) {
        var names=$(".pagingInput").attr("name");//0----所有 1---已交押金
        var path=$(".pagingBox").attr("sate");
        var searchDate={
            limit:pag,
            userName:name
        }
        console.log(JSON.stringify(searchDate))
        $.ajax({
            type: "post",//读取方式
            url: path,//通过url去读取json/数据
            contentType: "application/json",
            data: JSON.stringify(searchDate),//是否向后台传参数
            success: function (data) {//result变量去接收json数据
                $("tbody").html("");
                var str='';
                if (data.status == false) {
                    for(var i=0;i<data.body.length;i++){
                        if(names==0){
                            str+=' <tr><td>'+data.body[i].userFull+'</td><td>'+data.body[i].userName+'</td>'
                            if(data.body[i].dizhi==null||data.body[i].dizhi==""){
                                str+='<td>未填写地址</td>'
                            }else{
                                str+='<td>'+data.body[i].dizhi.dizhiDiqu+data.body[i].dizhi.dizhiXiangxi+'</td>'
                            }
                            if(data.body[i].binding_status==1) {
                                str+='<td>已安装</td>'
                            }else {
                                str+='<td>未安装</td>'
                            }
                            if(data.body[i].deposit_status==1){
                                str+='<td>已交</td><td class="table-btn"><button class="reject" value="'+data.body[i].userName+'">退还押金</button></td></tr>'
                            }else {
                                str+='<td>未交</td><td class="table-btn"><button class="pass" value="'+data.body[i].userName+'">充值押金</button></td></tr>'
                            }
                        }else if(names==1){
                            str+=' <tr><td>'+data.body[i].userFull+'</td><td>'+data.body[i].userName+'</td>'
                            if(data.body[i].dizhi==null||data.body[i].dizhi==""){
                                str+='<td>未填写地址</td>'
                            }else{
                                str+='<td>'+data.body[i].dizhi.dizhiDiqu+data.body[i].dizhi.dizhiXiangxi+'</td>'
                            }
                            if(data.body[i].binding_status==1) {
                                str+='<td>已安装</td>'
                            }else {
                                str+='<td>未安装</td>'
                            }
                            if(data.body[i].deposit_status==1){
                                str+='<td>已交</td><td class="table-btn"><button class="reject" value="'+data.body[i].userName+'">退还押金</button></td></tr>'
                            }else {
                                str+='<td>未交</td><td class="table-btn"><button class="pass" value="'+data.body[i].userName+'">充值押金</button></td></tr>'
                            }
                        }
                    }
                    $("tbody").append(str);
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
    // 页面加载时获取全部
    function depositSearch() {
        var searchInput=$(".searchInput").val();
        var searchDate={
            userName:searchInput,
            limit:"1"
        }
        console.log(JSON.stringify(searchDate));
        $.ajax({
            type: "post",//读取方式
            url: "../api/deposit/searchUser",//通过url去读取json/数据
            contentType: "application/json",
            data: JSON.stringify(searchDate),//是否向后台传参数
            success: function (data) {//result变量去接收json数据
                $("tbody").html("")
                if (data.status == false) {
                    $(".error").text(data.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(3000)
                    })
                    var str='';
                    for(var i=0;i<data.body.length;i++){
                        str+=' <tr><td>'+data.body[i].userFull+'</td><td>'+data.body[i].userName+'</td>'
                        if(data.body[i].dizhi==null||data.body[i].dizhi==""){
                            str+='<td>未填写地址</td>'
                        }else{
                            str+='<td>'+data.body[i].dizhi.dizhiDiqu+data.body[i].dizhi.dizhiXiangxi+'</td>'
                        }
                        if(data.body[i].binding_status==1) {
                            str+='<td>已安装</td>'
                        }else {
                            str+='<td>未安装</td>'
                        }
                        if(data.body[i].deposit_status==1){
                            str+='<td>已交</td><td class="table-btn"><button class="reject" value="'+data.body[i].userName+'">退还押金</button></td></tr>'
                        }else {
                            str+='<td>未交</td><td class="table-btn"><button class="pass" value="'+data.body[i].userName+'">充值押金</button></td></tr>'
                        }
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