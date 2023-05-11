$(function(){

    $(".cals").bind("click", function () {
        var href = $(this).attr("href");
       if(href==null||href==""||href=="#")
       {
           window.location.href=searchGetPath+"?cid="+$(this).attr("attr-id");
       }else{
           window.location.href=href;
       }
    });

    $(".category_a").bind("click", function () {
        var href = $(this).attr("href");
        if(href==null||href==""||href=="#"||href=="javascript:void(0)")
        {
            window.location.href=searchGetPath+"?cid="+$(this).attr("attr-id");
        }else{
            window.location.href=href;
        }
    });


});