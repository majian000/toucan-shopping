
//快递模板
var expressTablePos=0;
var expressRegionTableMap = new Map();

//EMS模板
var emsTablePos=0;
var emsRegionTableMap = new Map();



//平邮模板
var ordinaryMailTablePos=0;
var ordinaryMailRegionTableMap = new Map();





//========================================快递表格==================================================================

//重置快递模板表
function resetexpressTable()
{
    $("#expressDefaultWeight").val("");
    $("#expressDefaultWeight").removeAttr("lay-verify");
    $("#expressDefaultWeightMoney").val("");
    $("#expressDefaultWeightMoney").removeAttr("lay-verify");
    $("#expressDefaultAppendWeight").val("");
    $("#expressDefaultAppendWeight").removeAttr("lay-verify");
    $("#expressDefaultAppendWeightMoney").val("");
    $("#expressDefaultAppendWeightMoney").removeAttr("lay-verify");
    $("#expressTableBody").html("<tr class=\"tabTh\">\n" +
        "                                        <td style=\"text-align: center;\">运送到</td>\n" +
        "                                        <td style=\"text-align:center\">首重量(kg)</td>\n" +
        "                                        <td style=\"text-align:center\">首费(元)</td>\n" +
        "                                        <td style=\"text-align:center\">续重量(kg)</td>\n" +
        "                                        <td style=\"text-align:center\">续费(元)</td>\n" +
        "                                    </tr>");




    var checkeds = $(".express-select-region").find("input:checked");
    if(checkeds!=null&&checkeds.length>0)
    {
        for(var i=0;i<checkeds.length;i++)
        {
            $(checkeds[i]).prop("disabled", false);
            $(checkeds[i]).prop("checked", false);
        }
    }

    expressRegionTableMap = new Map();

}

/**
 * 快递模板添加一行
 */
function expressTableAddRowEvent()
{
    $("#expressTableBody").append("<tr id=\"expressTable_row_"+expressTablePos+"\">\n" +
        "                                        <td style=\"text-align:center\"><div id=\"expressTable_row_"+expressTablePos+"_areas\" class=\"form-control-static\">\n" +
        "                                </div><input type=\"hidden\" id=\"expressTable_row_"+expressTablePos+"_areas_hidden\" class=\"expressTableAreas\"  value=\"\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" id=\"expressTable_row_"+expressTablePos+"_firstWeight\"  style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" id=\"expressTable_row_"+expressTablePos+"_firstWeightMoney\"  style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" id=\"expressTable_row_"+expressTablePos+"_appendWeight\"  style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" id=\"expressTable_row_"+expressTablePos+"_appendWeightMoney\"  style=\"width:60px;\"></td>\n" +
        "                                    </tr>");

    expressTablePos++;
}




//======================================================================================================================




//========================================EMS表格==================================================================

//重置EMS模板表
function resetEmsTable()
{
    $("#emsDefaultWeight").val("");
    $("#emsDefaultWeight").removeAttr("lay-verify");
    $("#emsDefaultWeightMoney").val("");
    $("#emsDefaultWeightMoney").removeAttr("lay-verify");
    $("#emsDefaultAppendWeight").val("");
    $("#emsDefaultAppendWeight").removeAttr("lay-verify");
    $("#emsDefaultAppendWeightMoney").val("");
    $("#emsDefaultAppendWeightMoney").removeAttr("lay-verify");
    $("#emsTableBody").html("<tr class=\"tabTh\">\n" +
        "                                        <td style=\"text-align: center;\">运送到</td>\n" +
        "                                        <td style=\"text-align:center\">首重量(kg)</td>\n" +
        "                                        <td style=\"text-align:center\">首费(元)</td>\n" +
        "                                        <td style=\"text-align:center\">续重量(kg)</td>\n" +
        "                                        <td style=\"text-align:center\">续费(元)</td>\n" +
        "                                    </tr>");





    var checkeds = $(".ems-select-region").find("input:checked");
    if(checkeds!=null&&checkeds.length>0)
    {
        for(var i=0;i<checkeds.length;i++)
        {
            $(checkeds[i]).prop("disabled", false);
            $(checkeds[i]).prop("checked", false);
        }
    }

    emsRegionTableMap = new Map();

}


/**
 * EMS模板添加一行
 */
function emsTableAddRowEvent()
{
    $("#emsTableBody").append("<tr id=\"emsTable_row_"+emsTablePos+"\">\n" +
        "                                        <td style=\"text-align:center\"><div id=\"emsTable_row_"+emsTablePos+"_areas\" class=\"form-control-static\">\n" +
        "                                </div><input type=\"hidden\" id=\"emsTable_row_"+emsTablePos+"_areas_hidden\" class=\"emsTableAreas\"  value=\"\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" id=\"emsTable_row_"+emsTablePos+"_firstWeight\" style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" id=\"emsTable_row_"+emsTablePos+"_firstWeightMoney\"  style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" id=\"emsTable_row_"+emsTablePos+"_appendWeight\"  style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" id=\"emsTable_row_"+emsTablePos+"_appendWeightMoney\" style=\"width:60px;\"></td>\n" +
        "                                    </tr>");
    emsTablePos++;

}





//======================================================================================================================




//========================================平邮表格==================================================================

//重置平邮模板表
function resetordinaryMailTable()
{
    $("#ordinaryMailDefaultWeight").val("");
    $("#ordinaryMailDefaultWeight").removeAttr("lay-verify");
    $("#ordinaryMailDefaultWeightMoney").val("");
    $("#ordinaryMailDefaultWeightMoney").removeAttr("lay-verify");
    $("#ordinaryMailDefaultAppendWeight").val("");
    $("#ordinaryMailDefaultAppendWeight").removeAttr("lay-verify");
    $("#ordinaryMailDefaultAppendWeightMoney").val("");
    $("#ordinaryMailDefaultAppendWeightMoney").removeAttr("lay-verify");
    $("#ordinaryMailTableBody").html("<tr class=\"tabTh\">\n" +
        "                                        <td style=\"text-align: center;\">运送到</td>\n" +
        "                                        <td style=\"text-align:center\">首重量(kg)</td>\n" +
        "                                        <td style=\"text-align:center\">首费(元)</td>\n" +
        "                                        <td style=\"text-align:center\">续重量(kg)</td>\n" +
        "                                        <td style=\"text-align:center\">续费(元)</td>\n" +
        "                                    </tr>");





    var checkeds = $(".ordinaryMail-select-region").find("input:checked");
    if(checkeds!=null&&checkeds.length>0)
    {
        for(var i=0;i<checkeds.length;i++)
        {
            $(checkeds[i]).prop("disabled", false);
            $(checkeds[i]).prop("checked", false);
        }
    }

    ordinaryMailRegionTableMap = new Map();


}

/**
 * 平邮模板添加一行
 */
function ordinaryMailTableAddRowEvent()
{
    $("#ordinaryMailTableBody").append("<tr id=\"ordinaryMailTable_row_"+ordinaryMailTablePos+"\">\n" +
        "                                        <td style=\"text-align:center\"><div id=\"ordinaryMailTable_row_"+ordinaryMailTablePos+"_areas\" class=\"form-control-static\">\n" +
        "                                </div><input type=\"hidden\" id=\"ordinaryMailTable_row_"+ordinaryMailTablePos+"_areas_hidden\" class=\"ordinaryMailTableAreas\" name=\"ordinaryMailAreaRules["+ordinaryMailTablePos+"].selectAreas\" value=\"\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" id=\"ordinaryMailTable_row_"+ordinaryMailTablePos+"_firstWeight\"  style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" id=\"ordinaryMailTable_row_"+ordinaryMailTablePos+"_firstWeightMoney\"  style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" id=\"ordinaryMailTable_row_"+ordinaryMailTablePos+"_appendWeight\"  style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" id=\"ordinaryMailTable_row_"+ordinaryMailTablePos+"_appendWeightMoney\" style=\"width:60px;\"></td>\n" +
        "                                    </tr>");
    ordinaryMailTablePos++;

}







//======================================================================================================================




$(function () {

    loading.showLoading({
        type:1,
        tip:"加载中..."
    });


    $('#backBtn').on('click', function () {
        window.location.href=basePath+"/page/freightTemplate/index";
    });

    $('#transportModel_express').on('click', function () {
        var transportModel_express = $(this);
        if ($(this).prop("checked")) {
            $("#expressDefaultWeight").attr("lay-verify","required|decimal3w");
            $("#expressDefaultWeightMoney").attr("lay-verify","required|decimal3w");
            $("#expressDefaultAppendWeight").attr("lay-verify","required|decimal3w");
            $("#expressDefaultAppendWeightMoney").attr("lay-verify","required|decimal3w");

            $("#expressTableDiv").show();

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

    $('#transportModel_ems').on('click', function () {
        var transportModel_ems = $(this);
        if ($(this).prop("checked")) {

            $("#emsDefaultWeight").attr("lay-verify","required|decimal3w");
            $("#emsDefaultWeightMoney").attr("lay-verify","required|decimal3w");
            $("#emsDefaultAppendWeight").attr("lay-verify","required|decimal3w");
            $("#emsDefaultAppendWeightMoney").attr("lay-verify","required|decimal3w");

            $("#emsTableDiv").show();
        }
    });


    $('#transportModel_ordinaryMail').on('click', function () {
        var transportModel_ordinaryMail = $(this);
        if ($(this).prop("checked")) {

            $("#ordinaryMailDefaultWeight").attr("lay-verify","required|decimal3w");
            $("#ordinaryMailDefaultWeightMoney").attr("lay-verify","required|decimal3w");
            $("#ordinaryMailDefaultAppendWeight").attr("lay-verify","required|decimal3w");
            $("#ordinaryMailDefaultAppendWeightMoney").attr("lay-verify","required|decimal3w");

            $("#ordinaryMailTableDiv").show();
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

    $.ajax({
        type: "POST",
        url: basePath+"/api/freightTemplate/findById",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify({id:$("#id").val()}),
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
                var retObj = result.data;
                $("#freightTemplateName").val(retObj.name);
                var deliverPath="";
                if(retObj.deliverProvinceName!=null&&retObj.deliverProvinceName!="")
                {
                    deliverPath+=retObj.deliverProvinceName;
                }
                if(retObj.deliverCityName!=null&&retObj.deliverCityName!="")
                {
                    deliverPath+="/"+retObj.deliverCityName;
                }
                if(retObj.deliverAreaName!=null&&retObj.deliverAreaName!="")
                {
                    deliverPath+="/"+retObj.deliverAreaName;
                }
                $("#ms_city").val(deliverPath);

                if(retObj.freightStatus==1)
                {
                    $("#freightStatus_1").click();
                    $(".expressDiv").show();
                }else{
                    $("#freightStatus_2").click();
                    $(".expressDiv").hide();
                }
                if(retObj.valuationMethod==1)
                {
                    $("#valuationMethod_1").click();
                    $(".dynamicUnitDefaultText").html("件内");
                    $(".dynamicUnitDefaultAppendText").html("件");
                    $(".dynamicUnitRowFirstText").html("首件数(件)");
                    $(".dynamicUnitRowAppendText").html("续件数(件)");
                }else{
                    $("#valuationMethod_2").click();
                    $(".dynamicUnitDefaultText").html("kg内");
                    $(".dynamicUnitDefaultAppendText").html("kg");
                    $(".dynamicUnitRowFirstText").html("首重量(kg)");
                    $(".dynamicUnitRowAppendText").html("续重量(kg)");
                }

                //快递表格渲染
                if(retObj.expressDefaultRule!=null)
                {
                    $('#transportModel_express').click();
                    $("#expressDefaultWeight").val(retObj.expressDefaultRule.defaultWeight);
                    $("#expressDefaultWeightMoney").val(retObj.expressDefaultRule.defaultWeightMoney);
                    $("#expressDefaultAppendWeight").val(retObj.expressDefaultRule.defaultAppendWeight);
                    $("#expressDefaultAppendWeightMoney").val(retObj.expressDefaultRule.defaultAppendWeightMoney);

                    if(retObj.expressAreaRules!=null
                        &&retObj.expressAreaRules.length>0)
                    {
                        for(var i=0;i<retObj.expressAreaRules.length;i++)
                        {
                            var expressAreaRule = retObj.expressAreaRules[i];
                            expressTableAddRowEvent();
                            var tablePos =expressTablePos-1;
                            $("#expressTable_row_"+tablePos+"_areas").html(expressAreaRule.selectAreas);
                            $("#expressTable_row_"+tablePos+"_firstWeight").val(expressAreaRule.firstWeight);
                            $("#expressTable_row_"+tablePos+"_firstWeightMoney").val(expressAreaRule.firstWeightMoney);
                            $("#expressTable_row_"+tablePos+"_appendWeight").val(expressAreaRule.appendWeight);
                            $("#expressTable_row_"+tablePos+"_appendWeightMoney").val(expressAreaRule.appendWeightMoney);
                        }
                    }
                }

                //EMS表格渲染
                if(retObj.emsDefaultRule!=null)
                {
                    $('#transportModel_ems').click();
                    $("#emsDefaultWeight").val(retObj.emsDefaultRule.defaultWeight);
                    $("#emsDefaultWeightMoney").val(retObj.emsDefaultRule.defaultWeightMoney);
                    $("#emsDefaultAppendWeight").val(retObj.emsDefaultRule.defaultAppendWeight);
                    $("#emsDefaultAppendWeightMoney").val(retObj.emsDefaultRule.defaultAppendWeightMoney);

                    if(retObj.emsAreaRules!=null
                        &&retObj.emsAreaRules.length>0)
                    {
                        for(var i=0;i<retObj.emsAreaRules.length;i++)
                        {
                            var emsAreaRule = retObj.emsAreaRules[i];
                            emsTableAddRowEvent();
                            var tablePos =emsTablePos-1;
                            $("#emsTable_row_"+tablePos+"_areas").html(emsAreaRule.selectAreas);
                            $("#emsTable_row_"+tablePos+"_firstWeight").val(emsAreaRule.firstWeight);
                            $("#emsTable_row_"+tablePos+"_firstWeightMoney").val(emsAreaRule.firstWeightMoney);
                            $("#emsTable_row_"+tablePos+"_appendWeight").val(emsAreaRule.appendWeight);
                            $("#emsTable_row_"+tablePos+"_appendWeightMoney").val(emsAreaRule.appendWeightMoney);
                        }
                    }
                }

                //平邮表格渲染
                if(retObj.ordinaryMailDefaultRule!=null)
                {
                    $('#transportModel_ordinaryMail').click();
                    $("#ordinaryMailDefaultWeight").val(retObj.ordinaryMailDefaultRule.defaultWeight);
                    $("#ordinaryMailDefaultWeightMoney").val(retObj.ordinaryMailDefaultRule.defaultWeightMoney);
                    $("#ordinaryMailDefaultAppendWeight").val(retObj.ordinaryMailDefaultRule.defaultAppendWeight);
                    $("#ordinaryMailDefaultAppendWeightMoney").val(retObj.ordinaryMailDefaultRule.defaultAppendWeightMoney);

                    if(retObj.ordinaryMailAreaRules!=null
                        &&retObj.ordinaryMailAreaRules.length>0)
                    {
                        for(var i=0;i<retObj.ordinaryMailAreaRules.length;i++)
                        {
                            var ordinaryMailAreaRule = retObj.ordinaryMailAreaRules[i];
                            ordinaryMailTableAddRowEvent();
                            var tablePos =ordinaryMailTablePos-1;
                            $("#ordinaryMailTable_row_"+tablePos+"_areas").html(ordinaryMailAreaRule.selectAreas);
                            $("#ordinaryMailTable_row_"+tablePos+"_firstWeight").val(ordinaryMailAreaRule.firstWeight);
                            $("#ordinaryMailTable_row_"+tablePos+"_firstWeightMoney").val(ordinaryMailAreaRule.firstWeightMoney);
                            $("#ordinaryMailTable_row_"+tablePos+"_appendWeight").val(ordinaryMailAreaRule.appendWeight);
                            $("#ordinaryMailTable_row_"+tablePos+"_appendWeightMoney").val(ordinaryMailAreaRule.appendWeightMoney);
                        }
                    }
                }

                $('#transportModel_express').off("click");
                $('#transportModel_ems').off("click");
                $('#transportModel_ordinaryMail').off("click");
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


});