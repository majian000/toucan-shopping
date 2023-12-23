
$(function(){
    var shopId=$("#shopId").val();
    $.ajax({
        type: "POST",
        url: basePath+"/api/shop/index/categorys",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify({shopId:shopId}),
        dataType: "json",
        success: function (result) {
            if(result.code>0)
            {
                var categoryHtml="" +
                    "                            <li><a href=\"#\" class=\"menu-button menu-drop\"><span class=\"menu-label\">手机</span></a>\n" +
                    "                                <div class=\"menu-dropdown menu-dropdown1\">\n" +
                    "                                    <ul class=\"menu-sub\">\n" +
                    "                                        <li><a href=\"#\" class=\"menu-subbutton\"><span class=\"menu-label\">5G手机</span></a></li>\n" +
                    "                                        <li><a href=\"#\" class=\"menu-subbutton\"><span class=\"menu-label\">游戏手机</span></a></li>\n" +
                    "                                    </ul>\n" +
                    "                                </div>\n" +
                    "                            </li>";

                if(result.data!=null&&result.data.length>0)
                {
                    for(var i=0;i<result.data.length;i++)
                    {
                        var category = result.data[i];
                        categoryHtml+=getCategoryHtml(category);
                    }
                }
                $(".menu-top").append(categoryHtml);
            }
        }
    });

});

/**
 * 拿到分类html
 * @param category
 */
function getCategoryHtml(category){
    var href="";
    if(category.href!=null)
    {
        href=category.href;
    }else{
        //TODO:跳转到商品列表
        href="/productListByCategoryId?categoryId="+category.id;
    }
    var categoryHtml="<li><a href=\""+href+"\" class=\"menu-button\"><span class=\"menu-label\">"+category.name+"</span></a>";
    if(category.children!=null&&category.children.length>0)
    {
        categoryHtml+="                                <div class=\"menu-dropdown menu-dropdown1\">\n" +
        "                                    <ul class=\"menu-sub\">\n" ;
        for(var j=0;j<category.children.length;j++)
        {
            var categoryChild = category.children[j];
            href="";
            if(categoryChild.href!=null)
            {
                href=categoryChild.href;
            }else{
                //TODO:跳转到商品列表
                href="/productListByCategoryId?categoryId="+categoryChild.id;
            }
            categoryHtml+="  <li><a href=\""+href+"\" class=\"menu-subbutton\"><span class=\"menu-label\">"+categoryChild.name+"</span></a></li>\n";
        }
        categoryHtml+="  </ul> </div>\n";
    }
    categoryHtml+="</li>\n";
    return categoryHtml;
}