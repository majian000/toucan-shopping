
var g_isSendVC=0;
var g_goUserCenterSecond=5;


$(function () {

    bindSendVerifyCodeEvent();

    $("#nextBtn").click(function(){
        if(g_isSendVC==0)
        {
            $("#modify_mobile_phone_msg").text("请先发送验证码");
        }else{
            var s1VerifyCode=$("#s1VerifyCode").val();
            if(s1VerifyCode==null||s1VerifyCode==""){
                $("#modify_mobile_phone_msg").text("请先发送验证码");
                return ;
            }

            loading.showLoading({
                type: 1,
                tip: "请等待..."
            });
            $.ajax({
                type: "POST",
                url: basePath+"/api/user/modify/mobilePhone/valid/verify/code",
                contentType: "application/json;charset=utf-8",
                data: JSON.stringify({vcode:s1VerifyCode}),
                dataType: "json",
                success: function (result) {
                    if (result.code <= 0) {
                        $("#modify_mobile_phone_msg").text(result.msg);
                        return;
                    }
                    $("#securityCode").val(result.data);
                    $("#modify_mobile_phone_msg").text("");
                    $("#verifyCodeDiv").hide();
                    $("#confirmPwdDiv").show();
                },
                error: function (result) {
                    $("#modify_mobile_phone_msg").text("验证失败,请稍后重试");
                },
                complete: function (data, status) {
                    loading.hideLoading();
                }
            });
        }
    });

    $("#modifyBtn").click(function(){
        $("#modify_mobile_phone_msg").css("color","#ff4e00");
        $("#modify_mobile_phone_msg").text("");
        var newMobile = $("#newMobile").val();
        var securityCode = $("#securityCode").val();
        var verifyCode = $("#verifyCode").val();
        if(newMobile==null||newMobile=="")
        {
            $("#modify_mobile_phone_msg").text("请输入新手机号");
            return;
        }
        if(!checkInput.phone[0].test(newMobile))
        {
            $("#modify_mobile_phone_msg").text(checkInput.phone[1]);
            return;
        }
        if(verifyCode==null||verifyCode=="")
        {
            $("#modify_mobile_phone_msg").text("请输入验证码");
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
            data: JSON.stringify({mobile:newMobile,vcode:verifyCode,securityCode:securityCode}),
            dataType: "json",
            success: function (result) {
                if (result.code <= 0) {
                    $("#modify_mobile_phone_msg").text(result.msg);
                    return;
                }

                $("#modify_mobile_phone_msg").css("color","green");
                goUserCenterPage();
            },
            error: function (result) {
                $("#modify_mobile_phone_msg").text("修改失败,请稍后重试");
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
        $("#modify_mobile_phone_msg").text("");
        var mobile = $("#mobile").val();
        var mobileSendVcBtnObj=$(this);
        if(mobile==null||mobile=="")
        {
            $("#modify_mobile_phone_msg").text("请先绑定手机号");
        }else {
            loading.showLoading({
                type: 1,
                tip: "请等待..."
            });
            $.ajax({
                type: "POST",
                url: basePath + "/api/vcode/text/mobile/user/modifyMobilePhone",
                contentType: "application/json;charset=utf-8",
                data: JSON.stringify({mobilePhone:mobile}),
                dataType: "json",
                success: function (result) {
                    if (result.code <= 0) {
                        $("#modify_mobile_phone_msg").text(result.msg);
                        return;
                    }
                    mobileSendVcBtnObj.text("已发送");
                    mobileSendVcBtnObj.css("color", "grey");
                    mobileSendVcBtnObj.unbind("click");
                    //TODO:接入短信网关后删除
                    $("#modify_mobile_phone_msg").text("手机验证码是:"+result.data);
                    g_isSendVC = 1;
                },
                error: function (result) {
                    $("#modify_mobile_phone_msg").text("发送失败,请稍后重试");
                },
                complete: function (data, status) {
                    loading.hideLoading();
                }
            });
        }
    });


    $("#newMobileSendVcBtn").click(function(){
        $("#modify_mobile_phone_msg").text("");
        var mobile = $("#newMobile").val();
        var mobileSendVcBtnObj=$(this);
        if(mobile==null||mobile=="")
        {
            $("#modify_mobile_phone_msg").text("请输入新手机号");
        }else {
            loading.showLoading({
                type: 1,
                tip: "请等待..."
            });
            $.ajax({
                type: "POST",
                url: basePath + "/api/vcode/text/mobile/user/modifyMobilePhone",
                contentType: "application/json;charset=utf-8",
                data: JSON.stringify({mobilePhone:mobile}),
                dataType: "json",
                success: function (result) {
                    if (result.code <= 0) {
                        $("#modify_mobile_phone_msg").text(result.msg);
                        return;
                    }
                    mobileSendVcBtnObj.text("已发送");
                    mobileSendVcBtnObj.css("color", "grey");
                    mobileSendVcBtnObj.unbind("click");
                    //TODO:接入短信网关后删除
                    $("#modify_mobile_phone_msg").text("手机验证码是:"+result.data);
                    g_isSendVC = 1;
                },
                error: function (result) {
                    $("#modify_mobile_phone_msg").text("发送失败,请稍后重试");
                },
                complete: function (data, status) {
                    loading.hideLoading();
                }
            });
        }
    });

    $("#emailSendVcBtn").click(function(){
        $("#modify_mobile_phone_msg").text("");
        var email = $("#email").val();
        var emailSendVcBtnObj=$(this);
        if(email==null||email=="")
        {
            $("#modify_mobile_phone_msg").text("请先绑定邮箱");
        }else {
            loading.showLoading({
                type: 1,
                tip: "请等待..."
            });
            $.ajax({
                type: "POST",
                url: basePath + "/api/vcode/text/email/user/modifyMobilePhone",
                contentType: "application/json;charset=utf-8",
                data: JSON.stringify({email:email}),
                dataType: "json",
                success: function (result) {
                    if (result.code <= 0) {
                        $("#modify_mobile_phone_msg").text(result.msg);
                        return;
                    }
                    emailSendVcBtnObj.text("已发送");
                    emailSendVcBtnObj.css("color", "grey");
                    emailSendVcBtnObj.unbind("click");
                    g_isSendVC=1;
                },
                error: function (result) {
                    $("#modify_mobile_phone_msg").text("发送失败,请稍后重试");
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
        $("#modify_mobile_phone_msg").text("修改成功,"+g_goUserCenterSecond + '秒后跳转到个人中心');
    }
    g_goUserCenterSecond--;
    setTimeout(function () {
        goUserCenterPage();
    }, 1000);

}