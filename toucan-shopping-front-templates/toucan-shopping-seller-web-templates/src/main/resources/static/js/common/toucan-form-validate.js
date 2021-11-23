
/**
 * 验证方式为两种
 * 1、数组类型：要求length=1，key：正则表达式，value：提示内容
 * 2、函数：value为文本框的值、验证失败:return 提示文字，验证成功：return true（不能不写）
 */
var checkInput = {
    shopName: [
        /^[\u0391-\uFFE5a-zA-Z0-9]{2,15}$/, '必须由2-15位的组成,不能包含特殊符号'
    ],
    phone: [
        /^1(3|4|5|6|7|8|9)\d{9}$/, '请输入正确的电话号码'
    ],
    required: function(value) {
        if (value == "" || value == null) {
            return "必填项不能为空";
        } else {
            return true;
        }
    }
};



//错误提示框和成功提示框
var showTip = {
    fall: function(value) {
        //清楚定时器
        clearTimeout(timer);
        //移除提示框
        $('[name="checkInputTip"]').remove();
        //设置提示框内容
        var tip = '<div name="checkInputTip" class="checkInputFallTip">' +
            '<span>' + value + '</span>' +
            '</div>';
        //添加提示框
        $('body').append(tip);
        //淡入提示框并震动
        $('[name="checkInputTip"]').fadeIn(20, function() {
            $('[name="checkInputTip"]').addClass('checkInputTipFallHover');
        });;
        timer = setTimeout(cleanTip, 2000);
    },
    success: function(value) {
        //清楚定时器
        clearTimeout(timer);
        //移除提示框
        $('[name="checkInputTip"]').remove();
        //设置提示框内容
        var tip = '<div name="checkInputTip" class="checkInputSuccessTip">' +
            '<span>' + value + '</span>' +
            '</div>';
        //添加提示框(提示框默认状态为隐藏)
        $('body').append(tip);
        //淡入提示框并震动
        $('[name="checkInputTip"]').fadeIn(20, function() {
            $('[name="checkInputTip"]').addClass('checkInputSuccessTip');
        });
        timer = setTimeout(cleanTip, 2000);
    }
}

//清除提示
function cleanTip() {
    $('[name="checkInputTip"]').fadeOut(500, function() {
        //移除提示框
        $('[name="checkInputTip"]').remove();
    });
}


//验证函数
function checkInputFunction(el) {
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
