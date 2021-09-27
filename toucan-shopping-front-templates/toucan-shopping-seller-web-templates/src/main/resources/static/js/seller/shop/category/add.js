
function scafbtn_clicn()
{

    $("#scaf_msg").hide();
    var shopCategoryName=$("#shopCategoryName").val();
    if(shopCategoryName==null||shopCategoryName=="")
    {
        $("#scaf_msg").show();
        $("#scaf_msg").text("类别名称不能为空");
        $("#scaf_msg").css("color","red");
        return;
    }

    if(shopCategoryName.length<2||shopCategoryName.length>25)
    {
        $("#scaf_msg").show();
        $("#scaf_msg").text("类别名称长度只能在2-25位字符之间");
        $("#scaf_msg").css("color","red");
        return;
    }

    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
    parent.layer.close(index);

}


$(function () {

    $('#scafbtn').on('click', function () {
        scafbtn_clicn();
    });
});