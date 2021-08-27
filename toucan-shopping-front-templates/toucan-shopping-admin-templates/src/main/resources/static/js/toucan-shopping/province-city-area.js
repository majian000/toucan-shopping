var g_toucan_shopping_province_control_id;
var g_toucan_shopping_province_default_value;
var g_toucan_shopping_city_control_id;
var g_toucan_shopping_city_default_value;
var g_toucan_shopping_area_control_id;
var g_toucan_shopping_area_default_value;



/**
 * 初始化省市区控件
 * @param provinceControlId
 * @param provinceDefaultValue
 * @param cityControlId
 * @param cityDefaultValue
 * @param areaControlId
 * @param areaDefaultValue
 */
function initProvinceCityAreaControl(provinceControlId,provinceDefaultValue,cityControlId,cityDefaultValue,areaControlId,areaDefaultValue)
{
    g_toucan_shopping_province_control_id = provinceControlId;
    g_toucan_shopping_province_default_value = provinceDefaultValue;
    g_toucan_shopping_city_control_id = cityControlId;
    g_toucan_shopping_city_default_value = cityDefaultValue;
    g_toucan_shopping_area_control_id = areaControlId;
    g_toucan_shopping_area_default_value = areaDefaultValue;
    //初始化省份控件
    initProvinceControl("-1");

    //省份控件事件
    $('#'+g_toucan_shopping_province_control_id).change(function(){
        var code = $("#"+g_toucan_shopping_province_control_id+" option:selected").val();
        var isMunicipality = $('#'+g_toucan_shopping_province_control_id).find("option:selected").attr("data-is-municipality");
        //是直辖市
        if(isMunicipality=="1")
        {
            //直接查询区县
            initAreaControl(code);
        }else{
            //查询地市
            initCityControl(code);
        }
    });


    //地市控件事件
    $('#'+g_toucan_shopping_city_control_id).change(function(){
        var code = $("#"+g_toucan_shopping_city_control_id+" option:selected").val();
        //查询区县
        initAreaControl(code);
    });

}

/**
 * 初始化省控件
 * @param selectControlId
 * @param selectDefaultValue
 */
function initProvinceControl(parentCode)
{
    $("#"+g_toucan_shopping_province_control_id).append("<option val='-1' selected>请选择</option>");
    $.ajax({
        url:"[[@{/area/list/by/parentCode/"+parentCode+"}]]",
        contentType: "application/json; charset=utf-8",
        type:'GET',
        success:function(data){
            if(data.data!=null&&data.data.length>0)
            {
                for(var i=0;i<data.data.length;i++)
                {
                    var row = data.data[i];
                    if(g_toucan_shopping_province_default_value==row.code)
                    {
                        $("#" + g_toucan_shopping_province_control_id).append("<option val='" + row.code + "'  data-is-municipality='"+row.isMunicipality+"' selected>" + row.name + "</option>");
                        //是直辖市
                        if(row.isMunicipality=="1")
                        {
                            //直接查询区县
                            initAreaControl(row.code);
                        }else{
                            //查询地市
                            initCityControl(row.code);
                        }
                    }else {
                        $("#" + g_toucan_shopping_province_control_id).append("<option val='" + row.code + "' data-is-municipality='"+row.isMunicipality+"' >" + row.name + "</option>");
                    }
                }
            }
        }
    });
}


/**
 * 初始化地市控件
 * @param selectControlId
 * @param selectDefaultValue
 */
function initCityControl(parentCode)
{
    $("#"+g_toucan_shopping_city_control_id).append("<option val='-1' selected>请选择</option>");
    $.ajax({
        url:"[[@{/area/list/by/parentCode/"+parentCode+"}]]",
        contentType: "application/json; charset=utf-8",
        type:'GET',
        success:function(data){
            if(data.data!=null&&data.data.length>0)
            {
                for(var i=0;i<data.data.length;i++)
                {
                    var row = data.data[i];
                    if(g_toucan_shopping_city_default_value==row.code)
                    {
                        $("#" + g_toucan_shopping_city_control_id).append("<option val='" + row.code + "' selected>" + row.name + "</option>");
                        //查询区县
                        initAreaControl(row.code);
                    }else {
                        $("#" + g_toucan_shopping_city_control_id).append("<option val='" + row.code + "'>" + row.name + "</option>");
                    }
                }
            }
        }
    });
}




/**
 * 初始化地市控件
 * @param selectControlId
 * @param selectDefaultValue
 */
function initAreaControl(parentCode)
{
    $("#"+g_toucan_shopping_area_control_id).append("<option val='-1' selected>请选择</option>");
    $.ajax({
        url:"[[@{/area/list/by/parentCode/"+parentCode+"}]]",
        contentType: "application/json; charset=utf-8",
        type:'GET',
        success:function(data){
            if(data.data!=null&&data.data.length>0)
            {
                for(var i=0;i<data.data.length;i++)
                {
                    var row = data.data[i];
                    if(g_toucan_shopping_area_default_value==row.code)
                    {
                        $("#" + g_toucan_shopping_area_control_id).append("<option val='" + row.code + "' selected>" + row.name + "</option>");
                    }else {
                        $("#" + g_toucan_shopping_area_control_id).append("<option val='" + row.code + "'>" + row.name + "</option>");
                    }
                }
            }
        }
    });
}

