/**
 * Created by Administrator on 2017/8/7.
 */
$(function () {
    // 选择搜索条件
    $(".searchSelect").on("change",function () {
       var slectText= $(".searchSelect option:selected").text();
       var slectVal= $(".searchSelect option:selected").val();
        console.log(slectVal)
        if(slectVal==0){
            $(".searchInput").attr("disabled","disabled").val(slectText);
        }else {
            $(".searchInput").attr("disabled",false).val(slectText);
        }
    })
    // 大图预览图片
    $(document).on("click",".binding-img",function () {
        var srcLarge=$(this).attr("src");
        getImageWidth(srcLarge,function(w,h){
            console.log({width:w,height:h});
            if(w>1200||h>600){
                w=1000;
                h=500;
            }
            $(".preview").css({"width":w+"px","height":h+"px","margin-top":"-"+h/2+"px","margin-left":"-"+w/2+"px","margin-bottom":"-"+h/2+"px","margin-right":"-"+w/2+"px"})
        });
        $(".preview-pictures").fadeIn(300);
        $(".preview-img").attr("src",srcLarge)
    });
    // 关闭弹窗
    $(".preview-picturesClone").on("click",function () {
        $(this).parent().parent().fadeOut(300);
        $(".reject-text").val("");
    });
})
// 计算图片宽高
function getImageWidth(url,callback){
    var img = new Image();
    img.src = url;
    // 如果图片被缓存，则直接返回缓存数据
    if(img.complete){
        callback(img.width, img.height);
    }else{
        // 完全加载完毕的事件
        img.onload = function(){
            callback(img.width, img.height);
        }
    }
}