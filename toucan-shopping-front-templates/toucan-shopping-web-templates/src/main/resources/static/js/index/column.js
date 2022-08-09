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

                    }
                }

                $("#indexColumns").html(columnsHtml);
            }
        }
    });

});