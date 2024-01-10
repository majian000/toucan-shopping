
function loadCategoryMenu(){
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
                var categoryHtml="";

                if(result.data!=null&&result.data.length>0)
                {
                    for(var i=0;i<result.data.length;i++)
                    {
                        var category = result.data[i];
                        categoryHtml+=getCategoryHtml(category,shopId);
                    }
                }
                $(".menu-top").append(categoryHtml);
            }
        }
    });

}

/**
 * 拿到分类html
 * @param category
 */
function getCategoryHtml(category,shopId){
    var href="";
    if(category.href!=null)
    {
        href=category.href;
    }else{
        href="/page/shop/category/product/list?scid="+category.id+"&sid="+shopId;
    }
    var categoryHtml="";
    if(category.children==null||category.children.length<=0)
    {
        categoryHtml+="<li><a href=\""+href+"\" class=\"menu-button\"><span class=\"menu-label\">"+category.name+"</span></a></li>";
    }else{
        categoryHtml+=" <li><a href=\""+href+"\" class=\"menu-button menu-drop\"><span class=\"menu-label\">"+category.name+"</span></a>";
        categoryHtml+="<div class=\"menu-dropdown menu-dropdown1\">" +
            "                <ul class=\"menu-sub\">";
        for(var j=0;j<category.children.length;j++)
        {
            var categoryChild = category.children[j];
            href="";
            if(categoryChild.href!=null)
            {
                href=categoryChild.href;
            }else{
                href="/page/shop/category/product/list?scid="+categoryChild.id+"&sid="+shopId;
            }
            categoryHtml+="<li><a href=\""+href+"\" class=\"menu-subbutton\"><span class=\"menu-label\">"+categoryChild.name+"</span></a></li>";
        }
        categoryHtml+=" </ul> " +
            "         </div>\n" +
            "        </li>";
    }

    return categoryHtml;
}