$(function () {

    $("#upbtn").click(function(){

        $('#avatar-modal').modal('show');
    });

    $('#avatarInput').on('change', function(e) {
        var filemaxsize = 1024 * 5;//5M
        var target = $(e.target);
        var Size = target[0].files[0].size / 1024;
        if(Size > filemaxsize) {
            alert('图片过大，请重新选择!');
            $(".avatar-wrapper").childre().remove;
            return false;
        }
        if(!this.files[0].type.match(/image.*/)) {
            alert('请选择正确的图片!')
        } else {
            var filename = document.querySelector("#avatar-name");
            var texts = document.querySelector("#avatarInput").value;
            var teststr = texts; //你这里的路径写错了
            testend = teststr.match(/[^\\]+\.[^\(]+/i); //直接完整文件名的
            filename.innerHTML = testend;
        }

    });

    $(".avatar-save").on("click", function() {
        var img_lg = document.getElementById('imageHead');
        // 截图小的显示框内的内容
        html2canvas(img_lg, {
            allowTaint: true,
            taintTest: false,
            outputType:"png",
            onrendered: function(canvas) {
                canvas.id = "mycanvas";
                //生成base64图片数据
                var dataUrl = canvas.toDataURL("image/jpeg");
                var newImg = document.createElement("img");
                newImg.src = dataUrl;
                imagesAjax(dataUrl)
            }
        });
    });

    function imagesAjax(imgData) {
        loading.showLoading({
            type:1,
            tip:"上传中..."
        });
        $("#headSculptureBase64").val(imgData);
        $('#uhnform').ajaxSubmit({
            url: basePath+"/api/user/head/sculpture/approve/save",
            type:'POST',
            success: function (data) {
                if(data.code==401)
                {
                    window.location.href=basePath+data.data;
                }else  if(data.code==0)
                {
                    $("#tn_msg").text(data.msg);
                }else if(data.code==1)
                {
                    window.location.href=basePath+"/page/user/head/sculpture/approve/page";
                }
            },
            complete:function(data,status){
                loading.hideLoading();
            }
        });
    }

});