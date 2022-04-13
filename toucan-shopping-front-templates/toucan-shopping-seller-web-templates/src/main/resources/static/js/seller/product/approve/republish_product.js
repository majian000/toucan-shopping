
var g_goApproveSecond=3;
function showSetp2Page()
{
    $("#step1").hide();
    $("#step2").show();
}

function initProductPublishForm(productApprove)
{

    //设置选择分类默认值
    var selectCategoryArray = productApprove.categoryIdPath;
    for(var i=(selectCategoryArray.length-1);i>=0;i--)
    {
        $("#category_"+selectCategoryArray[i]).click();
    }

    //设置选择品牌默认值
    gSelectBrandId = productApprove.brandId;
    $("#selectBrandDiv").empty();
    $("#selectBrandDiv").append("<input type=\"text\" name=\"brandId\" id=\"selectBrand\" data-init=\""+productApprove.brandId+"\" >");
    initBrandListControlDefalutValue("selectBrandDiv","brandId","selectBrand",selectCategoryArray[0]);

    $("#articleNumber").val(productApprove.articleNumber);

    $("#name").val(productApprove.name);
    $("#sellerNo").val(productApprove.sellerNo);

    //设置商品主图
    $("#mainPhotoImg").attr("src",productApprove.httpMainPhotoFilePath);

    //设置商品预览图
    if(productApprove.httpPreviewPhotoPaths!=null)
    {
        $("#fileNum").val(productApprove.httpPreviewPhotoPaths.length);
        for(var i=0;i<productApprove.httpPreviewPhotoPaths.length;i++)
        {
            $("#img_src"+i).attr("src",productApprove.httpPreviewPhotoPaths[i]);
            $("#imgBg_div"+i).attr("data-src",productApprove.httpPreviewPhotoPaths[i]);
            $("#uploading-tip"+i).attr("style","");
        }
    }

}

function initPage()
{

    loading.showLoading({
        type:6,
        tip:"查询中..."
    });

    $.ajax({
        type: "POST",
        url: basePath+"/api/shop/product/approve/detail",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify({id:$("#approveId").val()}),
        dataType: "json",
        success: function (result) {
            if(result.code<=0)
            {
                $.message({
                    message: "查询失败,请稍后重试",
                    type: 'error'
                });
                $("#approveId").val("");
                return ;
            }
            if(result.data.categoryIdPath==null||result.data.categoryIdPath.length==0)
            {
                $.message({
                    message: "查询失败,请稍后重试",
                    type: 'error'
                });
                $("#approveId").val("");
                return ;
            }
            initProductPublishForm(result.data)
        },
        error: function (result) {
            $.message({
                message: "查询失败,请稍后重试",
                type: 'error'
            });
            $("#approveId").val("");
        },
        complete:function()
        {
            loading.hideLoading();
        }

    });
}

$(function () {

    intRootCategory();

    initPage();

    $("#mainPhotoFile").on("change", function(){
        // Get a reference to the fileList
        var files = !!this.files ? this.files : [];

        // If no files were selected, or no FileReader support, return
        if (!files.length || !window.FileReader) {
            $("#mainPhotoImg").attr("src","/static/lib/tupload/images/imgadd.png");
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
                $("#mainPhotoImg").attr("src",this.result);
            }

        }

    });


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

        var skuTablePhotos = $(".skuTablePhotos");
        if(skuTablePhotos==null||skuTablePhotos.length<=0)
        {
            $.message({
                message: "请选择商品属性",
                type: 'error'
            });
            return ;
        }
        if(skuTablePhotos.length>0)
        {
            for(var i=0;i<skuTablePhotos.length;i++)
            {
                var skuTablePhoto = $(skuTablePhotos[i]).val();
                if(skuTablePhoto==null||skuTablePhoto=="")
                {
                    $.message({
                        message: "请上传销售规格中的商品图片",
                        type: 'error'
                    });
                    return ;
                }
            }
        }

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
        $("#refreshCaptcha").attr("src",basePath+"/api/shop/product/vcode?"+new Date().getTime());
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


/**
 * 输入商品库存数量
 */
function inputStock(o)
{
    var num=0;
    $(".skuStockInput").each(function (i, n) {
        var stockVal = $(this).val();
        if(stockVal!=null&&stockVal!="") {
            num += parseInt($(this).val());
        }
    });
    $("#num").val(num);
}


function goApprovePage(msg)
{
    if(g_goApproveSecond<=0)
    {
        window.location.href=basePath+"/index";
    }else
    {
        $("#publish_msg_c").show();
        $("#publish_msg").text(msg+","+g_goApproveSecond+"秒后跳转卖家首页");
        $("#publish_msg").css("color","green");

    }
    g_goApproveSecond--;
    setTimeout(function () {
        goApprovePage(msg);
    }, 1000);

}

$("#ppfbtn").click(function() {

    loading.showLoading({
        type:6,
        tip:"发布中..."
    });

    $('#productReleaseForm').ajaxSubmit({
        url: basePath+'/api/shop/product/republish',
        dataType:"json",
        contentType:"application/json;charset=utf-8",
        success: function (result) {
            loading.hideLoading();
            if(result.code<=0)
            {
                $("#vcode").val("");
                $("#refreshCaptcha").attr("src",basePath+"/api/shop/product/vcode?"+new Date().getTime());
                $.message({
                    message: result.msg,
                    type: 'error'
                });
            }else{
                // $.message({
                //     message: "发布成功",
                //     type: 'success',
                //     showClose:true,
                //     autoClose:false,
                //     onClose:function(){
                //         window.location.href=basePath+"/index";
                //     }
                // });

                $("#ppfbtn").unbind();
                goApprovePage(result.msg);

            }
        }
    });

});