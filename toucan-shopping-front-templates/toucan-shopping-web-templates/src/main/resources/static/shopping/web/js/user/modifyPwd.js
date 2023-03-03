
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
        }
    });
});


function bindSendVerifyCodeEvent()
{
    $("#mobileSendVcBtn").click(function(){
        $(this).text("已发送");
        $(this).css("color","grey");
        $(this).unbind("click");
        alert("手机验证码是:1234");
        g_isSendVC=1;
    });


    $("#emailSendVcBtn").click(function(){
        var email = $("#email").val();
        if(email==null||email=="")
        {
            $("#modify_pwd_msg").text("请先绑定邮箱");
        }else {
            $(this).text("已发送");
            $(this).css("color", "grey");
            $(this).unbind("click");
            g_isSendVC=1;
        }
    });
}