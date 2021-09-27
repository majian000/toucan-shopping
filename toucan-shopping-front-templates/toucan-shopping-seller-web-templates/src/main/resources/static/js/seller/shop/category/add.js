$(function () {

    $('#addsc').on('click', function(){
        layer.open({
            type: 2,
            title: '添加类别',
            fix: false,
            shadeClose: true,
            maxmin: true,
            area: ['700px', '670px'],
            content: basePath+"/page/shop/category/add"
        });

    });
});