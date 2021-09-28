$(function () {
    drawTable();
    $('#addsc').on('click', function(){
        layer.open({
            type: 2,
            title: '添加类别',
            fix: false,
            shadeClose: true,
            maxmin: true,
            area: ['500px', '260px'],
            content: basePath+"/page/shop/category/add"
        });

    });
});


function drawTable()
{
    $('.tree').treegrid();
}