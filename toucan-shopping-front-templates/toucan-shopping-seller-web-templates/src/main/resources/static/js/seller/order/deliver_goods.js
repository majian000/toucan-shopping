
function drawDeliverCompanyList(){
    loading.showLoading({
        type:6,
        tip:"查询中..."
    });

    $.ajax({
        type: "POST",
        url: basePath+"/api/orderExpressDelivery/queryCompanyList",
        contentType: "application/json;charset=utf-8",
        data:  null,
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
                var datas = result.data;
                if(datas!=null&&datas.length>0) {
                    for(var i=0;i<datas.length;i++) {
                        var company = datas[i];
                        $("#companyType").append("<option value='"+company.code+"'>"+company.name+"</option>");
                    }
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
}

function saveDeliverGoods(){
    if(!checkInputFunction($('#deliverGoodsBtn'),2)){
        return false;
    }

    loading.showLoading({
        type:6,
        tip:"保存中..."
    });
    var fromData={
        orderId:$("#orderId").val(),
        companyTypeCode:$("#companyType").find("option:selected").val(),
        companyTypeName:$("#companyType").find("option:selected").text(),
        courierNumber:$("#courierNumber").val()
    };
    $.ajax({
        type: "POST",
        url: basePath+'/api/orderExpressDelivery/delivery',
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify(fromData),
        dataType: "json",
        success: function (data) {
            loading.hideLoading();
            if(data.code==401)
            {
                window.location.href=basePath+data.data;
            }else if(data.code<=0)
            {
                $.message({
                    time:'4000',
                    message: data.msg,
                    type: 'error'
                });
            }else if(data.code==1)
            {
                window.location.href=basePath+"/page/order/index";
            }
        }
    });
}

function backDeliverGoods(){
    window.location.href=basePath+"/page/order/index";
}


$(function () {

    drawDeliverCompanyList();


});