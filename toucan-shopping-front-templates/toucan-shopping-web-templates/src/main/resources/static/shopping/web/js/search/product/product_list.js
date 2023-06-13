

var g_um_cpage=1;

$(function(){

    loadPageBefore();

    drawPageJumpBtns();

    bindAddBuyCar();

    bindRemoveBrandEvent();

    bindAttributeEvent();

});


function loadPageBefore()
{

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
        callback: function (pageNum) {
            if (g_um_cpage != pageNum) {
                var keyword=$(".s_ipt").val();
                var searchUrl=searchGetPath+"?keyword="+keyword;
                window.location=searchUrl+"&page="+pageNum;
            }
        }
    });


}


function bindAttributeEvent(){
    $(".qb_labels").bind("click", function () {
        doSearchByProductList(this,1);
    });

    $(".rpab").bind("click", function () {
        var attrKV=$(this).attr("attr-kv");
        var attrKeyId=$(this).attr("attr-key-id");
        var ab = $("#ab").val();
        var abids = $("#abids").val();
        if(ab!=null&&ab!="")
        {
            var abArray = ab.split(",");
            var releaseAbArray =new Array();
            for(var i=0;i<abArray.length;i++)
            {
                if(abArray[i]!=attrKV)
                {
                    releaseAbArray.push(abArray[i]);
                }
            }
            if(releaseAbArray.length>0) {
                $("#ab").val(releaseAbArray.join(","));
            }else{
                $("#ab").val("");
            }
        }

        if(abids!=null&&abids!="") {
            var abidsArray = abids.split(",");
            var releaseAbidsArray = new Array();
            for (var i = 0; i < abidsArray.length; i++) {
                if (abidsArray[i] != attrKeyId) {
                    releaseAbidsArray.push(abidsArray[i]);
                }
            }
            if(releaseAbidsArray.length>0) {
                $("#abids").val(releaseAbidsArray.join(","));
            }else{
                $("#abids").val("");
            }
        }

        doSearchByProductList(null,0);
    });

}


/**
 *
 * @param eventSrcObj
 * @param type 1:属性查询 0:默认查询
 */
function doSearchByProductList(eventSrcObj,type)
{
    var keywrd=$(".s_ipt").val();
    var params ="?keyword="+keywrd;
    var ab = $("#ab").val();
    var abids = $("#abids").val();
    if(type==1)
    {
        var attrKV = $(eventSrcObj).attr("attr-kv");
        if(ab==null||ab=="")
        {
            ab=attrKV;
        }else{
            ab+=","+attrKV;
        }

        if(abids==null||abids=="")
        {
            abids=$(eventSrcObj).attr("attr-key-id");
        }else{
            abids+=","+$(eventSrcObj).attr("attr-key-id");
        }
    }
    params+="&ab="+ab;
    params+="&abids="+abids;
    window.location.href=searchGetPath+params;
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


function bindRemoveBrandEvent()
{
    $(".bcb").bind("click", function () {
        var bid = $(this).attr("attr-bid");
        var ebids = $("#ebids").val();
        var ebidArray = new Array();
        if(ebids!=null&&ebids!='')
        {
            ebidArray.addAll(ebids.split(","));
        }
        ebidArray.push(bid);
        $("#ebids").val(ebidArray.join(","));

        doSearch();
    });

    $(".qball").bind("click", function () {
        $("#qbs").val("f");
        doSearch();
    });
}