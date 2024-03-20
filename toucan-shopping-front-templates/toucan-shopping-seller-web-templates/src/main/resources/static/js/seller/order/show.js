




$(function () {

    loading.showLoading({
        type:1,
        tip:"加载中..."
    });


    $('#backBtn').on('click', function () {
        window.location.href=basePath+"/page/order/index";
    });


    // $.ajax({
    //     type: "POST",
    //     url: basePath+"/api/order/findById",
    //     contentType: "application/json;charset=utf-8",
    //     data:  JSON.stringify({id:$("#id").val()}),
    //     dataType: "json",
    //     success: function (result) {
    //         if(result.code<=0)
    //         {
    //             $.message({
    //                 message: "查询失败,请稍后重试",
    //                 type: 'error'
    //             });
    //             return ;
    //         }
    //         if(result.data!=null) {
    //             var retObj = result.data;
    //         }
    //     },
    //     error: function (result) {
    //         $.message({
    //             message: "查询失败,请稍后重试",
    //             type: 'error'
    //         });
    //     },
    //     complete:function()
    //     {
    //         loading.hideLoading();
    //     }
    //
    // });


});