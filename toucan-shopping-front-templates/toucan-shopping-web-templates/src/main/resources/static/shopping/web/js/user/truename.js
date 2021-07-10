


function submitTrueName()
{
    var trueNameValue=$("#trueName").val();
    var idcardTypeValue=$('#idcardType option:selected').val();
    var idCardValue=$("#idCard").val();
    var vcodeValue=$("#utm_vcode").val();


    $('#utnform').ajaxSubmit({
        url: basePath+'/api/user/true/name/approve/save',
        type:'POST',
        headers:{"ts-auth":"uid="+window.localStorage.getItem("uid")+";lt="+window.localStorage.getItem("lt")},
        success: function (data) {
            if(data.code==401)
            {
                window.location.href=basePath+data.data;
            }
        }
    });


}
