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

    $(".components").click(function(){
        var attrCompoentType= $(this).attr("attr-compoent-type");
        if(attrCompoentType!=null)
        {
            var attributePanel =$(this).attr("attribute-panel");
            var tabPageItems = $(".mt-tabpage-item");
            for(var j=0;j<tabPageItems.length;j++)
            {
                var tabPanel = $(tabPageItems[j]);
                if(tabPanel.attr("attr-page")==attributePanel)
                {
                    tabPanel.click();
                    tabPanel.addClass("mt-tabpage-item-cur");
                }
            }

            var componentAttributes = $(".component-attributes");
            for(var i=0;i<componentAttributes.length;i++) {
                if(componentAttributes[i].attr("component-id")!=attrCompoentType) {
                    componentAttributes[i].hide();
                }else{
                    componentAttributes[i].show();
                }
            }
        }
    });

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