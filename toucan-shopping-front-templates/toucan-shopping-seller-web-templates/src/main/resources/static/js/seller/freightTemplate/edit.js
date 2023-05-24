
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
        var rowAreas = $("#expressTable_row_"+i+"_areas_hidden").val();
        if(rowAreas!=null&&rowAreas!="")
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
        var rowAreas = $("#emsTable_row_"+p+"_areas_hidden").val();
        //存在该行数据
        if(rowAreas!=null&&rowAreas!="")
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
        var rowAreas = $("#ordinaryMailTable_row_"+s+"_areas_hidden").val();
        //存在该行数据
        if(rowAreas!=null&&rowAreas!="")
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

    if(formDataJson.freightStatus=="1")
    {
        if((formDataJson.transportModelExpress==null||formDataJson.transportModelExpress=="")
            &&(formDataJson.transportModelEms==null||formDataJson.transportModelEms=="")
            &&(formDataJson.transportModelOrdinaryMail==null||formDataJson.transportModelOrdinaryMail==""))
        {

            $.message({
                message: "请选择运送方式",
                type: 'error'
            });
            return;
        }
    }

    loading.showLoading({
        type:6,
        tip:"保存中..."
    });
    $.ajax({
        type: "POST",
        url: basePath+'/api/freightTemplate/update',
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(formDataJson),
        dataType: "json",
        success: function (data) {
            if(data.code==1)
            {
                window.location.href=basePath+"/page/freightTemplate/index";
            }else{
                loading.hideLoading();
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
            loading.hideLoading();
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

    var selectvaluationMethodVal = $("input[name='valuationMethod']:checked").val();
    var columnFirstUnit = "";
    var columnAppendUnit = "";
    if(selectvaluationMethodVal=="1")
    {
        columnFirstUnit="首件数(件)";
        columnAppendUnit="续件数(件)";
    }else if(selectvaluationMethodVal=="2")
    {
        columnFirstUnit="首重量(kg)";
        columnAppendUnit="续重量(kg)";
    }else{
        columnFirstUnit="首体积(m³)";
        columnAppendUnit="续体积(m³)";
    }

    $("#expressTableBody").html("<tr class=\"tabTh\">\n" +
        "                                        <td style=\"text-align: center;\">运送到</td>\n" +
        "                                        <td style=\"text-align:center\"><a class=\"dynamicUnitRowFirstText\">"+columnFirstUnit+"</a></td>\n" +
        "                                        <td style=\"text-align:center\"><font color=\"#ff4e00\">*</font>首费(元)</td>\n" +
        "                                        <td style=\"text-align:center\"><a class=\"dynamicUnitRowAppendText\">"+columnAppendUnit+"</td>\n" +
        "                                        <td style=\"text-align:center\"><font color=\"#ff4e00\">*</font>续费(元)</td>\n" +
        "                                        <td style=\"text-align:center\">操作</td>\n" +
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
        "                                        <td style=\"text-align:center\">\n" +
        "                                            <a data-row-id=\""+expressTablePos+"\" data-toggle=\"modal\" class='expressTableSelectRegion'  style=\"color:blue;cursor: pointer;\">选择区域</a>\n" +
        "                                            &nbsp;\n" +
        "                                            <a data-row-id=\""+expressTablePos+"\" class='expressTableDelRow' style=\"color:red;cursor: pointer;\">删除</a>\n" +
        "                                        </td>\n" +
        "                                    </tr>");

    expressTablePos++;
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

    var selectvaluationMethodVal = $("input[name='valuationMethod']:checked").val();
    var columnFirstUnit = "";
    var columnAppendUnit = "";
    if(selectvaluationMethodVal=="1")
    {
        columnFirstUnit="首件数(件)";
        columnAppendUnit="续件数(件)";
    }else if(selectvaluationMethodVal=="2")
    {
        columnFirstUnit="首重量(kg)";
        columnAppendUnit="续重量(kg)";
    }else{
        columnFirstUnit="首体积(m³)";
        columnAppendUnit="续体积(m³)";
    }

    $("#emsTableBody").html("<tr class=\"tabTh\">\n" +
        "                                        <td style=\"text-align: center;\">运送到</td>\n" +
        "                                        <td style=\"text-align:center\"><a class=\"dynamicUnitRowFirstText\">"+columnFirstUnit+"</a></td>\n" +
        "                                        <td style=\"text-align:center\"><font color=\"#ff4e00\">*</font>首费(元)</td>\n" +
        "                                        <td style=\"text-align:center\"><a class=\"dynamicUnitRowAppendText\">"+columnAppendUnit+"</td>\n" +
        "                                        <td style=\"text-align:center\"><font color=\"#ff4e00\">*</font>续费(元)</td>\n" +
        "                                        <td style=\"text-align:center\">操作</td>\n" +
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
        "                                        <td style=\"text-align:center\">\n" +
        "                                            <a data-row-id=\""+emsTablePos+"\" data-toggle=\"modal\" class='emsTableSelectRegion'  style=\"color:blue;cursor: pointer;\">选择区域</a>\n" +
        "                                            &nbsp;\n" +
        "                                            <a data-row-id=\""+emsTablePos+"\" class='emsTableDelRow' style=\"color:red;cursor: pointer;\">删除</a>\n" +
        "                                        </td>\n" +
        "                                    </tr>");
    emsTablePos++;
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

    var selectvaluationMethodVal = $("input[name='valuationMethod']:checked").val();
    var columnFirstUnit = "";
    var columnAppendUnit = "";
    if(selectvaluationMethodVal=="1")
    {
        columnFirstUnit="首件数(件)";
        columnAppendUnit="续件数(件)";
    }else if(selectvaluationMethodVal=="2")
    {
        columnFirstUnit="首重量(kg)";
        columnAppendUnit="续重量(kg)";
    }else{
        columnFirstUnit="首体积(m³)";
        columnAppendUnit="续体积(m³)";
    }

    $("#ordinaryMailTableBody").html("<tr class=\"tabTh\">\n" +
        "                                        <td style=\"text-align: center;\">运送到</td>\n" +
        "                                        <td style=\"text-align:center\"><a class=\"dynamicUnitRowFirstText\">"+columnFirstUnit+"</a></td>\n" +
        "                                        <td style=\"text-align:center\"><font color=\"#ff4e00\">*</font>首费(元)</td>\n" +
        "                                        <td style=\"text-align:center\"><a class=\"dynamicUnitRowAppendText\">"+columnAppendUnit+"</td>\n" +
        "                                        <td style=\"text-align:center\"><font color=\"#ff4e00\">*</font>续费(元)</td>\n" +
        "                                        <td style=\"text-align:center\">操作</td>\n" +
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
        "                                        <td style=\"text-align:center\">\n" +
        "                                            <a data-row-id=\""+ordinaryMailTablePos+"\" data-toggle=\"modal\" class='ordinaryMailTableSelectRegion'  style=\"color:blue;cursor: pointer;\">选择区域</a>\n" +
        "                                            &nbsp;\n" +
        "                                            <a data-row-id=\""+ordinaryMailTablePos+"\" class='ordinaryMailTableDelRow' style=\"color:red;cursor: pointer;\">删除</a>\n" +
        "                                        </td>\n" +
        "                                    </tr>");
    ordinaryMailTablePos++;
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

        }else {
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
                if($('#transportModel_express').is(':checked'))
                {
                    $('#transportModel_express').prop("checked",false);
                    $("#expressTableDiv").hide();
                    resetexpressTable();
                }

                if($('#transportModel_ems').is(':checked'))
                {
                    $('#transportModel_ems').prop("checked",false);
                    $("#emsTableDiv").hide();
                    resetEmsTable();
                }

                if($('#transportModel_ordinaryMail').is(':checked'))
                {
                    $('#transportModel_ordinaryMail').prop("checked",false);
                    $("#ordinaryMailTableDiv").hide();
                    resetordinaryMailTable();
                }

                $("#valuationMethod_1").click();

                $(".expressDiv").hide();
                layer.close(index);

            }, function(){
                $("#freightStatus_1").prop("checked",true);
            });
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
                $("#province").val(retObj.deliverProvinceName);
                $("#city").val(retObj.deliverCityName);
                $("#area").val(retObj.deliverAreaName);
                $("#province_code").val(retObj.deliverProvinceCode);
                $("#city_code").val(retObj.deliverCityCode);
                $("#area_code").val(retObj.deliverAreaCode);

                $("#ms_city").val(deliverPath);

                if(retObj.freightStatus==1)
                {
                    $("#freightStatus_1").prop("checked", true);
                    $("#freightStatus_2").prop("checked", false);
                    $(".expressDiv").show();
                }else{
                    $("#freightStatus_1").prop("checked", false);
                    $("#freightStatus_2").prop("checked", true);
                    if($('#transportModel_express').is(':checked'))
                    {
                        $('#transportModel_express').prop("checked",false);
                        $("#expressTableDiv").hide();
                        resetexpressTable();
                    }

                    if($('#transportModel_ems').is(':checked'))
                    {
                        $('#transportModel_ems').prop("checked",false);
                        $("#emsTableDiv").hide();
                        resetEmsTable();
                    }

                    if($('#transportModel_ordinaryMail').is(':checked'))
                    {
                        $('#transportModel_ordinaryMail').prop("checked",false);
                        $("#ordinaryMailTableDiv").hide();
                        resetordinaryMailTable();
                    }

                    $("#valuationMethod_1").click();

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
                            $("#expressTable_row_"+tablePos+"_areas_hidden").val(expressAreaRule.selectAreas);
                            $("#expressTable_row_"+tablePos+"_firstWeight").val(expressAreaRule.firstWeight);
                            $("#expressTable_row_"+tablePos+"_firstWeightMoney").val(expressAreaRule.firstWeightMoney);
                            $("#expressTable_row_"+tablePos+"_appendWeight").val(expressAreaRule.appendWeight);
                            $("#expressTable_row_"+tablePos+"_appendWeightMoney").val(expressAreaRule.appendWeightMoney);

                            var expressRegionTableArray = new Array();
                            for(var j=0;j<expressAreaRule.selectItems.length;j++)
                            {
                                var selectItem = expressAreaRule.selectItems[j];
                                expressRegionTableArray.push({
                                    parentCode:selectItem.provinceCode,
                                    code:selectItem.cityCode
                                });
                            }
                            expressRegionTableMap.set(expressTablePos-1,expressRegionTableArray);
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
                            $("#emsTable_row_"+tablePos+"_areas_hidden").val(emsAreaRule.selectAreas);
                            $("#emsTable_row_"+tablePos+"_firstWeight").val(emsAreaRule.firstWeight);
                            $("#emsTable_row_"+tablePos+"_firstWeightMoney").val(emsAreaRule.firstWeightMoney);
                            $("#emsTable_row_"+tablePos+"_appendWeight").val(emsAreaRule.appendWeight);
                            $("#emsTable_row_"+tablePos+"_appendWeightMoney").val(emsAreaRule.appendWeightMoney);



                            var emsRegionTableArray = new Array();
                            for(var j=0;j<emsAreaRule.selectItems.length;j++)
                            {
                                var selectItem = emsAreaRule.selectItems[j];
                                emsRegionTableArray.push({
                                    parentCode:selectItem.provinceCode,
                                    code:selectItem.cityCode
                                });
                            }
                            emsRegionTableMap.set(emsTablePos-1,emsRegionTableArray);
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
                            $("#ordinaryMailTable_row_"+tablePos+"_areas_hidden").val(ordinaryMailAreaRule.selectAreas);
                            $("#ordinaryMailTable_row_"+tablePos+"_firstWeight").val(ordinaryMailAreaRule.firstWeight);
                            $("#ordinaryMailTable_row_"+tablePos+"_firstWeightMoney").val(ordinaryMailAreaRule.firstWeightMoney);
                            $("#ordinaryMailTable_row_"+tablePos+"_appendWeight").val(ordinaryMailAreaRule.appendWeight);
                            $("#ordinaryMailTable_row_"+tablePos+"_appendWeightMoney").val(ordinaryMailAreaRule.appendWeightMoney);

                            var ordinaryMailRegionTableArray = new Array();
                            for(var j=0;j<ordinaryMailAreaRule.selectItems.length;j++)
                            {
                                var selectItem = ordinaryMailAreaRule.selectItems[j];
                                ordinaryMailRegionTableArray.push({
                                    parentCode:selectItem.provinceCode,
                                    code:selectItem.cityCode
                                });
                            }
                            ordinaryMailRegionTableMap.set(ordinaryMailTablePos-1,ordinaryMailRegionTableArray);
                        }
                    }
                }
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