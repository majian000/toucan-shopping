

function bindComponentInstanceClick(clickId){
    console.log(clickId);
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
    console.log(componentInstance);
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

        var componentAttributes = $(".component-propertys");
        for(var i=0;i<componentAttributes.length;i++) {
            if($(componentAttributes[i]).attr("component-ref")!=compoentType) {
                $(componentAttributes[i]).hide();
            }else{
                if(componentInstance!=null) {
                    var propertyForm = $(".component-propertys-form-"+compoentType);
                    if(propertyForm!=null) {
                        propertyForm[0].reset();
                    }
                    if(compoentType=="image")
                    {
                        loadImageProperty(componentInstance);
                    }
                    $(componentAttributes[i]).attr("component-instance-ref",componentInstanceId);
                    $(componentAttributes[i]).show();
                }
            }
        }
    }
}



/**
 * 绑定属性事件
 */
function bindPropertyBtnEvent(){
    bindImagePropertyBtnEvent();
}

