

$("#ueibtn").click(function() {

    if(!checkInputFunction($('#ueibtn'),2)){
        return false;
    }

    var fields = $('#userEditInfo').serializeArray();
    var params = {}; //声明一个对象
    $.each(fields, function(index, field) {
        params[field.name] = field.value; //通过变量，将属性值，属性一起放到对象中
    });

    loading.showLoading({
        type:1,
        tip:"提交中..."
    });
    $.ajax({
        type: "POST",
        url: basePath+"/api/user/edit/info",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(params),
        dataType: "json",
        success: function (result) {
            if(result.code<=0)
            {
                $("#vcode").val("");
                $("#edit_info_msg").text(result.msg);
                $("#refreshCaptcha").attr("src",basePath+"/api/user/vcode?"+new Date().getTime());
            }else{
                window.location.href=basePath+"/page/user/info";
            }
        },
        error: function (result) {
            $("#edit_info_msg").text("修改失败,请稍后重试");
        },
        complete:function(data,status){
            loading.hideLoading();
        }
    });
});