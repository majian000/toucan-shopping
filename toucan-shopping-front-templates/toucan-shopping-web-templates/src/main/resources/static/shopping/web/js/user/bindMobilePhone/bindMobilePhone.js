
$(function () {

    $("#mobilePhoneSendVcBtn").click(function(){
        $("#bind_mobilePhone_msg").text("");
        $("#bind_mobilePhone_msg").css("color","#ff4e00;");
        var mobilePhone = $("#mobilePhone").val();
        var mobilePhoneSendVcBtnObj=$(this);
        if(mobilePhone==null||mobilePhone=="")
        {
            $("#bind_mobilePhone_msg").text("请输入邮箱地址");
            return;
        }

        if(!checkInput.mobilePhone[0].test(mobilePhone))
        {
            $("#bind_mobilePhone_msg").text(checkInput.mobilePhone[1]);
            return;
        }
        loading.showLoading({
            type: 1,
            tip: "请等待..."
        });
        var params = {}; //声明一个对象
        params.mobilePhone = mobilePhone;
        $.ajax({
            type: "POST",
            url: basePath + "/api/vcode/text/mobile/user/bindMobilePhone",
            contentType: "application/json;charset=utf-8",
            data:  JSON.stringify(params),
            dataType: "json",
            success: function (result) {
                if (result.code <= 0) {
                    $("#bind_mobilePhone_msg").text(result.msg);
                    return;
                }
                //TODO:接入短信网关后这里注释掉
                $("#bind_mobilePhone_msg").text(result.data);
                mobilePhoneSendVcBtnObj.text("已发送");
                mobilePhoneSendVcBtnObj.css("color", "grey");
                mobilePhoneSendVcBtnObj.unbind("click");
                g_isSendVC=1;
            },
            error: function (result) {
                $("#bind_mobilePhone_msg").text("发送失败,请稍后重试");
            },
            complete: function (data, status) {
                loading.hideLoading();
            }
        });

    });


    $("#ueibtn").click(function() {
        $("#bind_mobilePhone_msg").css("color","#ff4e00;");

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
            url: basePath+"/api/user/bind/mobilePhone/bind",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify(params),
            dataType: "json",
            success: function (result) {
                if(result.code<=0)
                {
                    $("#bind_mobilePhone_msg").text(result.msg);
                }else{
                    $("#bind_mobilePhone_msg").css("color","green");
                    $("#bind_mobilePhone_msg").text("绑定成功");
                }
            },
            error: function (result) {
                $("#bind_mobilePhone_msg").text("修改失败,请稍后重试");
            },
            complete:function(data,status){
                loading.hideLoading();
            }
        });
    });


});
