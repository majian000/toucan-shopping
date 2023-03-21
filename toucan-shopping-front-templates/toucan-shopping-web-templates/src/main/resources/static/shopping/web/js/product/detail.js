
var g_productVo;

$(function(){
    tabsEvent();
    bindProductNumEvent();
    var id = getProductId();
    var ptype = $("#ptype").val();
    if(id!=null&&id!="") {
        var type= getShopProductPreviewType();
        var requestPath ="";
        if(ptype=="detail") {
            requestPath = basePath + "/api/product/detail";
            if (type == 1) {
                requestPath = basePath + "/api/product/detail/pid";
            }
        }else if(ptype=="preview"){
            requestPath = basePath + "/api/product/preview";
            if(type==1)
            {
                requestPath  = basePath + "/api/product/preview/pid";
            }
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
                if(result.data==null)
                {
                    $.message({
                        message: "该商品已下架",
                        type: 'error'
                    });
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
        disabledAttribute(productVO);
        bindAttributeCheckboxEvent();
    }
}

/**
 * 将库存为0的和下架的禁用
 * @param productVO
 */
function disabledAttribute(productVO)
{
    var attChks = $(".att_chks");
    if(attChks!=null&&attChks.length>0)
    {
    }
}

function bindAttributeCheckboxEvent()
{
    $(".att_chks").bind("click", function () {
        var attrRow = $(this).attr("attr-row");
        var preSelectAttrObj=null; //上一个选择的属性项
        var attCheckRowObjects = $(".att_check"+attrRow);
        for(var i=0;i<attCheckRowObjects.length;i++)
        {
            if($(attCheckRowObjects[i]).hasClass("checked"))
            {
                preSelectAttrObj = attCheckRowObjects[i];
                break;
            }
        }
        attCheckRowObjects.removeClass("checked");
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
                        var currentLocation=getShopProductPreviewHrefIngoreId();
                        window.location.href = currentLocation+"/"+sku.id;
                        return;
                    }
                }

                if(preSelectAttrObj!=null) {
                    attCheckRowObjects.removeClass("checked");
                    $(preSelectAttrObj).addClass("checked");
                }
                $.message({
                    message: "该商品已下架",
                    type: 'error'
                });
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

        if(productVO.roughWeight!=null)
        {
            $(".product_detail_rough_weight").html(productVO.roughWeight+"kg");
        }

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

        if(productVO.shopProductDescriptionVO!=null)
        {
            var productDescHtml="";
            var shopProductDescriptionVO=productVO.shopProductDescriptionVO;
            if(shopProductDescriptionVO.productDescriptionImgs!=null
                &&shopProductDescriptionVO.productDescriptionImgs.length>0)
            {
                for(var i=0;i<shopProductDescriptionVO.productDescriptionImgs.length;i++)
                {
                    var img = shopProductDescriptionVO.productDescriptionImgs[i];
                    productDescHtml+="<p><a href=\""+img.link+"\"><img title=\""+img.title+"\" src=\""+img.httpFilePath+"\" style=\"width:100%\"></a></p>"
                }
            }
            $(".des_con_p").html(productDescHtml);

            if(productVO.shopProductDescriptionVO.brandNameAttribute!=null)
            {
                $(".description-attribute-brand").html("");
                $(".description-attribute-brand").append(" <li title=\""+productVO.shopProductDescriptionVO.brandNameAttribute.key+"\">品牌：\n" +
                    "                                <a href=\"#\" class=\"brand-name\" target=\"_blank\">"+productVO.shopProductDescriptionVO.brandNameAttribute.values[0]+"</a>\n" +
                    "                            </li>");
            }

            if(productVO.shopProductDescriptionVO.attributes!=null)
            {
                $(".description-attribute-more").html("");
                for(var j=0;j<productVO.shopProductDescriptionVO.attributes.length;j++)
                {
                    var descriptionAttribute = productVO.shopProductDescriptionVO.attributes[j];
                    if(descriptionAttribute.values!=null&&descriptionAttribute.values.length>0) {
                        var descriptionAttributeValue = descriptionAttribute.values[0];
                        $(".description-attribute-more").append("<li title=\""+descriptionAttributeValue+"\">"+descriptionAttribute.key+"："+descriptionAttributeValue+"</li>");
                    }
                }

            }
        }

        drawSpuAttributes(productVO.productId);

        showSaveBurCarBtns();
    }
}

function showSaveBurCarBtns()
{
    $(".suc_btn_panel").show();
}

function drawSpuAttributeChildHtml(attributeChild)
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
            salesAttributeChildHtml += drawSpuAttributeChildHtml(attributeChild.children[p]);
        }
    }
    salesAttributeChildHtml+="</dl>";
    salesAttributeChildHtml+="</dl>";
    salesAttributeChildHtml+="</div>";
    return salesAttributeChildHtml;
}

function drawSpuAttribute(spu)
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
                        salesAttributeHtml += drawSpuAttributeChildHtml(attributeTree.children[p]);
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
        url: basePath + "/api/product/spu/info",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify({"productId": productId}),
        dataType: "json",
        success: function (result) {
            if (result.code <= 0) {
                return ;
            }
            drawSpuAttribute(result.data);
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


function bindProductNumEvent()
{
    $("#buyCount").change(function(){
        var bnum = $(this).val();
        if(isNaN(bnum))
        {
            $(this).val("1");
        }
    });
}


function saveUserCar()
{
    var ptype = $("#ptype").val();
    if(ptype=="preview")
    {
        return;
    }
    loading.showLoading({
        type:1,
        tip:"提交中..."
    });
    var params = {
        shopProductSkuId:g_productVo.id,
        buyCount:$("#buyCount").val()
    };
    $.ajax({
        type: "POST",
        url: basePath+"/api/user/buyCar/save",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(params),
        dataType: "json",
        success: function (result) {
            if(result.code==1 ||result.code == 201)
            {
                if(result.data!=null){
                    $("#buyCarItemCount").html(result.data.length);
                }
                var buyCarPriceTotal=new BigNumber(0);
                for(var i=0;i<result.data.length;i++)
                {
                    var buyCarItem = result.data[i];
                    buyCarPriceTotal = buyCarPriceTotal.plus(new BigNumber(buyCarItem.productPrice).times(new BigNumber(buyCarItem.buyCount)));
                }

                $(".buy_car_price_total").html(buyCarPriceTotal.toFixed(2));
                ShowDiv_1('userBuyCarMsg','fade1');
                loadBuyCarPreviewPanel();
            }else if(result.code==403){
                window.location.href=basePath+result.data+"?redirectUrl="+encodeURIComponent(getProductPageUrl());
            }
        },
        error: function (result) {
        },
        complete:function(data,status){
            loading.hideLoading();
        }
    });

}

