/*容器2参数*/
var pagegizationConfigObject={
    obj_box:'.pageToolbar',//翻页容器
    total_item:1,//条目总数
    per_num:10,//每页条目数
    current_page:1//当前页
};
var g_freightTemplate_query_obj={page:pagegizationConfigObject.current_page,limit:pagegizationConfigObject.per_num};
function doPage()
{
    loading.showLoading({
        type:6,
        tip:"查询中..."
    });
    g_freightTemplate_query_obj.page = pagegizationConfigObject.current_page;
    $.ajax({
        type: "POST",
        url: basePath+"/api/freightTemplate/list",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify(g_freightTemplate_query_obj),
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
        "                            <td style=\"width:100px;\" >名称</td>\n" +
        "                            <td style=\"width:100px;\" >发货地</td>\n" +
        "                            <td style=\"width:300px;\">运送到</td>\n" +
        "                            <td style=\"width:100px;\" >运送方式</td>\n" +
        "                            <td style=\"width:100px;\" >首件(个)</td>\n" +
        "                            <td style=\"width:100px;\" >运费(元)</td>\n" +
        "                            <td style=\"width:100px;\" >续件(个)</td>\n" +
        "                            <td style=\"width:100px;\" >运费(元)</td>\n" +
        "                            <td style=\"width:100px;\" >发布时间</td>\n" +
        "                            <td style=\"width:200px;\">操作</td>\n" +
        "                        </tr>";
    if(pageResult!=null&&pageResult.list!=null&&pageResult.list.length>0)
    {
        for(var i=0;i<pageResult.list.length;i++)
        {
            var row = pageResult.list[i];

        }

    }
    $("#freightTemplateTableBody").html(tableHtml);
    $("#freightTemplateTable").FrozenTable(2,0,0);
}




function initPagination()
{

    $(".pageToolbar").html("<table id=\"freightTemplateTable\" class=\"freezeTable\" border=\"1\" style=\"width:90%;\">\n" +
        "                        <tbody id=\"freightTemplateTableBody\">\n" +
        "                        </tbody>\n" +
        "                    </table>");

    loading.showLoading({
        type:6,
        tip:"查询中..."
    });

    $.ajax({
        type: "POST",
        url: basePath+"/api/freightTemplate/list",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify(g_freightTemplate_query_obj),
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
        g_freightTemplate_query_obj.page = 1;
        g_freightTemplate_query_obj.name=$("#name").val();
        g_freightTemplate_query_obj.startDateYMDHS=$("#startDate").val();
        g_freightTemplate_query_obj.endDateYMDHS=$("#endDate").val();

        initPagination();
    });

    $('#addFreightTemplate').on('click', function(){
        window.location.href=basePath+"/page/freightTemplate/add";
    });

    $("#resetBtn").bind( 'click' ,function(){
        $("#freightTemplateForm").resetForm();
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