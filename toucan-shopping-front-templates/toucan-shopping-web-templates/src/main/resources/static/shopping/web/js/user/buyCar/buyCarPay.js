

$(function () {
    $(".buy_item_pay").click(function(){
        var buyItemPays = $(".buy_item_pay");
        for(var i=0;i<buyItemPays.length;i++)
        {
            $(buyItemPays[i]).removeClass("checked")
        }
        $(this).addClass("checked");
    });
    queryMainOrder();
});




function queryMainOrder() {
    loading.showLoading({
        type: 1,
        tip: "加载中..."
    });

    var params = {
        orderNo:$("#orderNo").val()
    };
    $.ajax({
        type: "POST",
        url: basePath + "/api/order/main/order/detail",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(params),
        dataType: "json",
        success: function (result) {
            if(result.code!=1)
            {
                $.message({
                    message:result.msg,
                    type: 'error'
                });
                return;
            }

        },
        error: function (result) {
        },
        complete:function(data,status){
            loading.hideLoading();
        }
    });

}