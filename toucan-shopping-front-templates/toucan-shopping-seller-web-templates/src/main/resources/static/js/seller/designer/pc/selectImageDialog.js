/*容器2参数*/
var pagegizationConfigObject={
    obj_box:'.pageToolbar',//翻页容器
    total_item:1,//条目总数
    per_num:10,//每页条目数
    current_page:1//当前页
};
var g_image_query_obj={page:pagegizationConfigObject.current_page,limit:pagegizationConfigObject.per_num};
var g_selectImageDialogHandler;
var g_selectRowCallbackHandler=null;

/**
 * 打开选择图片对话框
 */
function openSelectImageDialog(comInstId,selectRowCallback)
{

    var imageTableHtml="  <div class=\"pageToolbar\" style='margin-top:2%'>\n" +
        "                    <table id=\"imageTable\" class=\"freezeTable\" border=\"1\" width=\"900\">\n" +
        "                        <tbody id=\"imageTableBody\">\n" +
        "                        </tbody>\n" +
        "                    </table>\n" +
        "                </div>";
    imageTableHtml+="<input type=\"hidden\" id=\"imageDialog_componentInstanceId\" value=\""+comInstId+"\" />";
    g_selectRowCallbackHandler = selectRowCallback;
    g_selectImageDialogHandler = layer.open({
        type: 1,
        title:"选择图片",
        area: ['55%', '60%'], //宽高
        content: imageTableHtml
    });
}






function initImagePagination()
{

    $(".pageToolbar").html("<table id=\"imageTable\" class=\"freezeTable\" border=\"1\" width=\"900\">\n" +
        "                        <tbody id=\"imageTableBody\">\n" +
        "                        </tbody>\n" +
        "                    </table>");

    loading.showLoading({
        type:6,
        tip:"查询中..."
    });

    $.ajax({
        type: "POST",
        url: basePath+"/api/designer/image/list",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify(g_image_query_obj),
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
                drawImageTable(result.data);
                page_ctrl(pagegizationConfigObject, doImagePage);
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


function doImagePage()
{
    loading.showLoading({
        type:6,
        tip:"查询中..."
    });
    g_image_query_obj.page = pagegizationConfigObject.current_page;
    $.ajax({
        type: "POST",
        url: basePath+"/api/designer/image/list",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify(g_image_query_obj),
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
                drawImageTable(result.data);
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

function drawImageTable(pageResult)
{
    var tableHtml="";
    tableHtml+=" <tr class=\"tabTh\">\n" +
        "                            <td style=\"width:50px;\" >序号</td>\n" +
        "                            <td style=\"width:100px;\" >标题</td>\n" +
        "                            <td style=\"width:100px;\" >预览</td>\n" +
        "                            <td style=\"width:200px;\">操作</td>\n" +
        "                        </tr>";
    if(pageResult!=null&&pageResult.list!=null&&pageResult.list.length>0)
    {
        for(var i=0;i<pageResult.list.length;i++)
        {
            var row = pageResult.list[i];
            var deliverArea="";


            tableHtml+=" <tr align=\"center\" class=\"tabTd\">\n" ;
            tableHtml+=   "                            <td><div class=\"tabTdWrap\">"+(i+1)+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.title+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\"><img style=\"width:100px;height:100px;\" src=\""+row.httpImgPath+"\"></div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">" ;
            tableHtml+=     "                                &nbsp;<a attr-id=\""+row.id+"\" attr-img-path=\""+row.httpImgPath+"\"  class=\"selectRow\" style=\"color:blue;cursor: pointer;\">选择</a>\n" ;
            tableHtml+=    "</div></td>\n" ;
            tableHtml+=    "                        </tr>";
        }

    }
    $("#imageTableBody").html(tableHtml);
    $("#imageTable").FrozenTable(2,0,0);

    bindRowEvent();
}

function bindRowEvent()
{

    $(".selectRow").unbind("click");
    $(".selectRow").bind("click", function () {
        if(g_selectRowCallbackHandler!=null)
        {
            var row={
                id:$(this).attr("attr-id"),
                httpImgPath:$(this).attr("attr-img-path")
            };
            g_selectRowCallbackHandler(row);
        }
        layer.close(g_selectImageDialogHandler);
    });

}
