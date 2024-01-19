
function bindProductListCollectProductEvent(eventClass){
    var ssdArray = $(eventClass);
    var productSkuIds = new Array();
    if(ssdArray!=null&&ssdArray.length>0)
    {
        for(var i=0;i<ssdArray.length;i++)
        {
            var cbtn = ssdArray[i];
            productSkuIds.push($(cbtn).attr("attr-v"));
        }
    }


    $.ajax({
        type: "POST",
        url: basePath+"/api/user/collect/product/isCollect",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify({"productSkuIds":productSkuIds}),
        dataType: "json",
        success: function (result) {
            //设置默认样式
            for (var i = 0; i < ssdArray.length; i++) {
                var cbtn = ssdArray[i];
                $(cbtn).removeClass("ss_disabled");
                $(cbtn).addClass("ss");
            }
            //设置选择样式
            if (result.data != null && result.data.length > 0) {
                for (var i = 0; i < result.data.length; i++) {
                    var userCollectProduct = result.data[i];
                    for (var j = 0; j < ssdArray.length; j++) {
                        var ssdObj = ssdArray[j];
                        var attrV = $(ssdObj).attr("attr-v");
                        if (userCollectProduct.productSkuId == attrV) {
                            $(ssdObj).removeClass("ss");
                            $(ssdObj).addClass("ss_select");
                            $(ssdObj).attr("attr-t", "0");
                            break;
                        }
                    }
                }
            }

            //绑定收藏事件
            $(".cplist").bind("click", function () {
                var productSkuId = $(this).attr("attr-v");
                var type = $(this).attr("attr-t");
                var selectObj = $(this);

                $.ajax({
                    type: "POST",
                    url: basePath + "/api/user/collect/product/collect",
                    contentType: "application/json;charset=utf-8",
                    data: JSON.stringify({"productSkuId": productSkuId, "type": type}),
                    dataType: "json",
                    success: function (result) {
                        if(result.code==403){
                            window.location.href=basePath+result.data+"?redirectUrl="+encodeURIComponent(getCurrentPageUrl());
                        }else{
                            if (type == "1") {
                                selectObj.attr("attr-t", "0");
                                selectObj.addClass("ss_select");
                                selectObj.removeClass("ss");
                            } else {
                                selectObj.attr("attr-t", "1");
                                selectObj.addClass("ss");
                                selectObj.removeClass("ss_select");
                            }
                        }
                    }
                });
            });

        },
        error: function (result) {
        },
        complete:function(data,status){
        }
    });
}
