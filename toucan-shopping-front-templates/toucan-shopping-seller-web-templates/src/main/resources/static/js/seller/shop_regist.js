
var g_countdownNum = 60;


//定时器id
var timer = null;




function registUserShop()
{
    var nameValue=$("#name").val();
    var vcodeValue=$("#verifyCode").val();

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
                $("#tn_msg").show();
                $("#tn_msg").text(data.msg);
            }else if(data.code==1)
            {
                window.location.href=basePath+"/index";
            }
        },
        error: function (result) {
            $("#tn_msg").show();
            $("#tn_msg").text("申请失败,请重试");
        }
    });

}






function send_verify_code(mobilePhone)
{
    $.post(basePath+"/api/shop/sendRegistVerifyCode",{mobilePhone:mobilePhone},function(result){
        if(result.code<=0)
        {
            $("#tn_msg").show();
            $("#tn_msg").text(result.msg);
        }else{
            countdown();
        }
    });
}



function getvcode()
{
    $("#verifyCode").removeAttr("lay-verify");
    if(!checkInputFunction($('#regBtn'))){
        return false;
    }

    $("#verifyCode").attr("lay-verify","required");

    //todo:临时代码
    $("#tn_msg").show();
    $("#tn_msg").text("验证码为1234");

    send_verify_code($("#registShopPhone").val());

}


function countdown() {
    if(g_countdownNum == 0) {
        $("#get_vcode").attr("onclick","getvcode();");
        $("#get_vcode").val('获取验证码');
        g_countdownNum = 60;
        return;
    } else {
        $("#get_vcode").removeAttr("onclick");
        $("#get_vcode").val(g_countdownNum + '秒后重发');
        g_countdownNum--;
    }

    setTimeout(function () {
        countdown();
    }, 1000);
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


    $("#get_vcode").attr("onclick","getvcode();");

});