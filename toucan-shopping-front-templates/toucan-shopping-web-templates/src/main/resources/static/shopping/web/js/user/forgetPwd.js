

$(function () {

});


function forgetPwdStep2()
{
    var username=$("#username").val();

    if(username=="")
    {
        $("#wrn_msg").show();
        $("#wrn_msg_tip").text("请输入用户名");
        return ;
    }
}