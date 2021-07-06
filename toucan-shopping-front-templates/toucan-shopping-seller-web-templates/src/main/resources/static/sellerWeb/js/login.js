



function login()
{
    var username=$("#username").val();
    var password=$("#password").val();
    if(username=="")
    {
        $("#login_msg").html("<b></b> 请输入账号");
        $("#login_msg").attr("class","msg-warn ");
        return ;
    }
    if(password=="")
    {
        $("#login_msg").html("<b></b> 请输入密码");
        $("#login_msg").attr("class","msg-warn ");
        return ;
    }
    $("#login_msg").attr("class","msg-warn hide ");
    $.ajax({
        type: "POST",
        url: basePath+"/api/user/login/password",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify({"loginUserName":username,"password":password}),
        dataType: "json",
        success: function (result) {
            if(result.code<=0)
            {
                $("#login_msg").attr("class","msg-warn ");
                $("#login_msg").html("<b></b> "+result.msg);
            }else{
                if(!window.localStorage)
                {
                    $("#login_msg").attr("class","msg-warn ");
                    $("#login_msg").text("<b></b> 浏览器太旧了,请升级浏览器");
                    return;
                }
                window.localStorage.removeItem("uid");
                window.localStorage.removeItem("lt");

                window.localStorage.setItem("uid", result.data.id);
                window.localStorage.setItem("lt", result.data.loginToken);
                window.location.href=basePath+"/page/shop/shop_regist/"+result.data.id;
            }
        },
        error: function (result) {
            $("#login_msg").attr("class","msg-warn ");
            $("#login_msg").html("<b></b>  登陆失败,请重试");
        }
    });
}
