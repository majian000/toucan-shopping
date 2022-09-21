/*容器2参数*/
var pagegizationConfigObject={
    obj_box:'.pageToolbar',//翻页容器
    total_item:1,//条目总数
    per_num:10,//每页条目数
    current_page:1//当前页
};
var g_fireghtTemplate_query_obj={page:pagegizationConfigObject.current_page,limit:pagegizationConfigObject.per_num};
function doPage()
{
    loading.showLoading({
        type:6,
        tip:"查询中..."
    });
    g_fireghtTemplate_query_obj.page = pagegizationConfigObject.current_page;
    $.ajax({
        type: "POST",
        url: basePath+"/api/shop/fireghtTemplate/list",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify(g_fireghtTemplate_query_obj),
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
        "                            <td style=\"width:100px;\" >商品预览</td>\n" +
        "                            <td style=\"width:300px;\">商品名称</td>\n" +
        "                            <td style=\"width:75px;\" >商品分类</td>\n" +
        "                            <td style=\"width:75px;\" >上架状态</td>\n" +
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
            tableHtml+=    "                            <td><div class=\"tabTdWrap\"><img src=\""+row.httpMainPhotoFilePath+"\" style=\"width:65px;height:65px; margin-top: 4%; margin-bottom: 4%;\"></div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.name+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.categoryName+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+(row.status==1?"<a style='color:green'>已上架</a>":"<a style='color:red'>已下架</a>")+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.createDate+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">\n" ;
            tableHtml+=     "                                &nbsp;<a attr-id=\""+row.id+"\" class=\"previewRow\" style=\"color:blue;cursor: pointer;\">商品预览</a>\n" ;
            if(row.status==1)
            {
                tableHtml+=     "                                &nbsp;<a attr-id=\""+row.id+"\" attr-status=\""+row.status+"\" class='shelvesBtn' style=\"color:red;cursor: pointer;\">下架</a>\n" ;
            }else{
                tableHtml+=     "                                &nbsp;<a attr-id=\""+row.id+"\" attr-status=\""+row.status+"\" class='shelvesBtn' style=\"color:blue;cursor: pointer;\">上架</a>\n" ;
            }
            tableHtml+=     "                                &nbsp;&nbsp;\n" ;
            tableHtml+=    "                            </div></td>\n" ;
            tableHtml+=    "                        </tr>";
        }

    }
    $("#fireghtTemplateTableBody").html(tableHtml);
    $("#fireghtTemplateTable").FrozenTable(2,0,0);
    bindPreviewEvent();
}


function bindPreviewEvent()
{
    $(".previewRow").unbind("click");
    //SKU信息
    $(".previewRow").bind("click", function () {
        var attrId = $(this).attr("attr-id");

        window.open(shoppingPcPath+fireghtTemplateDetailPage+attrId);
    });

    $(".shelvesBtn").unbind("click");
    $(".shelvesBtn").bind("click", function () {
        var attrId = $(this).attr("attr-id");
        var attrStatus = $(this).attr("attr-status");
        var optText="";
        if(attrStatus=="1")
        {
            optText="下架";
        }else{
            optText="上架";
        }
        layer.confirm("确定要"+optText+"?", {
            btn: ['确定','关闭'], //按钮
            title:'提示信息'
        }, function(index) {


            $.ajax({
                type: "POST",
                url: basePath+"/api/shop/fireghtTemplate/shelves",
                contentType: "application/json;charset=utf-8",
                data:  JSON.stringify({id:attrId}),
                dataType: "json",
                success: function (result) {
                    if(result.code<=0)
                    {
                        $.message({
                            message: "操作失败,请稍后重试",
                            type: 'error'
                        });
                        return ;
                    }

                    $.message({
                        message: "操作成功",
                        type: 'success'
                    });
                },
                error: function (result) {
                    $.message({
                        message: "操作失败,请稍后重试",
                        type: 'error'
                    });
                },
                complete:function()
                {
                    layer.close(index);
                    $("#queryBtn").click();
                }

            });


        });
    });

}


function initPagination()
{

    $(".pageToolbar").html("<table id=\"fireghtTemplateTable\" class=\"freezeTable\" border=\"1\" style=\"width:90%;\">\n" +
        "                        <tbody id=\"fireghtTemplateTableBody\">\n" +
        "                        </tbody>\n" +
        "                    </table>");

    loading.showLoading({
        type:6,
        tip:"查询中..."
    });

    $.ajax({
        type: "POST",
        url: basePath+"/api/shop/fireghtTemplate/list",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify(g_fireghtTemplate_query_obj),
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
        g_fireghtTemplate_query_obj.page = 1;
        g_fireghtTemplate_query_obj.name=$("#name").val();
        g_fireghtTemplate_query_obj.startDateYMDHS=$("#startDate").val();
        g_fireghtTemplate_query_obj.endDateYMDHS=$("#endDate").val();

        initPagination();
    });


    $("#resetBtn").bind( 'click' ,function(){
        $("#fireghtTemplateForm").resetForm();
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