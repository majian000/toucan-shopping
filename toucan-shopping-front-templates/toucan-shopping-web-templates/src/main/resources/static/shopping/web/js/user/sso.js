/**
 * 注意:
 *      采用这种方式需要服务端安装HTTPS证书,新版本浏览器不支持跨域写Cookie,除非安装HTTPS证书还有设置Secure=True;SameSite=None;响应头
 */

function share_cookie()
{
    var redirectUrl = $("#redirectUrl").val();
    $.ajax({
        type: "POST",
        url: basePath+"/api/sso/query/domains",
        contentType: "application/json;charset=utf-8",
        data:  null,
        dataType: "json",
        timeout: 3000,
        success: function (result) {
            if(result.code==1)
            {
                var pos =0;
                set_cookie(redirectUrl,pos,result.data)
            }else{
                if(redirectUrl!=null&&redirectUrl!="")
                {
                    window.location.href =  redirectUrl;
                }else {
                    window.location.href = basePath + "/page/user/info";
                }
            }
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        if(redirectUrl!=null&&redirectUrl!="")
        {
            window.location.href =  redirectUrl;
        }else {
            window.location.href = basePath + "/page/user/info";
        }
    });
}

function set_cookie(redirectUrl,pos,domainList)
{
    $.ajax({
        type:"GET",
        dataType:"jsonp",
        url: domainList[pos],
        data:{cookies:document.cookie},
        crossDomain:true,
        timeout: 3000,
        complete:function(data,status){
            if(pos+1<domainList.length)
            {
                set_cookie(redirectUrl,pos+1,domainList);
            }else{
                if(redirectUrl!=null&&redirectUrl!="")
                {
                    window.location.href =  redirectUrl;
                }else {
                    window.location.href = basePath + "/page/user/info";
                }
            }
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        if(redirectUrl!=null&&redirectUrl!="")
        {
            window.location.href =  redirectUrl;
        }else {
            window.location.href = basePath + "/page/user/info";
        }
    });
}