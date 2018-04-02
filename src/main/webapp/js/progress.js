var progress={
    maxValue:100,
    minValue:0,
    Initialize:function (value) {
        if( value > this.maxValue){
            $(".cct-progress-drag span").css("margin-left",(-10+5*100)+"px");
        }else if(value < this.minValue){
            $(".cct-progress-drag span").css("margin-left",(-10)+"px");
        }else{
            $(".car-user").css("width",(value*5)+"px");
            $(".company").css("width",((100-value)*5)+"px");
            $(".cct-progress-drag span").css("margin-left",(-10+5*value)+"px");
            $(".ratio").html(value+"%:"+(100-value)+"%");
        }
    }
};

var value=0;
$.ajax({
    url:"/aqb/api/divided/queryDivided",
    type:"get",
    datatype:"json",
    success:function(data){
        value=data.body.dividedBili*100;
        $(function () {
            progress.Initialize(value);
            $('.cct-progress-drag span').mousedown(function (e) {
                var x1 = Number(e.pageX);
                var Span=$(".cct-progress-drag span");
                var num=Number(Span.css("margin-left").substring(0,Span.css("margin-left").indexOf("p")));

                $(document).mousemove(function (e) {
                    var x2 = Number(e.pageX);
                    $(".cct-progress-drag span").css("margin-left",(num+x2-x1)+"px");
                    progress.Initialize(((Number(Span.css("margin-left").substring(0,Span.css("margin-left").indexOf("p"))))+10)/5);
                    $(document).mouseup(function () {
                        if(value!=((Number(Span.css("margin-left").substring(0,Span.css("margin-left").indexOf("p"))))+10)/5){
                            $(document).off('mousemove');
                            value=((Number(Span.css("margin-left").substring(0,Span.css("margin-left").indexOf("p"))))+10)/5;
                            var newDivide={
                                dividedBili:(value/100)
                            };
                            $.ajax(
                                {
                                    url:"/aqb/api/divided/updateDivided",
                                    type:"post",
                                    contentType:"applycation/json",
                                    dataType:"json",
                                    data:JSON.stringify(newDivide),
                                    success:function (data) {
                                        //0--成功, 10--验证码错误, 7 --客户不存在,9-- 密码不正确
                                        alert(data.message);
                                    }
                                }
                            )
                        }
                        $(document).off('mousemove');
                    });
                });
            });
        });
    }
})
