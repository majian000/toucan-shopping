
function upimg() {
    var pic = $('#upload')[0].files[0];
    var file = new FormData();
    file.append('file', pic);
    $.ajax({
        url: basePath + "/page/api/upload/user/headsculpture",
        type: "post",
        data: file,
        cache: false,
        contentType: false,
        processData: false,
        success: function (resp) {
            clear_file_ipt();
            if (resp.code < 1) {
                $.message({
                    message: resp.msg,
                    type: 'error'
                });
            } else {
                $("#pic").attr("src", resp.data.httpLogo);
            }
        },
        complete:function(){
        }
    });
}

function clear_file_ipt()
{
    $("#file_ipt_p").html("");
    $("#file_ipt_p").html("<input id='upload' name='file' accept='image/*' type='file' style='display: none' />");
    $("#upload").on("change", function() {
        upimg();
    });
}


function bind_upload_event()
{
    $("#upload").on("change", function() {
        upimg();
    });

    $("#pic").click(function() {
        $("#upload").click(); //隐藏了input:file样式后，点击头像就可以本地上传
    });


    $("#logoTips").click(function() {
        $("#upload").click(); //隐藏了input:file样式后，点击头像就可以本地上传
    });
}