


var g_ivcode = false;

$(function () {

    var ivcodeVal = $("#ivcode").val();

    if(ivcodeVal=="1")
    {
        showVCode();
    }

    $(document).keyup(function(event){
        if(event.keyCode ==13){
            $("#loginsubmit").trigger("click");
        }
    });

    $("#refreshCaptcha").bind( 'click' ,function(){
        $("#refreshCaptcha").attr("src",basePath+"/api/user/login/faild/vcode?"+new Date().getTime());
    });

});

function showVCode()
{

    g_ivcode = true;
    $("#ldiv").css("height","440px");
    $("#vcodetr").show();
    $("#refreshCaptcha").attr("src",basePath+"/api/user/login/faild/vcode?"+new Date().getTime());
}

function login()
{
    var username=$("#username").val();
    var password=$("#password").val();
    if(username=="")
    {
        $("#login_msg_c").show();
        $("#login_msg").text("请输入用户名");
        return ;
    }
    if(password=="")
    {
        $("#login_msg_c").show();
        $("#login_msg").text("请输入密码");
        return ;
    }
    var vcode=$("#vcode").val();
    if(g_ivcode)
    {

        if(vcode=="")
        {
            $("#login_msg_c").show();
            $("#login_msg").text("请输入验证码");
            return ;
        }
    }
    $("#loginsubmit").text("登陆中...");
    $.ajax({
        type: "POST",
        url: basePath+"/api/user/login/password",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify({"loginUserName":username,"password":password,"vcode":vcode}),
        dataType: "json",
        success: function (result) {
            if(result.code<=0)
            {
                $("#loginsubmit").text("登  录");
                $("#vcode").val("");
                $("#login_msg_c").show();
                $("#login_msg").text(result.msg);
                //显示验证码
                if(result.code==-11||result.code==-12)
                {
                    showVCode();
                }
            }else{
                share_cookie();
            }
        },
        error: function (result) {
            $("#login_msg_c").show();
            $("#login_msg").text("登陆失败,请重试");
        }
    });

}
