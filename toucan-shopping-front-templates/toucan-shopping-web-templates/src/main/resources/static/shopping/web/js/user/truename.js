


function submitTrueName()
{
    var mobilePhoneValue=$("#regist_mobile_phone").val();
    var passwordValue=$("#regist_password").val();
    var confirmPasswordValue=$("#regist_confirmPassword").val();
    var vcodeValue=$("#regist_vcode").val();
    $.post(basePath+"/api/user/regist",{mobilePhone:mobilePhoneValue,password:passwordValue,confirmPassword:confirmPasswordValue,vcode:vcodeValue},function(result){
        if(result.code<=0)
        {
            $("#regist_msg").text(result.msg);
            $("#regist_msg").css("color","red");
        }else{
            goLoginPage(result.msg);
        }
    });
}
