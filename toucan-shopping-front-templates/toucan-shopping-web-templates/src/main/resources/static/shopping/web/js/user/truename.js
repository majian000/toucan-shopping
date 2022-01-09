


function submitTrueName()
{
    var trueNameValue=$("#trueName").val();
    var idCardValue=$("#idCard").val();
    var vcodeValue=$("#utm_vcode").val();

    if(trueNameValue=="")
    {
        $.message({
            message: "请输入姓名",
            type: 'error'
        });
        return ;
    }
    if(idCardValue=="")
    {
        $.message({
            message: "请输入证件号码",
            type: 'error'
        });
        return ;
    }
    if($("#idcardImg1File").val() =="")
    {
        $.message({
            message: "请上传证件正面照片",
            type: 'error'
        });
        return ;
    }
    if($("#idcardImg2File").val() =="")
    {
        $.message({
            message: "请上传证件背面照片",
            type: 'error'
        });
        return ;
    }
    if(vcodeValue==null||vcodeValue=="")
    {
        $.message({
            message: "请输入验证码",
            type: 'error'
        });
        return ;
    }

    loading.showLoading({
        type:1,
        tip:"提交中..."
    });
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
        },
        complete:function(data,status){
            loading.hideLoading();
        }
    });


}
