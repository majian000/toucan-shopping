
$(function () {

    loading.showLoading({
        type:6,
        tip:"查询中..."
    });

    $.ajax({
        type: "POST",
        url: basePath+"/api/shop/product/approve/detail",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify({id:$("#approveId").val()}),
        dataType: "json",
        success: function (result) {
            if(result.code<=0)
            {
                $.message({
                    message: "查询失败,请稍后重试",
                    type: 'error'
                });
                return ;
            }
            $("#rejectedText").html(result.data.rejectText);
        },
        error: function (result) {
            $.message({
                message: "查询失败,请稍后重试",
                type: 'error'
            });
        },
        complete:function()
        {
            loading.hideLoading();
        }

    });


    $('#backBtn').on('click', function () {
        window.location.href=basePath+"/page/shop/product/approve/index";
    });

});