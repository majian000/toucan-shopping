


function submitTrueName()
{
    var trueNameValue=$("#trueName").val();
    var idcardTypeValue=$('#idcardType option:selected').val();
    var idCardValue=$("#idCard").val();
    var vcodeValue=$("#utm_vcode").val();


    $('#utnform').ajaxSubmit({
        url: basePath+'/api/user/true/name/approve/save',
        type:'POST',
        contentType: "application/json; charset=utf-8",
        headers:{"ts_auth":"uid="+window.localStorage.getItem("uid")+";lt="+window.localStorage.getItem("lt")},
        success: function (data) {

        }
    });


}
