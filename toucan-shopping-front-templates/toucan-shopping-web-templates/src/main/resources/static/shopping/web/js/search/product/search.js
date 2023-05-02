

$(function(){

    $(".s_btn").bind("click", function () {
        $(".s_form").attr("action",basePath+"/api/product/p/search");
        $(".s_form").submit();
    });

    $(".q_labels").bind("click", function () {
        $(".s_form .s_ipt").val($(this).text());
        $(".s_form").attr("action",basePath+"/api/product/p/search");
        $(".s_form").submit();
    });

});
