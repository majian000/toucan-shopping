

$(function () {

    $("#go_step2_btn").click(function(){
        $("#wrn_msg").hide();
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

        loading.showLoading({
            type: 1,
            tip: "请等待..."
        });
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
                    $(".mobileMethod").hide();
                    $(".emailMethod").hide();
                    $(".idcardMethod").hide();
                    $(".passportMethod").hide();
                    $(".overseasMethod").hide();

                    if(result.data.mobileMethod==1)
                    {
                        $(".mobileMethod").show();
                    }
                    if(result.data.emailMethod==1)
                    {
                        $(".emailMethod").show();
                    }
                    if(result.data.idcardMethod==1)
                    {
                        $(".idcardMethod").show();
                    }
                    if(result.data.passportMethod==1)
                    {
                        $(".passportMethod").show();
                    }
                    if(result.data.overseasMethod==1)
                    {
                        $(".overseasMethod").show();
                    }

                    $(".forgetPwdStep2").show();
                }
            },
            error: function (result) {
                $("#wrn_msg").show();
                $("#wrn_msg_tip").text("提交失败,请重试");
            },
            complete: function (data, status) {
                loading.hideLoading();
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
                    $(".step3_vcode_panel").hide();
                    $(".step3_idcard_panel").hide();

                    if(verifyMethod==0||verifyMethod==1)
                    {
                        $(".step3_vcode_panel").show();
                    }
                    if(verifyMethod==2||verifyMethod==3||verifyMethod==4)
                    {
                        $(".step3_idcard_panel").show();
                    }
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


    $("#forgetPwdBtn").click(function(){
        $("#wrn_msg").hide();
        var params={};
        var verifyMethod = $("#verifyMethod option:selected").val();
        var password = $("#password").val();
        if(password=="")
        {
            $("#wrn_msg").show();
            $("#wrn_msg_tip").text("请输入密码");
            return ;
        }
        var confirm_password = $("#confirm_password").val();
        if(confirm_password=="")
        {
            $("#wrn_msg").show();
            $("#wrn_msg_tip").text("请输入确认密码");
            return ;
        }
        if(password!=confirm_password)
        {
            $("#wrn_msg").show();
            $("#wrn_msg_tip").text("密码与确认密码输入不一致");
            return ;
        }
        if(verifyMethod==0||verifyMethod==1)
        {
            var vcode=$("#step3_vcode").val();
            if(vcode==null)
            {
                $("#wrn_msg").show();
                $("#wrn_msg_tip").text("请输入验证码");
                return ;
            }
            params.vcode=vcode;
        }
        if(verifyMethod==2||verifyMethod==3||verifyMethod==4)
        {
            var step3_true_name=$("#step3_true_name").val();
            if(step3_true_name==null)
            {
                $("#wrn_msg").show();
                $("#wrn_msg_tip").text("请输入姓名");
                return ;
            }
            var step3_id_card=$("#step3_id_card").val();
            if(step3_id_card==null)
            {
                $("#wrn_msg").show();
                $("#wrn_msg_tip").text("请输入证件号码");
                return ;
            }
            params.trueName=step3_true_name;
            params.idCard=step3_id_card;
        }

        loading.showLoading({
            type: 1,
            tip: "请等待..."
        });
        var username = $("#step1_username").val();
        params.username=username;
        params.verifyMethod=verifyMethod;
        params.password=password;
        params.confirmPassword=confirm_password;
        $.ajax({
            type: "POST",
            url: basePath+"/api/user/forget/pwd/modify/pwd",
            contentType: "application/json;charset=utf-8",
            data:  JSON.stringify(params),
            dataType: "json",
            success: function (result) {
                if(result.code==1)
                {
                    $(".forgetPwdStep3").hide();
                    $(".forgetPwdStep4").show();
                }else{
                    $("#wrn_msg").show();
                    $("#wrn_msg_tip").text(result.msg);
                }
            },
            error: function (result) {
                $("#wrn_msg").show();
                $("#wrn_msg_tip").text("操作失败,请重试");
            },
            complete: function (data, status) {
                loading.hideLoading();
            }
        });
    });


});

function flushVcode()
{
    $("#refreshCaptcha").attr("src",basePath+"/api/user/forget/pwd/vcode?"+new Date().getTime());
}
