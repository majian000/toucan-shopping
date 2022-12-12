

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

            var timeRemaining = result.data.timeRemaining; //剩余时间
            var maxPayTime = result.data.maxPayTime;
            //30分钟超时了
            if(timeRemaining<=0)
            {
                $(".payfo_tips").html("支付时间已超时");
                $(".payfo_tips").css("padding-left","30%");
                $(".oder_me").hide();
                $(".two_bg").hide();
                $(".btn_sure").hide();
                $(".pay_amount").hide();
                $(".p_pay").hide();

                return;
            }

            $(".pay_amount").html("￥"+result.data.orderAmount);

            countDown();
            setInterval(countDown, 1000);
            function countDown() {
                timeRemaining+=1000;
                var s = parseInt(((maxPayTime-timeRemaining)/1000)%60); //得到余数秒
                var m = parseInt((maxPayTime-timeRemaining)/1000/60); //得到整数分
                $("#t_m").html(m);
                $("#t_s").html(s);
            }
        },
        error: function (result) {
        },
        complete:function(data,status){
            loading.hideLoading();
        }
    });

}



