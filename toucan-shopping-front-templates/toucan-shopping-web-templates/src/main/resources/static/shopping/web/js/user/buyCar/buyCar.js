$(function () {

    loadBuyCarPanel();

});




function loadBuyCarPanel(){
    //查询信息
    $.post(basePath + "/api/user/buyCar/list", {}, function (result) {
        if(result.code == 1){
            var productHtmls="";
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
                    "                <td align=\"center\"><a onclick=\"ShowDiv('removeBuyCar','fade')\">删除</a></td>\n" +
                    "            </tr>\n" +
                    "           ";
                productPriceTotal+=(buyCarItem.productPrice*buyCarItem.buyCount);
            }
            productHtmls+="<tr height=\"70\">\n" +
                "                <td colspan=\"6\" style=\"font-family:'Microsoft YaHei'; border-bottom:0;\">\n" +
                "                    <label class=\"r_rad\"><input type=\"checkbox\" name=\"clear\" checked=\"checked\" /></label><label class=\"r_txt\">清空购物车</label>\n" +
                "                    <span class=\"fr\">商品总价：<b style=\"font-size:22px; color:#ff4e00;\">￥<a id=\"productPriceTotal\" style=\"color: #ff4e00;\">"+productPriceTotal+"</a></b></span>\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "            <tr valign=\"top\" height=\"150\">\n" +
                "                <td colspan=\"6\" align=\"right\">\n" +
                "                    <a href=\"#\"><img src=\""+basePath+"/static/images/buy1.gif\" /></a>&nbsp; &nbsp; <a href=\"#\"><img src=\""+basePath+"/static/images/buy2.gif\" /></a>\n" +
                "                </td>\n" +
                "            </tr>";


            $(".mcar_tab").append(productHtmls);

            bindBuyItemNumEvent();
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
            $(this).val("1");
            bnum = "1";
        }
        $("#buyItemTotal_"+cid).html((parseInt(bnum)*parseFloat($("#productPrice_"+cid).val())));
        calculatePriceTotal();
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

    $("#buyItemTotal_"+cid).html((parseInt($("#num_"+cid).val())*parseFloat($("#productPrice_"+cid).val())));
    calculatePriceTotal();
}



function addNum(cid)
{
    var c = $("#num_"+cid).val();
    c=parseInt(c)+1;
    $("#num_"+cid).val(c);

    $("#buyItemTotal_"+cid).html((parseInt($("#num_"+cid).val())*parseFloat($("#productPrice_"+cid).val())));
    calculatePriceTotal();
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

}