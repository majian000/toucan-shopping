
function bindComponentClick(){
    $(".components").unbind("click");
    $(".components").click(function(){
        componentClickCallback(this);
    });
}

function bindComponentInstanceClick(obj){
    $(obj).unbind("click");
    $(obj).click(function(){
        componentClickCallback(this);
    });
}

function componentClickCallback(obj){
    var attrCompoentType= $(obj).attr("attr-compoent-type");
    if(attrCompoentType!=null)
    {
        var componentConfig = g_componentConfig.get(attrCompoentType);
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
            if($(componentAttributes[i]).attr("component-ref")!=attrCompoentType) {
                $(componentAttributes[i]).hide();
            }else{
                console.log(g_current_component);
                if(g_current_component!=null) {
                    g_current_component.propertys.forEach(function(element) {
                        $("#" + element.name).val(element.value);
                    });
                }
                $(componentAttributes[i]).show();
            }
        }
    }
}

/**
 * 绑定组件属性事件
 */
function bindPropertyEvent(){
    bindInputPropertyEvent();
}

/**
 * 绑定输入类型属性事件
 */
function bindInputPropertyEvent(){
    $(".component-propertys-input").blur(function(){
        var propertyName = $(this).attr("property-name");
        var propertyValue = $(this).val();
        var existsProperty = false;
        if(g_current_component.propertys!=null&&g_current_component.propertys.length>0)
        {
            for(var i=0;i<g_current_component.propertys.length;i++)
            {
                if(g_current_component.propertys[i].name=propertyName){
                    g_current_component.propertys[i].value=propertyValue;
                    existsProperty=true;
                }
            }
        }
        if(!existsProperty) {
            g_current_component.propertys.push({"name": propertyName, "value":propertyValue});
        }
    });
}