/*容器2参数*/
var pagegizationConfigObject={
    obj_box:'.pageToolbar',//翻页容器
    total_item:1,//条目总数
    per_num:20,//每页条目数
    current_page:1//当前页
};
var g_product_approve_query_obj={page:pagegizationConfigObject.current_page,limit:pagegizationConfigObject.per_num};
function doPage()
{
    loading.showLoading({
        type:6,
        tip:"查询中..."
    });
    g_product_approve_query_obj.page = pagegizationConfigObject.current_page;
    $.ajax({
        type: "POST",
        url: basePath+"/api/shop/product/approve/list",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify(g_product_approve_query_obj),
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
    tableHtml+=" <tr class=\"tabTh\">\n" +
        "                            <td >序号</td>\n" +
        "                            <td >审核状态</td>\n" +
        "                            <td >商品名称</td>\n" +
        "                            <td >商品分类</td>\n" +
        "                            <td >发布时间</td>\n" +
        "                            <td >操作</td>\n" +
        "                        </tr>";
    if(pageResult!=null&&pageResult.list!=null&&pageResult.list.length>0)
    {
        for(var i=0;i<pageResult.list.length;i++)
        {
            var row = pageResult.list[i];
            tableHtml+=" <tr align=\"center\" class=\"tabTd\">\n" ;
            tableHtml+=   "                            <td>"+(i+1)+"</td>\n" ;
            if(row.approveStatus==1)
            {
                tableHtml+=    "                            <td>审核中</td>\n" ;
            }else if(row.approveStatus==2)
            {
                tableHtml+=    "                            <td><a style=\"color:green;\">审核通过</a></td>\n" ;
            }else if(row.approveStatus==3)
            {
                tableHtml+=    "                            <td><a style=\"color:red;\">审核驳回</a></td>\n" ;
            }
            tableHtml+=    "                            <td>"+row.name+"</td>\n" ;
            tableHtml+=    "                            <td>"+row.categoryName+"</td>\n" ;
            tableHtml+=    "                            <td>"+row.createDate+"</td>\n" ;
            tableHtml+=    "                            <td>\n" ;
            if(row.approveStatus==3)
            {
                tableHtml+=     "                                <a href=\"#\" style=\"color:red\">查看驳回原因</a>\n" ;
                tableHtml+=     "                                &nbsp;&nbsp;\n" ;
                tableHtml+=     "                                <a href=\"#\" style=\"color:blue\">重新发布</a>\n" ;
            }
            tableHtml+=    "                            </td>\n" ;
            tableHtml+=    "                        </tr>";
        }

    }
    $("#productApproveTableBody").html(tableHtml);
    $("#productApproveTable").FrozenTable(2,0,0);
}

function initPagination()
{

    loading.showLoading({
        type:6,
        tip:"查询中..."
    });

    $.ajax({
        type: "POST",
        url: basePath+"/api/shop/product/approve/list",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify(g_product_approve_query_obj),
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

$(function () {


    $("#queryBtn").bind( 'click' ,function(){

    });


    $("#resetBtn").bind( 'click' ,function(){
        $("#productApproveForm").resetForm();
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