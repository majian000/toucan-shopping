

var g_um_cpage=1;

$(function(){

    loadPageBefore();

    drawPageJumpBtns();

    productListBindAddBuyCar(".p_l_ac");

    bindRemoveBrandEvent();

    bindAttributeEvent();

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
        doSearch();
    });


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
                doSearch("&page="+pageNum);
            }
        }
    });


}



function bindAttributeEvent(){

    $(".qb_labels").bind("click", function () {
        doSearchByProductList(this,1);
    });

    //品牌名称条件查询
    $(".abb_labels").bind("click", function () {
        var attrId = $(this).attr("attr-id");
        var attrName = $(this).attr("attr-name");
        var ebids=$("#ebids").val();
        //将这个品牌条件从排除的品牌ID列表中移除
        if(ebids!=null&&ebids!="")
        {
            var ebidsArray = ebids.split(",");
            var releaseArray =new Array();
            for(var i=0;i<ebidsArray.length;i++)
            {
                if(ebidsArray[i]!=attrId)
                {
                    releaseArray.push(ebidsArray[i]);
                }
            }
            if(releaseArray.length>0) {
                $("#ebids").val(releaseArray.join(","));
            }else{
                $("#ebids").val("");
            }
        }
        $("#bid").val(attrId);
        $("#bn").val(attrName);
        $("#qbs").val("f");
        doSearch();
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

        doSearch();
    });


    $(".bcb").bind("click", function () {
        $("#bid").val("");
        $("#bn").val("");
        doSearch();
    });

    //属性条件查询
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
 * 查询商品列表
 * @param eventSrcObj
 * @param type 1:属性查询 0:默认查询
 */
function doSearchByProductList(eventSrcObj,type)
{
    var params="";
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
        $("#ab").val(ab);

        if(abids==null||abids=="")
        {
            abids=$(eventSrcObj).attr("attr-key-id");
        }else{
            abids+=","+$(eventSrcObj).attr("attr-key-id");
        }
        $("#abids").val(abids);
    }
    doSearch(params);
}


/**
 * 移除品牌条件
 */
function bindRemoveBrandEvent()
{
    $(".bcb").bind("click", function () {
        var bid = $(this).attr("attr-bid");
        var ebids = $("#ebids").val();
        var ebidArray = new Array();
        if(ebids!=null&&ebids!='')
        {
            ebidArray.push(ebids.split(","));
        }
        ebidArray.push(bid);
        $("#ebids").val(ebidArray.join(","));
        $("#qbs").val("f");

        doSearch();
    });

    $(".qball").bind("click", function () {
        $("#qbs").val("f");
        doSearch();
    });
}


