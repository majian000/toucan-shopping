

$(function(){
    if(pageView!=null) {
        if (pageView.backgroundColor != null && pageView.backgroundColor != "") {
            $(".shop-index-main-page").css("background-color", pageView.backgroundColor);
        }
        if(pageView.title!=null&&pageView.title!="")
        {
            $("title").text(pageView.title);
        }
    }
});