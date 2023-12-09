

function bindComponentInstanceClick(obj){
    $(obj).unbind("click");
    //点击组件实例时
    $(obj).click(function(){
        var components = $(".components");
        for(var i=0;i<components.length;i++) {
            $(components[i]).css("border", "");
        }
        $(this).css("border", "1px solid #7dabff");
        componentClickCallback($(this));
    });
}

function componentClickCallback(obj){
    var compoentType= $(obj).attr("compoent-type");
    var componentInstanceId= $(obj).attr("component-instance-id");
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

        var componentInstance = getComponentInstanceByInstanceId(componentInstanceId);
        console.log(componentInstance);
        var componentAttributes = $(".component-propertys");
        for(var i=0;i<componentAttributes.length;i++) {
            if($(componentAttributes[i]).attr("component-ref")!=compoentType) {
                $(componentAttributes[i]).hide();
            }else{
                if(componentInstance!=null) {
                    $(".component-propertys-form")[0].reset();
                    componentInstance.propertys.forEach(function(element) {
                        $(componentAttributes[i]).find("#" + element.name).val(element.value);
                    });
                    $(componentAttributes[i]).attr("component-instance-ref",componentInstanceId);
                    $(componentAttributes[i]).show();
                }
            }
        }
    }
}

/**
 * 绑定输入类型属性事件
 */
function bindInputPropertyEvent(){
    $(".component-propertys-input").off("blur");
    $(".component-propertys-input").blur(function(){
        var propertyName = $(this).attr("property-name");
        var componentInstanceId = $(this).parents(".component-propertys").attr("component-instance-ref");
        var propertyValue = $(this).val();
        var existsProperty = false;
        var componentInstance = getComponentInstanceByInstanceId(componentInstanceId);
        console.log(g_components);
        if(componentInstance!=null) {
            if (componentInstance.propertys != null && componentInstance.propertys.length > 0) {
                for (var i = 0; i < componentInstance.propertys.length; i++) {
                    if (componentInstance.propertys[i].name = propertyName) {
                        componentInstance.propertys[i].value = propertyValue;
                        existsProperty = true;
                    }
                }
            }
            if (!existsProperty) {
                componentInstance.propertys.push({"name": propertyName, "value": propertyValue});
            }
        }
    });
}