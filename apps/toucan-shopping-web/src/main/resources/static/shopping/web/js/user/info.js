
function loadUserInfo()
{

    $.ajax({
        url: basePath+"/api/user/info",
        type: 'put',
        dataType: 'json',
        data: JSON.stringify({"uid":window.localStorage.getItem("uid")}),
        cache: false,
        headers: {
            "bbs-auth-header":"uid="+window.localStorage.getItem("uid")+";lt="+window.localStorage.getItem("lt"),
        },
        success: function(res){

        }
    });
}

