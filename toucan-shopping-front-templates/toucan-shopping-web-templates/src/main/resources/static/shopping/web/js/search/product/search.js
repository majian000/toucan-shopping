

$(function(){

    $(".s_btn").bind("click", function () {
        var sq=$(".s_ipt").val();
        $(".s_link").attr("href",basePath+"/api/product/search?keyword="+sq);
        $(".s_link").click();
    });

});
