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
        var length = validateRules.betweenLength(option.value.replace(/[^\x00-\xff]/g, "**"), 2, 15);
        var format = validateRules.isProductName(option.value);
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
    step2_form_validate:function() {
        $("#productName").jdValidate(validatePrompt.productName, validateFunction.productName, true);
        return validateFunction.FORM_submit(["#productName"]);
    },
    step3_form_validate:function() {
        return true;
    },
    step4_form_validate:function() {
        return true;
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
