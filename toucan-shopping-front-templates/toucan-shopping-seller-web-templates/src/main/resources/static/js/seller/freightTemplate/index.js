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
            tableHtml+=     "                                &nbsp;<a attr-id=\""+row.id+"\" class=\"editRow\" style=\"color:blue;cursor: pointer;\">修改</a>\n" ;
            tableHtml+=     "                                &nbsp;<a attr-id=\""+row.id+"\" class=\"previewRow\" style=\"color:blue;cursor: pointer;\">查看</a>\n" ;
            tableHtml+=     "                                &nbsp;<a attr-id=\""+row.id+"\" attr-name=\""+row.name+"\" class=\"delwRow\" style=\"color:red;cursor: pointer;\">删除</a>\n" ;
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

    $(".previewRow").unbind("click");
    $(".previewRow").bind("click", function () {
        var attrId = $(this).attr("attr-id");
        window.location.href = basePath+"/page/freightTemplate/show/"+attrId;
    });


    $(".editRow").unbind("click");
    $(".editRow").bind("click", function () {
        var attrId = $(this).attr("attr-id");
        window.location.href = basePath+"/page/freightTemplate/edit/"+attrId;
    });

    $(".delwRow").unbind("click");
    $(".delwRow").bind("click", function () {
        var attrId = $(this).attr("attr-id");
        var name = $(this).attr("attr-name");
        layer.confirm('确定删除'+name+'?', {
            btn: ['确定','关闭'], //按钮
            title:'提示信息'
        }, function(index){
            pagegizationConfigObject.current_page=1;
            $.ajax({
                type: "POST",
                url: basePath+'/api/freightTemplate/delete/'+attrId,
                contentType: "application/json;charset=utf-8",
                data:  null,
                dataType: "json",
                success: function (data) {
                    if(data.code==1)
                    {
                        $("#queryBtn").click();
                    }else{
                        $.message({
                            message: data.msg,
                            time:4000,
                            type: 'error'
                        });
                    }
                    layer.close(index);
                },
                error: function (result) {
                    $("#queryBtn").click();
                }
            });
        }, function(){

        });
    });

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