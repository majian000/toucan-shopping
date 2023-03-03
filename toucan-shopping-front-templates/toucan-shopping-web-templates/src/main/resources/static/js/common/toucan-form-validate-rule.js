/**
 * 验证方式为两种
 * 1、数组类型：要求length=1，key：正则表达式，value：提示内容
 * 2、函数：value为文本框的值、验证失败:return 提示文字，验证成功：return true（不能不写）
 */
var checkInput = {
    nickName: [
        /^[\u0391-\uFFE5a-zA-Z0-9]{2,15}$/, '必须由2-15位的组成,不能包含特殊符号'
    ],
    password: [
        /^[a-zA-Z0-9_*]{6,15}$/, '密码只能由6-15位的组成,可以包含字母、数字、下划线(_)或星号(*)'
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

