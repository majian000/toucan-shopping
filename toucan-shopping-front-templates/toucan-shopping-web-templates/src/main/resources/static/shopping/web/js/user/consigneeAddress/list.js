
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
                                    listHtml+=" <a attr-id=\""+obj.id+"\" class=\"default_ca\" style=\"color:#ff4e00;cursor:pointer;\">设为默认</a>&nbsp; &nbsp; " ;
                                }
                                listHtml+="<a class=\"update_ca\" attr-id=\""+obj.id+"\" style=\"cursor:pointer;\">修改</a>&nbsp; &nbsp; " ;
                                listHtml+="<a class=\"delete_ca\" attr-id=\""+obj.id+"\" style=\"cursor:pointer;\">删除</a>" +
                                    "</td>\n" +
                                "\n" +
                                "                    </tr>";
                        }
                        $("#consigneeAddressTable").html(listHtml);
                        bindDeleteConsigneeAddressEvent();
                        bindSetDefaultEvent();
                        bindUpdateEvent();

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

                    }else{
                        $("#consigneeAddressTable").html("");
                        $(".pagination").html("<a style='font-size:20px;'>您暂时没有收货信息~</a>");
                    }
                }
            },
            complete:function()
            {
                loading.hideLoading();
                if(total<=0)
                {
                    $(".pagination").html("<a style='font-size:20px;'>您暂时没有收货信息~</a>");
                }
            }

        });
    }

}


function deleteConsigneeAddress()
{
    confirmMessageDialog.hide();
    var consigneeAddressId = $("#caid").val();

    loading.showLoading({
        type:1,
        tip:"删除中..."
    });


    $.ajax({
        type: "POST",
        url: basePath + "/api/user/consigneeAddress/delete",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify({id: consigneeAddressId}),
        dataType: "json",
        success: function (result) {
            if (result.code > 0) {
                queryConsignessAddressList(1);
            }
            loading.hideLoading();
        },
        complete: function () {
            loading.hideLoading();
        }
    });
}


function bindDeleteConsigneeAddressEvent()
{

    $(".delete_ca").click(function(){
        confirmMessageDialog.init("确定要删除吗?",deleteConsigneeAddress);
        $("#cmd_extp").html("<input type=\"hidden\" id=\"caid\" value=\""+($(this).attr("attr-id"))+"\"  />");
        confirmMessageDialog.show();
    });
}


function bindUpdateEvent(){

    $(".update_ca").click(function(){
        var rowId= $(this).attr("attr-id");
        window.location.href = basePath+"/page/user/consigneeAddress/edit/"+rowId;
    });
}

function setDefaultConsigneeAddress()
{
    confirmMessageDialog.hide();
    var consigneeAddressId = $("#caid").val();

    loading.showLoading({
        type:1,
        tip:"设置中..."
    });


    $.ajax({
        type: "POST",
        url: basePath + "/api/user/consigneeAddress/set/default",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify({id: consigneeAddressId}),
        dataType: "json",
        success: function (result) {
            if (result.code > 0) {
                queryConsignessAddressList(1);
            }
            loading.hideLoading();
        },
        complete: function () {
            loading.hideLoading();
        }
    });
}

function bindSetDefaultEvent()
{
    $(".default_ca").click(function(){
        confirmMessageDialog.init("要设置成默认收货地址吗?",setDefaultConsigneeAddress);
        $("#cmd_extp").html("<input type=\"hidden\" id=\"caid\" value=\""+($(this).attr("attr-id"))+"\"  />");
        confirmMessageDialog.show();
    });
}