

$(function () {




    loadNonPayOrders();
});

function bindConsigneeDetailPageEvent()
{
    $("#orderState").mouseover(function() {
        $(this).addClass("deal-state-hover");
    });

    $("#orderState").mouseout(function() {
        $(this).removeClass("deal-state-hover");
    });

    $(".consignee .txt").mouseover(function() {
        var attrIndex=$(this).attr("attr-index");
        $(".consignee-detail-"+attrIndex).css("display","block");
    });

    $(".consignee .txt").mouseout(function() {
        var attrIndex=$(this).attr("attr-index");
        $(".consignee-detail-"+attrIndex).css("display","none");
    });
}

/**
 * 加载未支付订单页
 */
function loadNonPayOrders()
{
    $.ajax({
        type: "POST",
        url: basePath+"/api/order/query/nonPayment/list",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify({}),
        dataType: "json",
        success: function (result) {
            if(result.code!=0)
            {
                bindConsigneeDetailPageEvent();
            }
        },
        complete:function(data,status){
        }
    });
}