$(function(){
    $.ajax({
        type: "POST",
        url: basePath+"/api/index/columns",
        contentType: "application/json;charset=utf-8",
        data:  null,
        dataType: "json",
        success: function (result) {
            console.log(result);
            if(result.code>0)
            {

            }
        }
    });

});