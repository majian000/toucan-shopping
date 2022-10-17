
//快递模板
var expressTablePos=0;
var expressRegionTableMap = new Map();
var g_currentExpressId;

//EMS模板
var emsTablePos=0;
var emsRegionTableMap = new Map();
var g_currentEmsId;



//平邮模板
var ordinaryMailTablePos=0;
var ordinaryMailRegionTableMap = new Map();
var g_currentordinaryMailId;

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

    var expressTableAreas=$(".expressTableAreas");
    if(expressTableAreas!=null&&expressTableAreas.length>0)
    {
        for(var i=0;i<expressTableAreas.length;i++)
        {
            if($(expressTableAreas[i]).val()=="")
            {
                $.message({
                    message: "请在快递运送方式中选择指定区域",
                    type: 'error'
                });
                return;
            }
        }
    }


    var emsTableAreas=$(".emsTableAreas");
    if(emsTableAreas!=null&&emsTableAreas.length>0)
    {
        for(var i=0;i<emsTableAreas.length;i++)
        {
            if($(emsTableAreas[i]).val()=="")
            {
                $.message({
                    message: "请在EMS运送方式中选择指定区域",
                    type: 'error'
                });
                return;
            }
        }
    }


    var ordinaryMailTableAreas=$(".ordinaryMailTableAreas");
    if(ordinaryMailTableAreas!=null&&ordinaryMailTableAreas.length>0)
    {
        for(var i=0;i<ordinaryMailTableAreas.length;i++)
        {
            if($(ordinaryMailTableAreas[i]).val()=="")
            {
                $.message({
                    message: "请在平邮运送方式中选择指定区域",
                    type: 'error'
                });
                return;
            }
        }
    }

    var formDataJson = getAjaxFormDataOnlyObject("#scaf");
    formDataJson.expressDefaultRule={
        "defaultWeight":$("#expressDefaultWeight").val(),
        "defaultWeightMoney":$("#expressDefaultWeightMoney").val(),
        "defaultAppendWeight":$("#expressDefaultAppendWeight").val(),
        "defaultAppendWeightMoney":$("#expressDefaultAppendWeightMoney").val()
    };
    formDataJson.emsDefaultRule={
        "defaultWeight":$("#emsDefaultWeight").val(),
        "defaultWeightMoney":$("#emsDefaultWeightMoney").val(),
        "defaultAppendWeight":$("#emsDefaultAppendWeight").val(),
        "defaultAppendWeightMoney":$("#emsDefaultAppendWeightMoney").val()
    };
    formDataJson.ordinaryMailDefaultRule={
        "defaultWeight":$("#ordinaryMailDefaultWeight").val(),
        "defaultWeightMoney":$("#ordinaryMailDefaultWeightMoney").val(),
        "defaultAppendWeight":$("#ordinaryMailDefaultAppendWeight").val(),
        "defaultAppendWeightMoney":$("#ordinaryMailDefaultAppendWeightMoney").val()
    };

    formDataJson.expressAreaRules = new Array();
    for(var i=0;i<expressTablePos;i++)
    {
        //存在该行数据
        if($("expressTable_row_"+i)!=null)
        {
            formDataJson.expressAreaRules.push({
                selectAreas:$("#expressTable_row_"+i+"_areas_hidden").val(),
                firstWeight:$("#expressTable_row_"+i+"_firstWeight").val(),
                firstWeightMoney:$("#expressTable_row_"+i+"_firstWeightMoney").val(),
                appendWeight:$("#expressTable_row_"+i+"_appendWeight").val(),
                appendWeightMoney:$("#expressTable_row_"+i+"_appendWeightMoney").val()
            });
        }
    }

    formDataJson.emsAreaRules = new Array();
    for(var p=0;p<emsTablePos;p++)
    {
        //存在该行数据
        if($("emsTable_row_"+p)!=null)
        {
            formDataJson.emsAreaRules.push({
                selectAreas:$("#emsTable_row_"+p+"_areas_hidden").val(),
                firstWeight:$("#emsTable_row_"+p+"_firstWeight").val(),
                firstWeightMoney:$("#emsTable_row_"+p+"_firstWeightMoney").val(),
                appendWeight:$("#emsTable_row_"+p+"_appendWeight").val(),
                appendWeightMoney:$("#emsTable_row_"+p+"_appendWeightMoney").val()
            });
        }
    }



    formDataJson.ordinaryMailAreaRules = new Array();
    for(var s=0;s<ordinaryMailTablePos;s++)
    {
        //存在该行数据
        if($("ordinaryMailTable_row_"+s)!=null)
        {
            formDataJson.ordinaryMailAreaRules.push({
                selectAreas:$("#ordinaryMailTable_row_"+s+"_areas_hidden").val(),
                firstWeight:$("#ordinaryMailTable_row_"+s+"_firstWeight").val(),
                firstWeightMoney:$("#ordinaryMailTable_row_"+s+"_firstWeightMoney").val(),
                appendWeight:$("#ordinaryMailTable_row_"+s+"_appendWeight").val(),
                appendWeightMoney:$("#ordinaryMailTable_row_"+s+"_appendWeightMoney").val()
            });
        }
    }

    $.ajax({
        type: "POST",
        url: basePath+'/api/freightTemplate/save',
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(formDataJson),
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
        "                                        <td style=\"text-align:center\">操作</td>\n" +
        "                                    </tr>");



    bindExpressTableDelRowEvent();

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
    expressTablePos++;
    $("#expressTableBody").append("<tr id=\"expressTable_row_"+expressTablePos+"\">\n" +
        "                                        <td style=\"text-align:center\"><div id=\"expressTable_row_"+expressTablePos+"_areas\" class=\"form-control-static\">\n" +
        "                                </div><input type=\"hidden\" id=\"expressTable_row_"+expressTablePos+"_areas_hidden\" class=\"expressTableAreas\"  value=\"\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" id=\"expressTable_row_"+expressTablePos+"_firstWeight\"  style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" id=\"expressTable_row_"+expressTablePos+"_firstWeightMoney\"  style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" id=\"expressTable_row_"+expressTablePos+"_appendWeight\"  style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" id=\"expressTable_row_"+expressTablePos+"_appendWeightMoney\"  style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\">\n" +
        "                                            <a data-row-id=\""+expressTablePos+"\" data-toggle=\"modal\" class='expressTableSelectRegion'  style=\"color:blue;cursor: pointer;\">选择区域</a>\n" +
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
            expressRegionTableMap.forEach((val,key) => {
                if(key==attrId)
            {
                if(val!=null&&val.length>0) {
                    for (var i = 0; i < val.length; i++) {
                        var city  =val[i];
                        if(i==0||(i>0&&val[i-1].parentCode!=city.parentCode)) {
                            $(".chk-express-select-region" + city.parentCode).attr("checked", false);
                            $(".chk-express-select-region" + city.parentCode).prop("disabled", false);
                        }
                        $(".chk-express-select-region"+city.code).attr("checked", false);
                        $(".chk-express-select-region"+city.code).prop("disabled", false);
                    }
                }
            }
        });
            expressRegionTableMap.set(attrId,null);
            layer.close(index);
        });
    });
    $(".expressTableSelectRegion").unbind("click");
    $(".expressTableSelectRegion").on('click', function () {
        var attrId = $(this).attr("data-row-id");
        g_currentExpressId = attrId;

        //将所有多选框设置为不勾选
        var checkeds = $(".express-select-region").find("input:checked");
        if(checkeds!=null&&checkeds.length>0)
        {
            for(var i=0;i<checkeds.length;i++)
            {
                $(checkeds[i]).prop("disabled", false);
                $(checkeds[i]).prop("checked", false);
            }
        }

        //设置勾选
        expressRegionTableMap.forEach((val,key) => {
            if(val!=null&&val.length>0)
            {
                for (var i = 0; i < val.length; i++) {
                    var city = val[i];
                    if (i == 0 || (i > 0 && val[i - 1].parentCode != city.parentCode)) {
                        $(".chk-express-select-region" + city.parentCode).prop("checked", true);
                    }
                    $(".chk-express-select-region" + city.code).prop("checked", true);
                }
            }
        });


        //将其他行选择的地市 设置为禁用
        expressRegionTableMap.forEach((val,key) => {
            if(key!=g_currentExpressId)
        {
            if(val!=null&&val.length>0) {
                for (var i = 0; i < val.length; i++) {
                    var city  =val[i];
                    if(i==0||(i>0&&val[i-1].parentCode!=city.parentCode)) {
                        $(".chk-express-select-region" + city.parentCode).prop("disabled", true);
                    }
                    $(".chk-express-select-region"+city.code).prop("disabled",true);
                }
            }
        }else{
            if(val!=null&&val.length>0) {
                for (var i = 0; i < val.length; i++) {
                    var city  =val[i];
                    if(i==0||(i>0&&val[i-1].parentCode!=city.parentCode)) {
                        $(".chk-express-select-region" + city.parentCode).prop("disabled", false);

                        //如果当前省份下的地市 不是所有被当前行选中,其他行也选择了这个省份的地市,那么这个省份就是禁用的
                        expressRegionTableMap.forEach((val2,key2) => {
                            if(key2!=g_currentExpressId)
                        {
                            if(val2!=null&&val2.length>0) {
                                for (var j = 0; j < val2.length; j++) {
                                    var city2 = val2[j];
                                    //在其他行选择了这个省份下的其他地市
                                    if(city2.parentCode==city.parentCode)
                                    {
                                        $(".chk-express-select-region" + city.parentCode).prop("disabled", true);
                                        break;
                                    }
                                }
                            }
                        }
                    });
                    }
                    $(".chk-express-select-region"+city.code).prop("disabled",false);
                }
            }
        }
    });

        $('#expressSelectRegionModal').modal('show');
    });
}



/**
 * 初始化快递 地区选择器
 * @param reginDatas
 */
function initExpressDialog(reginDatas)
{
    //初始化地区选择器
    GetRegionPlug(reginDatas,"express-select-region");
    $(".expressSelectBtn").click(function() {
        var selectRegions = GetChecked("express-select-region");
        expressRegionTableMap.set(g_currentExpressId, selectRegions);
        var selectRegionNames="";
        if(selectRegions!=null&&selectRegions.length>0)
        {
            for(var i=0;i<selectRegions.length;i++)
            {
                if(selectRegions[i].name!=null)
                {
                    selectRegionNames+=selectRegions[i].name;
                }
                if(i+1<selectRegions.length)
                {
                    selectRegionNames+="，";
                }
            }
        }

        $("#expressTable_row_"+g_currentExpressId+"_areas").html(selectRegionNames);
        $("#expressTable_row_"+g_currentExpressId+"_areas_hidden").val(selectRegionNames);

        $('#expressSelectRegionModal').modal('hide');
    });
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
        "                                        <td style=\"text-align:center\">操作</td>\n" +
        "                                    </tr>");



    bindEmsTableDelRowEvent();


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
    emsTablePos++;
    $("#emsTableBody").append("<tr id=\"emsTable_row_"+emsTablePos+"\">\n" +
        "                                        <td style=\"text-align:center\"><div id=\"emsTable_row_"+emsTablePos+"_areas\" class=\"form-control-static\">\n" +
        "                                </div><input type=\"hidden\" id=\"emsTable_row_"+emsTablePos+"_areas_hidden\" class=\"emsTableAreas\"  value=\"\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" id=\"emsTable_row_"+emsTablePos+"_firstWeight\" style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" id=\"emsTable_row_"+emsTablePos+"_firstWeightMoney\"  style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" id=\"emsTable_row_"+emsTablePos+"_appendWeight\"  style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" id=\"emsTable_row_"+emsTablePos+"_appendWeightMoney\" style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\">\n" +
        "                                            <a data-row-id=\""+emsTablePos+"\" data-toggle=\"modal\" class='emsTableSelectRegion'  style=\"color:blue;cursor: pointer;\">选择区域</a>\n" +
        "                                            &nbsp;\n" +
        "                                            <a data-row-id=\""+emsTablePos+"\" class='emsTableDelRow' style=\"color:red;cursor: pointer;\">删除</a>\n" +
        "                                        </td>\n" +
        "                                    </tr>");

    bindEmsTableDelRowEvent();
}




/**
 * EMS模板删除行
 */
function bindEmsTableDelRowEvent()
{
    $(".emsTableDelRow").unbind("click");
    $(".emsTableDelRow").on('click', function () {
        var attrId = $(this).attr("data-row-id");
        layer.confirm('确定删除?', {
            btn: ['确定','关闭'], //按钮
            title:'提示信息'
        }, function(index){
            $("#emsTable_row_"+attrId).remove();
            emsRegionTableMap.forEach((val,key) => {
                if(key==attrId)
            {
                if(val!=null&&val.length>0) {
                    for (var i = 0; i < val.length; i++) {
                        var city  =val[i];
                        if(i==0||(i>0&&val[i-1].parentCode!=city.parentCode)) {
                            $(".chk-ems-select-region" + city.parentCode).attr("checked", false);
                            $(".chk-ems-select-region" + city.parentCode).prop("disabled", false);
                        }
                        $(".chk-ems-select-region"+city.code).attr("checked", false);
                        $(".chk-ems-select-region"+city.code).prop("disabled", false);
                    }
                }
            }
        });
            emsRegionTableMap.set(attrId,null);
            layer.close(index);
        });
    });
    $(".emsTableSelectRegion").unbind("click");
    $(".emsTableSelectRegion").on('click', function () {
        var attrId = $(this).attr("data-row-id");
        g_currentEmsId = attrId;


        //将所有多选框设置为不勾选
        var checkeds = $(".ems-select-region").find("input:checked");
        if(checkeds!=null&&checkeds.length>0)
        {
            for(var i=0;i<checkeds.length;i++)
            {
                $(checkeds[i]).prop("disabled", false);
                $(checkeds[i]).prop("checked", false);
            }
        }

        //设置勾选
        emsRegionTableMap.forEach((val,key) => {
            if(val!=null&&val.length>0)
            {
                for (var i = 0; i < val.length; i++) {
                    var city = val[i];
                    if (i == 0 || (i > 0 && val[i - 1].parentCode != city.parentCode)) {
                        $(".chk-ems-select-region" + city.parentCode).prop("checked", true);
                    }
                    $(".chk-ems-select-region" + city.code).prop("checked", true);
                }
            }
        });

        //将其他行选择的地市 设置为禁用
        emsRegionTableMap.forEach((val,key) => {
            if(key!=g_currentEmsId)
        {
            if(val!=null&&val.length>0) {
                for (var i = 0; i < val.length; i++) {
                    var city  =val[i];
                    if(i==0||(i>0&&val[i-1].parentCode!=city.parentCode)) {
                        $(".chk-ems-select-region" + city.parentCode).prop("disabled", true);
                    }
                    $(".chk-ems-select-region"+city.code).prop("disabled",true);
                }
            }
        }else{
            if(val!=null&&val.length>0) {
                for (var i = 0; i < val.length; i++) {
                    var city  =val[i];
                    if(i==0||(i>0&&val[i-1].parentCode!=city.parentCode)) {
                        $(".chk-ems-select-region" + city.parentCode).prop("disabled", false);

                        //如果当前省份下的地市 不是所有被当前行选中,其他行也选择了这个省份的地市,那么这个省份就是禁用的
                        emsRegionTableMap.forEach((val2,key2) => {
                            if(key2!=g_currentEmsId)
                        {
                            if(val2!=null&&val2.length>0) {
                                for (var j = 0; j < val2.length; j++) {
                                    var city2 = val2[j];
                                    //在其他行选择了这个省份下的其他地市
                                    if(city2.parentCode==city.parentCode)
                                    {
                                        $(".chk-ems-select-region" + city.parentCode).prop("disabled", true);
                                        break;
                                    }
                                }
                            }
                        }
                    });
                    }
                    $(".chk-ems-select-region"+city.code).prop("disabled",false);
                }
            }
        }
    });

        $('#emsSelectRegionModal').modal('show');
    });
}




/**
 * 初始化Ems 地区选择器
 * @param reginDatas
 */
function initEmsDialog(reginDatas)
{
    //初始化地区选择器
    GetRegionPlug(reginDatas,"ems-select-region");
    $(".emsSelectBtn").click(function() {
        var selectRegions = GetChecked("ems-select-region");
        emsRegionTableMap.set(g_currentEmsId, selectRegions);
        var selectRegionNames="";
        if(selectRegions!=null&&selectRegions.length>0)
        {
            for(var i=0;i<selectRegions.length;i++)
            {
                if(selectRegions[i].name!=null)
                {
                    selectRegionNames+=selectRegions[i].name;
                }
                if(i+1<selectRegions.length)
                {
                    selectRegionNames+="，";
                }
            }
        }

        $("#emsTable_row_"+g_currentEmsId+"_areas").html(selectRegionNames);
        $("#emsTable_row_"+g_currentEmsId+"_areas_hidden").val(selectRegionNames);

        $('#emsSelectRegionModal').modal('hide');
    });
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
        "                                        <td style=\"text-align:center\">操作</td>\n" +
        "                                    </tr>");



    bindordinaryMailTableDelRowEvent();


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
    ordinaryMailTablePos++;
    $("#ordinaryMailTableBody").append("<tr id=\"ordinaryMailTable_row_"+ordinaryMailTablePos+"\">\n" +
        "                                        <td style=\"text-align:center\"><div id=\"ordinaryMailTable_row_"+ordinaryMailTablePos+"_areas\" class=\"form-control-static\">\n" +
        "                                </div><input type=\"hidden\" id=\"ordinaryMailTable_row_"+ordinaryMailTablePos+"_areas_hidden\" class=\"ordinaryMailTableAreas\" name=\"ordinaryMailAreaRules["+ordinaryMailTablePos+"].selectAreas\" value=\"\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" id=\"ordinaryMailTable_row_"+ordinaryMailTablePos+"_firstWeight\"  style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" id=\"ordinaryMailTable_row_"+ordinaryMailTablePos+"_firstWeightMoney\"  style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" id=\"ordinaryMailTable_row_"+ordinaryMailTablePos+"_appendWeight\"  style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" id=\"ordinaryMailTable_row_"+ordinaryMailTablePos+"_appendWeightMoney\" style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\">\n" +
        "                                            <a data-row-id=\""+ordinaryMailTablePos+"\" data-toggle=\"modal\" class='ordinaryMailTableSelectRegion'  style=\"color:blue;cursor: pointer;\">选择区域</a>\n" +
        "                                            &nbsp;\n" +
        "                                            <a data-row-id=\""+ordinaryMailTablePos+"\" class='ordinaryMailTableDelRow' style=\"color:red;cursor: pointer;\">删除</a>\n" +
        "                                        </td>\n" +
        "                                    </tr>");

    bindordinaryMailTableDelRowEvent();
}


/**
 * 平邮模板删除行
 */
function bindordinaryMailTableDelRowEvent()
{
    $(".ordinaryMailTableDelRow").unbind("click");
    $(".ordinaryMailTableDelRow").on('click', function () {
        var attrId = $(this).attr("data-row-id");
        layer.confirm('确定删除?', {
            btn: ['确定','关闭'], //按钮
            title:'提示信息'
        }, function(index){
            $("#ordinaryMailTable_row_"+attrId).remove();
            ordinaryMailRegionTableMap.forEach((val,key) => {
                if(key==attrId)
            {
                if(val!=null&&val.length>0) {
                    for (var i = 0; i < val.length; i++) {
                        var city  =val[i];
                        if(i==0||(i>0&&val[i-1].parentCode!=city.parentCode)) {
                            $(".chk-ordinaryMail-select-region" + city.parentCode).attr("checked", false);
                            $(".chk-ordinaryMail-select-region" + city.parentCode).prop("disabled", false);
                        }
                        $(".chk-ordinaryMail-select-region"+city.code).attr("checked", false);
                        $(".chk-ordinaryMail-select-region"+city.code).prop("disabled", false);
                    }
                }
            }
        });
            ordinaryMailRegionTableMap.set(attrId,null);
            layer.close(index);
        });
    });
    $(".ordinaryMailTableSelectRegion").unbind("click");
    $(".ordinaryMailTableSelectRegion").on('click', function () {
        var attrId = $(this).attr("data-row-id");
        g_currentordinaryMailId = attrId;


        //将所有多选框设置为不勾选
        var checkeds = $(".ordinaryMail-select-region").find("input:checked");
        if(checkeds!=null&&checkeds.length>0)
        {
            for(var i=0;i<checkeds.length;i++)
            {
                $(checkeds[i]).prop("disabled", false);
                $(checkeds[i]).prop("checked", false);
            }
        }

        //设置勾选
        ordinaryMailRegionTableMap.forEach((val,key) => {
            if(val!=null&&val.length>0)
        {
            for (var i = 0; i < val.length; i++) {
                var city = val[i];
                if (i == 0 || (i > 0 && val[i - 1].parentCode != city.parentCode)) {
                    $(".chk-ordinaryMail-select-region" + city.parentCode).prop("checked", true);
                }
                $(".chk-ordinaryMail-select-region" + city.code).prop("checked", true);
            }
        }
    });

        //将其他行选择的地市 设置为禁用
        ordinaryMailRegionTableMap.forEach((val,key) => {
            if(key!=g_currentordinaryMailId)
            {
                if(val!=null&&val.length>0) {
                    for (var i = 0; i < val.length; i++) {
                        var city  =val[i];
                        if(i==0||(i>0&&val[i-1].parentCode!=city.parentCode)) {
                            $(".chk-ordinaryMail-select-region" + city.parentCode).prop("disabled", true);
                        }
                        $(".chk-ordinaryMail-select-region"+city.code).prop("disabled",true);
                    }
                }
            }else{
                if(val!=null&&val.length>0) {
                    for (var i = 0; i < val.length; i++) {
                        var city  =val[i];
                        if(i==0||(i>0&&val[i-1].parentCode!=city.parentCode)) {
                            $(".chk-ordinaryMail-select-region" + city.parentCode).prop("disabled", false);

                            //如果当前省份下的地市 不是所有被当前行选中,其他行也选择了这个省份的地市,那么这个省份就是禁用的
                            ordinaryMailRegionTableMap.forEach((val2,key2) => {
                                if(key2!=g_currentordinaryMailId)
                                {
                                    if(val2!=null&&val2.length>0) {
                                        for (var j = 0; j < val2.length; j++) {
                                            var city2 = val2[j];
                                            //在其他行选择了这个省份下的其他地市
                                            if(city2.parentCode==city.parentCode)
                                            {
                                                $(".chk-ordinaryMail-select-region" + city.parentCode).prop("disabled", true);
                                                break;
                                            }
                                        }
                                    }
                                }
                            });
                        }
                        $(".chk-ordinaryMail-select-region"+city.code).prop("disabled",false);
                    }
                }
            }
        });

        $('#ordinaryMailSelectRegionModal').modal('show');
    });
}



/**
 * 初始化平邮 地区选择器
 * @param reginDatas
 */
function initordinaryMailDialog(reginDatas)
{
    //初始化地区选择器
    GetRegionPlug(reginDatas,"ordinaryMail-select-region");
    $(".ordinaryMailSelectBtn").click(function() {
        var selectRegions = GetChecked("ordinaryMail-select-region");
        ordinaryMailRegionTableMap.set(g_currentordinaryMailId, selectRegions);
        var selectRegionNames="";
        if(selectRegions!=null&&selectRegions.length>0)
        {
            for(var i=0;i<selectRegions.length;i++)
            {
                if(selectRegions[i].name!=null)
                {
                    selectRegionNames+=selectRegions[i].name;
                }
                if(i+1<selectRegions.length)
                {
                    selectRegionNames+="，";
                }
            }
        }

        $("#ordinaryMailTable_row_"+g_currentordinaryMailId+"_areas").html(selectRegionNames);
        $("#ordinaryMailTable_row_"+g_currentordinaryMailId+"_areas_hidden").val(selectRegionNames);

        $('#ordinaryMailSelectRegionModal').modal('hide');
    });
}


//======================================================================================================================





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
                reginData.children = new Array();
                var municipalityRegion = {};
                municipalityRegion.code=reginData.code;
                municipalityRegion.name=reginData.name;
                reginData.children.push(municipalityRegion);
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


        //初始化快递地区选择器
        initExpressDialog(reginDatas);

        //初始化EMS地区选择器
        initEmsDialog(reginDatas);

        //初始化平邮地区选择器
        initordinaryMailDialog(reginDatas);

    });




    $('#saveBtn').on('click', function () {
        scafbtn_click();
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

        } else {
            layer.confirm('将清空所有项,确定要清空吗?', {
                btn: ['确定','关闭'], //按钮
                title:'提示信息'
            }, function(index){
                $("#expressTableDiv").hide();
                resetexpressTable();
                layer.close(index);
            }, function(){
                $(transportModel_express).prop("checked",true);
            });

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
        } else {
            layer.confirm('将清空所有项,确定要清空吗?', {
                btn: ['确定','关闭'], //按钮
                title:'提示信息'
            }, function(index){
                $("#emsTableDiv").hide();
                resetEmsTable();
                layer.close(index);
            }, function(){
                $(transportModel_ems).prop("checked",true);
            });
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
        } else {
            layer.confirm('将清空所有项,确定要清空吗?', {
                btn: ['确定','关闭'], //按钮
                title:'提示信息'
            }, function(index){
                $("#ordinaryMailTableDiv").hide();
                resetordinaryMailTable();
                layer.close(index);
            }, function(){
                $(transportModel_ordinaryMail).prop("checked",true);
            });
        }
    });

    $('.freightStatus').on('click', function () {
        var freightStatusVal = $("input[name='freightStatus']:checked").val();
        if(freightStatusVal=="1")
        {
            $(".expressDiv").show();
        }else{
            layer.confirm('将清空自定义运费,确定要清空吗?', {
                btn: ['确定','关闭'], //按钮
                title:'提示信息'
            }, function(index){
                resetexpressTable();
                resetEmsTable();
                resetordinaryMailTable();
                $(".expressDiv").hide();
                layer.close(index);

            }, function(){
                $("#freightStatus_1").prop("checked",true);
            });
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


    $('.emsTableAddRow').on('click', function () {
        emsTableAddRowEvent();
    });


    $('.ordinaryMailTableAddRow').on('click', function () {
        ordinaryMailTableAddRowEvent();
    });


});