
//定时器id
var toucan_timer = null;
var toucan_wrn_type = 1; //1:默认 2:messagebox





//错误提示框和成功提示框
var showTip = {
    fall: function(value) {
        if(toucan_wrn_type==1) {
            //清楚定时器
            clearTimeout(toucan_timer);
            //移除提示框
            $('[name="checkInputTip"]').remove();
            //设置提示框内容
            var tip = '<div name="checkInputTip" class="checkInputFallTip">' +
                '<span>' + value + '</span>' +
                '</div>';
            //添加提示框
            $('body').append(tip);
            //淡入提示框并震动
            $('[name="checkInputTip"]').fadeIn(20, function () {
                $('[name="checkInputTip"]').addClass('checkInputTipFallHover');
            });
            toucan_timer = setTimeout(cleanTip, 2000);
        }else{
            $.message({
                message: value,
                type: 'error'
            });
        }
    },
    success: function(value) {
        if(toucan_wrn_type==1) {
            //清楚定时器
            clearTimeout(toucan_timer);
            //移除提示框
            $('[name="checkInputTip"]').remove();
            //设置提示框内容
            var tip = '<div name="checkInputTip" class="checkInputSuccessTip">' +
                '<span>' + value + '</span>' +
                '</div>';
            //添加提示框(提示框默认状态为隐藏)
            $('body').append(tip);
            //淡入提示框并震动
            $('[name="checkInputTip"]').fadeIn(20, function () {
                $('[name="checkInputTip"]').addClass('checkInputSuccessTip');
            });
            toucan_timer = setTimeout(cleanTip, 2000);
        }
    }
};

//清除提示
function cleanTip() {
    $('[name="checkInputTip"]').fadeOut(500, function() {
        //移除提示框
        $('[name="checkInputTip"]').remove();
    });
}

//验证函数
function checkInputFunctionByContainerId(containerId,wt) {
    if(wt!=null)
    {
        toucan_wrn_type = wt;
    }
    //定义i (当i=1时则验证失败，i=0验证通过)
    var i = 0;
    //通过传入的el对象获取其为form元素的祖先 并查找所有input对象
    var inputDate = $("#"+containerId).find('input');
    //通过传入的el对象获取其为form元素的祖先 并查找所有包含lay-verify的对象
    var layDate = $("#"+containerId).find('[lay-verify]');
    //遍历查找的到含有lay-verify对象 的集合,
    $.each(layDate, function() {
        //获取它的lay-verify的值
        var key = $(this).attr("lay-verify");
        var keys = key.split("|");
        for (k in keys) {
            var value = $(this).val();
            if (checkInput[keys[k]] != null) {
                if (typeof checkInput[keys[k]] == "function") {
                    //定义函数
                    var keyFunction = checkInput[keys[k]];
                    var retValue = keyFunction(value);
                    if (retValue != true) {
                        //失败提示
                        showTip.fall(retValue);
                        //获取焦点
                        $(this).focus();
                        i++;
                        return false;
                    }
                } else if (typeof checkInput[keys[k]] == "object") {
                    if (!checkInput[keys[k]][0].test(value)) {
                        //失败提示
                        showTip.fall(checkInput[keys[k]][1]);
                        //获取焦点
                        $(this).focus();
                        i++;
                        return false;
                    }
                }
            }
        }
    });
    //当i=1时则验证失败，i=0验证通过
    if (i == 0) {
        return true;
    } else {
        return false;
    }
}

//验证函数
function checkInputFunction(el,wt) {
    if(wt!=null)
    {
        toucan_wrn_type = wt;
    }
    //定义i (当i=1时则验证失败，i=0验证通过)
    var i = 0;
    //通过传入的el对象获取其为form元素的祖先 并查找所有input对象
    var inputDate = el.parents("form").find('input');
    //通过传入的el对象获取其为form元素的祖先 并查找所有包含lay-verify的对象
    var layDate = el.parents("form").find('[lay-verify]');
    //遍历查找的到含有lay-verify对象 的集合,
    $.each(layDate, function() {
        //获取它的lay-verify的值
        var key = $(this).attr("lay-verify");
        var keys = key.split("|");
        for (k in keys) {
            var value = $(this).val();
            if (checkInput[keys[k]] != null) {
                if (typeof checkInput[keys[k]] == "function") {
                    //定义函数
                    var keyFunction = checkInput[keys[k]];
                    var retValue = keyFunction(value);
                    if (retValue != true) {
                        //失败提示
                        showTip.fall(retValue);
                        //获取焦点
                        $(this).focus();
                        i++;
                        return false;
                    }
                } else if (typeof checkInput[keys[k]] == "object") {
                    if (!checkInput[keys[k]][0].test(value)) {
                        //失败提示
                        showTip.fall(checkInput[keys[k]][1]);
                        //获取焦点
                        $(this).focus();
                        i++;
                        return false;
                    }
                }
            }
        }
    })
    //当i=1时则验证失败，i=0验证通过
    if (i == 0) {
        return true;
    } else {
        return false;
    }
}
