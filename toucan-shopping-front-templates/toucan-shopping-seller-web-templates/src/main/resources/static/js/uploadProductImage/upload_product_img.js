
function upimg(fileId,imgPreviewId,fileGroupId) {
    var pic = $("#"+fileId)[0].files[0];
    var file = new FormData();
    file.append('file', pic);
    $.ajax({
        url: basePath + "/page/api/upload/logo",
        type: "post",
        data: file,
        cache: false,
        contentType: false,
        processData: false,
        success: function (resp) {
            clear_file_ipt(fileId,imgPreviewId,fileGroupId);
            if (resp.code < 1) {
                $.message({
                    message: resp.msg,
                    type: 'error'
                });
            } else {
                $("#"+imgPreviewId).attr("src", resp.data.httpLogo);
            }
        },
        complete:function(){
        }
    });
}

function clear_file_ipt(fileId,imgPreviewId,fileGroupId)
{
    $("#"+fileGroupId).html("");
    $("#"+fileGroupId).html("<input id='"+fileId+"' name='file' accept='image/*' type='file' style='display: none' />");
    $("#"+fileId).on("change", function() {
        upimg(fileId,imgPreviewId,fileGroupId);
    });
}


function bind_upload_event(fileId,tipsId,imgPreviewId,fileGroupId)
{
    $("#"+fileId).on("change", function() {
        upimg(fileId,imgPreviewId,fileGroupId);
    });

    $("#"+imgPreviewId).click(function() {
        $("#"+fileId).click(); //隐藏了input:file样式后，点击头像就可以本地上传
    });

	if(tipsId!=null&&tipsId!="")
	{
		$("#"+tipsId).click(function() {
			$("#"+fileId).click(); //隐藏了input:file样式后，点击头像就可以本地上传
		});
	}
}