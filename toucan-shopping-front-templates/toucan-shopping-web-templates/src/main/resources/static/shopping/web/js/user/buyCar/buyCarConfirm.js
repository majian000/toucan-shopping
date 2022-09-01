
var g_buy_car_item_req = 0;
var g_consignee_address_item_req = 0;

var requestCompleted=2;

var g_consigneeAddress;

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



    $(".mac_modify").click(function(){
        if(g_consignee_address_item_req==1)
        {
            return;
        }
        var opt = $(this).attr("attr-opt");
        if(opt!=null&&opt=="1") {
            $(this).html("关闭");
            $(this).attr("attr-opt","2");
            drawConsigneeAddressEditControl();
        }else{
            $(this).html("修改");
            $(this).attr("attr-opt","1");
            updateConsigneeAddress();
        }
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
            }
        },
        error: function (result) {
        },
        complete:function(data,status){
            hideLoadding();
        }
    });
}


function updateConsigneeAddress()
{
    // $.ajax({
    //     type: "POST",
    //     url: basePath + "/api/user/consigneeAddress/update",
    //     contentType: "application/json;charset=utf-8",
    //     data: {},
    //     dataType: "json",
    //     success: function (result) {
    //         if(result.code==1)
    //         {
    //             g_consigneeAddress = result.data;
    //             $("#ca_name").html(result.data.name);
    //             $("#ca_phone").html(result.data.phone);
    //             $("#ca_provice_name").html(result.data.provinceName);
    //             $("#ca_city_name").html(result.data.cityName);
    //             $("#ca_area_name").html(result.data.areaName);
    //             $("#ca_address").html(result.data.address);
    //         }else{
    //             drawConsigneeAddressEditControl(null);
    //         }
    //     },
    //     error: function (result) {
    //     },
    //     complete:function(data,status){
    //         hideLoadding();
    //     }
    // });
}

function drawConsigneeAddressEditControl(obj)
{

    $("#ca_name_l").html("<a style='color:red'>*</a>收货人");
    $("#ca_name").html(" <input type=\"text\" id=\"name\" name=\"name\" maxlength=\"30\" style=\"width:307px\" class=\"l_ipt\" tabindex=\"1\" lay-verify=\"required\"  />");
    $("#ca_phone_l").html("<a style='color:red'>*</a>电话号");
    $("#ca_phone").html(" <input type=\"text\" id=\"phone\" name=\"phone\" maxlength=\"20\" style=\"width:307px\" class=\"l_ipt\" tabindex=\"1\" lay-verify=\"required\"  />");

    //隐藏省市区
    $("#ca_pc_tr").hide();
    $("#ca_area_name_l").hide();
    $("#ca_area_name").hide();

    $("#ca_pcc_l").show();
    $("#ca_pcc").show();

    $("#ca_address_l").html("<a style='color:red'>*</a>详细信息");
    $("#ca_address").html(" <textarea id=\"address\" name=\"address\" class=\"l_ipt\"  maxlength=\"300\"  tabindex=\"2\" style=\"width: 334px; height: 85px;\"  lay-verify=\"required\"  ></textarea>");

    if(g_consigneeAddress!=null)
    {
        $("#name").val(g_consigneeAddress.name);
        $("#phone").val(g_consigneeAddress.phone);
        $("#address").val(g_consigneeAddress.address);
    }
}