
$(function () {

    $("#emailSendVcBtn").click(function(){
        $("#bind_meail_msg").text("");
        $("#bind_meail_msg").css("color","#ff4e00;");
        var email = $("#email").val();
        var emailSendVcBtnObj=$(this);
        if(email==null||email=="")
        {
            $("#bind_meail_msg").text("请输入邮箱地址");
            return;
        }

        if(!checkInput.email[0].test(email))
        {
            $("#bind_meail_msg").text(checkInput.email[1]);
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
            url: basePath + "/api/vcode/text/email/user/bindEmail",
            contentType: "application/json;charset=utf-8",
            data:  JSON.stringify(params),
            dataType: "json",
            success: function (result) {
                if (result.code <= 0) {
                    $("#bind_meail_msg").text(result.msg);
                    return;
                }
                emailSendVcBtnObj.text("已发送");
                emailSendVcBtnObj.css("color", "grey");
                emailSendVcBtnObj.unbind("click");
                g_isSendVC=1;
            },
            error: function (result) {
                $("#bind_meail_msg").text("发送失败,请稍后重试");
            },
            complete: function (data, status) {
                loading.hideLoading();
            }
        });
    
    });
    
    $("#ueibtn").click(function() {
        $("#bind_meail_msg").css("color","#ff4e00;");
    
        if(!checkInputFunction($('#ueibtn'),2)){
            return false;
        }
    
        var fields = $('#bindEmailForm').serializeArray();
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
            url: basePath+"/api/user/bind/email/bind",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify(params),
            dataType: "json",
            success: function (result) {
                if(result.code<=0)
                {
                    $("#bind_meail_msg").text(result.msg);
                }else{
                    $("#bind_meail_msg").css("color","green");
                    $("#bind_meail_msg").text("绑定成功");
                }
            },
            error: function (result) {
                $("#bind_meail_msg").text("修改失败,请稍后重试");
            },
            complete:function(data,status){
                loading.hideLoading();
            }
        });
    });


});
