/*容器2参数*/
var pagegizationConfigObject={
    obj_box:'.pageToolbar',//翻页容器
    total_item:1,//条目总数
    per_num:10,//每页条目数
    current_page:1//当前页
};
var g_shop_banner_query_obj={page:pagegizationConfigObject.current_page,limit:pagegizationConfigObject.per_num};
function doPage()
{
    loading.showLoading({
        type:6,
        tip:"查询中..."
    });
    g_shop_banner_query_obj.page = pagegizationConfigObject.current_page;
    $.ajax({
        type: "POST",
        url: basePath+"/api/shop/banner/list",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify(g_shop_banner_query_obj),
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
                pagegizationConfigObject.total_item = result.data.total;
                pagegizationConfigObject.current_page = result.data.page;
                drawTable(result.data);
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
}

function drawTable(pageResult)
{
    var tableHtml="";
    tableHtml+=" <tr class=\"tabTh\">\n" +
        "                            <td style=\"width:50px;\" >序号</td>\n" +
        "                            <td style=\"width:100px;\">标题</td>\n" +
        "                            <td style=\"width:100px;\" >图片预览</td>\n" +
        "                            <td style=\"width:100px;\">跳转地址</td>\n" +
        "                            <td style=\"width:100px;\">显示位置</td>\n" +
        "                            <td style=\"width:100px;\">显示状态</td>\n" +
        "                            <td style=\"width:100px;\">开始展示时间</td>\n" +
        "                            <td style=\"width:100px;\">结束展示时间</td>\n" +
        "                            <td style=\"width:100px;\">创建时间</td>\n" +
        "                            <td style=\"width:200px;\">操作</td>\n" +
        "                        </tr>";
    if(pageResult!=null&&pageResult.list!=null&&pageResult.list.length>0)
    {
        for(var i=0;i<pageResult.list.length;i++)
        {
            var row = pageResult.list[i];
            var positionVal=row.position;
            positionVal = positionVal.replace("PC_INDEX","PC首页");
            positionVal = positionVal.replace("H5_INDEX","H5首页");
            tableHtml+=" <tr align=\"center\" class=\"tabTd\">\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+(i+1)+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.title+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\"><img src=\""+row.httpImgPath+"\" style=\"width:65px;height:65px; margin-top: 4%; margin-bottom: 4%;\"></div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\"><a href=\""+row.clickPath+"\" target=\"_blank\">"+row.clickPath+"</a></div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+positionVal+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+(row.showStatus!=null&&row.showStatus==1?"显示":"隐藏")+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.startShowDate+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.endShowDate+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.createDate+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">\n" ;
            tableHtml+=     "                                &nbsp;<a class=\"bannerEditRow\" attr-id=\""+row.id+"\"  style=\"color:blue;cursor: pointer;\">修改</a>\n" ;
            tableHtml+=     "                                &nbsp;<a class=\"bannerDelRow\" attr-id=\""+row.id+"\"  attr-title=\""+row.title+"\" style=\"color:red;cursor: pointer;\">删除</a>\n" ;

            tableHtml+=    "                            </div></td>\n" ;
            tableHtml+=    "                        </tr>";
        }

    }
    $("#shopBannerTableBody").html(tableHtml);
    $("#shopBannerTable").FrozenTable(2,0,0);
    bindShopBannerEditEvent();
    bindShopBannerDelEvent();
}


function bindShopBannerDelEvent()
{
    $(".bannerDelRow").unbind("click");
    //SKU信息
    $(".bannerDelRow").bind("click", function () {
        var attrId = $(this).attr("attr-id");
        var attrTitle = $(this).attr("attr-title");
        layer.confirm("确定删除"+attrTitle+"?", {
            btn: ['确定','关闭'], //按钮
            title:'提示信息'
        }, function(index) {
            // $("#descriptionTableTr" +attrIndex ).remove();
            $.ajax({
                type: "POST",
                url: basePath+"/api/shop/banner/delete/"+attrId,
                contentType: "application/json;charset=utf-8",
                data:  null,
                dataType: "json",
                success: function (result) {
                    if(result.code<=0)
                    {
                        $.message({
                            message: "删除失败,请稍后重试",
                            type: 'error'
                        });
                        return ;
                    }

                    $.message({
                        message: "删除成功",
                        type: 'success'
                    });
                },
                error: function (result) {
                    $.message({
                        message: "删除失败,请稍后重试",
                        type: 'error'
                    });
                },
                complete:function()
                {
                    layer.close(index);
                    $("#queryBtn").click();
                }

            });


        });
    });
}


function bindShopBannerEditEvent()
{
    //SKU信息
    $(".bannerEditRow").bind("click", function () {
        var attrId = $(this).attr("attr-id");
        window.location.href=basePath+"/page/shop/banner/edit/"+attrId
    });
}

function initPagination()
{

    $(".pageToolbar").html("<table id=\"shopBannerTable\" class=\"freezeTable\" border=\"1\" style=\"width:90%;\">\n" +
        "                        <tbody id=\"shopBannerTableBody\">\n" +
        "                        </tbody>\n" +
        "                    </table>");

    loading.showLoading({
        type:6,
        tip:"查询中..."
    });

    $.ajax({
        type: "POST",
        url: basePath+"/api/shop/banner/list",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify(g_shop_banner_query_obj),
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
                pagegizationConfigObject.total_item = result.data.total;
                pagegizationConfigObject.current_page = result.data.page;
                drawTable(result.data);
                page_ctrl(pagegizationConfigObject, doPage);
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
}

$(function () {


    $("#queryBtn").bind( 'click' ,function(){
        pagegizationConfigObject.current_page = 1;
        g_shop_banner_query_obj.page = 1;
        g_shop_banner_query_obj.name=$("#name").val();
        g_shop_banner_query_obj.startShowDateYMDHS=$("#startShowDate").val();
        g_shop_banner_query_obj.endShowDateYMDHS=$("#endShowDate").val();
        g_shop_banner_query_obj.bannerStatus=$("#bannerStatus option:selected").val();

        initPagination();
    });


    $('#addShopBannerBtn').on('click', function(){
        window.location.href=basePath+"/page/shop/banner/add";
    });

    $("#resetBtn").bind( 'click' ,function(){
        $("#shopBannerForm").resetForm();
    });

    jeDate("#startShowDate",{
        format: "YYYY-MM-DD hh:mm:ss"
    });

    jeDate("#endShowDate",{
        format: "YYYY-MM-DD hh:mm:ss"
    });

    initPagination();
});