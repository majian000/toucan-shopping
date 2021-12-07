
function scefbtn_click()
{

    var shopCategoryName=$("#shopCategoryName").val();

    if(shopCategoryName.length<2||shopCategoryName.length>25)
    {
        $.message({
            message: "类别名称长度只能在2-25位字符之间",
            type: 'error'
        });
        return;
    }


    $.ajax({
        type: "POST",
        url: basePath+'/api/shop/category/update',
        contentType: "application/json;charset=utf-8",
        data:  getAjaxFormData("#scaf"),
        dataType: "json",
        success: function (data) {
            if(data.code==1)
            {
                window.location.href=basePath+"/page/shop/category/list";
            }else{

                $.message({
                    message: data.msg,
                    type: 'error'
                });
            }
        },
        error: function (result) {
            $.message({
                message: "修改失败,请稍后重试",
                type: 'error'
            });
        }
    });



}



$(function () {

    $('#updateBtn').on('click', function () {
        scefbtn_click();
    });



    $('#backBtn').on('click', function () {
        window.location.href=basePath+"/page/shop/category/list";
    });


});