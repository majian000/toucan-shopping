
function scefbtn_clicn()
{

    $("#scef_msg").hide();
    var shopCategoryName=$("#shopCategoryName").val();
    if(shopCategoryName==null||shopCategoryName=="")
    {
        $("#scef_msg").show();
        $("#scef_msg").text("类别名称不能为空");
        $("#scef_msg").css("color","red");
        return;
    }

    if(shopCategoryName.length<2||shopCategoryName.length>25)
    {
        $("#scef_msg").show();
        $("#scef_msg").text("类别名称长度只能在2-25位字符之间");
        $("#scef_msg").css("color","red");
        return;
    }


    $.ajax({
        type: "POST",
        url: basePath+'/api/shop/category/update',
        contentType: "application/json;charset=utf-8",
        data:  getAjaxFormData("#scef"),
        dataType: "json",
        success: function (data) {
            if(data.code==1)
            {
                window.location.href=basePath+"/page/shop/category/list";
            }else{
                $("#scef_msg").show();
                $("#scef_msg").css("color","red");
                $("#scef_msg").text(data.msg);
            }
        },
        error: function (result) {
            $("#scef_msg").show();
            $("#scef_msg").text("修改失败,请稍后重试");
        }
    });



}



$(function () {

    $('#scefbtn').on('click', function () {
        scefbtn_clicn();
    });
});