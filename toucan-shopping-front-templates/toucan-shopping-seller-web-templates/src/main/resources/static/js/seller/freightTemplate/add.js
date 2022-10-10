
var expressTablePos=0;

var expressRegionTableMap = new Map();
var g_currentExpressId;

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
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" name=\"expressAreaRules["+expressTablePos+"].firstWeight\" style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" name=\"expressAreaRules["+expressTablePos+"].firstWeightMoney\" style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" name=\"expressAreaRules["+expressTablePos+"].appendWeight\" style=\"width:60px;\"></td>\n" +
        "                                        <td style=\"text-align:center\"><input type=\"text\" lay-verify=\"required|decimal3w\" name=\"expressAreaRules["+expressTablePos+"].appendWeightMoney\" style=\"width:60px;\"></td>\n" +
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
        if(selectRegions!=null&&selectRegions.length>0) {
            if (expressRegionTableMap == null || Object.getOwnPropertyNames(expressRegionTableMap).length <= 0) {
                expressRegionTableMap.set(g_currentExpressId, selectRegions);
            } else {

            }
        }
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

        $('#expressSelectRegionModal').modal('hide');
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


        initExpressDialog(reginDatas);



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