

$(function(){

    $(".s_btn").bind("click", function () {
        var sq=$(".s_ipt").val();
        window.location.href=basePath+"/api/product/search?keyword="+sq;
    });

});
