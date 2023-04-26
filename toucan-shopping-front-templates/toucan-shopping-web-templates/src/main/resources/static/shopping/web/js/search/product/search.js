

$(function(){

    $(".s_btn").bind("click", function () {
        $(".s_form").attr("action",basePath+"/api/product/search");
        $(".s_form").submit();
    });

});
