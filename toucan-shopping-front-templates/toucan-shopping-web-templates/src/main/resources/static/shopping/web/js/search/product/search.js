

$(function(){

    $(".s_btn").bind("click", function () {
        // $(".s_form").attr("action","/api/product/g/search");
        // $(".s_form").submit();
        var cid=$("#cid").val();
        window.location.href=searchGetPath+"?keyword="+$(".s_ipt").val()+"&cid="+cid;
    });

    $(".q_labels").bind("click", function () {
        // $(".s_form .s_ipt").val($(this).text());
        // $(".s_form").attr("action","/api/product/g/search");
        // $(".s_form").submit();
        window.location.href=searchGetPath+"?keyword="+$(this).text();
    });

});
