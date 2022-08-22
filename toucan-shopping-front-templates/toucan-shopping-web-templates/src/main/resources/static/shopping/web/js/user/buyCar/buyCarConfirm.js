$(function () {
    loadBuyCarPanel();
});





function loadBuyCarPanel(){
    loading.showLoading({
        type:1,
        tip:"查询中..."
    });

    $.ajax({
        type: "POST",
        url: basePath + "/api/user/buyCar/list",
        contentType: "application/json;charset=utf-8",
        data: {},
        dataType: "json",
        success: function (result) {
            if(result.code == 1){
                var productHtmls=" <tr>\n" +
                    "                    <td class=\"car_th\" style = \"width:35%\">商品名称</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:15%\">属性</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:10%\">购买数量</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:20%\">小计</td>\n" +
                    "                </tr>";
                var productPriceTotal = 0;

                for(var i=0;i<result.data.length;i++)
                {
                    var buyCarItem = result.data[i];
                    productHtmls+="  <tr>\n" +
                        "                <td>\n" +
                        "                    <div class=\"c_s_img\"><a href=\""+basePath+"/page/product/detail/"+buyCarItem.shopProductSkuId+"\" target='_blank' ><img src=\""+buyCarItem.httpProductImgPath+"\" title=\""+buyCarItem.productSkuName+"\" width=\"73\" height=\"73\" /></a></div>\n" +
                        "                    <a href=\""+basePath+"/page/product/detail/"+buyCarItem.shopProductSkuId+"\" target='_blank'>"+buyCarItem.productSkuName+"</a>\n" +
                        "                </td>\n" +
                        "                <td align=\"center\">"+buyCarItem.attributePreview+"</td>\n" +
                        "                <td align=\"center\">\n" +buyCarItem.buyCount + "                </td>\n" +
                        "                <td align=\"center\" style=\"color:#ff4e00;\">￥<a id=\"buyItemTotal_"+buyCarItem.id+"\" style=\"color: #ff4e00;\">"+(buyCarItem.buyCount*buyCarItem.productPrice)+"</a></td>\n" +
                        "            </tr>\n" +
                        "           ";
                    productPriceTotal+=(buyCarItem.productPrice*buyCarItem.buyCount);
                }
                productHtmls+=" <tr>\n" +
                    "                    <td colspan=\"5\" align=\"right\" style=\"font-family:'Microsoft YaHei';\">\n" +
                    "                        商品总价：￥<a id=\"productPriceTotal\">"+productPriceTotal+"</a>\n" +
                    "                    </td>\n" +
                    "                </tr>";


                $(".mcar_tab").html(productHtmls);

            }
            loading.hideLoading();
        },
        error: function (result) {
        },
        complete:function(data,status){
            loading.hideLoading();
        }
    });
}



function loadModifyBuyCarPanel(){
    loading.showLoading({
        type:1,
        tip:"查询中..."
    });

    $.ajax({
        type: "POST",
        url: basePath + "/api/user/buyCar/list",
        contentType: "application/json;charset=utf-8",
        data: {},
        dataType: "json",
        success: function (result) {
            if(result.code == 1){
                var productHtmls=" <tr>\n" +
                    "                    <td class=\"car_th\" style = \"width:35%\">商品名称</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:15%\">属性</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:10%\">购买数量</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:20%\">小计</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:20%\">操作</td>\n" +
                    "                </tr>";
                var productPriceTotal = 0;

                for(var i=0;i<result.data.length;i++)
                {
                    var buyCarItem = result.data[i];
                    productHtmls+="  <tr>\n" +
                        "                <td>\n" +
                        "                    <div class=\"c_s_img\"><a href=\""+basePath+"/page/product/detail/"+buyCarItem.shopProductSkuId+"\" target='_blank' ><img src=\""+buyCarItem.httpProductImgPath+"\" title=\""+buyCarItem.productSkuName+"\" width=\"73\" height=\"73\" /></a></div>\n" +
                        "                    <a href=\""+basePath+"/page/product/detail/"+buyCarItem.shopProductSkuId+"\" target='_blank'>"+buyCarItem.productSkuName+"</a>\n" +
                        "                </td>\n" +
                        "                <td align=\"center\">"+buyCarItem.attributePreview+"</td>\n" +
                        "                <td align=\"center\">\n" +
                        "                    <div class=\"c_num\">\n" +
                        "                        <input type=\"button\" value=\"\" onclick=\"subNum('"+buyCarItem.id+"');\" class=\"car_btn_1\" />\n" +
                        "                        <input type=\"text\" value=\""+buyCarItem.buyCount+"\" id=\"num_"+buyCarItem.id+"\" attr-cid=\""+buyCarItem.id+"\" class=\"car_ipt mcar_pn\" />\n" +
                        "                        <input type=\"button\" value=\"\" onclick=\"addNum('"+buyCarItem.id+"');\" class=\"car_btn_2\" />\n" +
                        "                        <input type=\"hidden\" class='mcar_pp'  id=\"productPrice_"+buyCarItem.id+"\" value=\""+buyCarItem.productPrice+"\" />"+
                        "                    </div>\n" +
                        "                </td>\n" +
                        "                <td align=\"center\" style=\"color:#ff4e00;\">￥<a id=\"buyItemTotal_"+buyCarItem.id+"\" style=\"color: #ff4e00;\">"+(buyCarItem.buyCount*buyCarItem.productPrice)+"</a></td>\n" +
                        "                <td align=\"center\"><a onclick=\"showRemoveBuyCar('"+buyCarItem.id+"','"+buyCarItem.productSkuName+"')\">删除</a></td>\n" +
                        "            </tr>\n" +
                        "           ";
                    productPriceTotal+=(buyCarItem.productPrice*buyCarItem.buyCount);
                }
                productHtmls+=" <tr>\n" +
                    "                    <td colspan=\"5\" align=\"right\" style=\"font-family:'Microsoft YaHei';\">\n" +
                    "                        商品总价：￥<a id=\"productPriceTotal\">"+productPriceTotal+"</a>\n" +
                    "                    </td>\n" +
                    "                </tr>";


                $(".mcar_tab").html(productHtmls);

                bindBuyItemNumEvent();
            }
            loading.hideLoading();
        },
        error: function (result) {
        },
        complete:function(data,status){
            loading.hideLoading();
        }
    });
}
