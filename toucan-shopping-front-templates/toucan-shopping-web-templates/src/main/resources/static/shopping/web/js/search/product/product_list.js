

var g_um_cpage=1;

$(function(){


    drawPageJumpBtns();

    bindAddBuyCar();

});


function drawPageJumpBtns()
{
    var cpage = parseInt($("#page").val());
    var totalPage = parseInt($("#pageTotal").val());
    var total = parseInt($("#pageTotal").val());

    g_um_cpage = cpage;

    $(".pagination").empty();

    new pagination({
        pagination: $('.pagination'),
        maxPage: 7, //最大页码数,支持奇数，左右对称
        startPage: 1,    //默认第一页
        currentPage: cpage,          //当前页码
        totalItemCount: total,    //项目总数,大于0，显示页码总数
        totalPageCount: totalPage,        //总页数
        callback: function (pageNum) {
            if (g_um_cpage != pageNum) {
                var keyword=$(".s_ipt").val();
                var searchUrl=searchGetPath+"?keyword="+keyword;
                window.location=searchUrl+"&page="+pageNum;
            }
        }
    });


}



function bindAddBuyCar(){
    $(".p_l_ac").bind("click", function () {
        loading.showLoading({
            type:1,
            tip:"提交中..."
        });
        var skuId = $(this).attr("attr-pid");
        var params = {
            shopProductSkuId:skuId,
            buyCount:1
        };
        $.ajax({
            type: "POST",
            url: basePath+"/api/user/buyCar/save",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify(params),
            dataType: "json",
            success: function (result) {
                if(result.code==1 ||result.code == 201)
                {
                    if(result.data!=null){
                        $("#buyCarItemCount").html(result.data.length);
                    }
                    var buyCarPriceTotal=new BigNumber(0);
                    for(var i=0;i<result.data.length;i++)
                    {
                        var buyCarItem = result.data[i];
                        buyCarPriceTotal = buyCarPriceTotal.plus(new BigNumber(buyCarItem.productPrice).times(new BigNumber(buyCarItem.buyCount)));
                    }

                    $(".buy_car_price_total").html(buyCarPriceTotal.toFixed(2));
                    ShowDiv_1('userBuyCarMsg','fade1');
                    loadBuyCarPreviewPanel();
                }else if(result.code==403){
                    window.open(basePath+result.data+"?redirectUrl="+encodeURIComponent(getProductPageUrl()));
                }
            },
            error: function (result) {
            },
            complete:function(data,status){
                loading.hideLoading();
            }
        });
    });
}
