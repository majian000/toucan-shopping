$(function () {
    queryNewestList();
});


function queryNewestList()
{

    $.ajax({
        type: "POST",
        url: basePath+"/api/shop/product/approve/queryProductApproveListTop4",
        contentType: "application/json;charset=utf-8",
        data:  null,
        dataType: "json",
        success: function (result) {
            if(result.code==1)
            {
                var newProductHtmls="";

                if(result.data!=null&&result.data.length>0)
                {
                    for(var i=0;i<result.data.length;i++)
                    {
                        var newProduct = result.data[i];
                        newProductHtmls+=" <li>\n" +
                            "     <a href=\""+shoppingPcPath+productApprovePreviewPage+newProduct.id+"\" title=\""+newProduct.productSkuVOList[0].name+"\" target='_blank' class=\"pic_box\"><img src=\""+newProduct.httpMainPhotoFilePath+"\"><em class=\"btn btn-danger btn-round\">查看商品</em></a>\n" +
                            "     <a href=\""+shoppingPcPath+productApprovePreviewPage+newProduct.id+"\" target='_blank' title=\""+newProduct.productSkuVOList[0].name+"\">\n" +
                            "          <p><b>￥"+newProduct.productSkuVOList[0].price+"</b></p>\n" +
                            "     <span>"+newProduct.productSkuVOList[0].name+"</span>\n" +
                            "     </a>\n" +
                            "   </li>";
                    }
                }
                $(".to_new_product_ul").html(newProductHtmls);

                registNewwestProductEvent();
            }
        },
        error: function (result) {
        },
        complete:function()
        {
        }

    });
}