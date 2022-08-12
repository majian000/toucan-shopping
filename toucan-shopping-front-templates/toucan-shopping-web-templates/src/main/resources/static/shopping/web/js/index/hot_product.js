$(function () {

    $.post(basePath+"/api/index/hot_product",null,function(result){
        if(result.code>0)
        {
            if(result.data!=null&&result.data.length>0)
            {
                var ulHtml="";
                for(var i=0;i<result.data.length;i++)
                {
                    var obj = result.data[i];

                    var targetBlankHtml="";
                    if(obj.clickPath!=null
                        &&obj.clickPath!=""
                        &&obj.clickPath!="#")
                    {
                        targetBlankHtml = " target=\"_blank\"";
                    }

                    var template=" <li class='featureBox'>\n" +
                        "                            <div class='box'>\n" +
                        "                                <div class='imgbg'>\n" +
                        "                                    <a href='"+obj.clickPath+"' "+targetBlankHtml+"><img src='"+obj.httpImgPath+"' width='160' height='136' /></a>\n" +
                        "                                </div>\n" +
                        "                                <div class='name'>\n" +
                        "                                    <a href='#'>\n" +
                        "                                        <h2>"+obj.productName+"</h2>\n" +
                        "                                        "+obj.productDesc+"\n" +
                        "                                    </a>\n" +
                        "                                </div>\n" +
                        "                                <div class='price'>\n" +
                        "                                    <font>￥<span>"+obj.productPrice+"</span></font>\n" +
                        "                                </div>\n" +
                        "                            </div>\n" +
                        "                        </li>";
                    ulHtml+=template;
                }
                $("#hot_product_ul").html(ulHtml);
                //初始化滚动事件
                initlrscroll();
            }
        }
    });
});