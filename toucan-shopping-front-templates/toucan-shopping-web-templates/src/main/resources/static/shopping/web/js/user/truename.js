


function submitTrueName()
{
    var trueNameValue=$("#trueName").val();
    var idcardTypeValue=$('#idcardType option:selected').val();
    var idCardValue=$("#idCard").val();
    var vcodeValue=$("#utm_vcode").val();

    $.ajax(
        {
            url:basePath+"/api/user/true/name/approve/save",
            type:'POST',
            dateType:'JSON',

            headers:{"ts_auth":"uid="+window.localStorage.getItem("uid")+";lt="+window.localStorage.getItem("lt")},
            data:{
                trueName:trueNameValue,
                idcardType:idcardTypeValue,
                idCard:idCardValue,
                vcode:vcodeValue
            },
            success:function(data){
                if(result.code<=0)
                {
                    $("#regist_msg").text(result.msg);
                    $("#regist_msg").css("color","red");
                }
            },
            error:function(data){
            }
        }
    );
}
