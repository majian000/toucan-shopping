

var g_um_cpage=1;

$(function(){


    drawPageJumpBtns();


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

