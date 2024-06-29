

function initTabs(){
    var widget = $('.tabs-basic');

    var tabs = widget.find('ul a'),
        content = widget.find('.tabs-content-placeholder > div');

    tabs.on('click', function (e) {

        e.preventDefault();

        // Get the data-index attribute, and show the matching content div

        var index = $(this).data('index');

        tabs.removeClass('tab-active');
        content.removeClass('tab-content-active');

        $(this).addClass('tab-active');
        content.eq(index).addClass('tab-content-active');

    });

}


$(function () {

    initTabs();

    loading.showLoading({
        type:1,
        tip:"加载中..."
    });


    $('#backBtn').on('click', function () {
        window.location.href=basePath+"/page/shop/product/index";
    });


    $.ajax({
        type: "POST",
        url: basePath+"/api/shop/product/findById",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify({id:$("#id").val()}),
        dataType: "json",
        success: function (result) {
            if(result.code<=0)
            {
                $.message({
                    message: "查询失败,请稍后重试",
                    type: 'error'
                });
                return ;
            }
            if(result.data!=null) {
                var retObj = result.data;
                if(retObj!=null) {

                    var productStatusName="";
                    if(retObj.status==0){
                        productStatusName="未上架";
                        $("#productStatus").css("color","red")
                    }else if(retObj.status==1){
                        productStatusName="已上架";
                        $("#productStatus").css("color","green")
                    }
                    $("#productStatus").html(productStatusName);

                    $("#productName").html(retObj.name);
                    $("#categoryPath").html(retObj.categoryPath);

                    if(retObj.productSkuVOList!=null&&retObj.productSkuVOList.length>0){
                        drawProductSkuTable(retObj);
                    }


                }
            }
        },
        error: function (result) {
            $.message({
                message: "查询失败,请稍后重试",
                type: 'error'
            });
        },
        complete:function()
        {
            loading.hideLoading();
        }

    });


});



function drawProductSkuTable(orderVO){
    var productSkuList = orderVO.productSkuVOList;

    var tableHtml="";
    tableHtml+=" <tr class=\"tabTh\" style=\"height:50px;\">\n" +
        "                            <td style=\"width:50px;\" >序号</td>\n" +
        "                            <td style=\"width:100px;\" >名称</td>\n" +
        "                            <td style=\"width:100px;\" >单价</td>\n" +
        "                            <td style=\"width:150px;\" >库存数量</td>\n" +
        "                            <td style=\"width:100px;\"  >预览图</td>\n" +
        "                            <td style=\"width:100px;\"  >毛重(单位kg)</td>\n" +
        "                            <td style=\"width:100px;\"  >净重(单位kg)</td>\n" +
        "                            <td style=\"width:100px;\"  >介绍图片</td>\n" +
        "                        </tr>";


    for(var i=0;i<productSkuList.length;i++)
    {
        var row = productSkuList[i];

        tableHtml += " <tr align=\"center\" class=\"tabTd\">\n";
        tableHtml += "                            <td><div class=\"tabTdWrap\">" + (i + 1) + "</div></td>\n";
        tableHtml += "                            <td><div class=\"tabTdWrap\">" + row.name + "</div></td>\n";
        tableHtml += "                            <td><div class=\"tabTdWrap\">" + row.price + "</div></td>\n";
        tableHtml += "                            <td><div class=\"tabTdWrap\">" + row.stockNum + "</div></td>\n";
        tableHtml += "                            <td><div class=\"tabTdWrap\"><a href=\"javascript:window.open('" + row.httpProductPreviewPath + "')\"><img width=\"100\" height=\"100\" src=\"" + row.httpProductPreviewPath + "\"></a>" + "</div></td>\n";
        tableHtml += "                            <td><div class=\"tabTdWrap\">" + row.roughWeight + "</div></td>\n";
        tableHtml += "                            <td><div class=\"tabTdWrap\">" + row.suttle + "</div></td>\n";
        if(row.httpDescriptionImgPath!=null&&row.httpDescriptionImgPath!=""){
            tableHtml += "                            <td><div class=\"tabTdWrap\"><a href=\"javascript:window.open('" + row.httpDescriptionImgPath + "')\"><img width=\"100\" height=\"100\" src=\"" + row.httpDescriptionImgPath + "\"></a>" + "</div></td>\n";
        }else{
            tableHtml += "                            <td><div class=\"tabTdWrap\"></div></td>\n";
        }
        tableHtml += "                        </tr>";
    }

    $("#productSkuTableBody").html(tableHtml);
    $("#productSkuList").FrozenTable(2,0,0);


}
