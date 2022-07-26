
var g_productVo;

$(function(){

    tabsEvent();
    var id = getProductId();
    if(id!=null&&id!="") {
        var type= getPreviewType();
        var requestPath = basePath + "/api/product/approve/detail";
        if(type==1)
        {
            requestPath  = basePath + "/api/product/approve/detail/paid";
        }
        $.ajax({
            type: "POST",
            url: requestPath,
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify({"id": id}),
            dataType: "json",
            success: function (result) {
                if (result.code <= 0) {
                    $.message({
                        message: "查询失败,请稍后重试",
                        type: 'error'
                    });
                    return ;
                }
                drawProductPage(result.data);
            },
            error: function (result) {

            }
        });
    }
});

function drawAttributeList(productVO)
{
    if(productVO.productAttributes!=null) {
        var attributesObject = JSON.parse(productVO.productAttributes);
        var attributeHtml="";
        var skuAttributeValuePos = 0 ;
        var attributeValueGroupArray = null;
        var rowIndex=0;
        if(productVO.attributeValueGroup.indexOf("_")!=-1) {
            attributeValueGroupArray = productVO.attributeValueGroup.split("_");
        }else{
            attributeValueGroupArray = new Array();
            attributeValueGroupArray.push(productVO.attributeValueGroup);
        }
        for (var attributeKey in attributesObject) {
            attributeHtml+="<div class=\"des_choice\">";
            attributeHtml+=" <span class=\"fl\">"+attributeKey+"：</span>";
            attributeHtml+=" <ul>";
            var attributeValues= attributesObject[attributeKey];
            if(attributeValues!=null&&attributeValues.length>0)
            {
                for(var j=0;j<attributeValues.length;j++)
                {
                    if(attributeValueGroupArray[skuAttributeValuePos]==attributeValues[j])
                    {
                        attributeHtml+="<li class=\"checked att_chks att_check"+rowIndex+"\" attr-row=\""+rowIndex+"\" attr-value=\""+attributeValues[j]+"\">"+attributeValues[j]+"<div class=\"ch_img\"></div></li>";
                    }else{
                        attributeHtml+="<li class=\"att_chks att_check"+rowIndex+"\" attr-row=\""+rowIndex+"\" attr-value=\""+attributeValues[j]+"\">"+attributeValues[j]+"<div class=\"ch_img\"></div></li>";
                    }
                }
            }
            attributeHtml+=" </ul>";
            attributeHtml+="</div>";
            skuAttributeValuePos++;
            rowIndex++;
        }
        $(".attributeList").append(attributeHtml);
        bindAttributeCheckboxEvent();
    }
}

function bindAttributeCheckboxEvent()
{
    $(".att_chks").bind("click", function () {
        var attrRow = $(this).attr("attr-row");
        $(".att_check"+attrRow).removeClass("checked");
        $(this).addClass("checked");

        var attributeChks=$(".att_chks");
        var attributeValueGroup="";
        if(attributeChks!=null&&attributeChks.length>0)
        {
            for(var i=0;i<attributeChks.length;i++)
            {
                var attributeChkObj = attributeChks[i];
                if($(attributeChkObj).hasClass("checked"))
                {
                    attributeValueGroup+=$(attributeChkObj).attr("attr-value");
                    attributeValueGroup+="_";
                }
            }
        }
        if(g_productVo!=null)
        {
            attributeValueGroup = attributeValueGroup.substring(0,attributeValueGroup.lastIndexOf("_"));
            if(g_productVo.productSkuVOList!=null&&g_productVo.productSkuVOList.length>0)
            {
                for(var p=0;p<g_productVo.productSkuVOList.length;p++)
                {
                    var sku = g_productVo.productSkuVOList[p];
                    if(sku.attributeValueGroup==attributeValueGroup)
                    {
                        var currentLocation=getApprovePreviewHrefIngoreId();
                        window.location.href = currentLocation+"/"+sku.id;
                    }
                }
            }
        }
    });
}

function drawProductPage(productVO)
{
    if(productVO!=null)
    {
        g_productVo = productVO;
        var photoPos = 0;
        $(".productPhotoPreview").empty();
        $(".productPhotoPreview").append("<li onclick=\"showPic("+photoPos+")\" rel=\"MagicZoom\" class=\"tsSelectImg\">" +
            "<img src=\""+productVO.httpMainPhotoFilePath+"\" tsImgS=\""+productVO.httpMainPhotoFilePath+"\" width=\"79\" height=\"79\" />" +
            "</li>");

        $("#MagicZoom").attr("href",productVO.httpMainPhotoFilePath);
        $("#MagicZoomImg").attr("src",productVO.httpMainPhotoFilePath);

        photoPos++;
        $(".productPhotoPreview").append("<li onclick=\"showPic("+photoPos+")\" rel=\"MagicZoom\" >" +
            "<img src=\""+productVO.httpProductPreviewPath+"\" tsImgS=\""+productVO.httpProductPreviewPath+"\" width=\"79\" height=\"79\" />" +
            "</li>");
        photoPos++;

        if(productVO.httpPreviewPhotoPaths!=null&&productVO.httpPreviewPhotoPaths.length>0)
        {
            for(var i=0;i<productVO.httpPreviewPhotoPaths.length;i++)
            {
                $(".productPhotoPreview").append("<li onclick=\"showPic("+photoPos+")\" rel=\"MagicZoom\">" +
                    "<img src=\""+productVO.httpPreviewPhotoPaths[i]+"\" tsImgS=\""+productVO.httpPreviewPhotoPaths[i]+"\" width=\"79\" height=\"79\" />" +
                    "</li>");
                photoPos++;
            }
        }

        $(".des_name").html("<p>"+productVO.name+"</p>");
        document.title = productVO.name+"-"+webName;
        $("#productPrice").html("￥"+productVO.price);

        initMagicZomm();
        initProductPhotoPreview();


        drawAttributeList(productVO);

        if(productVO.shopProductApproveDescriptionVO!=null)
        {
            var productDescHtml="";
            var shopProductApproveDescriptionVO=productVO.shopProductApproveDescriptionVO;
            if(shopProductApproveDescriptionVO.productDescriptionImgs!=null
                &&shopProductApproveDescriptionVO.productDescriptionImgs.length>0)
            {
                for(var i=0;i<shopProductApproveDescriptionVO.productDescriptionImgs.length;i++)
                {
                    var img = shopProductApproveDescriptionVO.productDescriptionImgs[i];
                    productDescHtml+="<p><a href=\""+img.link+"\"><img title=\""+img.title+"\" src=\""+img.httpFilePath+"\" style=\"width:100%\"></a></p>"
                }
            }
            $(".des_con_p").html(productDescHtml);

            if(productVO.shopProductApproveDescriptionVO.brandNameAttribute!=null)
            {
                $(".description-attribute-brand").html("");
                $(".description-attribute-brand").append(" <li title=\""+productVO.shopProductApproveDescriptionVO.brandNameAttribute.key+"\">品牌：\n" +
                    "                                <a href=\"#\" class=\"brand-name\" target=\"_blank\">"+productVO.shopProductApproveDescriptionVO.brandNameAttribute.values[0]+"</a>\n" +
                    "                            </li>");
            }

            if(productVO.shopProductApproveDescriptionVO.attributes!=null)
            {
                $(".description-attribute-more").html("");
                for(var j=0;j<productVO.shopProductApproveDescriptionVO.attributes.length;j++)
                {
                    var descriptionAttribute = productVO.shopProductApproveDescriptionVO.attributes[j];
                    if(descriptionAttribute.values!=null&&descriptionAttribute.values.length>0) {
                        var descriptionAttributeValue = descriptionAttribute.values[0];
                        $(".description-attribute-more").append("<li title=\""+descriptionAttributeValue+"\">"+descriptionAttribute.key+"："+descriptionAttributeValue+"</li>");
                    }
                }

            }
        }

        drawSpuAttributes(productVO.productId);
    }
}

function drawSalesAttributeChildHtml(attributeChild)
{
    var salesAttributeChildHtml="<div class=\"Ptable-item-child\">";
    salesAttributeChildHtml+="<dl style=\"Ptable-item-dl\">";
    salesAttributeChildHtml+="<dl class=\"clearfix\" style=\"margin:0\">";
    salesAttributeChildHtml+="<dt>";
    salesAttributeChildHtml+=attributeChild.name;
    salesAttributeChildHtml+="</dt>";
    salesAttributeChildHtml+=" <dd>";
    for(var j=0;j<attributeChild.values.length;j++) {
        var attributeValue = attributeChild.values[j];
        salesAttributeChildHtml+=attributeValue+"&nbsp;";
    }
    salesAttributeChildHtml+="  </dd>";
    salesAttributeChildHtml+="</dl>";
    salesAttributeChildHtml+="<dl class=\"clearfix\" style=\"margin:0\">";
    if(attributeChild.children!=null) {
        for (var p = 0; p < attributeChild.children.length; p++) {
            salesAttributeChildHtml += drawSalesAttributeChildHtml(attributeChild.children[p]);
        }
    }
    salesAttributeChildHtml+="</dl>";
    salesAttributeChildHtml+="</dl>";
    salesAttributeChildHtml+="</div>";
    return salesAttributeChildHtml;
}

function drawSalesAttribute(spu)
{
    if(spu!=null&&spu.attributeTree.length>0)
    {
        var salesAttributeHtml="<div class=\"Ptable\">";
        for(var i=0;i<spu.attributeTree.length;i++)
        {
            var attributeTree = spu.attributeTree[i];
            salesAttributeHtml+="<div class=\"Ptable-item\">";
            salesAttributeHtml+="<h3>"+attributeTree.name+"</h3>";
            if(attributeTree.values!=null&&attributeTree.values.length>0) {
                salesAttributeHtml += "<dl style=\"Ptable-item-dl\">";
                salesAttributeHtml+="<dl class=\"clearfix\" style=\"margin:0\">";
                salesAttributeHtml+="<dd style=\"margin-left: 20px;\">";
                for(var j=0;j<attributeTree.values.length;j++) {
                    var attributeValue = attributeTree.values[j];
                    salesAttributeHtml+=attributeValue+"&nbsp;";
                }
                salesAttributeHtml+="</dd>";
                salesAttributeHtml+="</dl>";
                salesAttributeHtml+="</dl>";
            }
            salesAttributeHtml+="</dl>";
            if(attributeTree.children!=null&&attributeTree.children.length>0)
            {
                salesAttributeHtml+="<dl class=\"clearfix\" style=\"margin:0\">";
                if(attributeTree.children!=null) {
                    for (var p = 0; p < attributeTree.children.length; p++) {
                        salesAttributeHtml += drawSalesAttributeChildHtml(attributeTree.children[p]);
                    }
                }
                salesAttributeHtml+="</dl>";
            }
            salesAttributeHtml+="</div>";
        }
        salesAttributeHtml+="</div>";
        $(".spuAttributeList").html(salesAttributeHtml);
    }
}




function drawSpuAttributes(productId)
{
    $.ajax({
        type: "POST",
        url: basePath + "/api/product/approve/spu/info",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify({"productId": productId}),
        dataType: "json",
        success: function (result) {
            if (result.code <= 0) {
                return ;
            }
            drawSalesAttribute(result.data);
        },
        error: function (result) {

        }
    });
}

function tabsEvent()
{
    $(".tabls").bind("click", function () {
        var tabNumber = $(this).attr("attr-n");
        var tabMax = $(this).attr("attr-m");
        for(var i=1;i<=tabMax;i++)
        {
            $(".step"+i).removeClass("current");
            $(".step"+i+"Panel").hide();
        }
        $(".step"+tabNumber).addClass("current");
        $(".step"+tabNumber+"Panel").show();
    });

    $(".moreAtt").bind("click", function () {
        var tabNumber = $(this).attr("attr-n");
        var tabMax = $(this).attr("attr-m");
        for(var i=1;i<=tabMax;i++)
        {
            $(".step"+i).removeClass("current");
            $(".step"+i+"Panel").hide();
        }
        $(".step"+tabNumber).addClass("current");
        $(".step"+tabNumber+"Panel").show();
    });
}