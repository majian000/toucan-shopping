$(function(){
    $.ajax({
        type: "POST",
        url: basePath+"/api/index/columns",
        contentType: "application/json;charset=utf-8",
        data:  null,
        dataType: "json",
        success: function (result) {
            if(result.code>0)
            {
                var columnsHtml="";
                if(result.data!=null&&result.data.length>0)
                {
                    for(var i=0;i<result.data.length;i++)
                    {
                        var column = result.data[i];

                        if(column.topBanner!=null) {
                            columnsHtml += "<div class=\"content mar_20\">\n" +
                                "    <img src=\""+column.topBanner.httpImgPath+"\" width=\"1200\" height=\"110\" />\n" +
                                "</div>";
                        }

                        columnsHtml+="<div class=\"i_t mar_10\">\n" +
                            "    <span class=\"floor_num\">"+(i+1)+"F</span>\n" +
                            "    <span class=\"fl\">"+column.title+"</span>\n" ;
                            columnsHtml+="    <span class=\"i_mores fr\">\n" ;
                            if(column.topLabels!=null&&column.topLabels.length>0)
                            {
                                for(var p=0;p<column.topLabels.length;p++)
                                {
                                    var topLabek = column.topLabels[p];
                                    columnsHtml+="<a href=\""+topLabek.clickPath+"\">"+topLabek.labelName+"</a>";
                                    if(p+1<column.topLabels.length)
                                    {
                                        columnsHtml+="&nbsp; &nbsp; &nbsp;";
                                    }
                                }
                            }
                            columnsHtml+="</span>";
                            columnsHtml+=  "</div>";

                            columnsHtml+="\n" +
                                "<div class=\"content\">\n" +
                                "    <div class=\"fresh_left\">\n" +
                                "        <div class=\"fre_ban\">\n" +
                                "            <div id=\"imgPlay1\">\n" +
                                "                <ul class=\"imgs\" id=\"actor1\">\n" ;
                                if(column.columnLeftBannerVOS!=null&&column.columnLeftBannerVOS.length>0) {
                                    for (var s = 0; s < column.columnLeftBannerVOS.length; s++) {
                                        var leftBanner = column.columnLeftBannerVOS[s];
                                        columnsHtml+=  "   <li><a href=\""+leftBanner.clickPath+"\"><img src=\""+leftBanner.httpImgPath+"\" width=\"211\" height=\"286\" title=\""+leftBanner.title+"\" /></a></li>\n" ;
                                    }
                                }
                             columnsHtml+=       "                </ul>\n" +
                                "                <div class=\"prevf\">上一张</div>\n" +
                                "                <div class=\"nextf\">下一张</div>\n" +
                                "            </div>\n" +
                                "        </div>\n" ;
                            columnsHtml+=   "        <div class=\"fresh_txt\">\n" +
                                "            <div class=\"fresh_txt_c\">\n" ;
                                    if(column.leftLabels!=null&&column.leftLabels.length>0)
                                    {
                                        for(var p=0;p<column.leftLabels.length;p++)
                                        {
                                            var topLabek = column.leftLabels[p];
                                            columnsHtml+="<a href=\""+topLabek.clickPath+"\">"+topLabek.labelName+"</a>";
                                            if(p+1<column.leftLabels.length)
                                            {
                                                columnsHtml+="&nbsp; &nbsp; &nbsp;";
                                            }
                                        }
                                    }
                            columnsHtml+="            </div>\n" +
                                "        </div>\n" +
                                "    </div>\n" +
                                "    <div class=\"fresh_mid\">\n" +
                                "        <ul>\n" +
                                "            <li>\n" +
                                "                <div class=\"name\"><a href=\"#\">新鲜美味  进口美食</a></div>\n" +
                                "                <div class=\"price\">\n" +
                                "                    <font>￥<span>198.00</span></font> &nbsp; 26R\n" +
                                "                </div>\n" +
                                "                <div class=\"img\"><a href=\"#\"><img src=\"${basePath}/static/images/fre_1.jpg\" width=\"185\" height=\"155\" /></a></div>\n" +
                                "            </li>\n" +
                                "            <li>\n" +
                                "                <div class=\"name\"><a href=\"#\">新鲜美味  进口美食</a></div>\n" +
                                "                <div class=\"price\">\n" +
                                "                    <font>￥<span>198.00</span></font> &nbsp; 26R\n" +
                                "                </div>\n" +
                                "                <div class=\"img\"><a href=\"#\"><img src=\"${basePath}/static/images/fre_2.jpg\" width=\"185\" height=\"155\" /></a></div>\n" +
                                "            </li>\n" +
                                "            <li>\n" +
                                "                <div class=\"name\"><a href=\"#\">新鲜美味  进口美食</a></div>\n" +
                                "                <div class=\"price\">\n" +
                                "                    <font>￥<span>198.00</span></font> &nbsp; 26R\n" +
                                "                </div>\n" +
                                "                <div class=\"img\"><a href=\"#\"><img src=\"${basePath}/static/images/fre_3.jpg\" width=\"185\" height=\"155\" /></a></div>\n" +
                                "            </li>\n" +
                                "            <li>\n" +
                                "                <div class=\"name\"><a href=\"#\">新鲜美味  进口美食</a></div>\n" +
                                "                <div class=\"price\">\n" +
                                "                    <font>￥<span>198.00</span></font> &nbsp; 26R\n" +
                                "                </div>\n" +
                                "                <div class=\"img\"><a href=\"#\"><img src=\"${basePath}/static/images/fre_4.jpg\" width=\"185\" height=\"155\" /></a></div>\n" +
                                "            </li>\n" +
                                "            <li>\n" +
                                "                <div class=\"name\"><a href=\"#\">新鲜美味  进口美食</a></div>\n" +
                                "                <div class=\"price\">\n" +
                                "                    <font>￥<span>198.00</span></font> &nbsp; 26R\n" +
                                "                </div>\n" +
                                "                <div class=\"img\"><a href=\"#\"><img src=\"${basePath}/static/images/fre_5.jpg\" width=\"185\" height=\"155\" /></a></div>\n" +
                                "            </li>\n" +
                                "            <li>\n" +
                                "                <div class=\"name\"><a href=\"#\">新鲜美味  进口美食</a></div>\n" +
                                "                <div class=\"price\">\n" +
                                "                    <font>￥<span>198.00</span></font> &nbsp; 26R\n" +
                                "                </div>\n" +
                                "                <div class=\"img\"><a href=\"#\"><img src=\"${basePath}/static/images/fre_6.jpg\" width=\"185\" height=\"155\" /></a></div>\n" +
                                "            </li>\n" +
                                "        </ul>\n" +
                                "    </div>\n" +
                                "    <div class=\"fresh_right\">\n" +
                                "        <ul>\n" +
                                "            <li><a href=\"#\"><img src=\"${basePath}/static/images/fre_b1.jpg\" width=\"260\" height=\"220\" /></a></li>\n" +
                                "            <li><a href=\"#\"><img src=\"${basePath}/static/images/fre_b2.jpg\" width=\"260\" height=\"220\" /></a></li>\n" +
                                "        </ul>\n" +
                                "    </div>\n" +
                                "</div>\n";
                    }
                }

                $("#indexColumns").html(columnsHtml);
            }
        }
    });

});