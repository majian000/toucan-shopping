$(function () {
    initDesignerProperty();

});



function initDesignerProperty()
{
    $(".mtc1").show();
    //监听属性选项卡
    $(".mt-tabpage-item").click(function(){
        var tabTitles = $(".mt-tabpage-item");
        for(var i=0;i<tabTitles.length;i++)
        {
            $(tabTitles[i]).removeClass("mt-tabpage-item-cur");
        }
        $(this).addClass("mt-tabpage-item-cur");
        $(".mt-tabpage-content").hide();
        $("."+$(this).attr("attr-page")).show();
    });

    bindComponentClick();

    bindPropertyEvent();

    //背景样式
    $("#bgType1").click(function(){
        $(".bgTypeColor").show();
        $(".bgTypeImg").hide();
    });
    $("#bgType2").click(function(){
        $(".bgTypeImg").show();
        $(".bgTypeColor").hide();
    });


}

