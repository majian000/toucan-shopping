$(function () {

    //查询信息
    $.post(basePath+"/api/user/login/info",{},function(result){
        if(result.code>0&&result.data!=null)
        {
            if(result.data.nickName!=null) {
                $("#user_info_user_name").text(result.data.nickName);
            }
            $("#user_info_container").show();
        }
    });
});

