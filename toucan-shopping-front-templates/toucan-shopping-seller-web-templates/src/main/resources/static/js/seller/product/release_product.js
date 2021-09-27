$.extend(validatePrompt, {
    title:{
        onFocus:"2-15位字符，可由中文或英文组成",
        succeed:"",
        isNull:"请输入昵称",
        error:{
            badLength:"昵称长度只能在2-15位字符之间",
            badFormat:"昵称只能由中文或英文组成"
        }
    },
    vcode:{
        isNull:"请输入验证码"
    }
});



$.extend(validateFunction, {
    title:function(option) {
        var length = validateRules.betweenLength(option.value.replace(/[^\x00-\xff]/g, "**"), 2, 15);
        var format = validateRules.istitle(option.value);
        if (!length) {
            validateSettings.error.run(option, option.prompts.error.badLength);
            return;
        } else {
            if (!format) {
                validateSettings.error.run(option, option.prompts.error.badFormat);
                return;
            }
        }
        validateSettings.succeed.run(option);
    },
    vcode:function(option) {
        var format = validateRules.isNull(option.value);
        if (format) {
            validateSettings.error.run(option, option.prompts.isNull);
            return;
        }
        validateSettings.succeed.run(option);
    },
    form_validate:function() {
        $("#title").jdValidate(validatePrompt.title, validateFunction.title, true);
        $("#vcode").jdValidate(validatePrompt.vcode, validateFunction.vcode, true);
        return validateFunction.FORM_submit(["#title","#vcode"]);
    }
});


//默认离开获得焦点
setTimeout(function() {
    $("#title").get(0).focus();
}, 0);
//用户名验证
$("#title").jdValidate(validatePrompt.title, validateFunction.title);

setTimeout(function() {
    $("#vcode").get(0).focus();
}, 0);
//验证码验证
$("#vcode").jdValidate(validatePrompt.vcode, validateFunction.vcode);

$("#ueibtn").click(function() {
    var flag = validateFunction.form_validate();
    if (flag) {
        var fields = $('#userEditInfo').serializeArray();
        var params = {}; //声明一个对象
        $.each(fields, function(index, field) {
            params[field.name] = field.value; //通过变量，将属性值，属性一起放到对象中
        });
        $.ajax({
            type: "POST",
            url: basePath+"/api/user/edit/info",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify(params),
            dataType: "json",
            success: function (result) {
                if(result.code<=0)
                {
                    $("#vcode").val("");
                    $("#edit_info_msg").text(result.msg);
                    $("#refreshCaptcha").attr("src",basePath+"/api/user/vcode?"+new Date().getTime());
                }else{
                    window.location.href=basePath+"/page/user/editInfo";
                }
            },
            error: function (result) {
                $("#edit_info_msg").text("修改失败,请稍后重试");
            }
        });
    }
});