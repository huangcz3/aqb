/**
 * Created by Administrator on 2017/8/10.
 */
$(function () {
    // 默认的设置
    $.ajax({
        type: "get",//读取方式
        url: "../api/shareReward/queryShareReward",//通过url去读取json/数据
        contentType: "application/json",
        // data: JSON.stringify(searchDate),//是否向后台传参数
        success: function (date) {//result变量去接收json数据
            if (date.status == false) {
                $(".re-one").val((date.body[0].rewardPercent)*100);
                $(".re-two").val((date.body[0].rewardPercent2)*100);
                $("#referee-twoModify").val(date.body[0].id)
                console.log(date.body[0].rewardPercent2)
            } else {
                $(".error").text(date.message);
                $(".error").fadeToggle(300,function () {
                    $(".error").fadeToggle(3000)
                })
            }
        },//success
    });
    // 搜索框
    $(".search").on("click",function () {
        var id=$(".searchInput").val();
        $.ajax({
            type: "get",//读取方式
            url: "../api/shareReward/queryShareRewardUserName?userName="+id,//通过url去读取json/数据
            contentType: "application/json",
            success: function (date) {//result变量去接收json数据
                console.log(date);
                $("tbody").empty();
                var str='';
                if (date.status == false) {
                    str+='<tr><td>'+date.body.userName+'</td>' +
                        '<td><input type="text" value="'+(date.body.shareReward.rewardPercent)*100+'" class="refereeInput refereeInput-one" disabled>%</td>' +
                        '<td><input type="text" value="'+(date.body.shareReward.rewardPercent2)*100+'" class="refereeInput refereeInput-two" disabled>%</td>' +
                        '<td> <button class="modify referee-modify" value="'+date.body.shareReward.id+'">修改 </button></td></tr>'
                }else if(date.status == 2){
                    str+='<tr><td>'+date.body+'</td>' +
                        '<td><input type="text" value="该账号没有设置" class="refereeInput" style="width: 100px" disabled></td>' +
                        '<td><input type="text" value="该账号没有设置" class="refereeInput " style="width: 100px" disabled></td>' +
                        '<td> <button class="pass referee-pass" value="'+date.body+'"> 去设置</button></td></tr>'
                } else {
                    $(".error").text(date.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(1000)
                    })
                }
                $("tbody").append(str)

            },//success
        });
    })
    // 修改
    $(document).on("click",".referee-modify",function () {
        var id=$(this).val();
        $(".save").attr("name",id);
        $(".changeArea").fadeIn(300);
        $("#userName").text($(this).parent().prev().prev().prev().text())
        $(".refereeInput-ones").val($(this).parent().prev().prev().find("input").val())
        $(".refereeInput-twos").val($(this).parent().prev().find("input").val())
        $(".save").addClass("ref-modify").removeClass("ref-pass")
    })
    $(document).on("click",".ref-modify",function () {
        var id=$(this).attr("name")
        var ones=$(".refereeInput-ones").val();
        var twos=$(".refereeInput-twos").val();
        ones=parseFloat(ones)/100;
        twos=parseFloat(twos)/100;
        var searchDate={
            id:id,
            rewardPercent:ones,
            rewardPercent2:twos
        }
        console.log(JSON.stringify(searchDate));
        $.ajax({
            type: "post",//读取方式
            url: "../api/shareReward/updateShareReward",//通过url去读取json/数据
            contentType: "application/json",
            data: JSON.stringify(searchDate),//是否向后台传参数
            success: function (date) {//result变量去接收json数据
                if (date.status == false) {
                    $(".error").text(date.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(300)
                    })
                    window.location.reload()
                } else {
                    $(".error").text(date.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(300)
                    })
                }
            },//success
        });
    })
    // 新增
    $(document).on("click",".referee-pass",function () {
        var id=$(this).val()
        $(".save").attr("name",id);
        $(".changeArea").fadeIn(300);
        $("#userName").text($(this).parent().prev().prev().prev().text())
        $(".save").addClass("ref-pass").removeClass("ref-modify")
    })
    $(document).on("click",".ref-pass",function () {
        var id=$(this).attr("name")
        var ones=$(".refereeInput-ones").val();
        var twos=$(".refereeInput-twos").val();
        ones=parseFloat(ones)/100;
        twos=parseFloat(twos)/100;
        var searchDate={
            userId:id,
            rewardPercent:ones,
            rewardPercent2:twos
        }
        console.log(JSON.stringify(searchDate));
        $.ajax({
            type: "post",//读取方式
            url: "../api/shareReward/addShareReward",//通过url去读取json/数据
            contentType: "application/json",
            data: JSON.stringify(searchDate),//是否向后台传参数
            success: function (date) {//result变量去接收json数据
                if (date.status == false) {
                    $(".error").text(date.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(300)
                    })
                    window.location.reload()
                } else {
                    $(".error").text(date.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(300)
                    })
                }
            },//success
        });
    })
    $("#referee-twoModify").on("click",function () {
        var id=$(this).val()
        var ones=$(".re-one").val();
        var twos=$(".re-two").val();
        ones=parseFloat(ones)/100;
        twos=parseFloat(twos)/100;
        var searchDate={
            id:id,
            rewardPercent:ones,
            rewardPercent2:twos
        }
        console.log(JSON.stringify(searchDate));
        $.ajax({
            type: "post",//读取方式
            url: "../api/shareReward/updateShareReward",//通过url去读取json/数据
            contentType: "application/json",
            data: JSON.stringify(searchDate),//是否向后台传参数
            success: function (date) {//result变量去接收json数据
                if (date.status == false) {
                    $(".error").text(date.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(300)
                        window.location.reload()
                    })

                } else {
                    $(".error").text(date.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(300)
                    })
                }
            },//success
        });
    })
})