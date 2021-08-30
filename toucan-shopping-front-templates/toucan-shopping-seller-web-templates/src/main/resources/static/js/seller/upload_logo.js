
function upimg() {
    var pic = $('#upload')[0].files[0];
    var file = new FormData();
    file.append('file', pic);
    $.ajax({
        url: basePath+"/page/api/upload/logo",
        type: "post",
        data: file,
        cache: false,
        contentType: false,
        processData: false,
        success: function(resp) {
            if(resp.code==1) {
                $("#resimg").append("<img src='" + resp.data.httpLogo + "' id='pic' width='90' height='90'/>")
            }
        }
    });
}