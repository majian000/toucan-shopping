

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

function clearImageProperty(compoentType){
    var propertyForm = $(".component-propertys-form-"+compoentType);
    if(propertyForm!=null) {
        propertyForm[0].reset();
    }
}

function selectImageCallback(row){
    $("#image_selectBackgroundImagePreview").attr("src",row.httpImgPath);
    $("#image_imgPath").val(row.imgPath);
    $("#image_httpImgPath").val(row.httpImgPath);
    $("#image_imgRefId").val(row.id);

}

function saveImageProperty(){

    var componentInstanceId = $("#image_propertyPanel").attr("component-instance-ref");
    console.log(componentInstanceId);
    var componentInstance = getComponentInstanceByInstanceId(componentInstanceId);
    var clickPathVal = $("#image_clickPath").val();
    var imgPathVal = $("#image_imgPath").val();
    var httpImgPathVal = $("#image_httpImgPath").val();
    var imgRefIdVal = $("#image_imgRefId").val();
    componentInstance.setComponentPorperty("clickPath",clickPathVal);
    componentInstance.setComponentPorperty("imgPath",imgPathVal);
    componentInstance.setComponentPorperty("httpImgPath",httpImgPathVal);
    componentInstance.setComponentPorperty("imgRefId",imgRefIdVal);


    //替换组件的背景为所选图片
    if(httpImgPathVal!=null&&httpImgPathVal!="") {
        $("#" + componentInstance.instanceId).find(".designer-component-image-bg").removeClass("designer-component-image-hover");
        $("#" + componentInstance.instanceId).find(".designer-component-image-bg").find(".fg-widget-handle").html("");
        $("#" + componentInstance.instanceId).find(".designer-component-image-bg").removeClass("fg-widget-inner-bg-color");
        $("#" + componentInstance.instanceId).find(".designer-component-image-bg").addClass("designer-component-image-hover");
        $("#" + componentInstance.instanceId).find(".designer-component-image-bg").css("background-image", "url('" + httpImgPathVal + "')");
    }

}

function resetImageProperty(clickHandle){
    $(clickHandle).parents(".component-propertys-form-image")[0].reset();
    $("#image_selectBackgroundImagePreview").attr("src","/static/images/designer/pc/imageBackgroundDefault.png");
    var componentInstanceId = $(clickHandle).parents(".component-propertys").attr("component-instance-ref");
    var componentInstance = getComponentInstanceByInstanceId(componentInstanceId);
    componentInstance.propertys.splice(0, componentInstance.propertys.length);
    //清空选择的图片
    $("#"+componentInstance.instanceId).find(".designer-component-image-bg").removeClass("designer-component-image-hover");
    $("#"+componentInstance.instanceId).find(".designer-component-image-bg").addClass("fg-widget-inner-bg-color");
    $("#"+componentInstance.instanceId).find(".designer-component-image-bg").css("background-image", "");
}

/**
 * 绑定图片属性事件
 */
function bindImagePropertyBtnEvent(){
    $("#image_SaveProperty").unbind("click");
    $("#image_SaveProperty").bind("click", function() {
        saveImageProperty();
    });
    $("#image_ResetProperty").unbind("click");
    $("#image_ResetProperty").bind("click", function() {
        resetImageProperty(this);
    });
    $("#image_selectBackgroundImageBtn").unbind("click");
    $("#image_selectBackgroundImageBtn").bind("click", function() {
        var componentInstanceId = $(this).parents(".component-propertys").attr("component-instance-ref");
        openSelectImageDialog(componentInstanceId,selectImageCallback);
        initImagePagination();
    });
}
