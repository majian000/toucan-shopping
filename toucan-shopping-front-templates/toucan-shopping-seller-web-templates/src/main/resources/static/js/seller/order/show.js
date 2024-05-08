

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
                    $("#createDate").html(retObj.createDate);
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
                        $("#cancelDate").html(retObj.cancelDate);
                        $("#cancelRemark").html(retObj.cancelRemark);

                        $(".pay-cancel-group").show();

                        $("#payStatus").css("color","red")
                    }

                    $("#payStatus").html(payStatusName);



                    $("#remark").html(retObj.remark);
                    $("#bestDate").html(retObj.bestDate);

                    if(retObj.orderConsigneeAddress!=null){
                        var orderConsigneeAddress = retObj.orderConsigneeAddress;
                        $("#oca_name").html(orderConsigneeAddress.name);
                        $("#oca_phone").html(orderConsigneeAddress.phone);
                        $("#oca_provinceCityAreaName").html(orderConsigneeAddress.provinceName+" "+orderConsigneeAddress.cityName+" "+orderConsigneeAddress.areaName);
                        $("#oca_address").html(orderConsigneeAddress.address);
                    }

                    if(retObj.orderItems!=null&&retObj.orderItems.length>0){
                        drawOrderItemTable(retObj);
                        drawOrderItemProductTable(retObj);
                    }

                    drawOrderExpressDeliver(retObj);

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


function drawOrderItemTable(orderVO){
    var orderItems = orderVO.orderItems;

    var tableHtml="";
    tableHtml+=" <tr class=\"tabTh\" style=\"height:50px;\">\n" +
        "                            <td style=\"width:50px;\" >序号</td>\n" +
        "                            <td style=\"width:100px;\" >商品预览</td>\n" +
        "                            <td style=\"width:150px;\" >商品名称</td>\n" +
        "                            <td style=\"width:100px;\"  >总金额</td>\n" +
        "                            <td style=\"width:100px;\"  >商品单价</td>\n" +
        "                            <td style=\"width:100px;\"  >购买数量</td>\n" +
        "                            <td style=\"width:100px;\"  >配送状态</td>\n" +
        "                        </tr>";


    for(var i=0;i<orderItems.length;i++)
    {
        var row = orderItems[i];
        var deliveryStatusName="";
        if(row.deliveryStatus==0){
            deliveryStatusName="未收货";
        }else if(row.deliveryStatus==1){
            deliveryStatusName="送货中";
        }else if(row.deliveryStatus==2){
            deliveryStatusName="已收货";
        }


        tableHtml+=" <tr align=\"center\" class=\"tabTd\">\n" ;
        tableHtml+=   "                            <td><div class=\"tabTdWrap\">"+(i+1)+"</div></td>\n" ;
        tableHtml+=    "                            <td><div class=\"tabTdWrap\"><a href=\"javascript:window.open('"+row.httpProductPreviewPath+"')\"><img width=\"100\" height=\"100\" src=\""+row.httpProductPreviewPath+"\"></a>"+"</div></td>\n" ;
        tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.productSkuName+"</div></td>\n" ;
        tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.orderItemAmount+"</div></td>\n" ;
        tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.productPrice+"</div></td>\n" ;
        tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.productNum+"</div></td>\n" ;
        tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+deliveryStatusName+"</div></td>\n" ;
        tableHtml+=    "                        </tr>";
    }

    $("#orderItemTableBody").html(tableHtml);
    $("#orderItemList").FrozenTable(2,0,0);


}




function drawOrderItemProductTable(orderVO){
    var orderItemProductSkus = orderVO.orderItemProductSkus;

    var tableHtml="";
    tableHtml+=" <tr class=\"tabTh\" style=\"height:50px;\">\n" +
        "                            <td style=\"width:50px;\" >序号</td>\n" +
        "                            <td style=\"width:100px;\" >商品编号</td>\n" +
        "                            <td style=\"width:100px;\" >商品预览</td>\n" +
        "                            <td style=\"width:150px;\" >商品名称</td>\n" +
        "                            <td style=\"width:100px;\"  >商品单价</td>\n" +
        "                            <td style=\"width:100px;\"  >分类</td>\n" +
        "                            <td style=\"width:100px;\"  >毛重</td>\n" +
        "                            <td style=\"width:100px;\"  >净重</td>\n" +
        "                        </tr>";


    for(var i=0;i<orderItemProductSkus.length;i++)
    {
        var row = orderItemProductSkus[i];

        tableHtml += " <tr align=\"center\" class=\"tabTd\">\n";
        tableHtml += "                            <td><div class=\"tabTdWrap\">" + (i + 1) + "</div></td>\n";
        tableHtml += "                            <td><div class=\"tabTdWrap\">" + row.productNo + "</div></td>\n";
        tableHtml += "                            <td><div class=\"tabTdWrap\"><a href=\"javascript:window.open('" + row.httpProductPreviewPath + "')\"><img width=\"100\" height=\"100\" src=\"" + row.httpProductPreviewPath + "\"></a>" + "</div></td>\n";
        tableHtml += "                            <td><div class=\"tabTdWrap\">" + row.name + "</div></td>\n";
        tableHtml += "                            <td><div class=\"tabTdWrap\">" + row.price + "</div></td>\n";
        tableHtml += "                            <td><div class=\"tabTdWrap\">" + row.categoryName + "</div></td>\n";
        tableHtml += "                            <td><div class=\"tabTdWrap\">" + row.roughWeight + "</div></td>\n";
        tableHtml += "                            <td><div class=\"tabTdWrap\">" + row.suttle + "</div></td>\n";
        tableHtml += "                        </tr>";
    }

    $("#orderItemProductTableBody").html(tableHtml);
    $("#orderItemProductList").FrozenTable(2,0,0);


}


function drawOrderExpressDeliver(orderVO){



}