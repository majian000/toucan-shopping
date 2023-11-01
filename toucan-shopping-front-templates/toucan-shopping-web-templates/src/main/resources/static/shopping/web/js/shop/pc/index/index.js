

$(function(){
    var backgroundColor=$("#backgroundColor").val();
    if(backgroundColor!=null&&backgroundColor!="")
    {
        $(".shop-index-main-page").css("background-color",backgroundColor);
    }
});