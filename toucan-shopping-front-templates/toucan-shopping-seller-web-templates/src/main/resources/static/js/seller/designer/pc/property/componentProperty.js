

function bindComponentInstanceClick(clickId){
    $("#"+clickId).unbind("click");
    //点击组件实例时
    $("#"+clickId).click(function(){
        var components = $(".components");
        for(var i=0;i<components.length;i++) {
            $(components[i]).css("border", "");
        }
        $(this).css("border", "1px solid #7dabff");
        componentClickCallback($(this));
    });
}

function componentClickCallback(clickObj){
    var compoentType= $(clickObj).attr("compoent-type");
    var componentInstanceId= $(clickObj).attr("component-instance-id");
    var componentInstance = getComponentInstanceByInstanceId(componentInstanceId);
    if(compoentType!=null)
    {
        var componentConfig = g_componentConfig.get(compoentType);
        var attributePanel =componentConfig.propertyPanel;
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

        var propertyPanel = $(".component-propertys");
        for(var i=0;i<propertyPanel.length;i++) {
            if($(propertyPanel[i]).attr("component-ref")!=compoentType) {
                $(propertyPanel[i]).hide();
            }else{
                if(componentInstance!=null) {
                    if(compoentType=="image")
                    {
                        //清空属性面板
                        clearImageProperty(compoentType);
                        //加载属性面板
                        loadImageProperty(componentInstance);
                    }
                    $(propertyPanel[i]).attr("component-instance-ref",componentInstanceId);
                    $(propertyPanel[i]).show();
                }
            }
        }
    }
}

/**
 * 选中页面属性设置
 */
function selectPageProperty(){
    var tabPageItems = $(".mt-tabpage-item");
    for(var j=0;j<tabPageItems.length;j++)
    {
        var tabPanel = $(tabPageItems[j]);
        if(tabPanel.attr("attr-page")=="mtc1")
        {
            tabPanel.click();
            tabPanel.addClass("mt-tabpage-item-cur");
        }
    }
}


/**
 * 绑定属性事件
 */
function bindPropertyBtnEvent(){
    bindImagePropertyBtnEvent();
}

