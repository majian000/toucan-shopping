$(function(){
    $.ajax({
        type: "POST",
        url: basePath+"/api/index/banners",
        contentType: "application/json;charset=utf-8",
        data:  null,
        dataType: "json",
        success: function (result) {
            if(result.code>0)
            {
                var bannerArray = new Array();
                var pointerHtml ="";
                if(result.data!=null && result.data.length>0)
                {
                    for(var i=0;i<result.data.length;i++)
                    {
                        if(i==0)
                        {
                            pointerHtml+="<li data-index=\"1\" class=\"current\"></li>";
                        }else{
                            pointerHtml+="<li data-index=\""+(i+1)+"\" ></li>";
                        }
                        var banner = result.data[i];
                        bannerArray.push({src:banner.httpImgPath,href:banner.clickPath});
                    }
                }
                jq("#defaultBannerPointer").append(pointerHtml);
                jq('#slider').sliders({
                    imgArr: bannerArray,
                    autoLoop: true,
                    current: 1,
                    time: 3000,
                    vWidth: 740
                });
            }
        }
    });

});