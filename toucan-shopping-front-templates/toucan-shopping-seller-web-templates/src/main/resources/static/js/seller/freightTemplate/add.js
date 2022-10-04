
var expressTablePos=0;

function scafbtn_click()
{


    if(!checkInputFunction($('#saveBtn'),2)){
        return false;
    }

    var freightTemplateName=$("#freightTemplateName").val();

    if(freightTemplateName.length<2||freightTemplateName.length>20)
    {
        $.message({
            message: "模板名称长度只能在2-20位字符之间",
            type: 'error'
        });
        return;
    }


    $.ajax({
        type: "POST",
        url: basePath+'/api/freightTemplate/save',
        contentType: "application/json;charset=utf-8",
        data:  getAjaxFormData("#scaf"),
        dataType: "json",
        success: function (data) {
            if(data.code==1)
            {
                window.location.href=basePath+"/page/freightTemplate/list";
            }else{
                $.message({
                    message: data.msg,
                    type: 'error'
                });
            }
        },
        error: function (result) {
            $.message({
                message: "请稍后重试",
                type: 'error'
            });
        }
    });



}


function resetexpressTable()
{
    $("#expressDefaultWeight").val("");
    $("#expressDefaultWeightMoney").val("");
    $("#expressDefaultAppendWeight").val("");
    $("#expressDefaultAppendWeightMoney").val("");
    $("#expressTableBody").html("<tr class=\"tabTh\">\n" +
        "                                        <td style=\"text-align: center;\">运送到</td>\n" +
        "                                        <td style=\"text-align:center\">首重量(kg)</td>\n" +
        "                                        <td style=\"text-align:center\">首费(元)</td>\n" +
        "                                        <td style=\"text-align:center\">续重量(kg)</td>\n" +
        "                                        <td style=\"text-align:center\">续费(元)</td>\n" +
        "                                        <td style=\"text-align:center\">操作</td>\n" +
        "                                    </tr>");


    $("#expressTableBody").append("<tr id=\"expressTable_row_"+expressTablePos+"\">\n" +
        "                                        <td style=\"text-align:center\"><div id=\"expressTable_row_"+expressTablePos+"_areas\" class=\"form-control-static\">\n" +
        "                                </div><input type=\"hidden\" id=\"expressTable_row_"+expressTablePos+"_areas\" name=\"expressAreaRules["+expressTablePos+"].selectAreas\" value=\"\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" name=\"expressAreaRules["+expressTablePos+"].firstWeight\" style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" name=\"expressAreaRules["+expressTablePos+"].firstWeightMoney\" style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" name=\"expressAreaRules["+expressTablePos+"].appendWeight\" style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" name=\"expressAreaRules["+expressTablePos+"].appendWeightMoney\" style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\">\n" +
        "                                            <a data-row-id=\""+expressTablePos+"\" style=\"color:blue;cursor: pointer;\">选择区域</a>\n" +
        "                                            &nbsp;\n" +
        "                                            <a data-row-id=\""+expressTablePos+"\" class='expressTableDelRow' style=\"color:red;cursor: pointer;\">删除</a>\n" +
        "                                        </td>\n" +
        "                                    </tr>");


    bindExpressTableDelRowEvent();

}

/**
 * 快递模板添加一行
 */
function expressTableAddRowEvent()
{
    expressTablePos++;
    $("#expressTableBody").append("<tr id=\"expressTable_row_"+expressTablePos+"\">\n" +
        "                                        <td style=\"text-align:center\"><div id=\"expressTable_row_"+expressTablePos+"_areas\" class=\"form-control-static\">\n" +
        "                                </div><input type=\"hidden\" id=\"expressTable_row_"+expressTablePos+"_areas\" name=\"expressAreaRules["+expressTablePos+"].selectAreas\" value=\"\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" name=\"expressAreaRules["+expressTablePos+"].firstWeight\" style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" name=\"expressAreaRules["+expressTablePos+"].firstWeightMoney\" style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" name=\"expressAreaRules["+expressTablePos+"].appendWeight\" style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" name=\"expressAreaRules["+expressTablePos+"].appendWeightMoney\" style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\">\n" +
        "                                            <a data-row-id=\""+expressTablePos+"\" style=\"color:blue;cursor: pointer;\">选择区域</a>\n" +
        "                                            &nbsp;\n" +
        "                                            <a data-row-id=\""+expressTablePos+"\" class='expressTableDelRow' style=\"color:red;cursor: pointer;\">删除</a>\n" +
        "                                        </td>\n" +
        "                                    </tr>");

    bindExpressTableDelRowEvent();
}

/**
 * 快递模板删除行
 */
function bindExpressTableDelRowEvent()
{
    $(".expressTableDelRow").on('click', function () {
        var attrId = $(this).attr("data-row-id");
        layer.confirm('确定删除?', {
            btn: ['确定','关闭'], //按钮
            title:'提示信息'
        }, function(index){
            alert($("expressTable_row_"+attrId));
            $("expressTable_row_"+attrId).remove();
            layer.close(index);
        });
    });
}

$(function () {

    $('#saveBtn').on('click', function () {
        scafbtn_click();
    });


    $('#backBtn').on('click', function () {
        window.location.href=basePath+"/page/freightTemplate/index";
    });

    $('#transportModel_express').on('click', function () {
        if ($(this).prop("checked")) {
            $("#expressTableDiv").show();
        } else {
            $("#expressTableDiv").hide();
            resetexpressTable();
        }
    });

    $('.freightStatus').on('click', function () {
        var freightStatusVal = $("input[name='freightStatus']:checked").val();
        if(freightStatusVal=="1")
        {
            $(".expressDiv").show();
        }else{
            $(".expressDiv").hide();
        }
    });

    $('.valuationMethod').on('click', function () {
        var valuationMethodVal = $("input[name='valuationMethod']:checked").val();
        if(valuationMethodVal=="1")
        {
            $(".dynamicUnitDefaultText").html("件内");
            $(".dynamicUnitDefaultAppendText").html("件");
            $(".dynamicUnitRowFirstText").html("首件数(件)");
            $(".dynamicUnitRowAppendText").html("续件数(件)");
        }else if(valuationMethodVal=="2")
        {
            $(".dynamicUnitDefaultText").html("kg内");
            $(".dynamicUnitDefaultAppendText").html("kg");
            $(".dynamicUnitRowFirstText").html("首重量(kg)");
            $(".dynamicUnitRowAppendText").html("续重量(kg)");
        }else if(valuationMethodVal=="3")
        {
            $(".dynamicUnitDefaultText").html("m³内");
            $(".dynamicUnitDefaultAppendText").html("m³");
            $(".dynamicUnitRowFirstText").html("首体积(m³)");
            $(".dynamicUnitRowAppendText").html("续体积(m³)");
        }
    });

    $('.expressTableAddRow').on('click', function () {
        expressTableAddRowEvent();
    });




});