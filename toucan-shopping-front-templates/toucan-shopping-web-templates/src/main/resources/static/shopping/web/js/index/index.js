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
});