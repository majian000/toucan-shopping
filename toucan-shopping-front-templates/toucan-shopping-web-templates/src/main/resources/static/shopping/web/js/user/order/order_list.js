

var g_om_cpage=1;
var g_query_order_type=-1;
var g_query_keyword="";

$(function () {
    loadListTable(1);
    bindTabEvent();
});



function drawOrderList(cpage,data)
{
    if(data.total>0)
    {
        $(".order-list-tips").hide();
        $(".order-list-table").show();
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
                "                                    <tr class=\"tr-bd\" >\n" +
                "                                    <td>\n" ;
            if(order.orderItems!=null&&order.orderItems.length>0)
            {
                for(var j=0;j<order.orderItems.length;j++) {
                    var orderItem =order.orderItems[j];

                    orderTableHtml+="                                        <div class=\"goods-item \">\n" +
                        "                                            <div class=\"p-img\">\n" +
                        "                                                <a href=\""+basePath+productSkuBasePath+orderItem.skuId+"\" target=\"_blank\">\n" +
                        "                                                    <img class=\"\" src=\""+orderItem.httpProductPreviewPath+"\" title=\""+orderItem.productSkuName+"\" data-lazy-img=\"done\" width=\"60\" height=\"60\">\n" +
                        "                                                </a>\n" +
                        "                                            </div>\n" +
                        "                                            <div class=\"p-msg\">\n" +
                        "                                                <div class=\"p-name\">\n" +
                        "                                                    <a href=\""+basePath+productSkuBasePath+orderItem.skuId+"\" class=\"a-link\"  target=\"_blank\" title=\""+orderItem.productSkuName+"\">"+orderItem.productSkuName+"</a>\n" +
                        "                                                </div>\n" +
                        "                                                <div class=\"p-extra\">\n" +
                        "                                                </div>\n" +
                        "\n" +
                        "                                            </div>\n" +
                        "                                        </div>\n" +
                        "                                        <div class=\"goods-number\">\n" +
                        "                                            x"+orderItem.productNum+"\n" +
                        "                                        </div>\n" +
                        "\n" +
                        "\n" +
                        "                                        <div class=\"goods-repair\">\n" +
                        "                                        </div>\n" +
                        "                                        <div class=\"clr\"></div>\n" ;
                }
            }
            orderTableHtml+= "                                    </td>\n" +
                "\n" +
                "                                    <td rowspan=\"1\">\n" +
                "                                        <div class=\"consignee tooltip\">\n" +
                "                                            <span class=\"txt\" attr-index=\""+consignee.id+"\">"+consignee.name+"</span><b></b>\n" +
                "                                            <div class=\"prompt-01 prompt-02 consignee-detail-"+consignee.id+"\" style=\"display: none;\">\n" +
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
                "                                            <a href=\""+basePath+orderDetailPath+"?docNo="+order.orderNo+"\"  target=\"_blank\">订单详情</a>\n" +
                "                                        </div>\n" +
                "                                    </td>\n" +
                "                                    <td rowspan=\"1\" >\n" +
                "                                        <div class=\"operate\">\n" +
                "                                            <div ></div>\n" +
                "                                                <br/>\n" +
                "                                                <a href=\"#\" class=\"btn-again btn-again-show \" target=\"_blank\" ><b></b>取消订单</a>|\n" +
                "                                                <a href=\""+basePath+orderPayPath+"?orderNo="+order.orderNo+"\" class=\"btn-again btn-again-show \" target=\"_blank\" ><b></b>立即付款</a>\n" +
                "                                            <br>\n" +
                "                                        </div>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "\n" +
                "                                </tbody>";
        }

        $(".order-list-table").empty();
        $(".order-list-table").append(orderTableHtml);

        drawPagination(cpage,data);

    }else{
        $(".order-list-tips").show();
        $(".order-list-table").hide();
        $(".pagination").empty();
    }
}

/**
 * 渲染分页工具条
 * @param cpage
 * @param data
 */
function drawPagination(cpage,data)
{

    var totalPage = 1;
    var total = data.total;

    if (data.total % data.size == 0) {
        totalPage = data.total / data.size;
    } else {
        totalPage = data.total / data.size;
        totalPage = parseInt(totalPage);
        totalPage += 1;
    }

    $(".pagination").empty();
    new pagination({
        pagination: $('.pagination'),
        maxPage: 7, //最大页码数,支持奇数，左右对称
        startPage: 1,    //默认第一页
        currentPage: cpage,          //当前页码
        totalItemCount: total,    //项目总数,大于0，显示页码总数
        totalPageCount: totalPage,        //总页数
        callback: function (pageNum) {
            if (g_om_cpage != pageNum) {
                loadListTable(pageNum);
            }
        }
    });
}


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
 * 加载订单页
 */
function loadListTable(cpage)
{

    loading.showLoading({
        type:1,
        tip:"查询中..."
    });

    var queryParams = {page:cpage,limit:5};
    if(g_query_order_type!=-1)
    {
        queryParams.tradeStatus=g_query_order_type;
    }
    if(g_query_keyword!="")
    {
        queryParams.keyword = g_query_keyword;
    }
    g_om_cpage = cpage;
    $.ajax({
        type: "POST",
        url: basePath+"/api/order/query/order/list",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(queryParams),
        dataType: "json",
        success: function (result) {
            if(result.code!=0)
            {
                drawOrderList(cpage,result.data);
                bindConsigneeDetailPageEvent();
                loading.hideLoading();
            }
        },
        complete:function(data,status){
            loading.hideLoading();
        }
    });
}

/**
 * 绑定选项卡事件
 */
function bindTabEvent()
{
    $("#orderAll").click(function(){
        g_query_order_type=-1;
        loadListTable(1);
        $(".order_tbs_li").removeClass("fore1");
        $(".order_tbs_a").removeClass("curr");
        $(this).parent().addClass("fore1");
        $(this).addClass("curr");
    });

    $("#ordertoPay").click(function(){
        g_query_order_type=0;
        loadListTable(1);
        $(".order_tbs_li").removeClass("fore1");
        $(".order_tbs_a").removeClass("curr");
        $(this).parent().addClass("fore1");
        $(this).addClass("curr");
    });

    $("#ordertoReceive").click(function(){
        g_query_order_type=1;
        loadListTable(1);
        $(".order_tbs_li").removeClass("fore1");
        $(".order_tbs_a").removeClass("curr");
        $(this).parent().addClass("fore1");
        $(this).addClass("curr");
    });

    $("#orderCompleted").click(function(){
        g_query_order_type=3;
        loadListTable(1);
        $(".order_tbs_li").removeClass("fore1");
        $(".order_tbs_a").removeClass("curr");
        $(this).parent().addClass("fore1");
        $(this).addClass("curr");
    });

    $("#orderCancel").click(function(){
        g_query_order_type=2;
        loadListTable(1);
        $(".order_tbs_li").removeClass("fore1");
        $(".order_tbs_a").removeClass("curr");
        $(this).parent().addClass("fore1");
        $(this).addClass("curr");
    });

    $(".order-search-btn").click(function(){
        g_query_keyword=$("#ip_keyword").val();
        loadListTable(1);
    });

    $("#ordertoRecycle").click(function(){
    });
}