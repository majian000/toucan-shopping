
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



$(function () {

    $('#saveBtn').on('click', function () {
        scafbtn_click();
    });


    $('#backBtn').on('click', function () {
        window.location.href=basePath+"/page/freightTemplate/index";
    });


});