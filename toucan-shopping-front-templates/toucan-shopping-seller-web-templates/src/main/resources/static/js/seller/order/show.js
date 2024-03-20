

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
                    $("#orderNo").val(retObj.orderNo);
                    $("#orderAmount").val(retObj.orderAmount);
                    $("#payAmount").val(retObj.payAmount);

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