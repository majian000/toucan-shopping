

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


    $(".cancel_order").click(function(){
        ShowDiv('cancelOrder','fade');
    });


    $(".cancel_order_sure").click(function(){
        cancelOrder();
    });
});


function cancelOrder()
{
    CloseDiv_1('cancelOrder','fade');
    loading.showLoading({
        type:1,
        tip:"加载中..."
    });
    var orderNo = $("#orderNo").val();
    var orderType=$("#orderType").val();

    var params = {
        orderNo:orderNo
    };
    var requestUrl = basePath + "/api/order/main/order/cancel";
    if(orderType=="2") //子订单取消
    {
        requestUrl = basePath + "/api/order/cancel";
    }
    $.ajax({
        type: "POST",
        url: basePath+"/api/order/main/order/cancel",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(params),
        dataType: "json",
        success: function (result) {
            if(result.code==1)
            {
                $(".payfo_tips").html("订单已取消");
                hideView();

                return;
            }else{
                $.message({
                    message: "取消失败,请稍后重试",
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

function hideView()
{

    $(".payfo_tips").css("padding-left","30%");
    $(".pay_sucess_panel").hide();
    $(".pay_error_panel").show();
}

function queryMainOrder() {
    loading.showLoading({
        type: 1,
        tip: "加载中..."
    });

    var orderType=$("#orderType").val();
    var params = {
        orderNo:$("#orderNo").val()
    };
    var requestUrl = basePath + "/api/order/main/order/detail";
    if(orderType=="2") //子订单支付
    {
        requestUrl = basePath + "/api/order/order/detail";
    }
    $.ajax({
        type: "POST",
        url: requestUrl,
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
            var isHide = false;
            if(result.data.payStatus==4)
            {
                $(".payfo_err_msg").html("订单已取消");
                isHide = true;
            }
            if(timeRemaining<=0)
            {
                $(".payfo_err_msg").html("支付时间已超时");
                isHide = true;
            }
            if(isHide)
            {
                hideView();
                return;
            }


            $(".pay_sucess_panel").show();

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
            $(".pay_btns").show();
        }
    });

}



