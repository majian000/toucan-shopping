
var g_isSendVC=0;
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
});


function bindSendVerifyCodeEvent()
{
    $("#mobileSendVcBtn").click(function(){
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
                        $("#modify_pwd_msg").text("生成失败,请稍后重试");
                        return;
                    }
                    mobileSendVcBtnObj.text("已发送");
                    mobileSendVcBtnObj.css("color", "grey");
                    mobileSendVcBtnObj.unbind("click");
                    alert("手机验证码是:1234");
                    g_isSendVC = 1;
                },
                error: function (result) {
                    $("#modify_pwd_msg").text("生成失败,请稍后重试");
                },
                complete: function (data, status) {
                    loading.hideLoading();
                }
            });
        }
    });


    $("#emailSendVcBtn").click(function(){
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
                        $("#modify_pwd_msg").text("生成失败,请稍后重试");
                        return;
                    }
                    emailSendVcBtnObj.text("已发送");
                    emailSendVcBtnObj.css("color", "grey");
                    emailSendVcBtnObj.unbind("click");
                    g_isSendVC=1;
                },
                error: function (result) {
                    $("#modify_pwd_msg").text("生成失败,请稍后重试");
                },
                complete: function (data, status) {
                    loading.hideLoading();
                }
            });

        }
    });
}