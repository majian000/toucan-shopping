
function bindFileUpload()
{
    $("#headSculptureFile").on("change", function(){
        // Get a reference to the fileList
        var files = !!this.files ? this.files : [];

        // If no files were selected, or no FileReader support, return
        if (!files.length || !window.FileReader) {
            $("#pic").attr("src",$("#httpHeadSculpture").val());
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

$(function () {


    bindFileUpload();

    $("#upbtn").click(function(){
        var headSculptureFileValue = $("#headSculptureFile").val();
        if(headSculptureFileValue==null||headSculptureFileValue=="")
        {
            $("#tn_msg").attr("style","color:#ff4e00;");
            $("#tn_msg").text("请先上传头像");
            return;
        }

        loading.showLoading({
            type:1,
            tip:"上传中..."
        });
        $('#uhnform').ajaxSubmit({
            url: basePath+"/api/user/head/sculpture/approve/save",
            type:'POST',
            success: function (data) {
                if(data.code==401)
                {
                    window.location.href=basePath+data.data;
                }else  if(data.code==0)
                {
                    $("#tn_msg").attr("style","color:#ff4e00;");
                    $("#tn_msg").text(data.msg);
                }else if(data.code==1)
                {
                    $("#tn_msg").attr("style","color:#008000;");
                    $("#tn_msg").text("上传成功,请等待工作人员审核!");
                }
            },
            complete:function(data,status){
                loading.hideLoading();
            }
        });
    });


});