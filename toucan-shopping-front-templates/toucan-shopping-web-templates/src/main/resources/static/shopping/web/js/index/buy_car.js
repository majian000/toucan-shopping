$(function () {
    loadBuyCarPreviewPanel();
});


function loadBuyCarPreviewPanel(){
    //查询信息
    $.post(basePath + "/api/user/buyCar/preview/info", {}, function (result) {

        if(result.code == 1){
            $(".buy_car_login_tip").hide();
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
                $(".buy_car_empty").hide();
                var productHtmls="";
                var productPriceTotal = 0;
                for(var i=0;i<result.data.length;i++)
                {
                    var buyCarItem = result.data[i];
                    var noAllowedBuyDesc="";
                    if(!buyCarItem.isAllowedBuy)
                    {
                        noAllowedBuyDesc="&nbsp;"+buyCarItem.noAllowedBuyDesc;
                    }

                    productHtmls+="<li>\n" +
                        "                <div class=\"img\"><a href=\""+basePath+"/page/product/detail/"+buyCarItem.shopProductSkuId+"\"><img src=\""+buyCarItem.httpProductImgPath+"\" title=\""+buyCarItem.productSkuName+"\" width=\"58\" height=\"58\" /></a></div>\n" ;
                    if(!buyCarItem.isAllowedBuy)
                    {
                        productHtmls+=    "                <div class=\"name\" title='"+buyCarItem.productSkuName+noAllowedBuyDesc+"'><del>"+buyCarItem.productSkuName+noAllowedBuyDesc+"</del></div>\n" ;
                    }else{
                        productHtmls+=    "                <div class=\"name\"><a href=\""+basePath+"/page/product/detail/"+buyCarItem.shopProductSkuId+"\" title=\""+buyCarItem.productSkuName+"\">"+buyCarItem.productSkuName+"</a></div>\n" ;
                    }
                    productHtmls+=    "                <div class=\"price\"><font color=\"#ff4e00\">￥"+buyCarItem.productPrice+"</font> x "+buyCarItem.buyCount+"</div>\n" +
                        "            </li>";

                    if(buyCarItem.isAllowedBuy) {
                        productPriceTotal += (buyCarItem.productPrice * buyCarItem.buyCount);
                    }
                }
                $(".cars").html(productHtmls);
                $("#mcars_price_total_label").html(productPriceTotal);

                $(".mcars").show();
                $(".mcars_price_total").show();
                $(".mcars_pay").show();

            }

            drawUserCenterBuyCar(result);
        }
    });

}


/**
 * 渲染个人中心购物车
 */
function drawUserCenterBuyCar(result){
    var ucBCars = $(".uc_bcars");
    if(ucBCars!=null&&ucBCars.length>0)
    {
        if(result.data==null||result.data.length<=0)
        {
            $(ucBCars).html(" <p class=\"no-news hidden\">您的购物车是空的，<a href=\""+basePath+"/index\">马上去购物~</a></p> ");
        }else{
            var productHtmls="";
            var dataLength = result.data.length;
            if(dataLength>2)
            {
                dataLength = 2;
            }
            for(var i=0;i<dataLength;i++)
            {
                var buyCarItem = result.data[i];
                productHtmls+=" <li style='height:200px;margin-left: 30px;'>\n" +
                    "     <a href=\""+basePath+"/page/product/detail/"+buyCarItem.shopProductSkuId+"\"><img src=\""+buyCarItem.httpProductImgPath+"\" title=\""+buyCarItem.productSkuName+"\" width=\"160px\" height=\"160px\"></a>\n" +
                    "     <a class=\"mp_name\" href=\""+basePath+"/page/product/detail/"+buyCarItem.shopProductSkuId+"\" title=\""+buyCarItem.productSkuName+"\">"+buyCarItem.productSkuName+"</a>\n" +
                    "   </li>";
            }
            $(ucBCars).html(productHtmls);
        }
    }
}