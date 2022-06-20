
$(function () {
    queryUnreadCount();
    setInterval(queryUnreadCount, genBlink()*1000);
});

function genBlink()
{
    var blink = localStorage.getItem('blink');
    if(blink==null)
    {
        blink = Math.floor(Math.random() * 50)+50;
        localStorage.setItem('tss_message_blink',blink);
    }
    return blink;
}

function queryUnreadCount()
{
    if(messageBasePath!="") {
        $.ajax({
            type: "POST",
            url: messageBasePath + "/api/user/message/unread/count",
            data: {"srcType":1},
            dataType: 'json',
            xhrFields: {
                withCredentials: true //允许跨域带Cookie
            },
            success: function(result) {
                if (result.code > 0) {
                    if(result.data>0) {
                        $("#messageCenterMenu").find("#unread").attr("style","color:red;");
                    }else{
                        $("#messageCenterMenu").find("#unread").attr("style","");
                    }
                    $("#messageCenterMenu").find("#unread").text(result.data);
                }
            }
        })
    }

}