


function registUserShop()
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
    $.ajax({
        type: "POST",
        url: basePath+'/api/user/shop/regist',
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify({"name":nameValue,"vcode":vcodeValue}),
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
                window.location.href=basePath+"/page/user/true/name/approve/submit_success";
            }
        },
        error: function (result) {
            $("#tn_msg").text("请求失败,请重试");
        }
    });

}
