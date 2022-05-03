
var g_goApproveSecond=3;
var delFilePos=new Array();


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

        delFilePos.push(attrIndex);
        var deletePosHidden = $("#previewPhotoDelPosArray");
        if(deletePosHidden!=null)
        {
            deletePosHidden.val(delFilePos.join(","));
        }

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


function showSetp2Page()
{
    $("#step1").hide();
    $("#step2").show();
}


function showSetp3Page()
{
    var result = checkInputFunctionByContainerId("step2",2);

    var skuTablePhotos = $(".skuTablePhotos");
    var skuTableImgPaths = $(".skuTableImgPaths");
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
            var skuTableImgPath =  $(skuTableImgPaths[i]).val();
            if((skuTablePhoto==null||skuTablePhoto=="")&&(skuTableImgPath==null||skuTableImgPath==""))
            {
                $.message({
                    message: "请上传销售规格中的商品图片",
                    type: 'error'
                });
                return ;
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
}

/**
 * 生成属性值HTML
 * @param attributeValue
 * @returns {string}
 */
function genAttributeValueHtml(attributeValue)
{
    return  "<li class=\"rpai_li\" att-value-name=\""+attributeValue+"\">\n" +
        "                    <label>\n" +
        "                        <input type=\"checkbox\" class=\"chcBox_Width\" value=\""+attributeValue+"\">\n" +
        "                        <div style=\"background-color:null;width:15px;height:15px;float:right\"></div>"+attributeValue+"</label>\n" +
        "                </li>\n";
}

/**
 * 生成属性名HTML
 * @param skuAttributeKey
 * @param skuAttributeValueHtml
 * @returns {string}
 */
function genAttributeKeyHtml(number,skuAttributeKey,skuAttributeValueHtml)
{
    var inputUuid = toucan_uuid();
    return "<div class=\"item\" style=\"clear:both;text-align: left;\">\n" +
        "    <div style=\"padding-left: 6%;\"> <a>"+skuAttributeKey+"</a>\n" +
        "        <input type=\"text\" id=\"attributeInput_"+inputUuid+"\" name=\"attributeValue\" class=\"releaseProductInputText\" style=\"width:200px;\" tabindex=\"5\" maxlength=\"25\" placeholder=\"请输入自定义值\">\n" +
        "        <input type=\"button\" class=\"releaseProductButton attributeTableAddBtn\" value=\"添加\"  attr-data=\""+inputUuid+"\" > </div>\n" +
        "    <div class=\"attributeList\">\n" +
        "        <ul class=\"rp_attr_kt\" style=\"display:none\">\n" +
        "            <li>"+skuAttributeKey+"</li>\n" +
        "        </ul>\n" +
        "        <div class=\"field\" style=\"width:100%\">\n" +
        "            <ul class=\"rpai"+number+" rpai_key_ul\" id=\"rpai"+inputUuid+"\"  style=\"float:left;margin-left: 20px;\" attr-group-name=\""+skuAttributeKey+"\">\n"
        +skuAttributeValueHtml+
        "            </ul>\n" +
        "            <div class=\"clearfloat\"></div>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "</div>";
}

function drawAttributeControl()
{
    productApprove = g_productApprove;
    if(productApprove.skuAttributes!=null&&productApprove.skuAttributes.length>0)
    {
        var attributeKeys = $(".rpai_key_ul");
        //已有属性名列表
        if(attributeKeys!=null&&attributeKeys.length>0)
        {
            var attributeNameIsFind=false;
            var attributeValueIsFind;false;
            var maxAttributeNameNumber = 0 ;
            var attributeKeyIsDel=true;
            for(var i=0;i<productApprove.skuAttributes.length;i++) {
                var skuAttribute = productApprove.skuAttributes[i];
                attributeNameIsFind = false;
                maxAttributeNameNumber = 0;
                attributeKeyIsDel = true;
                for (var j = 0; j < attributeKeys.length; j++) {
                    var attributeKey = $(attributeKeys[j]);
                    maxAttributeNameNumber++;
                    //该属性名未被删除
                    if(skuAttribute.key==attributeKey.attr("attr-group-name"))
                    {
                        attributeKeyIsDel=false;
                        attributeNameIsFind = true;
                        var attributeValueLis = attributeKey.find("li");
                        //如果当前属性值列表为空
                        if(attributeValueLis==null||attributeValueLis.length<=0)
                        {
                            var attributeValueHtml="";
                            for (var p = 0; p < skuAttribute.values.length; p++) {
                                attributeValueHtml+=genAttributeValueHtml(skuAttribute.values[p]);
                            }
                            attributeKey.append(attributeValueHtml);
                        }else {

                            for (var p = 0; p < skuAttribute.values.length; p++) {
                                var skuAttributeValue = skuAttribute.values[p];
                                attributeValueIsFind=false;
                                //判断属性值有没有删除
                                for (var k = 0; k < attributeValueLis.length; k++) {
                                    var attributeValueControl = attributeValueLis[k];
                                    if(skuAttributeValue==$(attributeValueControl).attr("att-value-name"))
                                    {
                                        attributeValueIsFind = true;
                                    }
                                }
                                //不在当前控件列表中,就添加这个控件
                                if(!attributeValueIsFind)
                                {
                                    attributeKey.append(genAttributeValueHtml(skuAttribute.values[p]));
                                }

                            }
                        }
                    }
                }

                if(attributeKeyIsDel)
                {
                    $.message({
                        message: "\""+skuAttribute.key+"\"属性已经被删除",
                        type: 'error'
                    });
                }

            }
        }
        bindAttLabelEvent();
        bindInputAddEvent();
    }
    setAttributeChecked();
    //设置sku的值
    setSkuTableValue();
}

/**
 * 设置属性选中状态
 */
function setAttributeChecked()
{
    if(productApprove.skuAttributes!=null&&productApprove.skuAttributes.length>0)
    {
        for(var i=0;i<productApprove.skuAttributes.length;i++) {
            var skuAttribute = productApprove.skuAttributes[i];
            if(skuAttribute.values!=null&&skuAttribute.values.length>0) {
                for(var j=0;j<skuAttribute.values.length;j++) {
                    $("input:checkbox[value='" + skuAttribute.values[j] + "']").click();
                }
            }
        }

    }
}

function setSkuTableValue()
{
    if(productApprove.skuAttributes!=null&&productApprove.skuAttributes.length>0)
    {
        var stockNum=0;
        var skuTableAttributeValueControls = $(".productSkuAttributeValueHidden");
        var findCount = 0;
        if(skuTableAttributeValueControls!=null&&skuTableAttributeValueControls.length>0)
        {
            //遍历表格中每一行
            for(var i=0;i<skuTableAttributeValueControls.length;i++)
            {
                var skuTableAttributeValueControl = $(skuTableAttributeValueControls[i]);
                //拿到隐藏域的属性值列表
                var attributeValues =  skuTableAttributeValueControl.val().split("_");
                //遍历接口返回的商品实体
                for(var p=0;p<productApprove.productSkuVOList.length;p++)
                {
                    findCount=0;
                    var skuVo = productApprove.productSkuVOList[p];
                    var attributeValueGroup = skuVo.attributeValueGroup.split("_");
                    var result = toucan_compareArray(attributeValues,attributeValueGroup);
                    if(result)
                    {
                        var skuRowIndex = skuTableAttributeValueControl.attr("attr-row-index");

                        $("#productSkuVOList_"+skuRowIndex+"_price").val(skuVo.price);
                        $("#productSkuVOList_"+skuRowIndex+"_stockNum").val(skuVo.stockNum);
                        $("#skuPreview"+skuRowIndex).attr("src",skuVo.httpProductPreviewPath);
                        $("#skuPreviewPath_"+skuRowIndex).val(skuVo.productPreviewPath);
                        $("#skuTableuploading-tip" + skuRowIndex).show();

                        $("#productSkuVOList_"+skuRowIndex+"_id").val(skuVo.id);
                        stockNum+=skuVo.stockNum;
                    }
                }
            }
            $("#num").val(stockNum);
        }
    }
}

function initProductPublishForm(productApprove)
{
    attributeControl.imgUploadTitle="图片重新上传";
    //设置选择分类默认值
    var selectCategoryArray = productApprove.categoryNamePath;
    var selectCatePath = "";
    for(var i=(selectCategoryArray.length-1);i>=0;i--)
    {
        selectCatePath +=selectCategoryArray[i];
        if(i>0)
        {
            selectCatePath+="》";
        }
    }
    $("#selectProductCategory").html(selectCatePath);
    g_selectCategoryId = productApprove.categoryId;

    //绘制属性控件
    g_productApprove = productApprove;

    gCategoryDrawAttributeControl=drawAttributeControl;


    //设置选择分类默认值
    var selectCategoryArray = productApprove.categoryIdPath;
    for(var p=(selectCategoryArray.length-1);p>=0;p--)
    {
        $("#category_"+selectCategoryArray[p]).click();
    }


    //设置店铺分类默认值
    var selectShopCategoryArray = productApprove.shopCategoryIdPath;
    for(var p=(selectShopCategoryArray.length-1);p>=0;p--)
    {
        $("#shop_category_"+selectShopCategoryArray[p]).click();
    }

    //设置选择品牌默认值
    gSelectBrandId = productApprove.brandId;
    $("#selectBrandDiv").empty();
    $("#selectBrandDiv").append("<input type=\"text\" name=\"brandId\" id=\"selectBrand\" data-init=\""+productApprove.brandId+"\" >");
    initBrandListControlDefalutValue("selectBrandDiv","brandId","selectBrand",productApprove.categoryId);

    $("#articleNumber").val(productApprove.articleNumber);

    $("#name").val(productApprove.name);
    $("#sellerNo").val(productApprove.sellerNo);

    //设置商品主图
    $("#mainPhotoImg").attr("src",productApprove.httpMainPhotoFilePath);

    //设置商品预览图
    if(productApprove.httpPreviewPhotoPaths!=null)
    {
        for(var i=0;i<productApprove.httpPreviewPhotoPaths.length;i++)
        {
            $("#img_src"+i).attr("src",productApprove.httpPreviewPhotoPaths[i]);
            $("#imgBg_div"+i).attr("data-src",productApprove.httpPreviewPhotoPaths[i]);
            $("#uploading-tip" + i).show();

            $("#img_src"+i).unbind("click");
        }
    }



    //付款方式
    $(":radio[name='payMethod'][value='" + productApprove.payMethod + "']").prop("checked", "checked");

    //库存计数
    $(":radio[name='buckleInventoryMethod'][value='" + productApprove.buckleInventoryMethod + "']").prop("checked", "checked");

    //上架时间
    $(":radio[name='shelvesStatus'][value='" + productApprove.shelvesStatus + "']").prop("checked", "checked");

    //售后服务
    //提供发票
    if(productApprove.giveInvoice!=null) {
        $(":checkbox[name='giveInvoice'][value='" + productApprove.giveInvoice + "']").prop("checked", "checked");
    }

    //退换货承诺
    if(productApprove.changeOrReturn!=null) {
        $(":checkbox[name='changeOrReturn'][value='" + productApprove.changeOrReturn + "']").prop("checked", "checked");
    }

    //提取方式
    if(productApprove.etractMethod!=null) {
        $(":checkbox[name='etractMethod'][value='" + productApprove.etractMethod + "']").prop("checked", "checked");
    }

    //设置选择店铺分类默认值
    if(productApprove.shopCategoryId!=null) {
        var shopSelectCategoryArray = productApprove.shopCategoryNamePath;
        if(shopSelectCategoryArray!=null) {
            var shopSelectCatePath = "";
            for (var i = (shopSelectCategoryArray.length - 1); i >= 0; i--) {
                shopSelectCatePath += shopSelectCategoryArray[i];
                if (i > 0) {
                    shopSelectCatePath += "》";
                }
            }
            $("#shopSelectProductCategory").html(shopSelectCatePath);
        }
        $("#shopCategoryId").val(productApprove.shopCategoryId);
    }

}



function initPage()
{

    intRootCategory();
    intRootShopCategory();

    initPreviewPhotoUpload();

    loading.showLoading({
        type:6,
        tip:"查询中..."
    });

    $.ajax({
        type: "POST",
        url: basePath+"/api/shop/product/approve/detail",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify({id:$("#approveId").val()}),
        dataType: "json",
        success: function (result) {
            if(result.code<=0)
            {
                $.message({
                    message: "查询失败,请稍后重试",
                    type: 'error'
                });
                $("#approveId").val("");
                return ;
            }
            if(result.data.categoryIdPath==null||result.data.categoryIdPath.length==0)
            {
                $.message({
                    message: "查询失败,请稍后重试",
                    type: 'error'
                });
                $("#approveId").val("");
                return ;
            }
            initProductPublishForm(result.data)
        },
        error: function (result) {
            $.message({
                message: "查询失败,请稍后重试",
                type: 'error'
            });
            $("#approveId").val("");
        },
        complete:function()
        {
            loading.hideLoading();
        }

    });
}

$(function () {


    initPage();

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
        showSetp3Page();
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
        $("#refreshCaptcha").attr("src",basePath+"/api/shop/product/approve/vcode?"+new Date().getTime());
        $("#step5").show();
        $("#step4").hide();
        $("#step3").hide();
        $("#step2").hide();
        $("#step1").hide();
    });


    $("#step5Back").bind( 'click' ,function(){
        $("#step5").hide();
        $("#step4").show();
        $("#step3").hide();
        $("#step2").hide();
        $("#step1").hide();
    });



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

    loading.showLoading({
        type:6,
        tip:"发布中..."
    });

    //删除SKU表格中没有文件上传的控件
    var skuTableUploadFiles = $(".skuTableUploadFile");
    if(skuTableUploadFiles.length>0)
    {
        for(var i=0;i<skuTableUploadFiles.length;i++)
        {
            var skuTableUploadFile=$(skuTableUploadFiles[i]);
            if(skuTableUploadFile.val()==null||skuTableUploadFile.val()=="")
            {
                skuTableUploadFile.remove();
            }
        }
    }

    //删除商品主图控件
    if($("#mainPhotoFile").val()==null||$("#mainPhotoFile").val()=="")
    {
        $("#mainPhotoFile").remove();
    }

    $('#productReleaseForm').ajaxSubmit({
        url: basePath+'/api/shop/product/approve/republish',
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


function uploadPreviewPhoto(attrIndex)
{
    $("#previewPhotoFiles_"+attrIndex).click();
}