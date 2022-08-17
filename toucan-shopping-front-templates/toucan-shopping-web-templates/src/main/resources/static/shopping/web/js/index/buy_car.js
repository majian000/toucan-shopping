$(function () {

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
                    productHtmls+="<li>\n" +
                        "                <div class=\"img\"><a href=\"#\"><img src=\""+basePath+"/static/images/car1.jpg\" width=\"58\" height=\"58\" /></a></div>\n" +
                        "                <div class=\"name\"><a href=\"#\">法颂浪漫梦境50ML 香水女士持久清新淡香 送2ML小样3只</a></div>\n" +
                        "                <div class=\"price\"><font color=\"#ff4e00\">￥399</font> X1</div>\n" +
                        "            </li>";
                }
                $(".cars").html(productHtmls);
                $("#mcars_price_total_label").val(productPriceTotal);

                $(".mcars").show();
                $(".mcars_price_total").show();
                $(".mcars_pay").show();
            }
        }
    });

});