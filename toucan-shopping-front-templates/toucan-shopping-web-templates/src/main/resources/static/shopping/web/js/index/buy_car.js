$(function () {
    loadBuyCarPreviewPanel();
});


function loadBuyCarPreviewPanel(){
    //查询信息
    $.post(basePath + "/api/user/buyCar/preview/info", {}, function (result) {
        $(".buy_car_login_tip").hide();
        if(result.code == 1){
            var productCount = 0;
            if(result.data!=null&&result.data.length>0)
            {
                productCount = result.data.length;
            }
            $("#buyCarProductCount").html(productCount);

            if(result.data == null|| result.data.length<=0)
            {
                $(".buy_car_empty").show();
            }else{
                var productHtmls="";
                var productPriceTotal = 0;
                for(var i=0;i<result.data.length;i++)
                {
                    var buyCarItem = result.data[i];
                    productHtmls+="<li>\n" +
                        "                <div class=\"img\"><a href=\""+basePath+"/page/product/detail/"+buyCarItem.shopProductSkuId+"\"><img src=\""+buyCarItem.httpProductImgPath+"\" title=\""+buyCarItem.productSkuName+"\" width=\"58\" height=\"58\" /></a></div>\n" +
                        "                <div class=\"name\"><a href=\""+basePath+"/page/product/detail/"+buyCarItem.shopProductSkuId+"\" title=\""+buyCarItem.productSkuName+"\">"+buyCarItem.productSkuName+"</a></div>\n" +
                        "                <div class=\"price\"><font color=\"#ff4e00\">￥"+buyCarItem.productPrice+"</font> x "+buyCarItem.buyCount+"</div>\n" +
                        "            </li>";
                    productPriceTotal+=(buyCarItem.productPrice*buyCarItem.buyCount);
                }
                $(".cars").html(productHtmls);
                $("#mcars_price_total_label").html(productPriceTotal);

                $(".mcars").show();
                $(".mcars_price_total").show();
                $(".mcars_pay").show();
            }
        }
    });

}