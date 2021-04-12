



function login()
{
    var username=$("#username").val();
    var password=$("#password").val();
    if(username=="")
    {
        $("#login_msg").text("请输入账号");
        return ;
    }
    if(password=="")
    {
        $("#login_msg").text("请输入密码");
        return ;
    }
    $.ajax({
        type: "POST",
        url: basePath+"/api/user/login/password",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify({"loginUserName":username,"password":password}),
        dataType: "json",
        success: function (result) {
            if(result.code<=0)
            {
                $("#login_msg").text(result.msg);
            }else{
                if(!window.localStorage)
                {
                    $("#login_msg").text("浏览器太旧了,请升级浏览器");
                    return;
                }

                window.localStorage.setItem("uid", result.data.id);
                window.localStorage.setItem("lt", result.data.loginToken);
                window.location.href=basePath+"/page/user/info";
            }
        },
        error: function (result) {
            $("#login_msg").text("登陆失败,请重试");
        }
    });
}
