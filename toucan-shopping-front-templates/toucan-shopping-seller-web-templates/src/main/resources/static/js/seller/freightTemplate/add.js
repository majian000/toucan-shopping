
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

    $("#expressTableBody").append("<tr id=\"expressTable_toolbar\">\n" +
        "                                        <td style=\"text-align:center;background-color: #e8f2ff;\" colspan=\"6\">\n" +
        "                                            <a style=\"color:blue;cursor: pointer;\" >添加一行</a>\n" +
        "                                        </td>\n" +
        "                                    </tr>");

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

    $("#expressTable").FrozenTable(1,0,0);

});