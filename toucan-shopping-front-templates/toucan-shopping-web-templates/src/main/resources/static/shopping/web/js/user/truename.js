


function submitTrueName()
{
    var trueNameValue=$("#trueName").val();
    var idCardValue=$("#idCard").val();
    var vcodeValue=$("#utm_vcode").val();

    $("#trueName_msg").text(" ");
    $("#idCard_msg").text(" ");
    $("#utm_vcode_msg").text(" ");

    if(trueNameValue=="")
    {
        $("#trueName_msg").text("请输入姓名");
        return ;
    }
    if(idCardValue=="")
    {
        $("#idCard_msg").text("请输入证件号码");
        return ;
    }
    if($("#idcardImg1File").val() =="")
    {
        $("#idcardImg1File_msg").text("请上传证件正面照片");
        return ;
    }
    if($("#idcardImg2File").val() =="")
    {
        $("#idcardImg2File_msg").text("请上传证件背面照片");
        return ;
    }
    if(vcodeValue=="")
    {
        $("#utm_vcode_msg").text("请输入验证码");
        return ;
    }
    $('#utnform').ajaxSubmit({
        url: basePath+'/api/user/true/name/approve/save',
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
                window.location.href=basePath+"/page/user/true/name/approve/submit_success";
            }
        }
    });


}
