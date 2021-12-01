

$(function () {

});

function flushVcode()
{
    $("#refreshCaptcha").attr("src",basePath+"/api/user/forget/vcode?"+new Date().getTime());
}

function forgetPwdStep2()
{
    var username=$("#username").val();

    if(username=="")
    {
        $("#wrn_msg").show();
        $("#wrn_msg_tip").text("请输入用户名");
        return ;
    }

    var vcode=$("#vcode").val();
    if(vcode=="")
    {
        $("#wrn_msg").show();
        $("#wrn_msg_tip").text("请输入验证码");
        return ;
    }

    $.ajax({
        type: "POST",
        url: basePath+"/api/user/forget/pwd/step2",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify({"username":username,"vcode":vcode}),
        dataType: "json",
        success: function (result) {
            if(result.code<=0)
            {
                $("#wrn_msg").show();
                $("#wrn_msg_tip").text(result.msg);
                $("#vcode").val("");

                flushVcode();
            }else{
                window.location.href= result.data;
            }
        },
        error: function (result) {
            $("#login_msg_c").show();
            $("#login_msg").text("登陆失败,请重试");
        }
    });

}