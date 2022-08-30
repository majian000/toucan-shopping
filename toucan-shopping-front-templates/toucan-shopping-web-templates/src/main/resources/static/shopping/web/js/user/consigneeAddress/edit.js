$(function () {

    loadConsigneeAddressInfo();

    $("#ms_city").click(function (e) {
        SelCity(this,e);
    });
    $(".msc_l").click(function (e) {
        SelCity(document.getElementById("ms_city"),e);
    });



    $("#caibtn").click(function() {

        if(!checkInputFunction($('#caibtn'),2)){
            return false;
        }

        var fields = $('#consigneeAddressForm').serializeArray();
        var params = {}; //声明一个对象
        $.each(fields, function(index, field) {
            params[field.name] = field.value; //通过变量，将属性值，属性一起放到对象中
        });

        loading.showLoading({
            type:1,
            tip:"修改中..."
        });
        $.ajax({
            type: "POST",
            url: basePath+"/api/user/consigneeAddress/update",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify(params),
            dataType: "json",
            success: function (result) {
                if(result.code<=0)
                {
                    $("#vcode").val("");

                    $.message({
                        message: result.msg,
                        type: 'error'
                    });
                    $("#refreshCaptcha").attr("src",basePath+"/api/user/vcode?"+new Date().getTime());
                }else{
                    window.location.href=basePath+"/page/user/consigneeAddress/list";
                }
            },
            error: function (result) {
                $("#edit_info_msg").text("修改失败,请稍后重试");
            },
            complete:function(data,status){
                loading.hideLoading();
            }
        });
    });

});



function loadConsigneeAddressInfo()
{
    loading.showLoading({
        type:1,
        tip:"加载中..."
    });
    $.ajax({
        type: "POST",
        url: basePath+"/api/user/consigneeAddress/load",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify({id:$("#id").val()}),
        dataType: "json",
        success: function (result) {
            if(result.code<=0)
            {

            }
            loading.hideLoading();
        },
        error: function (result) {
            $("#edit_info_msg").text("修改失败,请稍后重试");
        },
        complete:function(data,status){
            loading.hideLoading();
        }
    });
}