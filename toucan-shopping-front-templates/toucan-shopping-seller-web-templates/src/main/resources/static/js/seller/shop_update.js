

function getFileUrl(file) {
    var url = null ;
    if (window.createObjectURL!=undefined) { // basic
        url = window.createObjectURL(file) ;
    } else if (window.URL!=undefined) { // mozilla(firefox)
        url = window.URL.createObjectURL(file) ;
    } else if (window.webkitURL!=undefined) { // webkit or chrome
        url = window.webkitURL.createObjectURL(file) ;
    }
    return url ;
}


function bindFileUpload()
{
    $("#logo").on("change",function(){
        var fileUrl = getFileUrl(this.files[0]);
        if(fileUrl!=null) {
            $("#pic").attr("src", fileUrl);
            $("#isUpload").val("1");
        }else{
            $("#isUpload").val("0");
        }
    });
}

function updateUserShop()
{

    $('#usform').ajaxSubmit({
        url: basePath+'/api/user/shop/edit',
        type:'POST',
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
                window.location.href=basePath+"/index";
            }
        }
    });


}
