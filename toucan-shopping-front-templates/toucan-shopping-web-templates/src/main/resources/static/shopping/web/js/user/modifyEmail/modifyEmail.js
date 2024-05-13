
var g_goUserCenterSecond=5;

$(function () {

    $("#emailSendVcBtn").click(function(){
        $("#modify_email_msg").text("");
        $("#modify_email_msg").css("color","#ff4e00;");
        var email = $("#email").val();
        var oldEmail = $("#old_email").val();
        var emailSendVcBtnObj=$(this);
        if(email==null||email=="")
        {
            $("#modify_email_msg").text("请输入邮箱地址");
            return;
        }
        if(oldEmail==email){
            $("#modify_email_msg").text("新邮箱地址不能与旧邮箱地址一致");
            return;
        }
        if(!checkInput.email[0].test(email))
        {
            $("#modify_email_msg").text(checkInput.email[1]);
            return;
        }
        loading.showLoading({
            type: 1,
            tip: "请等待..."
        });
        var params = {}; //声明一个对象
        params.email = email;
        $.ajax({
            type: "POST",
            url: basePath + "/api/vcode/text/email/user/modifyEmail",
            contentType: "application/json;charset=utf-8",
            data:  JSON.stringify(params),
            dataType: "json",
            success: function (result) {
                if (result.code <= 0) {
                    $("#modify_email_msg").text(result.msg);
                    return;
                }
                emailSendVcBtnObj.text("已发送");
                emailSendVcBtnObj.css("color", "grey");
                emailSendVcBtnObj.unbind("click");
                g_isSendVC=1;
            },
            error: function (result) {
                $("#modify_email_msg").text("发送失败,请稍后重试");
            },
            complete: function (data, status) {
                loading.hideLoading();
            }
        });
    
    });
    
    $("#ueibtn").click(function() {
        $("#modify_email_msg").css("color","#ff4e00;");
    
        if(!checkInputFunction($('#ueibtn'),2)){
            return false;
        }
    
        var fields = $('#modifyEmailForm').serializeArray();
        var params = {}; //声明一个对象
        $.each(fields, function(index, field) {
            params[field.name] = field.value; //通过变量，将属性值，属性一起放到对象中
        });
    
        loading.showLoading({
            type:1,
            tip:"提交中..."
        });
        $.ajax({
            type: "POST",
            url: basePath+"/api/user/modify/email/modify",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify(params),
            dataType: "json",
            success: function (result) {
                if(result.code<=0)
                {
                    $("#modify_email_msg").text(result.msg);
                }else{
                    $("#modify_email_msg").css("color","green");
                    goUserCenterPage();
                }
            },
            error: function (result) {
                $("#modify_email_msg").text("修改失败,请稍后重试");
            },
            complete:function(data,status){
                loading.hideLoading();
            }
        });
    });


});


function goUserCenterPage()
{
    if(g_goUserCenterSecond<=0)
    {
        window.location.href=basePath+"/page/user/info";
    }else
    {
        $("#modify_email_msg").text("修改成功,"+g_goUserCenterSecond + '秒后跳转到个人中心');
    }
    g_goUserCenterSecond--;
    setTimeout(function () {
        goUserCenterPage();
    }, 1000);

}