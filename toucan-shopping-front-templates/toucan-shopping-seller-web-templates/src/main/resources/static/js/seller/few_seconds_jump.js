

function initCountdownJumpPage(url,seconds,tipsId,pageName)
{
    var count=5;
    var interValObj;
    function countdown() {
        if (count == 0) {
            window.clearInterval(interValObj);//停止计时器
            window.location = url;
        }
        else {
            count--;
            $("#"+tipsId).text("系统会在" + count +"秒后自动跳转"+pageName+"！");
        }
    }

    //开始倒计时
    interValObj = window.setInterval(countdown, 1000);

}