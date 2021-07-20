

var g_countdownNum = 60;
var g_goLoginSecond=5;
function getvcode()
{
    $("#regist_msg_c").hide();
    $("#regist_msg").text("");
    var mobilePhoneValue=$("#regist_mobile_phone").val();

    if(mobilePhoneValue == ''){
        $("#regist_msg_c").show();
        $("#regist_msg").text("请输入手机号");
        $("#regist_msg").css("color","red");
        return ;
    }else if(mobilePhoneValue.length !=11||!validPhone(mobilePhoneValue)){
        $("#regist_msg_c").show();
        $("#regist_msg").text("请输入合法手机号");
        $("#regist_msg").css("color","red");
        return ;
    }
    send_verify_code(mobilePhoneValue);

}

function goLoginPage(msg)
{
    window.console.log(g_goLoginSecond);
    if(g_goLoginSecond<=0)
    {
        window.location.href=basePath+"/page/user/login";
    }else
    {
        $("#regist_msg_c").show();
        $("#regist_msg").text(msg+","+g_goLoginSecond+"秒后跳转登录");
        $("#regist_msg").css("color","green");

    }
    g_goLoginSecond--;
    setTimeout(function () {
        goLoginPage(msg);
    }, 1000);

}

function regist()
{
    var mobilePhoneValue=$("#regist_mobile_phone").val();
    var passwordValue=$("#regist_password").val();
    var confirmPasswordValue=$("#regist_confirmPassword").val();
    var vcodeValue=$("#regist_vcode").val();
    $.post(basePath+"/api/user/regist",{mobilePhone:mobilePhoneValue,password:passwordValue,confirmPassword:confirmPasswordValue,vcode:vcodeValue},function(result){
        if(result.code<=0)
        {
            $("#regist_msg_c").show();
            $("#regist_msg").text(result.msg);
            $("#regist_msg").css("color","red");
        }else{
            goLoginPage(result.msg);
        }
    });
}

function send_verify_code(mobilePhone)
{
    $.post(basePath+"/api/user/sendRegistVerifyCode",{mobilePhone:mobilePhone},function(result){
        if(result.code<=0)
        {
            $("#regist_msg_c").show();
            $("#regist_msg").text(result.msg);
            $("#regist_msg").css("color","red");
        }else{
            $("#regist_msg_c").hide();
            $("#regist_msg").text("");
            countdown();
        }
    });
}

function countdown() {
    if(g_countdownNum == 0) {
        $("#get_vcode").attr("onclick","getvcode();");
        $("#get_vcode").text('获取验证码');
        g_countdownNum = 60;
        return;
    } else {
        $("#get_vcode").removeAttr("onclick");
        $("#get_vcode").text(g_countdownNum + '秒后重新发送');
        g_countdownNum--;
    }

    setTimeout(function () {
        countdown();
    }, 1000);
}