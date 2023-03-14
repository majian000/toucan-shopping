

$(function () {

    $("#go_step2_btn").click(function(){
        var username=$("#step1_username").val();

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
            url: basePath+"/api/user/forget/pwd/check/username",
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
                    $("#wrn_msg").hide();
                    $(".forgetPwdStep1").hide();
                    $(".mobileMethod").show();
                    $(".emailMethod").show();
                    $(".idcardMethod").show();
                    if(result.data.mobileMethod==0)
                    {
                        $(".mobileMethod").hide();
                    }
                    if(result.data.emailMethod==0)
                    {
                        $(".emailMethod").hide();
                    }
                    if(result.data.idcardMethod==0)
                    {
                        $(".idcardMethod").hide();
                    }
                    $(".forgetPwdStep2").show();
                }
            },
            error: function (result) {
                $("#wrn_msg").show();
                $("#wrn_msg_tip").text("提交失败,请重试");
            }
        });
    });


    $("#back_step1_btn").click(function(){
        $(".forgetPwdStep2").hide();
        flushVcode();
        $("#step1Form")[0].reset();
        $("#wrn_msg").hide();
        $(".forgetPwdStep1").show();
    });

    $("#go_step3_btn").click(function(){
        var username = $("#step1_username").val();
        var verifyMethod = $("#verifyMethod option:selected").val();
        $.ajax({
            type: "POST",
            url: basePath+"/api/user/forget/pwd/send/vcode",
            contentType: "application/json;charset=utf-8",
            data:  JSON.stringify({"username":username,"verifyMethod":verifyMethod}),
            dataType: "json",
            success: function (result) {
                if(result.code==1)
                {
                    $(".forgetPwdStep2").hide();
                    $(".forgetPwdStep3").show();
                }else{
                    $("#wrn_msg").show();
                    $("#wrn_msg_tip").text(result.msg);

                }
            },
            error: function (result) {
                $("#wrn_msg").show();
                $("#wrn_msg_tip").text("操作失败,请重试");
            }
        });
    });


    $("#back_step2_btn").click(function(){
        $(".forgetPwdStep3").hide();
        $("#step2Form")[0].reset();
        $("#wrn_msg").hide();
        $(".forgetPwdStep2").show();
    });

});

function flushVcode()
{
    $("#refreshCaptcha").attr("src",basePath+"/api/user/forget/pwd/vcode?"+new Date().getTime());
}
