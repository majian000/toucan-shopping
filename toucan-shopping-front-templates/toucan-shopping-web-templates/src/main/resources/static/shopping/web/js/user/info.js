

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

function drawNonPaymentOrderList(data)
{
    if(data.total>0)
    {
        var orderTableHtml="";
        for(var i=0;i<data.list.length;i++)
        {
            var order = data.list[i];
            var consignee = order.orderConsigneeAddress;
            if(consignee==null)
            {
                consignee={};
            }
            orderTableHtml+="   <tbody id=\"tb-"+order.id+"\">\n" +
                "                                    <tr class=\"sep-row\">\n" +
                "                                        <td colspan=\"5\"></td>\n" +
                "                                    </tr>\n" +
                "                                    <tr class=\"tr-th\">\n" +
                "                                        <td colspan=\"5\">\n" +
                "                                            <span class=\"gap\"></span>\n" +
                "                                            <span class=\"dealtime\" title=\""+order.createDate+"\">"+order.createDate+"</span>\n" +
                "                                            <span class=\"number\">\n" +
                "                                            订单号：\n" +
                "                                            <a  target=\"_blank\" href=\"#\" >"+order.orderNo+"</a>\n" +
                "                                        </span>\n" +
                "\n" +
                "                                            <div class=\"tr-operate\">\n" +
                // "                                            <span class=\"order-shop\">\n" +
                // "                                                <a href=\"#\" target=\"_blank\" class=\"shop-txt venderName11621354 venderSite\" clstag=\"click|keycount|orderlist|vender\" title=\"凯尔林官方旗舰店\">凯尔林官方旗舰店</a>\n" +
                // "                                                <a class=\"btn-im venderChat11621354\" href=\"#\" target=\"_blank\" title=\"联系卖家\" clstag=\"click|keycount|orderinfo|chatim\"></a>\n" +
                // "                                            </span>\n" +
                "\n" +
                "                                                <span class=\"tel\">\n" +
                "                                                <i class=\"tel-icon venderSiteTel\" style=\"display: none;\"></i>\n" +
                "                                            </span>\n" +
                "\n" +
                "                                                <span class=\"order-tips\"></span>\n" +
                "                                            </div>\n" +
                "                                        </td>\n" +
                "                                    </tr>\n" +
                "                                    <tr class=\"tr-bd\" id=\"track257427220634\" oty=\"22,4,70\" cxl=\"0\">\n" +
                "                                    <td>\n" +
                "                                        <div class=\"goods-item p-10032283363234\">\n" +
                "                                            <div class=\"p-img\">\n" +
                "                                                <a href=\"//item.jd.com/10032283363234.html\" clstag=\"click|keycount|orderinfo|order_product\" target=\"_blank\">\n" +
                "                                                    <img class=\"\" src=\"//img10.360buyimg.com/N6/s60x60_jfs/t1/194465/37/7333/54749/60c187c6Ec5d017bc/3c486cee3de9869a.jpg\" title=\"凯尔林马桶水箱配件冲水按钮按键双按通用坐便器按压器马桶盖开关圆形 大号圆形马桶按键\" data-lazy-img=\"done\" width=\"60\" height=\"60\">\n" +
                "                                                </a>\n" +
                "                                            </div>\n" +
                "                                            <div class=\"p-msg\">\n" +
                "                                                <div class=\"p-name\">\n" +
                "                                                    <a href=\"//item.jd.com/10032283363234.html\" class=\"a-link\" clstag=\"click|keycount|orderinfo|order_product\" target=\"_blank\" title=\"凯尔林马桶水箱配件冲水按钮按键双按通用坐便器按压器马桶盖开关圆形 大号圆形马桶按键\">凯尔林马桶水箱配件冲水按钮按键双按通用坐便器按压器马桶盖开关圆形 大号圆形马桶按键</a>\n" +
                "                                                </div>\n" +
                "                                                <div class=\"p-extra\">\n" +
                "                                                </div>\n" +
                "\n" +
                "                                            </div>\n" +
                "                                        </div>\n" +
                "                                        <div class=\"goods-number\">\n" +
                "                                            x1\n" +
                "                                        </div>\n" +
                "\n" +
                "\n" +
                "                                        <div class=\"goods-repair\">\n" +
                "                                        </div>\n" +
                "                                        <div class=\"clr\"></div>\n" +
                "                                        <div class=\"goods-item p-10032283363234\">\n" +
                "                                            <div class=\"p-img\">\n" +
                "                                                <a href=\"//item.jd.com/10032283363234.html\" clstag=\"click|keycount|orderinfo|order_product\" target=\"_blank\">\n" +
                "                                                    <img class=\"\" src=\"//img10.360buyimg.com/N6/s60x60_jfs/t1/194465/37/7333/54749/60c187c6Ec5d017bc/3c486cee3de9869a.jpg\" title=\"凯尔林马桶水箱配件冲水按钮按键双按通用坐便器按压器马桶盖开关圆形 大号圆形马桶按键\" data-lazy-img=\"done\" width=\"60\" height=\"60\">\n" +
                "                                                </a>\n" +
                "                                            </div>\n" +
                "                                            <div class=\"p-msg\">\n" +
                "                                                <div class=\"p-name\">\n" +
                "                                                    <a href=\"//item.jd.com/10032283363234.html\" class=\"a-link\" clstag=\"click|keycount|orderinfo|order_product\" target=\"_blank\" title=\"凯尔林马桶水箱配件冲水按钮按键双按通用坐便器按压器马桶盖开关圆形 大号圆形马桶按键\">凯尔林马桶水箱配件冲水按钮按键双按通用坐便器按压器马桶盖开关圆形 大号圆形马桶按键</a>\n" +
                "                                                </div>\n" +
                "                                                <div class=\"p-extra\">\n" +
                "                                                </div>\n" +
                "\n" +
                "                                            </div>\n" +
                "                                        </div>\n" +
                "                                        <div class=\"goods-number\">\n" +
                "                                            x1\n" +
                "                                        </div>\n" +
                "\n" +
                "\n" +
                "                                        <div class=\"goods-repair\">\n" +
                "                                        </div>\n" +
                "                                        <div class=\"clr\"></div>\n" +
                "                                    </td>\n" +
                "\n" +
                "                                    <td rowspan=\"1\">\n" +
                "                                        <div class=\"consignee tooltip\">\n" +
                "                                            <span class=\"txt\" attr-index=\"02\">"+consignee.name+"</span><b></b>\n" +
                "                                            <div class=\"prompt-01 prompt-02 consignee-detail-02\" style=\"display: none;\">\n" +
                "                                                <div class=\"pc\">\n" +
                "                                                    <strong>"+consignee.name+"</strong>\n" +
                "                                                    <p>"+consignee.provinceName+" "+consignee.cityName+" "+consignee.areaName+" "+consignee.address+"</p>\n" +
                "                                                    <p>"+consignee.phone+"</p>\n" +
                "                                                </div>\n" +
                "                                                <div class=\"p-arrow p-arrow-left\"></div>\n" +
                "                                            </div>\n" +
                "                                        </div>\n" +
                "                                    </td>\n" +
                "                                    <td rowspan=\"1\">\n" +
                "                                        <div class=\"amount\">\n" +
                "                                            <span>¥"+order.orderAmount+"</span> <br>\n" +
                "                                            <span class=\"ftx-13\">在线支付</span>\n" +
                "                                        </div>\n" +
                "                                    </td>\n" +
                "                                    <td rowspan=\"1\">\n" +
                "                                        <div class=\"status\">\n" +
                "                                         <span class=\"order-status ftx-03\">\n" +
                "            \t                            未支付\n" +
                "                                        </span>\n" +
                "                                            <br>\n" +
                "                                            <a href=\"#\" clstag=\"click|keycount|orderlist|dingdanxiangqing\" target=\"_blank\">订单详情</a>\n" +
                "                                        </div>\n" +
                "                                    </td>\n" +
                "                                    <td rowspan=\"1\" id=\"operate257427220634\">\n" +
                "                                        <div class=\"operate\">\n" +
                "                                            <div id=\"pay-button-257427220634\" _baina=\"0\"></div>\n" +
                "                                                <br/>\n" +
                "                                                <a href=\"#\" class=\"btn-again btn-again-show \" target=\"_blank\" clstag=\"click|keycount|orderlist|buy\"><b></b>取消订单</a>|\n" +
                "                                                <a href=\"#\" class=\"btn-again btn-again-show \" target=\"_blank\" clstag=\"click|keycount|orderlist|buy\"><b></b>立即付款</a>\n" +
                "                                            <br>\n" +
                "                                        </div>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "\n" +
                "                                </tbody>";
        }

        $(".non-pay-order-table").append(orderTableHtml);
    }
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
                drawNonPaymentOrderList(result.data);
                bindConsigneeDetailPageEvent();
            }
        },
        complete:function(data,status){
        }
    });
}