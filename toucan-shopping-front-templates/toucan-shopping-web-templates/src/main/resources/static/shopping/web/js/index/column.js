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
                var coulumnCount = 0;
                if(result.data!=null&&result.data.length>0)
                {
                    coulumnCount = result.data.length;
                    for(var i=0;i<result.data.length;i++)
                    {
                        var column = result.data[i];

                        if(column.topBanner!=null
                            &&column.topBanner.imgPath!=null
                            &&column.topBanner.imgPath!="") {

                            var targetBlankHtml="";
                            if(column.topBanner.clickPath!=null
                                &&column.topBanner.clickPath!=""
                                &&column.topBanner.clickPath!="#")
                            {
                                targetBlankHtml = " target=\"_blank\"";
                            }

                            columnsHtml += "<div class=\"content mar_20\">\n" +
                                "    <a href=\""+column.topBanner.clickPath+"\"  "+targetBlankHtml+" ><img src=\""+column.topBanner.httpImgPath+"\" title=\""+column.topBanner.title+"\" width=\"1200\" height=\"110\" /></a>\n" +
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

                                    var targetBlankHtml="";
                                    if(topLabek.clickPath!=null
                                        &&topLabek.clickPath!=""
                                        &&topLabek.clickPath!="#")
                                    {
                                        targetBlankHtml = " target=\"_blank\"";
                                    }
                                    columnsHtml+="<a href=\""+topLabek.clickPath+"\" "+targetBlankHtml+" >"+topLabek.labelName+"</a>";
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
                                "            <div id=\"imgPlay"+i+"\" class='imgPlays'>\n" +
                                "                <ul class=\"imgs\" id=\"actor"+i+"\">\n" ;
                                if(column.columnLeftBannerVOS!=null&&column.columnLeftBannerVOS.length>0) {
                                    for (var s = 0; s < column.columnLeftBannerVOS.length; s++) {
                                        var leftBanner = column.columnLeftBannerVOS[s];
                                        var targetBlankHtml="";
                                        if(leftBanner.clickPath!=null
                                            &&leftBanner.clickPath!=""
                                            &&leftBanner.clickPath!="#")
                                        {
                                            targetBlankHtml = " target=\"_blank\"";
                                        }
                                        columnsHtml+=  "   <li><a href=\""+leftBanner.clickPath+"\" "+targetBlankHtml+"><img src=\""+leftBanner.httpImgPath+"\" width=\"211\" height=\"286\" title=\""+leftBanner.title+"\" /></a></li>\n" ;
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
                                            var leftLabel = column.leftLabels[p];
                                            var targetBlankHtml="";
                                            if(leftLabel.clickPath!=null
                                                &&leftLabel.clickPath!=""
                                                &&leftLabel.clickPath!="#")
                                            {
                                                targetBlankHtml = " target=\"_blank\"";
                                            }
                                            columnsHtml+="<a href=\""+leftLabel.clickPath+"\" "+targetBlankHtml+" title=\""+leftLabel.labelName+"\">"+leftLabel.labelName+"</a>";
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

                                        var targetBlankHtml="";
                                        if(recommendProduct.clickPath!=null
                                            &&recommendProduct.clickPath!=""
                                            &&recommendProduct.clickPath!="#")
                                        {
                                            targetBlankHtml = " target=\"_blank\"";
                                        }
                                        columnsHtml += "            <li>\n" +
                                            "                <div class=\"name\"><a href=\""+recommendProduct.clickPath+"\" "+targetBlankHtml+" title=\""+recommendProduct.productName+"\">"+recommendProduct.productName+"</a></div>\n" +
                                            "                <div class=\"price\">\n" +
                                            "                    <font>￥<span>"+recommendProduct.productPrice+"</span></font> &nbsp;\n" +
                                            "                </div>\n" +
                                            "                <div class=\"img\"><a href=\""+recommendProduct.clickPath+"\" "+targetBlankHtml+"><img src=\""+recommendProduct.httpImgPath+"\" width=\"185\" height=\"155\" /></a></div>\n" +
                                            "            </li>\n";
                                    }
                                }

                                columnsHtml+=      "        </ul>\n" +
                                "    </div>\n" +
                                "    <div class=\"fresh_right\">\n" +
                                "        <ul>\n" ;
                                if(column.rightTopBanner!=null)
                                {

                                    var targetBlankHtml="";
                                    if(column.rightTopBanner.clickPath!=null
                                        &&column.rightTopBanner.clickPath!=""
                                        &&column.rightTopBanner.clickPath!="#")
                                    {
                                        targetBlankHtml = " target=\"_blank\"";
                                    }
                                    columnsHtml+=  "            <li><a href=\""+column.rightTopBanner.clickPath+"\" "+targetBlankHtml+"><img src=\""+column.rightTopBanner.httpImgPath+"\" title=\""+column.rightTopBanner.title+"\" width=\"260\" height=\"220\" /></a></li>\n" ;
                                }
                                if(column.rightBottomBanner!=null)
                                {

                                    var targetBlankHtml="";
                                    if(column.rightBottomBanner.clickPath!=null
                                        &&column.rightBottomBanner.clickPath!=""
                                        &&column.rightBottomBanner.clickPath!="#")
                                    {
                                        targetBlankHtml = " target=\"_blank\"";
                                    }
                                    columnsHtml+=  "            <li><a href=\""+column.rightBottomBanner.clickPath+"\" "+targetBlankHtml+"><img src=\""+column.rightBottomBanner.httpImgPath+"\" title=\""+column.rightBottomBanner.title+"\" width=\"260\" height=\"220\" /></a></li>\n" ;
                                }
                                columnsHtml+=        "        </ul>\n" +
                                "    </div>\n" +
                                "</div>\n";


                                if(column.bottomBanner!=null
                                    &&column.bottomBanner.imgPath!=null
                                    &&column.bottomBanner.imgPath!="") {

                                    var targetBlankHtml="";
                                    if(column.bottomBanner.clickPath!=null
                                        &&column.bottomBanner.clickPath!=""
                                        &&column.bottomBanner.clickPath!="#")
                                    {
                                        targetBlankHtml = " target=\"_blank\"";
                                    }

                                    columnsHtml += "<div class=\"content mar_20\">\n" +
                                        "    <a href=\""+column.topBanner.clickPath+"\"  "+targetBlankHtml+" > <img src=\""+column.bottomBanner.httpImgPath+"\" title=\""+column.bottomBanner.title+"\" width=\"1200\" height=\"110\" /></a>\n" +
                                        "</div>";
                                }
                    }
                }

                $("#indexColumns").html(columnsHtml);


                for(var c=0;c<coulumnCount;c++) {
                    initfban("actor"+c,"imgPlay"+c);
                }
            }
        }
    });

});