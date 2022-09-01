
var g_buy_car_item_req = 0;

var requestCompleted=2;

var g_consigneeAddress; //收货人信息
var g_updateConsigneeAddressStatus=0; //修改收货人信息状态

var g_ca_cpage=1; //收货人当前页

$(function () {
    startLoadding();
    loadBuyCarPanel();
    loadDefaultConsigneeAddress();

    $("#ms_city").click(function (e) {
        SelCity(this,e);
    });
    $(".msc_l").click(function (e) {
        SelCity(document.getElementById("ms_city"),e);
    });

    $(".mcar_remove").click(function(){
        removeBuyCar();
    });

    $(".mcar_modify").click(function(){
        if(g_buy_car_item_req==1)
        {
            return;
        }
        var opt = $(this).attr("attr-opt");
        if(opt!=null&&opt=="1") {
            $(this).html("关闭");
            $(this).attr("attr-opt","2");
            loadModifyBuyCarPanel();
        }else{
            $(this).html("修改");
            $(this).attr("attr-opt","1");
            loadBuyCarPanel();
        }
    });

    $(".confirm_payment_btn").click(function(){
        if(g_updateConsigneeAddressStatus==1)
        {
            $.message({
                message: "请先保存收货人信息",
                type: 'error'
            });
        }
    });


    $(".mac_modify").click(function(){
        var opt = $(this).attr("attr-opt");
        if(opt!=null&&opt=="1") {
            $(this).html("保存");
            $(this).attr("attr-opt","2");
            drawConsigneeAddressEditControl();
            g_updateConsigneeAddressStatus=1;
        }else{
            updateConsigneeAddress(this);
        }
    });


    $(".ca_select").click(function(){
        queryConsignessAddressList(1);
    });

});




function startLoadding()
{
    loading.showLoading({
        type:1,
        tip:"查询中..."
    });
}

function hideLoadding()
{
    requestCompleted--;
    if(requestCompleted<=0)
    {
        loading.hideLoading();
    }
}


function loadBuyCarPanel(){
    $.ajax({
        type: "POST",
        url: basePath + "/api/user/buyCar/list",
        contentType: "application/json;charset=utf-8",
        data: {},
        dataType: "json",
        success: function (result) {
            if(result.code == 1){
                var productHtmls=" <tr>\n" +
                    "                    <td class=\"car_th\" style = \"width:35%\">商品名称</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:15%\">属性</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:10%\">购买数量</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:20%\">小计</td>\n" +
                    "                </tr>";
                var productPriceTotal = 0;

                for(var i=0;i<result.data.length;i++)
                {
                    var buyCarItem = result.data[i];
                    productHtmls+="  <tr id=\"tr_"+buyCarItem.id+"\">\n" +
                        "                <td>\n" +
                        "                    <div class=\"c_s_img\"><a href=\""+basePath+"/page/product/detail/"+buyCarItem.shopProductSkuId+"\" target='_blank' ><img src=\""+buyCarItem.httpProductImgPath+"\" title=\""+buyCarItem.productSkuName+"\" width=\"73\" height=\"73\" /></a></div>\n" +
                        "                    <a href=\""+basePath+"/page/product/detail/"+buyCarItem.shopProductSkuId+"\" target='_blank'>"+buyCarItem.productSkuName+"</a>\n" +
                        "                </td>\n" +
                        "                <td align=\"center\">"+buyCarItem.attributePreview+"</td>\n" +
                        "                <td align=\"center\">\n" +buyCarItem.buyCount + "                </td>\n" +
                        "                <td align=\"center\" style=\"color:#ff4e00;\">￥<a id=\"buyItemTotal_"+buyCarItem.id+"\" style=\"color: #ff4e00;\">"+(buyCarItem.buyCount*buyCarItem.productPrice)+"</a></td>\n" +
                        "            </tr>\n" +
                        "           ";
                    productPriceTotal+=(buyCarItem.productPrice*buyCarItem.buyCount);
                }
                productHtmls+=" <tr>\n" +
                    "                    <td colspan=\"5\" align=\"right\" style=\"font-family:'Microsoft YaHei';\">\n" +
                    "                        商品总价：￥<a id=\"productPriceTotal\">"+productPriceTotal+"</a>\n" +
                    "                    </td>\n" +
                    "                </tr>";


                $(".mcar_tab").html(productHtmls);



                $(".product_price_total").html("￥"+productPriceTotal);
                $(".order_price_total").html("￥"+productPriceTotal);

            }
        },
        error: function (result) {
        },
        complete:function(data,status){
            hideLoadding();
        }
    });
}




function loadModifyBuyCarPanel(){
    loading.showLoading({
        type:1,
        tip:"查询中..."
    });

    $.ajax({
        type: "POST",
        url: basePath + "/api/user/buyCar/list",
        contentType: "application/json;charset=utf-8",
        data: {},
        dataType: "json",
        success: function (result) {
            if(result.code == 1){
                var productHtmls=" <tr>\n" +
                    "                    <td class=\"car_th\" style = \"width:35%\">商品名称</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:15%\">属性</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:10%\">购买数量</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:20%\">小计</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:20%\">操作</td>\n" +
                    "                </tr>";
                var productPriceTotal = 0;

                for(var i=0;i<result.data.length;i++)
                {
                    var buyCarItem = result.data[i];
                    productHtmls+="  <tr id=\"tr_"+buyCarItem.id+"\">\n" +
                        "                <td>\n" +
                        "                    <div class=\"c_s_img\"><a href=\""+basePath+"/page/product/detail/"+buyCarItem.shopProductSkuId+"\" target='_blank' ><img src=\""+buyCarItem.httpProductImgPath+"\" title=\""+buyCarItem.productSkuName+"\" width=\"73\" height=\"73\" /></a></div>\n" +
                        "                    <a href=\""+basePath+"/page/product/detail/"+buyCarItem.shopProductSkuId+"\" target='_blank'>"+buyCarItem.productSkuName+"</a>\n" +
                        "                </td>\n" +
                        "                <td align=\"center\">"+buyCarItem.attributePreview+"</td>\n" +
                        "                <td align=\"center\">\n" +
                        "                    <div class=\"c_num\">\n" +
                        "                        <input type=\"button\" value=\"\" onclick=\"subNum('"+buyCarItem.id+"');\" class=\"car_btn_1\" />\n" +
                        "                        <input type=\"text\" value=\""+buyCarItem.buyCount+"\" id=\"num_"+buyCarItem.id+"\" attr-cid=\""+buyCarItem.id+"\" class=\"car_ipt mcar_pn\" />\n" +
                        "                        <input type=\"button\" value=\"\" onclick=\"addNum('"+buyCarItem.id+"');\" class=\"car_btn_2\" />\n" +
                        "                        <input type=\"hidden\" class='mcar_pp'  id=\"productPrice_"+buyCarItem.id+"\" value=\""+buyCarItem.productPrice+"\" />"+
                        "                    </div>\n" +
                        "                </td>\n" +
                        "                <td align=\"center\" style=\"color:#ff4e00;\">￥<a id=\"buyItemTotal_"+buyCarItem.id+"\" style=\"color: #ff4e00;\">"+(buyCarItem.buyCount*buyCarItem.productPrice)+"</a></td>\n" +
                        "                <td align=\"center\"><a onclick=\"showRemoveBuyCar('"+buyCarItem.id+"','"+buyCarItem.productSkuName+"')\">删除</a></td>\n" +
                        "            </tr>\n" +
                        "           ";
                    productPriceTotal+=(buyCarItem.productPrice*buyCarItem.buyCount);
                }

                productHtmls+=" <tr>\n" +
                    "                    <td colspan=\"5\" align=\"right\" style=\"font-family:'Microsoft YaHei';\">\n" +
                    "                        商品总价：￥<a id=\"productPriceTotal\">"+productPriceTotal+"</a>\n" +
                    "                    </td>\n" +
                    "                </tr>";


                $(".mcar_tab").html(productHtmls);


                $(".product_price_total").html("￥"+productPriceTotal);
                $(".order_price_total").html("￥"+productPriceTotal);

                bindBuyItemNumEvent();
            }
            loading.hideLoading();
        },
        error: function (result) {
        },
        complete:function(data,status){
            loading.hideLoading();
        }
    });
}



function bindBuyItemNumEvent()
{
    $(".mcar_pn").change(function(){
        var bnum = $(this).val();
        var cid = $(this).attr("attr-cid");
        if(isNaN(bnum))
        {
            bnum = "1";
        }
        updateRow(cid,bnum);
    });
}


function subNum(cid)
{
    var c = $("#num_"+cid).val();
    if(c<=1){
        c=1;
    }else{
        c=parseInt(c)-1;
        $("#num_"+cid).val(c);
    }
    updateRow(cid,$("#num_"+cid).val());
}



function addNum(cid)
{
    var c = $("#num_"+cid).val();
    c=parseInt(c)+1;
    $("#num_"+cid).val(c);

    updateRow(cid,$("#num_"+cid).val());
}



function updateRow(cid,bnum)
{
    g_buy_car_item_req = 1;
    loading.showLoading({
        type:1,
        tip:"提交中..."
    });

    var buyCarItem = {
        id:cid,
        buyCount:bnum
    };

    $.ajax({
        type: "POST",
        url: basePath+"/api/user/buyCar/update",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(buyCarItem),
        dataType: "json",
        success: function (result) {
            if(result.code==1)
            {
                $(this).val(bnum);
                $("#buyItemTotal_"+cid).html((parseInt(bnum)*parseFloat($("#productPrice_"+cid).val())));
                calculatePriceTotal();
            }else{
                $.message({
                    message: "请稍后重试",
                    type: 'error'
                });
            }
            g_buy_car_item_req=0;
        },
        error: function (result) {
        },
        complete:function(data,status){
            loading.hideLoading();
            g_buy_car_item_req=0;
        }
    });
}

function calculatePriceTotal()
{
    var pns = $(".mcar_pn"); //数量
    var pps = $(".mcar_pp"); //单价
    var productPriceTotal = 0;
    for(var i=0;i<pns.length;i++)
    {
        productPriceTotal+= (parseInt($(pns[i]).val())*parseFloat($(pps[i]).val()));
    }
    $("#productPriceTotal").html(productPriceTotal);

    $(".product_price_total").html("￥"+productPriceTotal);
    $(".order_price_total").html("￥"+productPriceTotal);
}




function showRemoveBuyCar(cid,cname)
{
    $("#removeBuyCarId").val(cid);
    $("#removeBuyCarProductName").html(cname);
    ShowDiv('removeBuyCar','fade');
}



function removeBuyCar()
{
    CloseDiv_1('removeBuyCar','fade');
    loading.showLoading({
        type:1,
        tip:"提交中..."
    });
    var buyCarId = $("#removeBuyCarId").val();

    var params = {
        id:buyCarId
    };
    $.ajax({
        type: "POST",
        url: basePath+"/api/user/buyCar/remove",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(params),
        dataType: "json",
        success: function (result) {
            if(result.code==1)
            {
                $("#tr_"+buyCarId).remove();
                calculatePriceTotal();
            }else{
                $.message({
                    message: "删除失败,请稍后重试",
                    type: 'error'
                });
            }
        },
        error: function (result) {
        },
        complete:function(data,status){
            loading.hideLoading();
        }
    });
}


function loadDefaultConsigneeAddress(){
    $.ajax({
        type: "POST",
        url: basePath + "/api/user/consigneeAddress/find/default",
        contentType: "application/json;charset=utf-8",
        data: {},
        dataType: "json",
        success: function (result) {
            if(result.code==1)
            {
                g_consigneeAddress = result.data;
                $("#ca_name").html(result.data.name);
                $("#ca_phone").html(result.data.phone);
                $("#ca_provice_name").html(result.data.provinceName);
                $("#ca_city_name").html(result.data.cityName);
                $("#ca_area_name").html(result.data.areaName);
                $("#ca_address").html(result.data.address);
            }else{
                drawConsigneeAddressEditControl(null);

                $(".mac_modify").html("保存");
                $(".mac_modify").attr("attr-opt","2");

                g_updateConsigneeAddressStatus=1;
            }
        },
        error: function (result) {
        },
        complete:function(data,status){
            hideLoadding();
        }
    });
}


function updateConsigneeAddress(acobj)
{
    var cau_attr = $("#caubtn").attr("attr-opt");

    if(cau_attr=="2") {
        if (!checkInputFunction($('#caubtn'), 2)) {
            return false;
        }

        var fields = $('#consigneeAddressForm').serializeArray();
        var params = {}; //声明一个对象
        $.each(fields, function (index, field) {
            params[field.name] = field.value; //通过变量，将属性值，属性一起放到对象中
        });

        $.ajax({
            type: "POST",
            url: basePath + "/api/user/consigneeAddress/update/save",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify(params),
            dataType: "json",
            success: function (result) {
                if (result.code == 1) {

                    g_consigneeAddress = result.data;
                    $(acobj).html("修改");
                    $(acobj).attr("attr-opt","1");
                    //隐藏修改表单
                    $("#ca_edit_form").hide();
                    $("#ca_form").show();

                    $("#ca_name").html(result.data.name);
                    $("#ca_phone").html(result.data.phone);
                    $("#ca_provice_name").html(result.data.provinceName);
                    $("#ca_city_name").html(result.data.cityName);
                    $("#ca_area_name").html(result.data.areaName);
                    $("#ca_address").html(result.data.address);

                    g_updateConsigneeAddressStatus=0;
                }
            },
            error: function (result) {
            },
            complete: function (data, status) {
            }
        });
    }
}

function drawConsigneeAddressEditControl(obj)
{
    //隐藏只读表单
    $("#ca_form").hide();
    $("#ca_edit_form").show();

    if(g_consigneeAddress!=null)
    {
        $("#ca_id").val(g_consigneeAddress.id);
        $("#ca_name_edit").val(g_consigneeAddress.name);
        $("#ca_phone_edit").val(g_consigneeAddress.phone);
        $("#ca_address_edit").val(g_consigneeAddress.address);


        $("#province").val(g_consigneeAddress.provinceName);
        $("#city").val(g_consigneeAddress.cityName);
        $("#area").val(g_consigneeAddress.areaName);
        $("#province_code").val(g_consigneeAddress.provinceCode);
        $("#city_code").val(g_consigneeAddress.cityCode);
        $("#area_code").val(g_consigneeAddress.areaCode);

        var ms_cityVal="";
        ms_cityVal = g_consigneeAddress.provinceName;
        if(g_consigneeAddress.cityName!=null&&g_consigneeAddress.cityName!="")
        {
            ms_cityVal+="/"+g_consigneeAddress.cityName;
        }
        if(g_consigneeAddress.areaName!=null&&g_consigneeAddress.areaName!="")
        {
            ms_cityVal+="/"+g_consigneeAddress.areaName;
        }
        $("#ms_city").val(ms_cityVal);
    }
}








function queryConsignessAddressList(cpage)
{

    var consigneeAddressTableHtml="     <table border=\"0\" class=\"consignee_address_tab\" style=\"width:930px;margin-top: 20px;\" cellspacing=\"0\" cellpadding=\"0\">\n" +
        "                                <thead>\n" +
        "                                <tr>\n" +
        "                                    <td align=\"center\" style=\"width:5%\">序号</td>\n" +
        "                                    <td align=\"center\" style=\"width:10%\">状态</td>\n" +
        "                                    <td align=\"center\" style=\"width:10%\">姓名</td>\n" +
        "                                    <td align=\"center\" style=\"width:15%\">地址</td>\n" +
        "                                    <td align=\"center\" style=\"width:10%\">电话</td>\n" +
        "                                    <td align=\"center\" style=\"width:10%\">省/直辖市</td>\n" +
        "                                    <td align=\"center\" style=\"width:10%\">地市</td>\n" +
        "                                    <td align=\"center\" style=\"width:10%\">区县</td>\n" +
        "                                </tr>\n" +
        "                                </thead>\n" +
        "                                <tbody id=\"consigneeAddressTable\">\n" +
        "\n" +
        "                                </tbody>\n" +
        "                            </table>\n" +
        "\n" +
        "\n" +
        "                            <div class=\"pagination\" id=\"consignee_address_pagination\">\n" +
        "                            </div>\n" +
        "\n";

    layer.open({
        type: 1,
        title:"选择收货人",
        area: ['75%', '40%'], //宽高
        content: consigneeAddressTableHtml
    });


    loading.showLoading({
        type:1,
        tip:"查询中..."
    });

    g_ca_cpage = cpage;

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
                                "                       " ;

                                "                    </tr>";
                        }
                        $("#consigneeAddressTable").html(listHtml);

                        $("#consignee_address_pagination").empty();
                        new pagination({
                            pagination: $('#consignee_address_pagination'),
                            maxPage: 7, //最大页码数,支持奇数，左右对称
                            startPage: 1,    //默认第一页
                            currentPage: cpage,          //当前页码
                            totalItemCount: total,    //项目总数,大于0，显示页码总数
                            totalPageCount: totalPage,        //总页数
                            callback: function (pageNum) {
                                if (g_ca_cpage != pageNum) {
                                    queryConsignessAddressList(pageNum);
                                }
                            }
                        });

                    }else{
                        $("#consigneeAddressTable").html("");
                        $("#consignee_address_pagination").html("<a style='font-size:20px;'>您暂时没有收货信息~</a>");
                    }

                }
            },
            complete:function()
            {
                alert(1);
                loading.hideLoading();
                if(total<=0)
                {
                    $("#consignee_address_pagination").html("<a style='font-size:20px;'>您暂时没有收货信息~</a>");
                }
            }

        });
    }

}
