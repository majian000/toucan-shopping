

/**
 * 加载图片组件属性窗口
 * @param componentInstance
 */
function loadImageProperty(componentInstance){
    $("#image_selectBackgroundImagePreview").attr("src","/static/images/designer/pc/imageBackgroundDefault.png");
    if(componentInstance.propertys!=null&&componentInstance.propertys.length>0) {
        componentInstance.propertys.forEach(function (element) {
            if(element.name=="clickPath")
            {
                $("#image_clickPath").val(element.value);
            }else if(element.name=="httpImgPath"){
                $("#image_selectBackgroundImagePreview").attr("src",element.value);
            }
        });
    }
}

function selectImageCallback(row){
    $("#image_selectBackgroundImagePreview").attr("src",row.httpImgPath);
    var componentInstance = getComponentInstanceByInstanceId(row.componentInstanceId);
    componentInstance.setComponentPorperty("imgPath",row.imgPath);
    componentInstance.setComponentPorperty("httpImgPath",row.httpImgPath);
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
        componentInstance.setComponentPorperty("clickPath",propertyValue);
    });
    $("#image_ResetProperty").unbind("click");
    $("#image_ResetProperty").bind("click", function() {
        $(this).parents(".component-propertys-form-image")[0].reset();
        $("#image_selectBackgroundImagePreview").attr("src","/static/images/designer/pc/imageBackgroundDefault.png");
        var componentInstanceId = $(this).parents(".component-propertys").attr("component-instance-ref");
        var componentInstance = getComponentInstanceByInstanceId(componentInstanceId);
        componentInstance.propertys.splice(0, componentInstance.propertys.length);
    });
    $("#image_selectBackgroundImageBtn").unbind("click");
    $("#image_selectBackgroundImageBtn").bind("click", function() {
        var componentInstanceId = $(this).parents(".component-propertys").attr("component-instance-ref");
        openSelectImageDialog(componentInstanceId,selectImageCallback);
        initImagePagination();
    });
}
