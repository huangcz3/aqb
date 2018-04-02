/**
 * Created by Administrator on 2017/8/9.
 */
$(function () {
    // 查询页面
    $.ajax({
        type: "get",//读取方式
        url: "../api/daxiaoquan/queryDaxiaoquan",//通过url去读取json/数据
        contentType: "application/json",
        success: function (date) {//result变量去接收json数据
            $("#cir-body").empty()
            if (date.status == false) {
                $("#small-money").val(date.body.one[0].daxiaoquanCanshu);
                $("#small-jifen").val(date.body.one[1].daxiaoquanCanshu);
                $("#money").val(date.body.one[0].id);
                $("#jifen").val(date.body.one[1].id);
                var itms=date.body.two;
                var str='';
                for(var i=0;i<itms.length;i++){
                    var time = itms[i].foundDate;
                    function getDate(tm){
                        var tt=new Date(tm).toLocaleString();
                        return tt;
                    }
                    var timeout = getDate(time);//时间戳转换
                    str+='<tr>'
                    if(itms[i].daxiaoquanCanshu==0){
                        str+= '<td>默认</td>'
                    }else {
                        str+= '<td> ><input type="text" value="'+itms[i].daxiaoquanCanshu+'" style="width: 60px;border:none" readonly name="1"></td>'
                    }
                    str+= '<td><p class="cir-color" style="background-color: '+itms[i].daxiaoquanYanse+'" name=" '+itms[i].daxiaoquanYanse+'"></p></td>'
                    if(itms[i].xulie==1){
                        str+= '<td>大圈</td>'
                    }else if(itms[i].xulie==2){
                        str+= '<td>小圈</td>'
                    }
                    str+= '<td>'+timeout+'</td>'
                    str+= '<td><button class="modify crilce-btn" value="'+itms[i].id+'"  name="2">修改</button></td></tr>'
                }
                $("#cir-body").append(str)
            } else {
                $(".error").text(date.message);
                $(".error").fadeToggle(300,function () {
                    $(".error").fadeToggle(300)
                })
            }
            console.log(date)
        }
    });
    // 新增大小圈
    $(".circle-add").on("click",function () {
        $(".cricle-modifyBox").fadeIn(300);
        $(".crilce-pass").addClass("criBtnAdd").removeClass("criBtnModify");
        $(".crilce-p").text("新增大小圈");
        $(".crilce input[type='text']").val("");
        $(".crilceSelect").show()
    })
    $(".crilce-btns").on("click",".criBtnAdd",function () {
        var money=$(".crilce-money").val();
        var criColor=$("#cp5text").val();
        var criSelect=$("#crilce-select option:selected").val();
        if(money&&criColor){
            var searchDate={
                daxiaoquanCanshu:money,
                daxiaoquanYanse:criColor,
                xulie:criSelect
            }
            console.log(JSON.stringify(searchDate))
            $.ajax({
                type: "post",//读取方式
                url:"../api/daxiaoquan/addDaxiaoquan",//通过url去读取json/数据
                contentType: "application/json",
                data: JSON.stringify(searchDate),//是否向后台传参数
                success: function (data) {//result变量去接收json数据
                    if (data.status == false) {
                        $(".error").text(data.message);
                        $(".error").fadeToggle(300,function () {
                            $(".error").fadeToggle(2000)
                            window.location.reload();
                        })

                    } else {
                        $(".error").text(data.message);
                        $(".error").fadeToggle(300,function () {
                            $(".error").fadeToggle(3000)
                        })
                    }
                },//success
            });
        }else if(money==false){
            $(".error").text("请输入价格").fadeToggle(300,function () {
                $(".error").fadeToggle(2000)
            });
        }else if(money==false){
            $(".error").text("请选择颜色").fadeToggle(300,function () {
                $(".error").fadeToggle(2000);
            });
        }

    })
    // 修改颜色价格
    $("#cir-body").on("click",".crilce-btn",function () {
        $(".cricle-modifyBox").fadeIn(300);
        $(".crilce-pass").addClass("criBtnModify").removeClass("criBtnAdd");
        $(".crilce-p").text("修改价格");
        $(".crilceSelect").hide()
        if($(this).parent().prev().prev().prev().prev().find("input").attr("name")==1){
            $(".crilce-moneyBox").show()
        }else {
            $(".crilce-moneyBox").hide()
        }
        $(".crilce-money").val($(this).parent().prev().prev().prev().prev().find("input").val());
        $("#cp5text").val($(this).parent().prev().prev().prev().find("p").attr("name"));
        $(".crilce-pass").val($(this).val())
    })
    $(".crilce-btns").on("click",".criBtnModify",function () {
        var money=$(".crilce-money").val();
        var criColor=$("#cp5text").val();
        var criSelect=$(this).val();
        if(money&&criColor){
            var searchDate={
                daxiaoquanCanshu:money,
                daxiaoquanYanse:criColor,
                id:criSelect
            }
            console.log(JSON.stringify(searchDate));
            $.ajax({
                type: "post",//读取方式
                url:"../api/daxiaoquan/updateDaxiaoquan",//通过url去读取json/数据
                contentType: "application/json",
                data: JSON.stringify(searchDate),//是否向后台传参数
                success: function (data) {//result变量去接收json数据
                    if (data.status == false) {
                        $(".error").text(data.message);
                        $(".error").fadeToggle(300,function () {
                            $(".error").fadeToggle(2000)
                            window.location.reload();
                        })

                    } else {
                        $(".error").text(data.message);
                        $(".error").fadeToggle(300,function () {
                            $(".error").fadeToggle(3000)
                        })
                    }
                },//success
            });
        }else if(money==false){
            $(".error").text("请输入价格").fadeToggle(300,function () {
                $(".error").fadeToggle(2000)
            });
        }else if(money==false){
            $(".error").text("请选择颜色").fadeToggle(300,function () {
                $(".error").fadeToggle(2000);
            });
        }

    });
    // 修改金额
    $("#money").on("click",function () {
        var moneys=$("#small-money").val()
        var id=$(this).val();
        modifyDaxiao(moneys,id)
    })
    // 修改积分
    $("#jifen").on("click",function () {
        var moneys=$("#small-jifen").val()
        var id=$(this).val();
        modifyDaxiao(moneys,id)
    })
    // 修改大小圈
    function modifyDaxiao(money,criSelect) {
        var searchDate={
            daxiaoquanCanshu:money,
            id:criSelect
        }
        console.log(JSON.stringify(searchDate));
        $.ajax({
            type: "post",//读取方式
            url:"../api/daxiaoquan/updateDaxiaoquan",//通过url去读取json/数据
            contentType: "application/json",
            data: JSON.stringify(searchDate),//是否向后台传参数
            success: function (data) {//result变量去接收json数据
                if (data.status == false) {
                    $(".error").text(data.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(2000)
                        window.location.reload();
                    })

                } else {
                    $(".error").text(data.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(3000)
                    })
                }
            },//success
        });
    }
    // 关闭弹窗
    $(".cancel").on("click",function () {
        $(".cricle-modifyBox").fadeOut(300);
        $(".crilce-pass").val("");
    })
})