
var g_isSendVC=0;
var g_goUserCenterSecond=5;


$(function () {

    bindSendVerifyCodeEvent();

    $("#nextBtn").click(function(){
        if(g_isSendVC==0)
        {
            $("#modify_pwd_msg").text("请先发送验证码");
        }else{
            $("#modify_pwd_msg").text("");
            $("#verifyCodeDiv").hide();
            $("#confirmPwdDiv").show();
        }
    });

    $("#modifyBtn").click(function(){
        $("#modify_pwd_msg").css("color","#ff4e00");
        $("#modify_pwd_msg").text("");
        var password = $("#password").val();
        var confirmPassword = $("#confirmPassword").val();
        var verifyCode = $("#verifyCode").val();
        if(password==null||password=="")
        {
            $("#modify_pwd_msg").text("请输入密码");
            return;
        }
        if(confirmPassword==null||confirmPassword=="")
        {
            $("#modify_pwd_msg").text("请输入确认密码");
            return;
        }
        if(!checkInput.password[0].test(password))
        {
            $("#modify_pwd_msg").text(checkInput.password[1]);
            return;
        }
        if(verifyCode==null||verifyCode=="")
        {
            $("#modify_pwd_msg").text("请输入验证码");
            return;
        }

        loading.showLoading({
            type: 1,
            tip: "请等待..."
        });
        $.ajax({
            type: "POST",
            url: basePath + "/api/user/modify/password",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify({password:password,confirmPassword:confirmPassword,vcode:verifyCode}),
            dataType: "json",
            success: function (result) {
                if (result.code <= 0) {
                    $("#modify_pwd_msg").text(result.msg);
                    return;
                }

                $("#modify_pwd_msg").css("color","green");
                goUserCenterPage();
            },
            error: function (result) {
                $("#modify_pwd_msg").text("修改失败,请稍后重试");
            },
            complete: function (data, status) {
                loading.hideLoading();
            }
        });
    });
});




function bindSendVerifyCodeEvent()
{
    $("#mobileSendVcBtn").click(function(){
        $("#modify_pwd_msg").text("");
        var mobile = $("#mobile").val();
        var mobileSendVcBtnObj=$(this);
        if(mobile==null||mobile=="")
        {
            $("#modify_pwd_msg").text("请先绑定手机号");
        }else {
            loading.showLoading({
                type: 1,
                tip: "请等待..."
            });
            $.ajax({
                type: "POST",
                url: basePath + "/api/vcode/text/mobile/user/modifyPwd",
                contentType: "application/json;charset=utf-8",
                data: null,
                dataType: "json",
                success: function (result) {
                    if (result.code <= 0) {
                        $("#modify_pwd_msg").text("发送失败,请稍后重试");
                        return;
                    }
                    mobileSendVcBtnObj.text("已发送");
                    mobileSendVcBtnObj.css("color", "grey");
                    mobileSendVcBtnObj.unbind("click");
                    alert("手机验证码是:1234");
                    g_isSendVC = 1;
                },
                error: function (result) {
                    $("#modify_pwd_msg").text("发送失败,请稍后重试");
                },
                complete: function (data, status) {
                    loading.hideLoading();
                }
            });
        }
    });


    $("#emailSendVcBtn").click(function(){
        $("#modify_pwd_msg").text("");
        var email = $("#email").val();
        var emailSendVcBtnObj=$(this);
        if(email==null||email=="")
        {
            $("#modify_pwd_msg").text("请先绑定邮箱");
        }else {
            loading.showLoading({
                type: 1,
                tip: "请等待..."
            });
            $.ajax({
                type: "POST",
                url: basePath + "/api/vcode/text/email/user/modifyPwd",
                contentType: "application/json;charset=utf-8",
                data: null,
                dataType: "json",
                success: function (result) {
                    if (result.code <= 0) {
                        $("#modify_pwd_msg").text("发送失败,请稍后重试");
                        return;
                    }
                    emailSendVcBtnObj.text("已发送");
                    emailSendVcBtnObj.css("color", "grey");
                    emailSendVcBtnObj.unbind("click");
                    g_isSendVC=1;
                },
                error: function (result) {
                    $("#modify_pwd_msg").text("发送失败,请稍后重试");
                },
                complete: function (data, status) {
                    loading.hideLoading();
                }
            });

        }
    });
}



function goUserCenterPage()
{
    if(g_goUserCenterSecond<=0)
    {
        window.location.href=basePath+"/page/user/info";
    }else
    {
        $("#modify_pwd_msg").text("修改成功,"+g_goUserCenterSecond + '秒后跳转到个人中心');
    }
    g_goUserCenterSecond--;
    setTimeout(function () {
        goUserCenterPage();
    }, 1000);

}