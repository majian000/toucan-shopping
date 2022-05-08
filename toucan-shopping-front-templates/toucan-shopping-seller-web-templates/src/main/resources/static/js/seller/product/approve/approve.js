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
        "                            <td style=\"width:50px;\" >序号</td>\n" +
        "                            <td style=\"width:100px;\" >审核状态</td>\n" +
        "                            <td style=\"width:300px;\">商品名称</td>\n" +
        "                            <td style=\"width:150px;\" >商品分类</td>\n" +
        "                            <td style=\"width:100px;\" >发布时间</td>\n" +
        "                            <td style=\"width:200px;\">操作</td>\n" +
        "                        </tr>";
    if(pageResult!=null&&pageResult.list!=null&&pageResult.list.length>0)
    {
        for(var i=0;i<pageResult.list.length;i++)
        {
            var row = pageResult.list[i];
            tableHtml+=" <tr align=\"center\" class=\"tabTd\">\n" ;
            tableHtml+=   "                            <td><div class=\"tabTdWrap\">"+(i+1)+"</div></td>\n" ;
            if(row.approveStatus==1)
            {
                tableHtml+=    "                            <td><div class=\"tabTdWrap\">审核中</div></td>\n" ;
            }else if(row.approveStatus==2)
            {
                tableHtml+=    "                            <td><div class=\"tabTdWrap\"><a style=\"color:green;\">审核通过</a></div></td>\n" ;
            }else if(row.approveStatus==3)
            {
                tableHtml+=    "                            <td><div class=\"tabTdWrap\"><a style=\"color:red;\">审核驳回</a></div></td>\n" ;
            }
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.name+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.categoryName+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.createDate+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">\n" ;
            if(row.approveStatus==3)
            {
                tableHtml+=     "                                &nbsp;&nbsp;<a href=\""+basePath+"/page/shop/product/approve/rejected/"+row.id+"\" style=\"color:red\">驳回原因</a>\n" ;
                tableHtml+=     "                                &nbsp;&nbsp;\n" ;
                tableHtml+=     "                                <a  href=\""+basePath+"/page/shop/product/approve/republish/"+row.id+"\" style=\"color:blue\">重新发布</a>\n" ;
            }else if(row.approveStatus==1)
            {
                tableHtml+=     "                                &nbsp;&nbsp;<a  href=\""+basePath+"/page/shop/product/approve/republish/"+row.id+"\" style=\"color:blue\">重新编辑</a>\n" ;
            }
            tableHtml+=     "                                &nbsp;&nbsp;<a href=\"\" style=\"color:blue\">商品预览</a>\n" ;
            tableHtml+=    "                            </div></td>\n" ;
            tableHtml+=    "                        </tr>";
        }

    }
    $("#productApproveTableBody").html(tableHtml);
    $("#productApproveTable").FrozenTable(2,0,0);
}

function initPagination()
{

    $(".pageToolbar").html("<table id=\"productApproveTable\" class=\"freezeTable\" border=\"1\" width=\"900\">\n" +
        "                        <tbody id=\"productApproveTableBody\">\n" +
        "                        </tbody>\n" +
        "                    </table>");

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
        pagegizationConfigObject.current_page = 1;
        g_product_approve_query_obj.page = 1;
        g_product_approve_query_obj.name=$("#name").val();
        g_product_approve_query_obj.startDateYMDHS=$("#startDate").val();
        g_product_approve_query_obj.endDateYMDHS=$("#endDate").val();
        g_product_approve_query_obj.approveStatus=$("#approveStatus option:selected").val();

        initPagination();
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