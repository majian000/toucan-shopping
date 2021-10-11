$.extend(validatePrompt, {
    productName:{
        onFocus:"2-15位字符，可由中文或英文组成",
        succeed:"",
        isNull:"请输入商品名称",
        error:{
            badLength:"商品名称长度只能在2-15位字符之间",
            badFormat:"商品名称只能由中文或英文组成"
        }
    },
    vcode:{
        isNull:"请输入验证码"
    }
});



$.extend(validateFunction, {
    productName:function(option) {
        var lengthStatus = true;
        if(option.value.length<2||option.value.length>15)
        {
            lengthStatus = false;
        }
        var format = validateRules.isProductName(option.value);
        if (!lengthStatus) {
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
    step2_form_validate:function() {
        $("#productName").jdValidate(validatePrompt.productName, validateFunction.productName, true);
        return validateFunction.FORM_submit(["#productName"]);
    },
    step3_form_validate:function() {
        return true;
    },
    step4_form_validate:function() {
        return true;
    },
    step5_form_validate:function() {
        $("#vcode").jdValidate(validatePrompt.vcode, validateFunction.vcode, true);
        return validateFunction.FORM_submit(["#vcode"]);
    }
});


//默认离开获得焦点
setTimeout(function() {
    $("#productName").get(0).focus();
}, 0);
//名稱验证
$("#productName").jdValidate(validatePrompt.productName, validateFunction.productName);

setTimeout(function() {
    $("#vcode").get(0).focus();
}, 0);
//验证码验证
$("#vcode").jdValidate(validatePrompt.vcode, validateFunction.vcode);





$("#ppfbtn").click(function() {
    var flag = validateFunction.step5_form_validate();
    if (flag) {
        var fields = $('#productReleaseForm').serializeArray();
        var params = {}; //声明一个对象
        $.each(fields, function(index, field) {
            params[field.name] = field.value; //通过变量，将属性值，属性一起放到对象中
        });
        // $.ajax({
        //     type: "POST",
        //     url: basePath+"/api/user/edit/info",
        //     contentType: "application/json;charset=utf-8",
        //     data: JSON.stringify(params),
        //     dataType: "json",
        //     success: function (result) {
        //         if(result.code<=0)
        //         {
        //             $("#vcode").val("");
        //             $("#edit_info_msg").text(result.msg);
        //             $("#refreshCaptcha").attr("src",basePath+"/api/user/vcode?"+new Date().getTime());
        //         }else{
        //             window.location.href=basePath+"/page/user/info";
        //         }
        //     },
        //     error: function (result) {
        //         $("#edit_info_msg").text("修改失败,请稍后重试");
        //     }
        // });
    }
});