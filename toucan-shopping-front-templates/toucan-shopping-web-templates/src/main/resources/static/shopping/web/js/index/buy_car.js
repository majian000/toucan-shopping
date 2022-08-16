$(function () {

    //查询信息
    $.post(basePath + "/api/user/buyCar/preview/info", {}, function (result) {
        if (result.code == 403) {
            $(".buy_car_login_tip").show();
        }else if(result.code == 0){

        }
    });

});