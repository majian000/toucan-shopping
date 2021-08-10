$(function () {

    //查询信息
    $.post(basePath+"/api/user/login/info",{},function(result){
        if(result.code>0&&result.data!=null)
        {
            if(result.data.nickName!=null) {
                $("#unickname").text(result.data.nickName);
            }
            $("#userInfo").show();
        }else{

            $("#registOrLogin").show();
        }
    });
});

