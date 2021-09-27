
$(function () {

    $('#scafbtn').on('click', function(){
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        parent.layer.close(index);
    });
});