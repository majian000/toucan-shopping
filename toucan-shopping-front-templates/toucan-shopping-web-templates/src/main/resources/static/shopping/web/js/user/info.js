
function loadUserInfo()
{
    $.ajax({
        url: basePath+"/api/user/info",
        type: 'post',
        dataType: 'json',
        cache: false,
        success: function(res){

        }
    });
}

