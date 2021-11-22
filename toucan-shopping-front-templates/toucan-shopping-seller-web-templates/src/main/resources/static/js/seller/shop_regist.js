


/**
 * 验证方式为两种
 * 1、数组类型：要求length=1，key：正则表达式，value：提示内容
 * 2、函数：value为文本框的值、验证失败:return 提示文字，验证成功：return true（不能不写）
 */
var checkInput = {
    shopName: [
        /^{2,15}$/, '店铺名必须由2-15位的组成'
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
}

//定时器id
var timer = null;

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



function registUserShop()
{
    var nameValue=$("#name").val();
    var vcodeValue=$("#utm_vcode").val();

    $("#name_msg").text(" ");
    $("#utm_vcode_msg").text(" ");

    if(nameValue=="")
    {
        $("#name_msg").text("请输入店铺名称");
        return ;
    }
    if(vcodeValue=="")
    {
        $("#utm_vcode_msg").text("请输入验证码");
        return ;
    }
    $.ajax({
        type: "POST",
        url: basePath+'/api/user/shop/regist',
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify({"name":nameValue,"vcode":vcodeValue}),
        dataType: "json",
        success: function (data) {
            if(data.code==401)
            {
                window.location.href=basePath+data.data;
            }else  if(data.code==0)
            {
                $("#refreshCaptcha").attr("src",basePath+"/api/user/vcode?"+new Date().getTime());
                $("#tn_msg").text(data.msg);
            }else if(data.code==1)
            {
                window.location.href=basePath+"/page/shop/regist_success";
            }
        },
        error: function (result) {
            $("#refreshCaptcha").attr("src",basePath+"/api/user/vcode?"+new Date().getTime());
            $("#tn_msg").text("请求失败,请重试");
        }
    });

}

$(function () {
    /**
     * 配置验证须知
     * 1、要引入jquery.js、base.js、base.css
     * 2、提交按钮应在<form></form>内部
     * 3、在要验证的input中写上lay-verify="验证方法名|验证方法名" 可写多个但要用|分隔开
     * 4、要添加验证方法可在base.js中添加
     * 5、诺只想引用提示弹出：showTip.success("弹出提示内容");、showTip.fall("弹出提示内容")
     * 6、base.css中可以自行修改弹窗样式
     */
    $('#regBtn').click(function(){
        if(!checkInputFunction($('#regBtn'))){
            return false;
        }
        //下面书写验证成功后执行的内容
        registUserShop();
    });

});