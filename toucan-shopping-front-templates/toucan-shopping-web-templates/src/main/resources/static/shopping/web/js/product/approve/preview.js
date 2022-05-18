$(function(){

    var urlParam = window.location.href;
    var id = urlParam.substring(urlParam.lastIndexOf("/")+1, urlParam.length);
    if(id!=null&&id!="") {
        $.ajax({
            type: "POST",
            url: basePath + "/api/product/approve/detail",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify({"id": id}),
            dataType: "json",
            success: function (result) {
                if (result.code <= 0) {

                }

            },
            error: function (result) {

            }
        });
    }
});