/**
 * Created by Administrator on 2017/8/5.
 */
$(function () {
    var auth=["send","top","bottom","blue","red","yellow","green"];
    getList();
    function getList() {
        $.ajax({
            url:"../api/vip/queryVip",
            type:"get",
            datatype:"json",
            success:function(data){
                list=data.body;
                var htmlStr='';
                $("#listTitle").empty()
                for(var i=0;i<list.length;i++){
                    htmlStr+='<tr><td>'+list[i].vipName+'</td><td>' +
                        '<label><input type="text" value="'+list[i].vipMoney+'" placeholder="请设置所需金额">元 </label>' +
                        '</td><td><label><input type="text" value="'+list[i].vipJifen+'" placeholder="请设置所需积分">个</label>' +
                        '</td><td><button class="modify" value="'+list[i].id+'">确认</button></td></tr>'
                }
                $("#listTitle").append(htmlStr);
                var str="";
                var itemList=[];
                for(var no in list){
                    var authItem=list[no];
                    str=str+"<tr><td>"+authItem.vipName+"</td>";
                    for(var authNo in auth){
                        var eId=""+authItem.id+"_"+auth[authNo]+"_"+authItem[auth[authNo]];
                        itemList.push("#"+eId);
                        if(authItem[auth[authNo]]){
                            str=str+"<td><input type='checkbox' class='checkbox' name='my-checkbox' id='"+eId+"' checked></td>";
                        }else{
                            str=str+"<td><input type='checkbox' class='checkbox' name='my-checkbox' id='"+eId+"' ></td>";
                        }
                    }
                    str=str+"</tr>";
                }
                $("#list").empty();
                $("#list").append(str);
                $("[name='my-checkbox']").bootstrapSwitch();
                $.fn.bootstrapSwitch.defaults.onColor = 'success';
                $.fn.bootstrapSwitch.defaults.offColor = 'danger';
                for(var itemNo in itemList){
                    $(itemList[itemNo]).on({
                        'init.bootstrapSwitch': function() {
                            $(itemList[itemNo]).bootstrapSwitch("state", state);// 初始化状态
                        },
                        'switchChange.bootstrapSwitch': function(event, state) {
                            // 处理
                            var group=$(this).attr("id").split("_");
                            var id=group[0].substring(0,group[0].length);
                            var type=group[1];
                            var val=group[2];
                            /*
                             错误的写法
                             var vip={
                             id:id,
                             type:!val,
                             }
                             */
                            var vip={};
                            vip["id"]=id;
                            if(val=="false"){
                                vip[type]=true;
                            }else{
                                vip[type]=false;
                            }
                            // console.log(id);
                            // console.log(type);
                            // console.log(val);
                            // console.log(vip);
                            $.ajax(
                                {
                                    url:"../api/vip/updateVip",
                                    type:"post",
                                    contentType:"applycation/json",
                                    dataType:"json",
                                    data:JSON.stringify(vip),
                                    success:function (data) {
                                        //0--成功, 10--验证码错误, 7 --客户不存在,9-- 密码不正确
                                        $(".error").text(data.message);
                                        $(".error").fadeToggle(300,function () {
                                            $(".error").fadeToggle(3000)
                                        })
                                        getList();
                                    }
                                }
                            )
                        }
                    });
                }

            }
        })
    }
    $(document).on("click",".modify",function () {
        var id=$(this).val();
        var jiFen=$(this).parent().prev().find("input").val();
        var money=$(this).parent().prev().prev().find("input").val();
        var idArr={
            id:id,
            vipMoney:money,
            vipJifen:jiFen
        }
        $.ajax({
                url:"../api/vip/updateVip",
                type:"post",
                contentType:"applycation/json",
                data:JSON.stringify(idArr),
                success:function (data) {
                    $(".error").text(data.message);
                    $(".error").fadeToggle(300,function () {
                        $(".error").fadeToggle(3000)
                    })
                    getList();
                }
            })
    })
    /*
     bootstrap switch组件
     */
    //
    // function addVIP() {
    //     var vip={
    //         vipName:$("#newVipName").val(),
    //         green:false,
    //         yellow:false,
    //         red:false,
    //         blue:false,
    //         bottom:false,
    //         top:false,
    //         send:false
    //     }
    //     $.ajax(
    //         {
    //             url:"../api/vip/addVip",
    //             type:"post",
    //             contentType:"applycation/json",
    //             dataType:"json",
    //             data:JSON.stringify(vip),
    //             success:function (data) {
    //                 //0--成功, 10--验证码错误, 7 --客户不存在,9-- 密码不正确
    //                 $(".error").text(data.message);
    //                 $(".error").fadeToggle(300,function () {
    //                     $(".error").fadeToggle(3000)
    //                 })
    //                 getList();
    //                 $('#editVipModal').modal('hide');
    //                 $("#newVipName").val("");
    //             }
    //         }
    //     )
    // }
})