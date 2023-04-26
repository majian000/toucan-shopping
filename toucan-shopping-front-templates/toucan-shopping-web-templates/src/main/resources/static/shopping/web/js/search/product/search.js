

$(function(){

    $(".s_btn").bind("click", function () {
        var sq=$(".s_ipt").val();
        window.open(basePath+"/api/product/search?keyword="+sq);
        window.close();
    });

});
