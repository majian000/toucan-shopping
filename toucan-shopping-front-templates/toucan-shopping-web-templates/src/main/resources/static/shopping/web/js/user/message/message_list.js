
var g_um_cpage=1;
$(function () {

    queryMessageList(1);
});


function queryMessageList(cpage)
{

    loading.showLoading({
        type:1,
        tip:"查询中..."
    });

    g_um_cpage = cpage;
    if(messageBasePath!="") {
        var totalPage = 1;
        var total = 0;
        $.ajax({
            type: "POST",
            url: messageBasePath + "/api/user/message/list",
            data: {page:cpage,srcType:1},
            dataType: 'json',
            xhrFields: {
                withCredentials: true //允许跨域带Cookie
            },
            success: function(result) {
                if (result.code > 0) {
                    total = result.data.total;
                    if(result.data.total>0) {
                        if (result.data.total % result.data.size == 0) {
                            totalPage = result.data.total / result.data.size;
                        } else {
                            totalPage = result.data.total / result.data.size;
                            totalPage = parseInt(totalPage);
                            totalPage += 1;
                        }
                        var listHtml = "";
                        for (var i = 0; i < result.data.list.length; i++) {
                            var obj = result.data.list[i];
                            var row = " <div class='accordion-item'>";
                            row += "<button id='accordion-button-" + (i + 1) + "' aria-expanded='false' onclick=\"messageExpand('accordion-button-" + (i + 1) + "','" + obj.id + "');\">";
                            row += "<span class='accordion-title'>";
                            if (obj.status == 0) {
                                row += "<i class='redpoint'></i>";
                            }
                            row += "&nbsp;" + obj.title + "&nbsp;&nbsp;&nbsp;";
                            row += "<a class='accordion-title-time'>" + obj.sendDate + "</a>";
                            row += "</span>";
                            row += "<span class='icon' aria-hidden='true'>展开</span>";
                            row += "</button>";
                            row += " <div class='accordion-content'>";
                            row += " <p>" + obj.content + "</p>";
                            row += "</div>";
                            row += "</div>";
                            listHtml += row;
                        }
                        $(".accordion").html(listHtml);
                    }
                }
            },
            complete:function()
            {
                loading.hideLoading();
                if(total<=0)
                {
                    $(".pagination").html("<a style='font-size:20px;'>您暂时没有消息~</a>");
                }else {
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
                                queryMessageList(pageNum);
                            }
                        }
                    });
                }
            }

        })
    }

}

function messageExpand(btnId,rowId)
{
    var expAttr = $("#"+btnId).attr("aria-expanded");
    if(expAttr=="true")
    {
        $("#"+btnId).find(".icon").html("展开");
        $("#"+btnId).attr("aria-expanded","false");
    }else{
        $("#"+btnId).find(".redpoint").remove();
        $("#"+btnId).find(".icon").html("收起");
        $("#"+btnId).attr("aria-expanded","true");
        readStatus(rowId);
    }
}

function readStatus(rowId)
{
    $.ajax({
        type: "POST",
        url: messageBasePath + "/api/user/message/read/"+rowId,
        data: null,
        dataType: 'json',
        xhrFields: {
            withCredentials: true //允许跨域带Cookie
        },
        success: function (result) {

        }
    });
}

function allRead()
{

    $.ajax({
        type: "POST",
        url: messageBasePath + "/api/user/message/read/all",
        data:  {"srcType":1},
        dataType: 'json',
        xhrFields: {
            withCredentials: true //允许跨域带Cookie
        },
        success: function (result) {
            if (result.code > 0) {
                queryMessageList(1);
            }
        }
    });
}