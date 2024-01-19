

var g_um_cpage=1;

$(function(){

    loadPageBefore();

    drawPageJumpBtns();

    bindAddBuyCar();

    bindShopCategoryEvent();

    bindProductListCollectProductEvent(".cplist");

});


function loadPageBefore()
{
    $(".search-price").mouseenter(function(){
        $(".search-price-edit").show();
    });
    $(".search-price").mouseleave(function(){
        $(".search-price-edit").hide();
    });
    $(".spclear").bind("click", function () {
        $("#psi").val("");
        $("#pei").val("");
    });

    $(".spbtn").bind("click", function () {
        $("#ps").val($("#psi").val());
        $("#pe").val($("#pei").val());
        queryShopCategoryProductList(null);
    });
    var scis = $(".sci");
    if(scis!=null&&scis.length>0)
    {
        var scid = $("#scid").val();
        for(var i=0;i<scis.length;i++)
        {
            var scisObj = $(scis[i]);
            if(scisObj.attr("attr-id")==scid)
            {
                scisObj.removeClass("sci_default");
                scisObj.addClass("cate_nav_active");
            }
        }
    }
}



function queryShopCategoryProductList(extParams)
{
    var sid = $("#sid").val();
    var scid = $("#scid").val();
    var params = "?sid="+sid+"&scid="+scid;
    if(extParams!=null&&extParams!="")
    {
        params+=extParams;
    }

    window.location.href=shopCategoryProductListPath+params;
}


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
        isShowTotalCount:false, //是否显示总页数
        callback: function (pageNum) {
            if (g_um_cpage != pageNum) {
                queryShopCategoryProductList("&page="+pageNum);
            }
        }
    });


}



function bindShopCategoryEvent(){

    $(".sci").bind("click", function () {
        $("#scid").val($(this).attr("attr-id"));
        queryShopCategoryProductList(null);
    });

    $(".pfla").bind("click", function () {
        var attrType=$(this).attr("attr-type");
        //默认排序
        if(attrType=="defult")
        {
            $("#pst").val("");
            $("#pdst").val("");
        }

        //价格排序
        if(attrType=="price")
        {
            $("#pdst").val("");
            var pstVal = $("#pst").val();
            if(pstVal==null||pstVal=="")
            {
                $("#pst").val("asc");
            }else if(pstVal=="asc")
            {
                $("#pst").val("desc");
            }else if(pstVal=="desc"){
                $("#pst").val("asc");
            }
        }
        if(attrType=="priceAsc")
        {
            $("#pdst").val("");
            $("#pst").val("asc");
        }
        if(attrType=="priceDesc")
        {
            $("#pdst").val("");
            $("#pst").val("desc");
        }

        //新品排序
        if(attrType=="newest")
        {
            $("#pst").val("");
            var pdstVal = $("#pdst").val();
            if(pdstVal==null||pdstVal=="")
            {
                $("#pdst").val("asc");
            }else if(pdstVal=="asc")
            {
                $("#pdst").val("desc");
            }else if(pdstVal=="desc"){
                $("#pdst").val("asc");
            }
        }
        if(attrType=="newestAsc")
        {
            $("#pst").val("");
            $("#pdst").val("asc");
        }
        if(attrType=="newestDesc")
        {
            $("#pst").val("");
            $("#pdst").val("desc");
        }

        queryShopCategoryProductList(null);
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


