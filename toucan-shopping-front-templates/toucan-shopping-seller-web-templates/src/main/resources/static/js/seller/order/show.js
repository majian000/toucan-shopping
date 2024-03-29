

function initTabs(){
    var widget = $('.tabs-basic');

    var tabs = widget.find('ul a'),
        content = widget.find('.tabs-content-placeholder > div');

    tabs.on('click', function (e) {

        e.preventDefault();

        // Get the data-index attribute, and show the matching content div

        var index = $(this).data('index');

        tabs.removeClass('tab-active');
        content.removeClass('tab-content-active');

        $(this).addClass('tab-active');
        content.eq(index).addClass('tab-content-active');

    });

}


$(function () {

    initTabs();

    loading.showLoading({
        type:1,
        tip:"加载中..."
    });


    $('#backBtn').on('click', function () {
        window.location.href=basePath+"/page/order/index";
    });


    $.ajax({
        type: "POST",
        url: basePath+"/api/order/findById",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify({id:$("#id").val()}),
        dataType: "json",
        success: function (result) {
            if(result.code<=0)
            {
                $.message({
                    message: "查询失败,请稍后重试",
                    type: 'error'
                });
                return ;
            }
            if(result.data!=null) {
                var retObj = result.data;
                if(retObj!=null) {
                    $("#orderNo").html(retObj.orderNo);
                    $("#orderAmount").html(retObj.orderAmount);
                    $("#payAmount").html(retObj.payAmount);
                    $("#freightAmount").html(retObj.freightAmount);
                    $("#redPackageAmount").html(retObj.redPackageAmount);
                    $("#couponAmount").html(retObj.couponAmount);


                    var tradeStatusName="";
                    if(retObj.tradeStatus==0){
                        tradeStatusName="待付款";
                    }else if(retObj.tradeStatus==1){
                        tradeStatusName="待收货";
                    }else if(retObj.tradeStatus==2){
                        tradeStatusName="已取消";
                        $("#tradeStatus").css("color","red")
                    }else if(retObj.tradeStatus==3){
                        tradeStatusName="已完成";
                        $("#tradeStatus").css("color","green")
                    }else if(retObj.tradeStatus==4){
                        tradeStatusName="待发货";
                    }
                    $("#tradeStatus").html(tradeStatusName);



                    var payStatusName="";
                    if(retObj.payStatus==0){
                        payStatusName="未支付";
                    }else if(retObj.payStatus==1){
                        payStatusName="已支付";

                        var payMethodName="";
                        if(retObj.payMethod==1){
                            payMethodName="线上支付";
                        }else if(retObj.payMethod==2){
                            payMethodName="线下支付";
                        }
                        $("#payMethod").html(payMethodName);


                        var payTypeName="";
                        if(retObj.payType==0){
                            payTypeName="微信";
                        }else if(retObj.payType==1){
                            payTypeName="支付宝";
                        }
                        $("#payType").html(payTypeName);

                        $("#outerTradeNo").html(retObj.outerTradeNo);

                        $(".pay-success-group").show();

                        $("#payStatus").css("color","green")

                    }else if(retObj.payStatus==4){
                        payStatusName="取消支付";
                        $("#cancelDate").val(retObj.cancelDate);
                        $("#cancelRemark").val(retObj.cancelRemark);

                        $(".pay-cancel-group").show();

                        $("#payStatus").css("color","red")
                    }

                    $("#payStatus").html(payStatusName);



                    $("#remark").val(retObj.remark);
                    $("#bestDate").val(retObj.bestDate);


                }
            }
        },
        error: function (result) {
            $.message({
                message: "查询失败,请稍后重试",
                type: 'error'
            });
        },
        complete:function()
        {
            loading.hideLoading();
        }

    });


});