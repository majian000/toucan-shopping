
function loadUserInfo()
{
    //查询信息
    $.post(basePath+"/api/user/info",{},function(result){
        alert(result);
    });

}

