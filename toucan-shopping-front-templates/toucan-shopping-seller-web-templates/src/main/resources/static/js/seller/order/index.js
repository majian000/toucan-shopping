/*容器2参数*/
var pagegizationConfigObject={
    obj_box:'.pageToolbar',//翻页容器
    total_item:1,//条目总数
    per_num:10,//每页条目数
    current_page:1//当前页
};
var g_order_query_obj={page:pagegizationConfigObject.current_page,limit:pagegizationConfigObject.per_num};
function doPage()
{
    loading.showLoading({
        type:6,
        tip:"查询中..."
    });
    g_order_query_obj.page = pagegizationConfigObject.current_page;
    $.ajax({
        type: "POST",
        url: basePath+"/api/order/list",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify(g_order_query_obj),
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
                pagegizationConfigObject.total_item = result.data.total;
                pagegizationConfigObject.current_page = result.data.page;
                drawTable(result.data);
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

function drawTable(pageResult)
{
    var tableHtml="";
    tableHtml+=" <tr class=\"tabTh\" style=\"height:50px;\">\n" +
        "                            <td style=\"width:50px;\" >序号</td>\n" +
        "                            <td style=\"width:150px;\" >订单编号</td>\n" +
        "                            <td style=\"width:150px;\" >支付流水号</td>\n" +
        "                            <td style=\"width:100px;padding-left: 2%;\"  class=\"sortColumns\" sortColumn=\"orderAmount\" >订单金额</td>\n" +
        "                            <td style=\"width:100px;padding-left: 2%;\"  class=\"sortColumns\" sortColumn=\"payAmount\" >付款金额</td>\n" +
        "                            <td style=\"width:100px;\" >交易状态</td>\n" +
        "                            <td style=\"width:100px;\" >支付状态</td>\n" +
        "                            <td style=\"width:100px;\" >下单时间</td>\n" +
        "                            <td style=\"width:100px;\">操作</td>\n" +
        "                        </tr>";


    if(pageResult!=null&&pageResult.list!=null&&pageResult.list.length>0)
    {
        for(var i=0;i<pageResult.list.length;i++)
        {
            var row = pageResult.list[i];
            var payStatusName="";
            var tradeStatusName="";
            if(row.payStatus==0){
                payStatusName="未支付";
            }else if(row.payStatus==1){
                payStatusName="已支付";
            }else if(row.payStatus==4){
                payStatusName="取消支付";
            }

            if(row.tradeStatus==0){
                tradeStatusName="待付款";
            }else if(row.tradeStatus==1){
                tradeStatusName="待收货";
            }else if(row.tradeStatus==2){
                tradeStatusName="已取消";
            }else if(row.tradeStatus==3){
                tradeStatusName="已完成";
            }else if(row.tradeStatus==4){
                tradeStatusName="待发货";
            }


            tableHtml+=" <tr align=\"center\" class=\"tabTd\">\n" ;
            tableHtml+=   "                            <td><div class=\"tabTdWrap\">"+(i+1)+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.orderNo+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+(row.outerTradeNo!=null?row.outerTradeNo:"")+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.orderAmount+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.payAmount+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+tradeStatusName+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+payStatusName+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.createDate+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">" ;
            tableHtml+=     "                                &nbsp;<a attr-id=\""+row.id+"\" class=\"previewRow\" style=\"color:blue;cursor: pointer;\">查看</a>\n" ;
            tableHtml+=    "</div></td>\n" ;
            tableHtml+=    "                        </tr>";
        }

    }
    $("#orderTableBody").html(tableHtml);
    drawSortColumn();
    $("#orderTable").FrozenTable(2,0,0);

    bindRowEvent();
}

function drawSortColumn(){
    //追加排序列
    var sortColumns = $(".sortColumns");
    if(sortColumns!=null&&sortColumns.length>0)
    {
        for(var i=0;i<sortColumns.length;i++){
            $(sortColumns[i]).append("<div class=\"tcc\"><div class=\"csi\"><a><i class=\"su\"></i><a style=\" margin-top: 5px;\"><i class=\"sd\"></i></div></div>");
        }
        //升序
        $(".csi .su").bind("click", function () {
            $("#sortColumn").val($(this).parents(".sortColumns").attr("sortColumn"));
            $("#sortBy").val("asc");
            doQuery();
        });
        //降序
        $(".csi .sd").bind("click", function () {
            $("#sortColumn").val($(this).parents(".sortColumns").attr("sortColumn"));
            $("#sortBy").val("desc");
            doQuery();
        });
    }
}

function bindRowEvent()
{

    $(".previewRow").unbind("click");
    $(".previewRow").bind("click", function () {
        var attrId = $(this).attr("attr-id");
        window.location.href = basePath+"/page/order/show/"+attrId;
    });

}




function initPagination()
{

    $(".pageToolbar").html("<table id=\"orderTable\" class=\"freezeTable\" border=\"1\" style=\"width:90%;\">\n" +
        "                        <tbody id=\"orderTableBody\">\n" +
        "                        </tbody>\n" +
        "                    </table>");

    loading.showLoading({
        type:6,
        tip:"查询中..."
    });

    $.ajax({
        type: "POST",
        url: basePath+"/api/order/list",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify(g_order_query_obj),
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
                pagegizationConfigObject.total_item = result.data.total;
                pagegizationConfigObject.current_page = result.data.page;
                drawTable(result.data);
                page_ctrl(pagegizationConfigObject, doPage);
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


function doQuery(){
    pagegizationConfigObject.current_page = 1;
    g_order_query_obj.page = 1;
    g_order_query_obj.orderNo=$("#orderNo").val();
    g_order_query_obj.outerTradeNo=$("#outerTradeNo").val();
    g_order_query_obj.tradeStatus=$("#tradeStatus option:selected").val();
    g_order_query_obj.payStatus=$("#payStatus option:selected").val();
    g_order_query_obj.startCreateDateYMDHS=$("#startDate").val();
    g_order_query_obj.endCreateDateYMDHS=$("#endDate").val();
    g_order_query_obj.sortColumn = $("#sortColumn").val();
    g_order_query_obj.sortBy = $("#sortBy").val();
    initPagination();
}

$(function () {


    $("#queryBtn").bind( 'click' ,function(){
        $("#sortColumn").val("");
        $("#sortBy").val("");
        doQuery();
    });


    $("#resetBtn").bind( 'click' ,function(){
        $("#orderForm").resetForm();
    });

    $('#startDate').datetimepicker({
        format:'Y-m-d H:i'
    });//初始化
    $('#endDate').datetimepicker({
        format:'Y-m-d H:i'
    });//初始化

    $.datetimepicker.setLocale('zh');//使用中文

    initPagination();
});