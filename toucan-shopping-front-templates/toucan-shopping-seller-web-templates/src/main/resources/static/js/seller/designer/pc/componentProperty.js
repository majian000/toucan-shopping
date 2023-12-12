

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
 * 加载图片组件属性窗口
 * @param componentInstance
 */
function loadImageProperty(componentInstance){

    if(componentInstance.propertys!=null&&componentInstance.propertys.length>0) {
        componentInstance.propertys.forEach(function (element) {
            if(element.name=="clickPath")
            {
                $("#image_clickPath").val(element.value);
            }
        });
    }
}

/**
 * 绑定属性事件
 */
function bindPropertyBtnEvent(){
    bindImagePropertyBtnEvent();
}

/**
 * 设置组件属性
 * @param componentInstance
 * @param propertyName
 * @param propertyValue
 */
function setComponentPorperty(componentInstance,propertyName,propertyValue){
    var existsProperty = false;
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
}

/**
 * 绑定图片属性事件
 */
function bindImagePropertyBtnEvent(){
    $("#image_SaveProperty").unbind("click");
    $("#image_SaveProperty").bind("click", function() {
        var componentInstanceId = $(this).parents(".component-propertys").attr("component-instance-ref");
        var componentInstance = getComponentInstanceByInstanceId(componentInstanceId);
        var propertyValue = $("#image_clickPath").val();
        setComponentPorperty(componentInstance,"clickPath",propertyValue);
    });
    $("#image_ResetProperty").unbind("click");
    $("#image_ResetProperty").bind("click", function() {
        $(this).parents(".component-propertys-form-image")[0].reset();
        var componentInstanceId = $(this).parents(".component-propertys").attr("component-instance-ref");
        var componentInstance = getComponentInstanceByInstanceId(componentInstanceId);
        componentInstance.propertys.splice(0, componentInstance.propertys.length);
    });
    $("#image_selectBackgroundImageBtn").unbind("click");
    $("#image_selectBackgroundImageBtn").bind("click", function() {
        openSelectImageDialog();
        initImagePagination();
    });
}