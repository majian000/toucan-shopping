

var g_om_cpage=1;
var g_query_order_type=-1;
var g_query_keyword="";

$(function () {
    loadListTable(1);
    bindTabEvent();
});



function drawCollectProductList(cpage,data)
{
    if(data.total>0)
    {
        $(".collect-product-list-tips").hide();
        $(".collect-product-list-table").show();
        var orderTableHtml="<colgroup>\n" +
            "                                <col class=\"number-col\">\n" +
            "                                <col class=\"amount-col\">\n" +
            "                                <col class=\"operate-col\">\n" +
            "                            </colgroup>\n" +
            "                            <thead>\n" +
            "                            <tr>\n" +
            "                                <th style=\"width:50%\">\n" +
            "                                    <div class=\"collect-product-detail-txt ac\">商品详情</div>\n" +
            "                                </th>\n" +
            "                                <th style=\"width:30%\">金额</th>\n" +
            "                                <th style=\"width:20%\">操作</th>\n" +
            "                            </tr>\n" +
            "\n" +
            "                            </thead>";
        for(var i=0;i<data.list.length;i++)
        {
            orderTableHtml+="<tr class=\"tr-bd\">\n" +
                "                                    <td>\n" +
                "                                        <div class=\"goods-item \">\n" +
                "                                            <div class=\"p-img\">\n" +
                "                                                <a href=\"http://localhost:8083/page/product/detail/1120741802802937900\" target=\"_blank\">\n" +
                "                                                    <img class=\"\" src=\"http://8.140.187.184:8049/group1/M00/00/0F/rB5PVWSRWMKAYgnOAAGSoNT8hWc34..JPG\" title=\"Apple iPhone 14 Pro Max (A2896) 256GB 银色 支持移动联通电信5G 双卡双待手机 黑色 8G\" data-lazy-img=\"done\" width=\"60\" height=\"60\">\n" +
                "                                                </a>\n" +
                "                                            </div>\n" +
                "                                            <div class=\"p-msg\">\n" +
                "                                                <div class=\"p-name\">\n" +
                "                                                    <a href=\"http://localhost:8083/page/product/detail/1120741802802937900\" class=\"a-link\" target=\"_blank\" title=\"Apple iPhone 14 Pro Max (A2896) 256GB 银色 支持移动联通电信5G 双卡双待手机 黑色 8G\">Apple iPhone 14 Pro Max (A2896) 256GB 银色 支持移动联通电信5G 双卡双待手机 黑色 8G</a>\n" +
                "                                                </div>\n" +
                "                                                <div class=\"p-extra\">\n" +
                "                                                </div>\n" +
                "\n" +
                "                                            </div>\n" +
                "                                        </div>\n" +
                "                                        \n" +
                "\n" +
                "\n" +
                "                                        <div class=\"goods-repair\">\n" +
                "                                        </div>\n" +
                "                                        <div class=\"clr\"></div>\n" +
                "                                        \n" +
                "                                        \n" +
                "\n" +
                "\n" +
                "                                        \n" +
                "                                        \n" +
                "                                    </td>\n" +
                "\n" +
                "                                    \n" +
                "                                    <td rowspan=\"1\">\n" +
                "                                        <div class=\"amount\">\n" +
                "                                            <span>¥38197</span> \n" +
                "                                            \n" +
                "                                        </div>\n" +
                "                                    </td>\n" +
                "                                    \n" +
                "                                    <td rowspan=\"1\">\n" +
                "                                        <div class=\"operate\">\n" +
                "                                            <div></div>\n" +
                "                                                <br>\n" +
                "                                                <a href=\"#\" class=\"btn-again btn-again-show \" target=\"_blank\"><b></b>取消收藏</a>\n" +
                "                                            <br>\n" +
                "                                        </div>\n" +
                "                                    </td>\n" +
                "                                </tr>";
        }

        $(".collect-product-list-table").empty();
        $(".collect-product-list-table").append(orderTableHtml);

        drawPagination(cpage,data);

    }else{
        $(".collect-product-list-tips").show();
        $(".collect-product-list-table").hide();
        $(".pagination").empty();
    }
}

/**
 * 渲染分页工具条
 * @param cpage
 * @param data
 */
function drawPagination(cpage,data)
{

    var totalPage = 1;
    var total = data.total;

    if (data.total % data.size == 0) {
        totalPage = data.total / data.size;
    } else {
        totalPage = data.total / data.size;
        totalPage = parseInt(totalPage);
        totalPage += 1;
    }

    $(".pagination").empty();
    new pagination({
        pagination: $('.pagination'),
        maxPage: 7, //最大页码数,支持奇数，左右对称
        startPage: 1,    //默认第一页
        currentPage: cpage,          //当前页码
        totalItemCount: total,    //项目总数,大于0，显示页码总数
        totalPageCount: totalPage,        //总页数
        callback: function (pageNum) {
            if (g_om_cpage != pageNum) {
                loadListTable(pageNum);
            }
        }
    });
}




/**
 * 加载收藏商品页
 */
function loadListTable(cpage)
{

    loading.showLoading({
        type:1,
        tip:"查询中..."
    });

    var queryParams = {page:cpage,limit:10};
    g_om_cpage = cpage;
    $.ajax({
        type: "POST",
        url: basePath+"/api/user/collect/product/list",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(queryParams),
        dataType: "json",
        success: function (result) {
            if(result.code!=0)
            {
                drawCollectProductList(cpage,result.data);
                loading.hideLoading();
            }
        },
        complete:function(data,status){
            loading.hideLoading();
        }
    });
}
