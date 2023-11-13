


function bindFileUpload()
{
    $("#bannerImg").on("change", function(){
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


function backShopBanner()
{
    window.location.href=basePath+"/page/shop/banner/list";
}

function saveShopBanner()
{
    if(!checkInputFunction($('#shopBannerBtn'),2)){
        return false;
    }


    var positionVals =[];
    $('input[name="position"]:checked').each(function(){
        positionVals.push($(this).val());
    });

    if(positionVals==null||positionVals.length<=0){
        $.message({
            time:'4000',
            message: "请选择显示位置",
            type: 'error'
        });
    }

    loading.showLoading({
        type:6,
        tip:"保存中..."
    });
    $('#shopBannerForm').ajaxSubmit({
        url: basePath+'/api/shop/banner/update',
        dataType:"json",
        contentType:"application/json;charset=utf-8",
        success: function (data) {
            loading.hideLoading();
            if(data.code==401)
            {
                window.location.href=basePath+data.data;
            }else  if(data.code<=0)
            {
                $.message({
                    time:'4000',
                    message: data.msg,
                    type: 'error'
                });
            }else if(data.code==1)
            {
                window.location.href=basePath+"/page/shop/banner/list";
            }
        }
    });


}
