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

            }
        }
    });

});