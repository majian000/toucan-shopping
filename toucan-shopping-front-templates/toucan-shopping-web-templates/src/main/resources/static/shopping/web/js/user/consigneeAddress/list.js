
var g_um_cpage=1;

$(function () {
    queryConsignessAddressList(1);
});





function queryConsignessAddressList(cpage)
{

    loading.showLoading({
        type:1,
        tip:"查询中..."
    });

    g_um_cpage = cpage;

    if(basePath!="") {
        var totalPage = 1;
        var total = 0;
        $.ajax({
            type: "POST",
            url: basePath+"/api/user/consigneeAddress/list",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify({page:cpage}),
            dataType: "json",
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
                            var defaultStatusName="非默认";
                            if(obj.defaultStatus!=null&&obj.defaultStatus=="1")
                            {
                                defaultStatusName="<a style='color:#ff4e00;'>默认</a>";
                            }
                            listHtml+="<tr>\n" +
                                "                        <td align=\"center\"  style=\"font-family:'宋体';\">\n" +
                                "                            "+(i+1)+"\n" +
                                "                        </td>\n" +
                                "                        <td align=\"center\"  style=\"font-family:'宋体';\">\n" +
                                "                            "+defaultStatusName+"\n" +
                                "                        </td>\n" +
                                "                        <td align=\"center\"  style=\"font-family:'宋体';\">\n" +
                                "                            "+obj.name+"\n" +
                                "                        </td>\n" +
                                "                        <td align=\"center\"  style=\"font-family:'宋体';\">\n" +
                                "                           "+obj.address+"\n" +
                                "                        </td>\n" +
                                "                        <td align=\"center\"  style=\"font-family:'宋体';\">\n" +
                                "                            "+obj.phone+"\n" +
                                "                        </td>\n" +
                                "                        <td align=\"center\"  style=\"font-family:'宋体';\">\n" +
                                "                            "+obj.provinceName+"\n" +
                                "                        </td>\n" +
                                "                        <td align=\"center\"  style=\"font-family:'宋体';\">\n" +
                                "                            "+obj.cityName+"\n" +
                                "                        </td>\n" +
                                "                        <td align=\"center\"  style=\"font-family:'宋体';\">\n" +
                                "                            "+obj.areaName+"\n" +
                                "                        </td>\n" +
                                "                        <td align=\"center\">" ;
                                if(obj.defaultStatus==null||obj.defaultStatus=="0") {
                                    listHtml+=" <a href=\"#\" style=\"color:#ff4e00;\">设为默认</a>&nbsp; &nbsp; " ;
                                }
                                listHtml+="<a class=\"delete_ca\" style=\"cursor:pointer;\">删除</a></td>\n" +
                                "\n" +
                                "                    </tr>";
                        }
                        $("#consigneeAddressTable").html(listHtml);
                        bindDeleteConsigneeAddressEvent();
                    }
                }
            },
            complete:function()
            {
                loading.hideLoading();
                if(total<=0)
                {
                    $(".pagination").html("<a style='font-size:20px;'>您暂时没有收货信息~</a>");
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
                                queryConsignessAddressList(pageNum);
                            }
                        }
                    });
                }
            }

        });
    }

}




function bindDeleteConsigneeAddressEvent()
{
    $(".delete_ca").click(function(){
       alert(1);
    });
}