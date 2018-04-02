/**
 * Created by Administrator on 2017/8/2.
 */
$(function () {
    // 页面完成加载时调用
    barrageAjax("","");
    // 搜索框
    $(".search").on("click",function () {
        var searchInput=$(".searchInput").val();
        barrageAjax(searchInput,"");
    })
    // 删除单条信息
    $(document).on("click",".reject",function () {
        var userId=$(this).val();
        var searchDate={
            id:userId
        }
        console.log(JSON.stringify(searchDate));
        $.ajax({
            type: "post",//读取方式
            url: "../api/barrage/deleteBarrage",//通过url去读取json/数据
            contentType: "application/json",
            data: JSON.stringify(searchDate),//是否向后台传参数
            success: function (date) {//result变量去接收json数据
                if (date.status == false) {
                    $(".error").text(date.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(300)
                    })
                    barrageAjax("","");
                } else {
                    $(".error").text(date.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(300)
                    })
                }
            },//success
        });
    })
    // 多选框操作
    $(".checkSelect").on("click",function () {
        if(this.checked){
            $("input[name='checkbox']").each(function(){this.checked=true;});
        }else{
            $("input[name='checkbox']").each(function(){this.checked=false;});
        }
    });
    $(document).on("click","input[name='checkbox']",function () {
        if($(this).is(':checked')==false){
            $(".checkSelect").attr("checked",false)
        }
    })
    // 删除多条信息
    $(".delect-one").on("click",function () {
        var s= $('input[name="checkbox"]:checked').length;
        if(s>1){
            $(".error").text("每次只能选择一个")
            $(".error").fadeToggle(300,function () {
                $(".error").fadeToggle(3000)
            })
        }else if(s<1){
            $(".error").text("请选择要删除的话题")
            $(".error").fadeToggle(300,function () {
                $(".error").fadeToggle(3000)
            })
        }else {
          var id=  $('input[name="checkbox"]:checked').val()
            var searchDate={
                id:id
            };
            console.log(JSON.stringify(searchDate));
            $.ajax({
                type:"post",//读取方式
                url:"../api/barrage/deleteTopicBarrage",//通过url去读取json/数据
                contentType: "application/json",
                data:JSON.stringify(searchDate),//是否向后台传参数
                success:function(data){//result变量去接收json数据
                    if(data.status==false){
                        $(".error").text(data.message);
                        $(".error").fadeToggle(300,function () {
                            $(".error").fadeToggle(3000)
                        })
                        barrageAjax("","");
                    }else {
                        $(".error").text(data.message);
                        $(".error").fadeToggle(300,function () {
                            $(".error").fadeToggle(3000)
                        })
                    }
                }//success
            });
        }
    })
    // 分页 首页
    $(".pageHome").on("click",function () {
        barrageAjax(1,"");
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
                barrageAjax(parseInt(pagNumber)-1,"")
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
                barrageAjax(parseInt(pagNumber)+1,"");
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
            barrageAjax(pagNumber,"");
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
    // 函数
    function barrageAjax(number,name) {
        $.ajax({
            type: "get",//读取方式
            url: "../api/barrage/queryBarrage?userFull="+name+"&currentPage="+number,//通过url去读取json/数据
            contentType: "application/json",
            success: function (data) {//result变量去接收json数据
                $("tbody").html("");
                console.log(data);
                var str='';
                for(var i=0;i<data.body.length;i++) {
                    str+='<tr> <td rowspan="'+data.body[i].barrageAndUsers.length+'"><input type="checkbox" class="check " name="checkbox" value="'+data.body[i].barrageAndUsers[0].topicId+'"></td><td rowspan="'+data.body[i].barrageAndUsers.length+'">'+data.body[i].topicTitle+'</td>'
                    for(var j=0;j<data.body[i].barrageAndUsers.length;j++){
                        str+=' <td>'+data.body[i].barrageAndUsers[j].userFull+'</td> ' +
                            '<td>'+data.body[i].barrageAndUsers[j].barrageContent+'</td>' +
                            '<td><button class="reject" value="'+data.body[i].barrageAndUsers[j].id+'">删除</button></td>  </tr>'
                    }
                }
                $(".pageTotal").text("共"+data.totalRow+"条");
                $("#pageFirstCont").text(data.currentPage);
                $(".pageCount").text("共"+data.totalPage+"页");
                $("tbody").append(str);

            },//success
        });
    }
})