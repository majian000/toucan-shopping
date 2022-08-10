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

                        if(column.topBanner!=null
                            &&column.topBanner.imgPath!=null
                            &&column.topBanner.imgPath!="") {
                            columnsHtml += "<div class=\"content mar_20\">\n" +
                                "    <a href=\""+column.topBanner.clickPath+"\"  target=\"_blank\" ><img src=\""+column.topBanner.httpImgPath+"\" title=\""+column.topBanner.title+"\" width=\"1200\" height=\"110\" /></a>\n" +
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
                                    columnsHtml+="<a href=\""+topLabek.clickPath+"\" target=\"_blank\" >"+topLabek.labelName+"</a>";
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
                                        columnsHtml+=  "   <li><a href=\""+leftBanner.clickPath+"\" target=\"_blank\"><img src=\""+leftBanner.httpImgPath+"\" width=\"211\" height=\"286\" title=\""+leftBanner.title+"\" /></a></li>\n" ;
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
                                            columnsHtml+="<a href=\""+topLabek.clickPath+"\" target=\"_blank\">"+topLabek.labelName+"</a>";
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
                                "        <ul>\n" ;
                                if(column.columnRecommendProducts!=null&&column.columnRecommendProducts.length>0)
                                {
                                    for(var s=0;s<column.columnRecommendProducts.length;s++) {
                                        var recommendProduct = column.columnRecommendProducts[s];
                                        columnsHtml += "            <li>\n" +
                                            "                <div class=\"name\"><a href=\""+recommendProduct.clickPath+"\" target=\"_blank\">"+recommendProduct.productName+"</a></div>\n" +
                                            "                <div class=\"price\">\n" +
                                            "                    <font>￥<span>"+recommendProduct.productPrice+"</span></font> &nbsp;\n" +
                                            "                </div>\n" +
                                            "                <div class=\"img\"><a href=\""+recommendProduct.clickPath+"\" target=\"_blank\"><img src=\""+recommendProduct.httpImgPath+"\" width=\"185\" height=\"155\" /></a></div>\n" +
                                            "            </li>\n";
                                    }
                                }

                                columnsHtml+=      "        </ul>\n" +
                                "    </div>\n" +
                                "    <div class=\"fresh_right\">\n" +
                                "        <ul>\n" ;
                                if(column.rightTopBanner!=null)
                                {
                                    columnsHtml+=  "            <li><a href=\""+column.rightTopBanner.clickPath+"\" target=\"_blank\"><img src=\""+column.rightTopBanner.httpImgPath+"\" title=\""+column.rightTopBanner.title+"\" width=\"260\" height=\"220\" /></a></li>\n" ;
                                }
                                if(column.rightBottomBanner!=null)
                                {
                                    columnsHtml+=  "            <li><a href=\""+column.rightBottomBanner.clickPath+"\" target=\"_blank\"><img src=\""+column.rightBottomBanner.httpImgPath+"\" title=\""+column.rightBottomBanner.title+"\" width=\"260\" height=\"220\" /></a></li>\n" ;
                                }
                                columnsHtml+=        "        </ul>\n" +
                                "    </div>\n" +
                                "</div>\n";


                                if(column.bottomBanner!=null
                                    &&column.bottomBanner.imgPath!=null
                                    &&column.bottomBanner.imgPath!="") {
                                    columnsHtml += "<div class=\"content mar_20\">\n" +
                                        "    <a href=\""+column.topBanner.clickPath+"\"  target=\"_blank\" > <img src=\""+column.bottomBanner.httpImgPath+"\" title=\""+column.bottomBanner.title+"\" width=\"1200\" height=\"110\" /></a>\n" +
                                        "</div>";
                                }
                    }
                }

                $("#indexColumns").html(columnsHtml);


                initfban();
                initf_ban();
                initmban();
                initbban();
                inithban();
                inittban();
            }
        }
    });

});