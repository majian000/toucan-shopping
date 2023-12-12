/*容器2参数*/
var pagegizationConfigObject={
    obj_box:'.pageToolbar',//翻页容器
    total_item:1,//条目总数
    per_num:10,//每页条目数
    current_page:1//当前页
};
var g_freightTemplate_query_obj={page:pagegizationConfigObject.current_page,limit:pagegizationConfigObject.per_num};


/**
 * 打开选择运费模板对话框
 */
function openSelectFreightTemplateDialog()
{

    var freightTemplateTableHtml="  <div class=\"pageToolbar\" style='margin-top:2%'>\n" +
        "                    <table id=\"freightTemplateTable\" class=\"freezeTable\" border=\"1\" width=\"900\">\n" +
        "                        <tbody id=\"freightTemplateTableBody\">\n" +
        "                        </tbody>\n" +
        "                    </table>\n" +
        "                </div>";

    g_sleectConsigneeAddressDialogHandler = layer.open({
        type: 1,
        title:"选择运费模板",
        area: ['55%', '60%'], //宽高
        content: freightTemplateTableHtml
    });
}






function initFreightTemplatePagination()
{

    $(".pageToolbar").html("<table id=\"freightTemplateTable\" class=\"freezeTable\" border=\"1\" width=\"900\">\n" +
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
                drawFreightTemplateTable(result.data);
                page_ctrl(pagegizationConfigObject, doFreightTemplatePage);
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


function doFreightTemplatePage()
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
                drawFreightTemplateTable(result.data);
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

function drawFreightTemplateTable(pageResult)
{
    var tableHtml="";
    tableHtml+=" <tr class=\"tabTh\">\n" +
        "                            <td style=\"width:50px;\" >序号</td>\n" +
        "                            <td style=\"width:100px;\" >名称</td>\n" +
        "                            <td style=\"width:100px;\" >发货地</td>\n" +
        "                            <td style=\"width:100px;\" >是否包邮</td>\n" +
        "                            <td style=\"width:100px;\" >运送方式</td>\n" +
        "                            <td style=\"width:100px;\" >计价方式</td>\n" +
        "                            <td style=\"width:100px;\" >发布时间</td>\n" +
        "                            <td style=\"width:200px;\">操作</td>\n" +
        "                        </tr>";
    if(pageResult!=null&&pageResult.list!=null&&pageResult.list.length>0)
    {
        for(var i=0;i<pageResult.list.length;i++)
        {
            var row = pageResult.list[i];
            var deliverArea="";
            if(row.deliverProvinceName!=null&&row.deliverProvinceName!="")
            {
                deliverArea+=row.deliverProvinceName;
            }
            if(row.deliverCityName!=null&&row.deliverCityName!="")
            {
                deliverArea+="/"+row.deliverCityName;
            }
            if(row.deliverAreaName!=null&&row.deliverAreaName!="")
            {
                deliverArea+="/"+row.deliverAreaName;
            }
            var transportModelName=row.transportModel;
            if(transportModelName==null) {
                transportModelName="";
            }
            transportModelName = transportModelName.replace('1', '快递');
            transportModelName = transportModelName.replace('2', 'EMS');
            transportModelName = transportModelName.replace('3', '平邮');
            transportModelName = transportModelName.replaceAll(",", "/");

            var valuationMethodName=row.valuationMethod;
            if(valuationMethodName==null)
            {
                valuationMethodName="";
            }
            valuationMethodName+="";

            valuationMethodName = valuationMethodName.replace('1', '按件数');
            valuationMethodName = valuationMethodName.replace('2', '按重量');
            valuationMethodName = valuationMethodName.replaceAll(",", "/");


            var freightStatusName="";
            if(row.freightStatus!=null&&row.freightStatus=="1")
            {
                freightStatusName="自定义运费";
            }else if(row.freightStatus!=null&&row.freightStatus=="2")
            {
                freightStatusName="包邮";
                valuationMethodName="";
            }


            tableHtml+=" <tr align=\"center\" class=\"tabTd\">\n" ;
            tableHtml+=   "                            <td><div class=\"tabTdWrap\">"+(i+1)+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.name+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+deliverArea+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+freightStatusName+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+transportModelName+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+valuationMethodName+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.createDate+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">" ;
            tableHtml+=     "                                &nbsp;<a attr-id=\""+row.id+"\" attr-name='"+row.name+"' class=\"selectRow\" style=\"color:blue;cursor: pointer;\">选择</a>\n" ;
            tableHtml+=    "</div></td>\n" ;
            tableHtml+=    "                        </tr>";
        }

    }
    $("#freightTemplateTableBody").html(tableHtml);
    $("#freightTemplateTable").FrozenTable(2,0,0);

    bindRowEvent();
}

function bindRowEvent()
{

    $(".selectRow").unbind("click");
    $(".selectRow").bind("click", function () {
        var attrId = $(this).attr("attr-id");
        var attrName = $(this).attr("attr-name");
        $("#freightTemplateIdHidden").val(attrId);
        $("#selectFreightTemplate").val(attrName);
        layer.close(g_sleectConsigneeAddressDialogHandler);
    });

}
