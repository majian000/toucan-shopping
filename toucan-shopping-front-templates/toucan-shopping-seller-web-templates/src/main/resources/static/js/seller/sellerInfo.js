$(function () {

    //查询信息
    $.post(basePath+"/api/shop/shop/info",{},function(result){
        if(result.code>0&&result.data!=null)
        {
            if(result.data.name!=null) {
                $("#shop_name").text(result.data.name);
                $("#shop_logo").attr("src",result.data.httpLogo);
            }else{
                $("#shop_name").text("店铺名称");
            }
        }
    });
});

