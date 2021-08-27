


/**
 * 初始化省市区控件
 * @param provinceControlId
 * @param provinceDefaultValue
 * @param cityControlId
 * @param cityDefaultValue
 * @param areaControlId
 * @param areaDefaultValue
 */
function initProvinceCityAreaControl(form,provinceControlId,provinceDefaultValue,cityControlId,cityDefaultValue,areaControlId,areaDefaultValue)
{
    //初始化省份控件
    initProvinceControl("-1");


    /**
     * 初始化省控件
     * @param selectControlId
     * @param selectDefaultValue
     */
    function initProvinceControl(parentCode)
    {
        $("#"+provinceControlId).empty();
        $("#"+provinceControlId).append("<option value='-1' >请选择</option>");
        $.ajax({
            url:basePath+"/area/list/by/parentCode/"+parentCode,
            contentType: "application/json; charset=utf-8",
            type:'GET',
            success:function(data){
                if(data.data!=null&&data.data.length>0)
                {
                    var optionHtmls ="";
                    for(var i=0;i<data.data.length;i++)
                    {
                        var row = data.data[i];
                        if(provinceDefaultValue==row.code)
                        {
                            optionHtmls+="<option value='" + row.code + "'  data-is-municipality='"+row.isMunicipality+"' selected>" + row.name + "</option>";
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
                            optionHtmls+="<option value='" + row.code + "' data-is-municipality='"+row.isMunicipality+"' >" + row.name + "</option>";
                        }
                    }
                    $("#" + provinceControlId).append(optionHtmls);
                    //layui重新渲染表单
                    form.render();
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
        $("#"+areaControlId).empty();
        $("#"+cityControlId).empty();
        $("#"+cityControlId).append("<option value='-1' >请选择</option>");
        $.ajax({
            url:basePath+"/area/list/by/parentCode/"+parentCode,
            contentType: "application/json; charset=utf-8",
            type:'GET',
            success:function(data){
                if(data.data!=null&&data.data.length>0)
                {
                    var optionHtmls ="";
                    for(var i=0;i<data.data.length;i++)
                    {
                        var row = data.data[i];
                        if(cityDefaultValue==row.code)
                        {
                            optionHtmls+="<option value='" + row.code + "' selected>" + row.name + "</option>";
                            //查询区县
                            initAreaControl(row.code);
                        }else {
                            optionHtmls+="<option value='" + row.code + "'>" + row.name + "</option>";
                        }
                    }
                    $("#" + cityControlId).append(optionHtmls);
                    //layui重新渲染表单
                    form.render();
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
        $("#"+areaControlId).empty();
        $("#"+areaControlId).append("<option value='-1' >请选择</option>");
        $.ajax({
            url:basePath+"/area/list/by/parentCode/"+parentCode,
            contentType: "application/json; charset=utf-8",
            type:'GET',
            success:function(data){
                if(data.data!=null&&data.data.length>0)
                {
                    var optionHtmls ="";
                    for(var i=0;i<data.data.length;i++)
                    {
                        var row = data.data[i];
                        if(areaDefaultValue==row.code)
                        {
                            optionHtmls+="<option value='" + row.code + "' selected>" + row.name + "</option>";
                        }else {
                            optionHtmls+="<option value='" + row.code + "'>" + row.name + "</option>";
                        }
                    }
                    $("#" + areaControlId).append(optionHtmls);
                    //layui重新渲染表单
                    form.render();
                }
            }
        });
    }


    //省份控件事件
    form.on("select("+provinceControlId+")",function(data){
        var code = $("#"+provinceControlId+" option:selected").val();
        if(code!="-1") {
            var isMunicipality = $('#' + provinceControlId).find("option:selected").attr("data-is-municipality");

            $("#" + areaControlId).empty();
            $("#" + cityControlId).empty();
            //是直辖市
            if (isMunicipality == "1") {
                //直接查询区县
                initAreaControl(code);
            } else {
                //查询地市
                initCityControl(code);
            }
        }else{

            $("#"+cityControlId).empty();
            $("#"+areaControlId).empty();
            //layui重新渲染表单
            form.render();
        }
    });


    //地市控件事件
    form.on("select("+cityControlId+")",function(data){

        var code = $("#"+cityControlId+" option:selected").val();
        if(code!="-1") {
            //查询区县
            initAreaControl(code);
        }else{
            $("#"+areaControlId).empty();
            //layui重新渲染表单
            form.render();
        }
    });


}

