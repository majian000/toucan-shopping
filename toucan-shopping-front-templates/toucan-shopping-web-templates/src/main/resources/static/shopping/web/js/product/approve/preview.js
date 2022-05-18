$(function(){

    alert(window.location.href);

    $.ajax({
        type: "POST",
        url: basePath+"/api/product/approve/detail",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify({"id":username}),
        dataType: "json",
        success: function (result) {
            if(result.code<=0)
            {

            }

        },
        error: function (result) {

        }
    });

});