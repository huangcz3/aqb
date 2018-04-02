/**
 * Created by Administrator on 2017/8/5.
 */
// ajax数据获取列表
$(function () {
    repairExamine(1);
    //查看详情
    $("tbody").on("click",".pass",function () {
        $(".merchant-promptBox").fadeIn(300)
        var id=$(this).val();
        var names=$(this).attr("name");
        names=parseInt(names);
        single(id,names)

    })
    //修改
    $(".mer-modify").on("click",function () {
        var s= $('input[name="checkbox"]:checked').length
        if(s>1){
            $(".error").text("每次只能修改一个")
            $(".error").fadeToggle(300,function () {
                $(".error").fadeToggle(3000)
            })
        }else if(s<1){
            $(".error").text("请勾选要选择的商家")
            $(".error").fadeToggle(300,function () {
                $(".error").fadeToggle(3000)
            })
        }else {
            $(".merchant-promptBox").fadeIn(300)
            var id =$('input[name="checkbox"]:checked').val();
            $(".merchant-input input[name='typeStatus']").attr("checked", "checked");
            var names = $(this).attr("name");
            names = parseInt(names);
            single(id, names)
        }
    });
    //删除
    $(".delect").on("click",function () {
        var s= $('input[name="checkbox"]:checked').length
        if(s>1){
            $(".error").text("每次只能修改一个")
            $(".error").fadeToggle(300,function () {
                $(".error").fadeToggle(3000)
            })
        }else if(s<1){
            $(".error").text("请勾选要选择的商家")
            $(".error").fadeToggle(300,function () {
                $(".error").fadeToggle(3000)
            })
        }else {
            var id =$('input[name="checkbox"]:checked').val();
            var searchDate={
                id:id
            }
            $.ajax({
                type: "post",//读取方式
                url: "../api/repair/deleteseller",//通过url去读取json/数据
                contentType: "application/json",
                data: JSON.stringify(searchDate),//是否向后台传参数
                success: function (date) {//result变量去接收json数据
                    if (date.status == false) {
                        $(".error").text(date.message);
                        $(".error").fadeToggle(300,function () {
                            $(".error").fadeToggle(300)
                        })
                        repairExamine(1);
                    } else {
                        $(".error").text(date.message);
                        $(".error").fadeToggle(300,function () {
                            $(".error").fadeToggle(300)
                        })
                    }
                },//success
            });
        }
    });
    // 新增
    $(".mer-add").on("click",function () {
        $(".merchant-promptBox").fadeIn(300);
        $(".merchant-promptBox input").attr("disabled",false);
        $(".merchant-promptBox input[type='text']").val("");
        $(".merchant-promptBox textarea").val("").attr("disabled",false);
        $(".repairAddress").attr("disabled",true);
        $('.merchant-title').text("新增商家");
        $(".merchant-pass").show().val("");
        $(".repairLongitude").text("");
        $(".repairLatitude").text("");
        $(".merchant-modify").text("设置").show();
        $(".merchant-img").attr("src","")
    })
    // 退出弹幕
    $(".cancel").on("click",function () {
        $(".merchant-promptBox").fadeOut(300)
    })
    // 地图弹窗出现
    $(".merchant-modify").on("click",function () {
        $("#mapCont").fadeIn(300);
        $("#r-result").fadeIn(300);
        $("#lng").val($(".repairLongitude").text());
        $("#lat").val($(".repairLatitude").text());
        $("#sever_add").val($(".userName").val());
    })
    // 关闭地图弹窗传值到表单
    $(".preview-picturesClone").on("click",function () {
        $("#mapCont").fadeOut(300);
        $("#r-result").fadeOut(300);
        $(".repairLongitude").text($("#lng").val());
        $(".repairLatitude").text( $("#lat").val());
        $(".repairAddress").val( $("#sever_add").val());
    })
    // 关闭地图弹窗传值到表单
    $(".map-pass").on("click",function () {
        $("#mapCont").fadeOut(300);
        $("#r-result").fadeOut(300);
        $(".repairLongitude").text($("#lng").val());
        $(".repairLatitude").text( $("#lat").val());
        $(".repairAddress").val( $("#sever_add").val());
    })
// 新增修改提交
    $(".merchant-pass").on("click",function () {
        var userId=$(this).val();//获取他的val
        var userName,userPhone,repairAddress,repairLongitude,repairLatitude,merchantText,typeStatus,merchantImg;
        userName=$(".userName").val();
        userPhone=$(".userPhone").val();
        repairAddress=$(".repairAddress").val();
        repairLongitude=$(".repairLongitude").text();
        repairLatitude=$(".repairLatitude").text();
        merchantText=$(".merchant-text").val();
        merchantImg=$(".merchant-img").attr("src");
        var id_array=new Array();
        $('input[name="typeStatus"]:checked').each(function(){
            id_array.push($(this).val());//向数组中添加元素
        });
        if(id_array.length==1){
            typeStatus=id_array.join(',');//将数组元素连接起来以构建一个字符串
        }
        if(id_array.length==2){
            var flag1=id_array[0];
            var flag2=id_array[1];
            if(flag1==1&&flag2==2){
                typeStatus=4
            }else if(flag1==1&&flag2==3){
                typeStatus=5
            }else if(flag1==2&&flag2==3){
                typeStatus=6
            }
        }
        if(id_array.length==3){
            typeStatus=7
        }
        console.log(typeStatus);
        console.log(merchantImg);
        if(userName && userPhone && repairAddress  && merchantText && typeStatus &&merchantImg){
            if(userId){
                var searchDate={
                    id:userId,
                    repairName:userName,
                    repairPhone:userPhone,
                    repairAddress:repairAddress,
                    repairLongitude:repairLongitude,
                    repairLatitude:repairLatitude,
                    repairIntroduce:merchantText,
                    status:typeStatus,
                    repairCover:merchantImg
                }
                console.log(JSON.stringify(searchDate))
                addMadify(searchDate,"../api/repair/updateseller")
            }else {
                var searchDate={
                    repairName:userName,
                    repairPhone:userPhone,
                    repairAddress:repairAddress,
                    repairLongitude:repairLongitude,
                    repairLatitude:repairLatitude,
                    repairIntroduce:merchantText,
                    status:typeStatus,
                    repairCover:merchantImg
                }
                console.log(JSON.stringify(searchDate))
                addMadify(searchDate,"../api/repair/addseller")
            }
        }else if(userName==false){
            $(".mer-error").text("请输入商家姓名")
        }else if(userPhone==false){
            $(".mer-error").text("请输入商家电话号码")
        }else if(repairAddress==false){
            $(".mer-error").text("请设置商家地址")
        }else if(merchantText==false){
            $(".mer-error").text("请输入商家介绍")
        }else if(typeStatus==undefined){
            $(".mer-error").text("请选择商家经营类型")
        }
        else if(merchantImg==undefined||merchantImg==null||merchantImg==false){
            $(".mer-error").text("请上传封面图")
        }
    })
    // 分页查询
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
    // 单条数据查询
    function single(id,names) {
        $.ajax({
            type: "get",//读取方式
            url: "../api/repair/queryById?id="+id,//通过url去读取json/数据
            contentType: "application/json",
            success: function (data) {//result变量去接收json数据
                console.log(data)
                if (data.status == false) {
                        if(names==1){
                            $('.merchant-title').text("查看商家");
                            $(".merchant-promptBox input").attr("disabled",true);
                            $(".merchant-promptBox textarea").attr("disabled",true);
                            $(".merchant-pass").hide();
                            $(".merchant-modify").hide();
                            $(".merchant-btns .cancel").text("关闭")
                        }else {
                            $('.merchant-title').text("修改商家");
                            $(".merchant-promptBox input").attr("disabled",false);
                            $(".merchant-promptBox textarea").attr("disabled",false);
                            $(".merchant-pass").show();
                            $(".repairAddress").attr("disabled",true);
                            $(".merchant-modify").show();
                            $(".merchant-btns .cancel").text("取消")
                        }
                        $(".userName").val(data.body.repairName);
                        $(".userPhone").val(data.body.repairPhone);
                        $(".repairAddress").val(data.body.repairAddress);
                        $(".repairLongitude").val(data.body.repairLongitude);
                        $(".repairLatitude").val(data.body.repairLatitude);
                        $(".merchant-text").val(data.body.repairIntroduce);
                        $(".merchant-img").attr("src",data.body.repairCover)
                        $(".fileInput").attr("id",data.body.id);
                        $(".merchant-pass").val(data.body.id);
                        if(data.body.status==1){
                            $(".merchant-input input[class='repair']").attr("checked", true);
                            $(".merchant-input input[class='maintain']").attr("checked", false);
                            $(".merchant-input input[class='car-wash']").attr("checked", false)
                        }else if(data.body.status==2){
                            $(".merchant-input input[class='repair']").attr("checked", false);
                            $(".merchant-input input[class='maintain']").attr("checked", true);
                            $(".merchant-input input[class='car-wash']").attr("checked", false)
                        }else if(data.body.status==3){
                            $(".merchant-input input[class='car-wash']").attr("checked", false)
                            $(".merchant-input input[class='repair']").attr("checked", false);
                            $(".merchant-input input[class='maintain']").attr("checked", true);
                        }else if(data.body.status==4){
                            $(".merchant-input input[class='repair']").attr("checked", true);
                            $(".merchant-input input[class='maintain']").attr("checked", true);
                            $(".merchant-input input[class='car-wash']").attr("checked", false)
                        }else if(data.body.status==5){
                            $(".merchant-input input[class='repair']").attr("checked", true);
                            $(".merchant-input input[class='maintain']").attr("checked", false);
                            $(".merchant-input input[class='car-wash']").attr("checked", true)
                        }else if(data.body.status==6){
                            $(".merchant-input input[class='repair']").attr("checked", false);
                            $(".merchant-input input[class='maintain']").attr("checked", true);
                            $(".merchant-input input[class='car-wash']").attr("checked", true)
                        }else if(data.body.status==7){
                            $(".merchant-input input[name='typeStatus']").attr("checked", true);
                        }
                    }else {
                    $(".error").text(data.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(3000)
                    })
                }
            },//success
        });
    }
    // 列表查询
    function repairExamine(pages){
        $.ajax({
            type: "get",//读取方式
            url: "../api/repair/queryShop?currentPage="+pages,//通过url去读取json/数据
            contentType: "application/json",
            // data: JSON.stringify(searchDate),//是否向后台传参数
            success: function (data) {//result变量去接收json数据
                $("tbody").html("");
                if (data.status == false) {
                    var str='';
                    for(var i=0;i<data.body.length;i++){
                        str+='<tr><td><input type="checkbox" class="check " name="checkbox" value="'+data.body[i].id+'"></td>'
                        str+= '<td>'+data.body[i].repairName+'</td>'
                        if(data.body[i].status==1){
                            str+= '<td>修车</td>'
                        }else if(data.body[i].status==2){
                            str+= '<td>保养</td>'
                        }else if(data.body[i].status==3){
                            str+= '<td>洗车</td>'
                        }else if(data.body[i].status==4){
                            str+= '<td>修车,保养</td>'
                        }else if(data.body[i].status==5){
                            str+= '<td>修车,洗车</td>'
                        }else if(data.body[i].status==6){
                            str+= '<td>保养,洗车</td>'
                        }else if(data.body[i].status==7){
                            str+= '<td>修车,保养,洗车</td>'
                        }
                        str+= '<td title="'+data.body[i].repairLongitude+','+data.body[i].repairLatitude+'">'+data.body[i].repairAddress+'</td>'
                        str+=  '<td>'+data.body[i].repairPhone+'</td><td><button class="pass" value="'+data.body[i].id+'" name="1">查看</button></td>'
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
    // 新增与修改
    function addMadify(searchDate,pash) {
        $.ajax({
            type: "post",//读取方式
            url: pash,//通过url去读取json/数据
            contentType: "application/json",
            data: JSON.stringify(searchDate),//是否向后台传参数
            success: function (date) {//result变量去接收json数据
                if (date.status == false) {
                    $(".error").text(date.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(300)
                    })
                    repairExamine(1);
                    $(".mer-error").text("&nbsp")
                    $(".merchant-input input[name='typeStatus']").attr("checked", true);
                } else {
                    $(".error").text(date.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(300)
                    })
                }
            },//success
        });
    }
})
