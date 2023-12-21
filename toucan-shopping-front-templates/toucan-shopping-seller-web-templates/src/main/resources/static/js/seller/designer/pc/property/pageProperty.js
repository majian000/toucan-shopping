/**
 * 绑定页面设置属性事件
 */
function bindPagePropertyBtnEvent(){

    //选择背景色/背景图片
    $("#bgType1").click(function(){
        $(".bgTypeColor").show();
        $(".bgTypeImg").hide();
        //重置图片选择
        $("#page_imgPath").val("");
        $("#page_httpImgPath").val("");
        $("#page_imgRefId").val("");
        $("#page_selectBackgroundImagePreview").attr("src","/static/images/designer/pc/imageBackgroundDefault.png");

    });
    $("#bgType2").click(function(){
        $(".bgTypeImg").show();
        $(".bgTypeColor").hide();
        $(".selectColorControlVal").val("");
        $(".flexgrid-container").css("background-color","");
        $(".sp-preview-inner").css("background-color","");
    });

    $("#page_selectBackgroundImageBtn").unbind("click");
    $("#page_selectBackgroundImageBtn").bind("click", function() {
        var componentInstanceId = $(this).parents(".component-propertys").attr("component-instance-ref");
        openSelectImageDialog(componentInstanceId,pageSelectImageCallback);
        initImagePagination();
    });
}



function pageSelectImageCallback(row){
    $("#page_selectBackgroundImagePreview").attr("src",row.httpImgPath);
    $("#page_imgPath").val(row.imgPath);
    $("#page_httpImgPath").val(row.httpImgPath);
    $("#page_imgRefId").val(row.id);

}
/**
 * 加载页面设置
 * @param pageModel
 */
function loadPageProperty(pageModel){
    $("#pageTitle").val(pageModel.title);
    if(pageModel.backgroundColor!=null&&pageModel.backgroundColor!="")
    {
        $("#bgType1").click();
        $(".selectColorControlVal").val(pageModel.backgroundColor);
        $(".flexgrid-container").css("background-color",pageModel.backgroundColor);
        $(".sp-preview-inner").css("background-color",pageModel.backgroundColor);
    }
}