$(function () {
    loadBuyCarPanel();

    $(".mcar_modifu").click(function(){
        var opt = $(this).attr("attr-opt");
        if(opt!=null&&opt=="1") {
            $(this).html("关闭");
            $(this).attr("attr-opt","2");
            loadModifyBuyCarPanel();
        }else{
            $(this).html("修改");
            $(this).attr("attr-opt","1");
            loadBuyCarPanel();
        }
    });
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



                $(".product_price_total").html("￥"+productPriceTotal);
                $(".order_price_total").html("￥"+productPriceTotal);

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


                $(".product_price_total").html("￥"+productPriceTotal);
                $(".order_price_total").html("￥"+productPriceTotal);

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



function bindBuyItemNumEvent()
{
    $(".mcar_pn").change(function(){
        var bnum = $(this).val();
        var cid = $(this).attr("attr-cid");
        if(isNaN(bnum))
        {
            bnum = "1";
        }
        updateRow(cid,bnum);
    });
}


function subNum(cid)
{
    var c = $("#num_"+cid).val();
    if(c<=1){
        c=1;
    }else{
        c=parseInt(c)-1;
        $("#num_"+cid).val(c);
    }
    updateRow(cid,$("#num_"+cid).val());
}



function addNum(cid)
{
    var c = $("#num_"+cid).val();
    c=parseInt(c)+1;
    $("#num_"+cid).val(c);

    updateRow(cid,$("#num_"+cid).val());
}



function updateRow(cid,bnum)
{
    loading.showLoading({
        type:1,
        tip:"提交中..."
    });

    var buyCarItem = {
        id:cid,
        buyCount:bnum
    };

    $.ajax({
        type: "POST",
        url: basePath+"/api/user/buyCar/update",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(buyCarItem),
        dataType: "json",
        success: function (result) {
            if(result.code==1)
            {
                $(this).val(bnum);
                $("#buyItemTotal_"+cid).html((parseInt(bnum)*parseFloat($("#productPrice_"+cid).val())));
                calculatePriceTotal();
            }else{
                $.message({
                    message: "请稍后重试",
                    type: 'error'
                });
            }
        },
        error: function (result) {
        },
        complete:function(data,status){
            loading.hideLoading();
        }
    });
}

function calculatePriceTotal()
{
    var pns = $(".mcar_pn"); //数量
    var pps = $(".mcar_pp"); //单价
    var productPriceTotal = 0;
    for(var i=0;i<pns.length;i++)
    {
        productPriceTotal+= (parseInt($(pns[i]).val())*parseFloat($(pps[i]).val()));
    }
    $("#productPriceTotal").html(productPriceTotal);

    $(".product_price_total").html("￥"+productPriceTotal);
    $(".order_price_total").html("￥"+productPriceTotal);
}
