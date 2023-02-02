
var g_goApproveSecond=3;
var g_descriptionTablePos=0;

function showSetp2Page()
{
    $("#step1").hide();
    $("#step2").show();
}


function initPreviewPhotoUploadDel()
{

    $(".uploading-img li").mouseenter(function () {
        $(this).find(".uploading-tip").stop().animate({ height: '25px' }, 200);
    });
    $(".uploading-img li").mouseleave(function () {
        $(this).find(".uploading-tip").stop().animate({ height: '0' }, 200);
    });

    $(".onDelPic").click(function(){
       var attrIndex = $(this).attr("data");
        $("#img_src"+attrIndex).attr("src","/static/lib/tupload/images/imgadd.png");
        $("#previewPhotoFiles_"+attrIndex).val(null);
        $("#uploading-tip" + attrIndex).hide();

        $("#img_src"+attrIndex).click(function() {
            uploadPreviewPhoto($(this).attr("attr-index"));
        });
    });

}
function initPreviewPhotoUpload()
{

    initPreviewPhotoUploadDel();
    for(var i=0;i<6;i++)
    {
        $("#img_src"+i).click(function() {
            uploadPreviewPhoto($(this).attr("attr-index"));
        });


        $("#previewPhotoFiles_"+i).on("change", function(){
            // Get a reference to the fileList
            var files = !!this.files ? this.files : [];
            var attrIndex=$(this).attr("attr-index");

            // If no files were selected, or no FileReader support, return
            if (!files.length || !window.FileReader) {
                $("#img_src"+attrIndex).attr("src","/static/lib/tupload/images/imgadd.png");
                return;
            }

            // Only proceed if the selected file is an image
            if (/^image/.test( files[0].type)){
                $("#img_src"+attrIndex).unbind("click");
                // Create a new instance of the FileReader
                var reader = new FileReader();

                // Read the local file as a DataURL
                reader.readAsDataURL(files[0]);

                // When loaded, set image data as background of div
                reader.onloadend = function(){
                    $("#img_src"+attrIndex).attr("src",this.result);
                    $("#uploading-tip" + attrIndex).show();
                }

            }

        });
    }
}

$(function () {

    intRootCategory();
    intRootShopCategory();

    $("#mainPhotoFile").on("change", function(){
        // Get a reference to the fileList
        var files = !!this.files ? this.files : [];

        // If no files were selected, or no FileReader support, return
        if (!files.length || !window.FileReader) {
            $("#mainPhotoImg").attr("src","/static/lib/tupload/images/imgadd.png");
            return;
        }

        // Only proceed if the selected file is an image
        if (/^image/.test( files[0].type)){
            // Create a new instance of the FileReader
            var reader = new FileReader();

            // Read the local file as a DataURL
            reader.readAsDataURL(files[0]);

            // When loaded, set image data as background of div
            reader.onloadend = function(){
                $("#mainPhotoImg").attr("src",this.result);
            }

        }

    });


    initPreviewPhotoUpload();


    $("#refreshCaptcha").bind( 'click' ,function(){
        $("#refreshCaptcha").attr("src",basePath+"/api/user/vcode?"+new Date().getTime());
    });


    $("#step2Back").bind( 'click' ,function(){

        $("#step5").hide();
        $("#step4").hide();
        $("#step3").hide();
        $("#step2").hide();
        $("#step1").show();
    });

    $("#step2Next").bind( 'click' ,function(){
        var mainPhotoFileObj = $("#mainPhotoFile").val();
        if(mainPhotoFileObj==null||mainPhotoFileObj=="")
        {
            $.message({
                message: "请上传商品主图",
                type: 'error'
            });
            return ;
        }
        var result = checkInputFunctionByContainerId("step2",2);

        var skuTablePhotos = $(".skuTablePhotos");
        if(skuTablePhotos==null||skuTablePhotos.length<=0)
        {
            $.message({
                message: "请选择商品属性",
                type: 'error'
            });
            return ;
        }
        if(skuTablePhotos.length>0)
        {
            for(var i=0;i<skuTablePhotos.length;i++)
            {
                var skuTablePhoto = $(skuTablePhotos[i]).val();
                if(skuTablePhoto==null||skuTablePhoto=="")
                {
                    $.message({
                        message: "请上传销售规格中的商品图片",
                        type: 'error'
                    });
                    return ;
                }
            }
        }

        var roughWeights = $(".roughWeights");
        if(roughWeights.length>0)
        {
            for(var i=0;i<roughWeights.length;i++)
            {
                var roughWeightVal = $(roughWeights[i]).val();
                if(roughWeightVal!=null&&roughWeightVal!="")
                {
                    if((!checkInput.decimal3w[0].test(roughWeightVal)))
                    {
                        $.message({
                            message: "销售规格中的毛重"+checkInput.decimal3w[1],
                            type: 'error'
                        });
                        $(roughWeights[i]).focus();
                        return ;
                    }
                }
            }
        }

        if(result) {
            $("#step5").hide();
            $("#step4").hide();
            $("#step3").show();
            $("#step2").hide();
            $("#step1").hide();
        }
    });


    $("#step3Back").bind( 'click' ,function(){
        $("#step5").hide();
        $("#step4").hide();
        $("#step3").hide();
        $("#step2").show();
        $("#step1").hide();
    });

    $("#step3Next").bind( 'click' ,function(){
        $("#step5").hide();
        $("#step4").show();
        $("#step3").hide();
        $("#step2").hide();
        $("#step1").hide();
    });

    $("#step4Back").bind( 'click' ,function(){
        $("#step5").hide();
        $("#step4").hide();
        $("#step3").show();
        $("#step2").hide();
        $("#step1").hide();
    });

    $("#step4Next").bind( 'click' ,function(){
        var result = checkInputFunctionByContainerId("step4",2);
        if(result) {
            $("#refreshCaptcha").attr("src", basePath + "/api/shop/product/approve/vcode?" + new Date().getTime());
            $("#step5").show();
            $("#step4").hide();
            $("#step3").hide();
            $("#step2").hide();
            $("#step1").hide();
        }
    });


    $("#step5Back").bind( 'click' ,function(){
        $("#step5").hide();
        $("#step4").show();
        $("#step3").hide();
        $("#step2").hide();
        $("#step1").hide();
    });

    $("#selectFreightTemplate").bind( 'click' ,function(){

        openSelectFreightTemplateDialog();
        initFreightTemplatePagination();
    });

    $(".publishProductButton").bind( 'click' ,function(){
        window.open(basePath+"/page/freightTemplate/add");
    });

    appendDescriptionTableRow();
    bindDescriptionTableDeleteRowEvent();
});


/**
 * 输入商品库存数量
 */
function inputStock(o)
{
    var num=0;
    $(".skuStockInput").each(function (i, n) {
        var stockVal = $(this).val();
        if(stockVal!=null&&stockVal!="") {
            num += parseInt($(this).val());
        }
    });
    $("#num").val(num);
}


function goApprovePage(msg)
{
    if(g_goApproveSecond<=0)
    {
        window.location.href=basePath+"/index";
    }else
    {
        $("#publish_msg_c").show();
        $("#publish_msg").text(msg+","+g_goApproveSecond+"秒后跳转卖家首页");
        $("#publish_msg").css("color","green");

    }
    g_goApproveSecond--;
    setTimeout(function () {
        goApprovePage(msg);
    }, 1000);

}

$("#ppfbtn").click(function() {


    //删除商品介绍表格中没有文件上传的控件
    var descriptionTableUploadFiles = $(".descriptionTableUploadFile");
    if(descriptionTableUploadFiles.length>0)
    {
        for(var i=0;i<descriptionTableUploadFiles.length;i++)
        {
            var descriptionTableUploadFile=$(descriptionTableUploadFiles[i]);
            if(descriptionTableUploadFile.val()==null||descriptionTableUploadFile.val()=="")
            {
                descriptionTableUploadFile.remove();
            }
        }
    }

    //删除SKU表格中没有介绍图片的控件
    var skuTableDescriptionUploadFiles = $(".skuTableDescriptionUploadFiles");
    if(skuTableDescriptionUploadFiles.length>0)
    {
        for(var i=0;i<skuTableDescriptionUploadFiles.length;i++)
        {
            var skuTableDescriptionTableUploadFile=$(skuTableDescriptionUploadFiles[i]);
            if(skuTableDescriptionTableUploadFile.val()==null||skuTableDescriptionTableUploadFile.val()=="")
            {
                skuTableDescriptionTableUploadFile.remove();
            }
        }
    }

    loading.showLoading({
        type:6,
        tip:"发布中..."
    });

    $('#productReleaseForm').ajaxSubmit({
        url: basePath+'/api/shop/product/approve/publish',
        dataType:"json",
        contentType:"application/json;charset=utf-8",
        success: function (result) {
            loading.hideLoading();
            if(result.code<=0)
            {
                $("#vcode").val("");
                $("#refreshCaptcha").attr("src",basePath+"/api/shop/product/approve/vcode?"+new Date().getTime());
                $.message({
                    message: result.msg,
                    type: 'error'
                });
            }else{
                // $.message({
                //     message: "发布成功",
                //     type: 'success',
                //     showClose:true,
                //     autoClose:false,
                //     onClose:function(){
                //         window.location.href=basePath+"/index";
                //     }
                // });

                $("#ppfbtn").unbind();
                goApprovePage(result.msg);

            }
        }
    });

});


function appendDescriptionTableRow()
{
    var rowHtml="<tr id=\"descriptionTableTr"+g_descriptionTablePos+"\">\n" +
        "                                                            <td>\n" +
        "\n" +
        "                                                                <div class=\"description-table-uploading-img\">\n" +
        "                                                                    <ul class=\"picView-magnify-list\">\n" +
        "                                                                        <li data-toggle=\"tooltip\" data-placement=\"top\" title=\"点击图片预览\">\n" +
        "                                                                            <div id=\"descriptionTableimgBg_div"+g_descriptionTablePos+"\" class=\"uploading-imgBg\" data-magnify=\"gallery\" data-src=\"/static/lib/tupload/images/imgadd.png\" data-caption=\"图片预览\">\n" +
        "                                                                                <img id=\"descriptionTablePreview"+g_descriptionTablePos+"\" attr-index=\""+g_descriptionTablePos+"\" src=\"/static/lib/tupload/images/imgadd.png\" style=\"width:100%;height:100%\">            </div>\n" +
        "                                                                            <div id=\"descriptionTableuploading-tip"+g_descriptionTablePos+"\" class=\"descriptionTableuploading-tip\" style=\"display: none; height: 0px;\">\n" +
        "                                                                                <i class=\"onDescriptionTableDelPic\" data=\""+g_descriptionTablePos+"\">删除</i>\n" +
        "                                                                            </div>\n" +
        "                                                                        </li>\n" +
        "                                                                        <input type=\"file\" class=\"descriptionTablePhotos descriptionTableUploadFile\" attr-index=\""+g_descriptionTablePos+"\" style=\"display: none\" name=\"productDescription.productDescriptionImgs["+g_descriptionTablePos+"].imgFile\" id=\"descriptionTableProviewFile"+g_descriptionTablePos+"\" />\n" +
        "<input type=\"hidden\" class=\"descriptionTableImgPaths\" id=\"descriptionTablePreviewPath_"+g_descriptionTablePos+"\""+
        "                                                                    </ul>\n" +
        "                                                                </div>\n" +
        "                                                            </td>\n" +
        "                                                            <td>\n" +
        "                                                                <input type=\"text\" id=\"descriptionTableTitle"+g_descriptionTablePos+"\"  class=\"releaseProductInputText\" name=\"productDescription.productDescriptionImgs["+g_descriptionTablePos+"].title\" placeholder=\"请输入标题(非必填)\" maxlength=\"100\" />\n" +
        "                                                            </td>\n" +
        "                                                            <td>\n" +
        "                                                                <input type=\"text\" id=\"descriptionTableLink"+g_descriptionTablePos+"\"  class=\"releaseProductInputText\" name=\"productDescription.productDescriptionImgs["+g_descriptionTablePos+"].link\" placeholder=\"请输入跳转链接(非必填)\" maxlength=\"1500\" />\n" +
        "                                                            </td>\n" +
        "                                                            <td>\n" +
        "                                                                <span class=\"comment\">\n" +
        "                                                                    <a  class=\"descriptionTableDelRow\" style=\"color:red;cursor: pointer;\" attr-index=\""+g_descriptionTablePos+"\"  >删除</a>\n" +
        "                                                                </span>\n" +
        "                                                            </td>\n" +
        "                                                        </tr>";
    $("#descriptionTableBody").append(rowHtml);
    descriptionTableUploadPreview(g_descriptionTablePos);
    initDescriptionTablePreviewPhotoUploadDel();
    g_descriptionTablePos++;
    bindDescriptionTableDeleteRowEvent();
}

$("#descriptionTableAddRowBtn").click(function() {
    appendDescriptionTableRow();
});


function uploadDescriptionTablePreviewPhoto(attrIndex)
{
    $("#descriptionTableProviewFile"+attrIndex).click();
}


/**
 * 初始化商品介绍列表中的删除按钮
 */
function initDescriptionTablePreviewPhotoUploadDel()
{

    $(".description-table-uploading-img li").mouseenter(function () {
        $(this).find(".descriptionTableuploading-tip").stop().animate({ height: '25px' }, 200);
    });
    $(".description-table-uploading-img li").mouseleave(function () {
        $(this).find(".descriptionTableuploading-tip").stop().animate({ height: '0' }, 200);
    });

    $(".onDescriptionTableDelPic").unbind("click");
    $(".onDescriptionTableDelPic").click(function(){
        var attrIndex = $(this).attr("data");
        $("#descriptionTablePreview"+attrIndex).unbind("click");

        $("#descriptionTablePreview"+attrIndex).attr("src","/static/lib/tupload/images/imgadd.png");
        $("#descriptionTableProviewFile"+attrIndex).val(null);
        $("#descriptionTableuploading-tip" + attrIndex).hide();

        $("#descriptionTablePreview"+attrIndex).click(function() {
            uploadDescriptionTablePreviewPhoto($(this).attr("attr-index"));
        });

        var descriptionPreviewPathObj = $("#descriptionTablePreviewPath_"+attrIndex);
        if(descriptionPreviewPathObj!=null)
        {
            descriptionPreviewPathObj.val("");
        }
    });

}

/**
 * 初始化商品介绍列表中点击上传图片
 * @param pos
 */
function descriptionTableUploadPreview(pos)
{

    $("#descriptionTablePreview"+pos).click(function() {
        uploadDescriptionTablePreviewPhoto($(this).attr("attr-index"));
    });
    if($("#descriptionTableProviewFile"+pos)!=null) {
        $("#descriptionTableProviewFile" + pos).on("change", function () {
            // Get a reference to the fileList
            var files = !!this.files ? this.files : [];

            // If no files were selected, or no FileReader support, return
            if (!files.length || !window.FileReader) {
                $("#descriptionTablePreview" + pos).attr("src", "/static/lib/tupload/images/imgadd.png");
                return;
            }

            // Only proceed if the selected file is an image
            if (/^image/.test(files[0].type)) {
                $("#descriptionTablePreview"+pos).unbind("click");
                // Create a new instance of the FileReader
                var reader = new FileReader();

                // Read the local file as a DataURL
                reader.readAsDataURL(files[0]);

                // When loaded, set image data as background of div
                reader.onloadend = function () {
                    $("#descriptionTablePreview" + pos).attr("src", this.result);
                    $("#descriptionTableuploading-tip" + pos).show();
                }

            }

        });
    }

}

function bindDescriptionTableDeleteRowEvent()
{
    $(".descriptionTableDelRow").unbind("click");
    //SKU信息
    $(".descriptionTableDelRow").bind("click", function () {
        var attrIndex = $(this).attr("attr-index");
        layer.confirm('确定删除?', {
            btn: ['确定','关闭'], //按钮
            title:'提示信息'
        }, function(index) {
            $("#descriptionTableTr" +attrIndex ).remove();
            layer.close(index);
        });
    });
}

function uploadPreviewPhoto(attrIndex)
{
    $("#previewPhotoFiles_"+attrIndex).click();
}