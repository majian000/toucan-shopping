


function updateUserShop()
{
    var nameValue=$("#name").val();
    var vcodeValue=$("#utm_vcode").val();

    $("#name_msg").text(" ");
    $("#utm_vcode_msg").text(" ");

    if(nameValue=="")
    {
        $("#name_msg").text("请输入店铺名称");
        return ;
    }
    if(vcodeValue=="")
    {
        $("#utm_vcode_msg").text("请输入验证码");
        return ;
    }

    var fields = $('#usform').serializeArray();
    var params = {}; //声明一个对象
    $.each(fields, function(index, field) {
        params[field.name] = field.value; //通过变量，将属性值，属性一起放到对象中
    });

    $.ajax({
        type: "POST",
        url: basePath+'/api/user/shop/edit',
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify(params),
        dataType: "json",
        success: function (data) {
            if(data.code==401)
            {
                window.location.href=basePath+data.data;
            }else  if(data.code==0)
            {
                $("#refreshCaptcha").attr("src",basePath+"/api/user/vcode?"+new Date().getTime());
                $("#tn_msg").text(data.msg);
            }else if(data.code==1)
            {
                window.location.href=basePath+"/page/shop/update_success";
            }
        },
        error: function (result) {
            $("#refreshCaptcha").attr("src",basePath+"/api/user/vcode?"+new Date().getTime());
            $("#tn_msg").text("请求失败,请重试");
        }
    });

}
