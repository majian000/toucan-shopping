$(function () {



});


function forgetPwdStep1(){
    window.location.href=basePath+"/page/user/forget/pwd";
}


function forgetPwdStep3()
{
    var username = $("#step3_username").val();
    var verifyMethod = $("#verifyMethod option:selected").val();
    $.ajax({
        type: "POST",
        url: basePath+"/api/user/send/forget/pwd/vcode",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify({"username":username,"verifyMethod":verifyMethod}),
        dataType: "json",
        success: function (result) {
            if(result.code<=0)
            {
                $("#wrn_msg").show();
                $("#wrn_msg_tip").text(result.msg);
            }else{
                $("#step3_username").val(username);
                $("#step3_verifyMethod").val(verifyMethod);
                $("#step3Submit").click();
            }
        },
        error: function (result) {
            $("#login_msg_c").show();
            $("#login_msg").text("操作失败,请重试");
        }
    });

}