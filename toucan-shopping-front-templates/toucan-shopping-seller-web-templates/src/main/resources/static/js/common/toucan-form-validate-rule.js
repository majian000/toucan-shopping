/**
 * 验证方式为两种
 * 1、数组类型：要求length=1，key：正则表达式，value：提示内容
 * 2、函数：value为文本框的值、验证失败:return 提示文字，验证成功：return true（不能不写）
 */
var checkInput = {
    shopName: [
        /^[\u0391-\uFFE5a-zA-Z0-9]{2,15}$/, '必须由2-15位的组成,不能包含特殊符号'
    ],
    productName: [
        /^[\u0391-\uFFE5a-zA-Z0-9]{1,100}$/, '必须由2-15位的组成,不能包含特殊符号'
    ],
    productCount:[
        /^[1-9\d]{1,7}/, '必须为正整数,长度最小1位,最大7位'
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