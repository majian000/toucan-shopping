


function bindFileUpload()
{
    $("#logo").on("change", function(){
        // Get a reference to the fileList
        var files = !!this.files ? this.files : [];

        // If no files were selected, or no FileReader support, return
        if (!files.length || !window.FileReader) {
            $("#pic").attr("src",$("#shopLogo").val());
            $("#isUpload").val("0");
            return;
        }

        // Only proceed if the selected file is an image
        if (/^image/.test( files[0].type)){
           // Create a new instance of the FileReader
            var reader = new FileReader();

            // Read the local file as a DataURL
            reader.readAsDataURL(files[0]);

            // When loaded, set image data as background of div
            reader.onloadend = function(){
                $("#pic").attr("src",this.result);
                $("#isUpload").val("1");
            }

        }

    });
}

function updateUserShop()
{
    if(!checkInputFunction($('#updateUserShopBtn'),2)){
        return false;
    }

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
                $("#tn_msg").show();
                $("#tn_msg").text(data.msg);
            }else if(data.code==1)
            {
                window.location.href=basePath+"/index";
            }
        }
    });


}
