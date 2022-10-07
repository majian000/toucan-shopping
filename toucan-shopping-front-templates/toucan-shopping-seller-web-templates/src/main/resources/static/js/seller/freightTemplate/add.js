
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
        "                                            <a data-row-id=\""+expressTablePos+"\" data-toggle=\"modal\" data-target=\"#expressSelectRegionModal\" style=\"color:blue;cursor: pointer;\">选择区域</a>\n" +
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
    $(".expressTableDelRow").unbind("click");
    $(".expressTableDelRow").on('click', function () {
        var attrId = $(this).attr("data-row-id");
        layer.confirm('确定删除?', {
            btn: ['确定','关闭'], //按钮
            title:'提示信息'
        }, function(index){
            $("#expressTable_row_"+attrId).remove();

            layer.close(index);
        });
    });
}

$(function () {

    loading.showLoading({
        type:1,
        tip:"加载中..."
    });

    $.get("/htmls/province/city/area",function (data) { //引入时的页面名称

        $("#pcaDiv").html(data);


        $("#ms_city").click(function (e) {
            SelCity(this,e);
        });
        $(".msc_l").click(function (e) {
            SelCity(document.getElementById("ms_city"),e);
        });

        //加载选择区域组件
        loading.hideLoading();

        //只保留省市节点,去掉区县节点
        var reginDatas = JSON.parse(JSON.stringify(province));
        for(var i=0;i<reginDatas.length;i++)
        {
            var reginData = reginDatas[i];
            if(reginData.isMunicipality==1)
            {
                reginData.children=null;
            }else{
                if(reginData.children!=null&&reginData.children.length>0)
                {
                    for(var j=0;j<reginData.children.length;j++)
                    {
                        reginData.children[j].children=null;
                    }
                }
            }
        }
        GetRegionPlug(reginDatas,"express-select-region");
        $(".expressSelectBtn").click(function() {
            var areas = GetChecked("express-select-region").join(",");
            $("#areas").html(areas);
            $("#selectedareas").val(areas);
            $('#expressSelectRegionModal').modal('hide');
        });


    });




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