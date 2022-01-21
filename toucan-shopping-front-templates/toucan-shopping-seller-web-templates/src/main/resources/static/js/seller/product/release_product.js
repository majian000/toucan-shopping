$(function () {


    $.Tupload.init({
        title: "",
        fileNum: 6, // 上传文件数量
        divId: "productPreviewImages", // div  id
        accept: "image/jpeg,image/x-png,image/x-jpg", // 上传文件的类型
        fileSize: 2 * 1048576,     // 上传文件的大小
        onSuccess: function (data, i) {
            console.log(data)
        },
        onDelete: function (i) {

        }
    });

    $("#refreshCaptcha").bind( 'click' ,function(){
        $("#refreshCaptcha").attr("src",basePath+"/api/user/vcode?"+new Date().getTime());
    });


    $("#step2Back").bind( 'click' ,function(){
        $("#step5").hide();
        $("#step4").hide();
        $("#step3").hide();
        $("#step2").hide();
        $("#step1").show();
    });

    $("#step2Next").bind( 'click' ,function(){
        var result = checkInputFunctionByContainerId("step2",2);
        if(result) {
            $("#step5").hide();
            $("#step4").hide();
            $("#step3").show();
            $("#step2").hide();
            $("#step1").hide();
        }
    });


    $("#step3Back").bind( 'click' ,function(){
        $("#step5").hide();
        $("#step4").hide();
        $("#step3").hide();
        $("#step2").show();
        $("#step1").hide();
    });

    $("#step3Next").bind( 'click' ,function(){
        $("#step5").hide();
        $("#step4").show();
        $("#step3").hide();
        $("#step2").hide();
        $("#step1").hide();
    });

    $("#step4Back").bind( 'click' ,function(){
        $("#step5").hide();
        $("#step4").hide();
        $("#step3").show();
        $("#step2").hide();
        $("#step1").hide();
    });

    $("#step4Next").bind( 'click' ,function(){
        $("#step5").show();
        $("#step4").hide();
        $("#step3").hide();
        $("#step2").hide();
        $("#step1").hide();
    });


    $("#step5Back").bind( 'click' ,function(){
        $("#step5").hide();
        $("#step4").show();
        $("#step3").hide();
        $("#step2").hide();
        $("#step1").hide();
    });
});


$("#ppfbtn").click(function() {

    loading.showLoading({
        type:1,
        tip:"发布中..."
    });

    $('#productReleaseForm').ajaxSubmit({
        url: basePath+'/api/shop/product/release',
        dataType:"json",
        contentType:"application/json;charset=utf-8",
        success: function (data) {
            if(result.code<=0)
            {
                $("#vcode").val("");
                $("#edit_info_msg").text(result.msg);
                $("#refreshCaptcha").attr("src",basePath+"/api/user/vcode?"+new Date().getTime());
            }else{
                window.location.href=basePath+"/page/user/info";
            }
        }
    });

});