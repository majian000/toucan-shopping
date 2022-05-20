$(function(){
    var id = getProductId();
    if(id!=null&&id!="") {
        $.ajax({
            type: "POST",
            url: basePath + "/api/product/approve/detail",
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
    if(productVO.attributes!=null) {
        var attributesObject = JSON.parse(productVO.productAttributes);
        var attributeHtml="";
        for (var attributeKey in attributesObject) {
            attributeHtml+="<div class=\"des_choice\">";
            attributeHtml+=" <span class=\"fl\">"+attributeKey+"：</span>";
            attributeHtml+=" <ul>";
            var attributeValues= attributesObject[attributeKey];
            if(attributeValues!=null&&attributeValues.length>0)
            {
                for(var j=0;j<attributeValues.length;j++)
                {
                    if(j==0)
                    {
                        attributeHtml+="<li class=\"checked\">"+attributeValues[j]+"<div class=\"ch_img\"></div></li>";
                    }else{
                        attributeHtml+="<li>"+attributeValues[j]+"<div class=\"ch_img\"></div></li>";
                    }
                }
            }

            attributeHtml+=" </ul>";
            attributeHtml+="</div>";
        }
        $(".attributeList").append(attributeHtml);
    }
}

function drawProductPage(productVO)
{
    if(productVO!=null)
    {
        var photoPos = 0;
        $(".productPhotoPreview").empty();
        $(".productPhotoPreview").append("<li onclick=\"showPic("+photoPos+")\" rel=\"MagicZoom\" class=\"tsSelectImg\">" +
            "<img src=\""+productVO.httpMainPhotoFilePath+"\" tsImgS=\""+productVO.httpMainPhotoFilePath+"\" width=\"79\" height=\"79\" />" +
            "</li>");

        $("#MagicZoom").attr("href",productVO.httpMainPhotoFilePath);
        $("#MagicZoomImg").attr("src",productVO.httpMainPhotoFilePath);
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
                    productDescHtml+="<p><a href=\""+img.link+"\"><img title=\""+img.title+"\" src=\""+img.httpFilePath+"\"></a></p>"
                }
            }
            $(".des_con_p").html(productDescHtml);
        }

    }
}