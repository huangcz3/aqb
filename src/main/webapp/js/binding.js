$(function () {
    $.ajax({
        url:"../api/advertisement/getCarsAdmin",
        type:"GET",
        success:function (data) {
            var items=data.body;
            var length=items.length;
            console.log(items)

            if(length>0){
                var j=0;
                for(var i=0;i<length;i++){
                    //var time = items[i].foundDate;
                    //function getDate(tm){
                    //    var tt=new Date(tm).toLocaleString();
                    //    return tt;
                    //}
                    //var timeout = getDate(time);//时间戳转换
                        j++;
                    $("tbody").append("<tr><td class=''>"+j+"</td><td class=''>"+items[i].devNum+"</td><td class=''>"+items[i].longitude+"</td><td class=''>"+items[i].latitude+"</td><td ><button class='seeDate'>查看</button></td></tr>");
                }
                $("#number span").text(length)
            }
        }
    })
});

$(document).on("click",".seeDate",function () {
    $('.mapBox').fadeIn(300);
    //$('.mapBox').css("display","block");
    var devNum,longitude,latitude;
    devNum=$(this).parent().prev().prev().prev().text();
    longitude=$(this).parent().prev().prev().text();
    latitude=$(this).parent().prev().text();
    // init()
    // function init() {
        map = new AMap.Map("mapContainer", {
            zoom: 14,
            center:[longitude,latitude]
        });
        marker = new AMap.Marker({
            map:map,
            position:[longitude,latitude]
        });
        marker.setLabel({
            offset: new AMap.Pixel(20, 20),//修改label相对于maker的位置
            content: devNum
        });
        map.addControl(new AMap.ToolBar());
        if(AMap.UA.mobile){
            document.getElementById('button_group').style.display='none';
        }
    // }

});
$(".mapBox-clone").on("click",function(){
    $('.mapBox').fadeOut(300);
    //$('.mapBox').css("display","none");
})
