/*容器2参数*/
var pagegizationConfigObject={
    obj_box:'.pageToolbar',//翻页容器
    total_item:1,//条目总数
    per_num:10,//每页条目数
    current_page:1//当前页
};



var g_product_query_obj={page:pagegizationConfigObject.current_page,limit:pagegizationConfigObject.per_num};
function doPage()
{
    loading.showLoading({
        type:6,
        tip:"查询中..."
    });
    g_product_query_obj.page = pagegizationConfigObject.current_page;
    $.ajax({
        type: "POST",
        url: basePath+"/api/shop/product/list",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify(g_product_query_obj),
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
        "                            <td style=\"width:100px;\" >商品预览</td>\n" +
        "                            <td style=\"width:300px;\">商品名称</td>\n" +
        // "                            <td style=\"width:75px;\" >库存</td>\n" +
        "                            <td style=\"width:75px;\" >商品分类</td>\n" +
        "                            <td style=\"width:75px;\" >上架状态</td>\n" +
        "                            <td style=\"width:100px;\" >发布时间</td>\n" +
        "                            <td style=\"width:200px;\">操作</td>\n" +
        "                        </tr>";
    if(pageResult!=null&&pageResult.list!=null&&pageResult.list.length>0)
    {
        for(var i=0;i<pageResult.list.length;i++)
        {
            var row = pageResult.list[i];
            tableHtml+=" <tr align=\"center\" class=\"tabTd\">\n" ;
            tableHtml+=   "                            <td><div class=\"tabTdWrap\">"+(i+1)+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\"><img src=\""+row.httpMainPhotoFilePath+"\" style=\"width:65px;height:65px; margin-top: 4%; margin-bottom: 4%;\"></div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.name+"</div></td>\n" ;
            // tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.stockNum+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.categoryName+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+(row.status==1?"<a style='color:green'>已上架</a>":"<a style='color:red'>已下架</a>")+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">"+row.createDate+"</div></td>\n" ;
            tableHtml+=    "                            <td><div class=\"tabTdWrap\">\n" ;
            tableHtml+=     "                                &nbsp;<a attr-id=\""+row.id+"\" class=\"previewRow\" style=\"color:blue;cursor: pointer;\">商品预览</a>\n" ;
            if(row.status==1)
            {
                tableHtml+=     "                                &nbsp;<a attr-id=\""+row.id+"\" attr-status=\""+row.status+"\" class='shelvesBtn' style=\"color:red;cursor: pointer;\">下架</a>\n" ;
            }else{
                tableHtml+=     "                                &nbsp;<a attr-id=\""+row.id+"\" attr-status=\""+row.status+"\" class='shelvesBtn' style=\"color:blue;cursor: pointer;\">上架</a>\n" ;
            }
            tableHtml+=     "                                &nbsp;<a attr-id=\""+row.id+"\" class=\"modifyStockRow\" style=\"color:blue;cursor: pointer;\">修改库存</a>\n" ;
            tableHtml+=     "                                &nbsp;<a attr-id=\""+row.id+"\" class=\"delRow\" style=\"color:red;cursor: pointer;\">删除</a>\n" ;
            // tableHtml+=     "                                &nbsp;<a attr-id=\""+row.uuid+"\" attr-name=\""+row.name+"\" class=\"modifyStockBtn\" style=\"color:blue;cursor: pointer;\">修改库存</a>\n" ;
            // tableHtml+=     "                                &nbsp;&nbsp;\n" ;
            tableHtml+=    "                            </div></td>\n" ;
            tableHtml+=    "                        </tr>";
        }

    }
    $("#productTableBody").html(tableHtml);
    $("#productTable").FrozenTable(2,0,0);
    bindRowEvents();
}


function bindRowEvents()
{
    bindPreviewEvent();
    bindShelvesEvent();
    bindDelEvent();
    bindStockEvent();
}


function bindPreviewEvent()
{
    //预览事件
    $(".previewRow").unbind("click");
    //SKU信息
    $(".previewRow").bind("click", function () {
        var attrId = $(this).attr("attr-id");

        window.open(shoppingPcPath+productPreviewPage+attrId);
    });
}

function bindShelvesEvent()
{

    //上架下架事件
    $(".shelvesBtn").unbind("click");
    $(".shelvesBtn").bind("click", function () {
        var attrId = $(this).attr("attr-id");
        var attrStatus = $(this).attr("attr-status");
        var optText="";
        if(attrStatus=="1")
        {
            optText="下架";
        }else{
            optText="上架";
        }
        layer.confirm("确定要"+optText+"?", {
            btn: ['确定','关闭'], //按钮
            title:'提示信息'
        }, function(index) {


            $.ajax({
                type: "POST",
                url: basePath+"/api/shop/product/shelves",
                contentType: "application/json;charset=utf-8",
                data:  JSON.stringify({id:attrId}),
                dataType: "json",
                success: function (result) {
                    if(result.code<=0)
                    {
                        $.message({
                            message: "操作失败,请稍后重试",
                            type: 'error'
                        });
                        return ;
                    }

                    $.message({
                        message: "操作成功",
                        type: 'success'
                    });
                },
                error: function (result) {
                    $.message({
                        message: "操作失败,请稍后重试",
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

function bindDelEvent()
{
    $(".delRow").unbind("click");
    //SKU信息
    $(".delRow").bind("click", function () {
        var attrId = $(this).attr("attr-id");
        layer.confirm('确定删除?', {
            btn: ['确定','关闭'], //按钮
            title:'提示信息'
        }, function(index) {
            // $("#descriptionTableTr" +attrIndex ).remove();
            $.ajax({
                type: "POST",
                url: basePath+"/api/shop/product/delete",
                contentType: "application/json;charset=utf-8",
                data:  JSON.stringify({id:attrId}),
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


//修改库存事件
function bindStockEvent()
{
    //预览事件
    $(".modifyStockRow").unbind("click");
    //SKU信息
    $(".modifyStockRow").bind("click", function () {

        var attrId = $(this).attr("attr-id");
        var skuTableHtml = "     <table border=\"0\" class=\"freezeTable\" style=\"width:90%;margin-top: 20px;margin-bottom: 20px;\" cellspacing=\"0\" cellpadding=\"0\">\n" +
            "                                <thead>\n" +
            "                                <tr class=\"tabTh\">\n" +
            "                                    <td align=\"center\" style=\"width:5%\">序号</td>\n" +
            "                                    <td align=\"center\" style=\"width:10%\">主图</td>\n" +
            "                                    <td align=\"center\" style=\"width:10%\">名称</td>\n" +
            "                                    <td align=\"center\" style=\"width:10%\">上架状态</td>\n" +
            "                                    <td align=\"center\" style=\"width:15%\">单价</td>\n" +
            "                                    <td align=\"center\" style=\"width:10%\">库存数量</td>\n" +
            "                                </tr>\n" +
            "                                </thead>\n" +
            "                                <tbody id=\"skuProductStockTable\">\n" +
            "\n" +
            "                                </tbody>\n" +
            "                            </table>\n" +
            "\n" +
            "\n";

        layer.open({
            type: 1,
            title: "修改库存",
            area: ['55%', '50%'], //宽高
            content: skuTableHtml
        });

        querySkuProductStockPage(attrId);
    });

}

/**
 * 查询SKU商品库存列表(查询全部,不做分页)
 * @param shopProductId
 */
function querySkuProductStockPage(shopProductId)
{
    $.ajax({
        type: "POST",
        url: basePath+"/api/product/sku/listByShopProductId",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify({shopProductId:shopProductId}),
        dataType: "json",
        success: function(result) {
            if(result.data!=null) {
                var listHtml = "";
                for (var i = 0; i < result.data.length; i++) {
                    var obj = result.data[i];
                    var statusName="";
                    if(obj.status==0)
                    {
                        statusName="<a style='color:red'>已下架</a>";
                    }else{
                        statusName="<a style='color:green'>已上架</a>";
                    }
                    listHtml+="<tr class=\"tabTd\">\n" +
                        "                        <td align=\"center\"  style=\"font-family:'宋体';\">\n" +
                        "                           "+(i+1)+"" +
                        "                        </td>\n" +
                        "                        <td align=\"center\"  style=\"font-family:'宋体';\">\n" +
                        "                            <img style='width:100px;height:100px' src='"+obj.httpProductPreviewPath+"'>\n" +
                        "                        </td>\n" +
                        "                        <td align=\"center\"  style=\"font-family:'宋体';\">\n" +
                        "                            "+obj.name+"\n" +
                        "                        </td>\n" +
                        "                        <td align=\"center\"  style=\"font-family:'宋体';\">\n" +
                        "                           "+statusName+"\n" +
                        "                        </td>\n" +
                        "                        <td align=\"center\"  style=\"font-family:'宋体';\">\n" +
                        "                           "+obj.price+"\n" +
                        "                        </td>\n" +
                        "                        <td align=\"center\"  style=\"font-family:'宋体';\">\n" +
                        "                           <input type='text' class='bootstrap-input-text' attr-id='"+obj.id+"' value='"+obj.stockNum+"' />\n" +
                        "                        </td>\n" +
                        "                    </tr>";
                }
                $("#skuProductStockTable").html(listHtml);

                // $(".select_cap").unbind();
                // $(".select_cap").click(function(){
                //     var attrId =$(this).attr("attr-id");
                //     if(g_selectConsigneeAddressPageData!=null&&g_selectConsigneeAddressPageData.length>0)
                //     {
                //
                //     }
                // });

            }else{
                $("#skuProductStockTable").html("<a style='font-size:20px;'>没有查询到商品信息~</a>");
            }

        },
        complete:function()
        {
        }

    });
}

function initPagination()
{

    $(".pageToolbar").html("<table id=\"productTable\" class=\"freezeTable\" border=\"1\" style=\"width:90%;\">\n" +
        "                        <tbody id=\"productTableBody\">\n" +
        "                        </tbody>\n" +
        "                    </table>");

    loading.showLoading({
        type:6,
        tip:"查询中..."
    });

    $.ajax({
        type: "POST",
        url: basePath+"/api/shop/product/list",
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify(g_product_query_obj),
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
        g_product_query_obj.page = 1;
        g_product_query_obj.name=$("#name").val();
        g_product_query_obj.status=$("#status option:selected").val();
        g_product_query_obj.startDateYMDHS=$("#startDate").val();
        g_product_query_obj.endDateYMDHS=$("#endDate").val();

        initPagination();
    });


    $("#resetBtn").bind( 'click' ,function(){
        $("#productForm").resetForm();
    });

    $('#startDate').datetimepicker({
        format:'Y-m-d H:i'
    });//初始化
    $('#endDate').datetimepicker({
        format:'Y-m-d H:i'
    });//初始化

    $.datetimepicker.setLocale('zh');//使用中文

    initPagination();
});