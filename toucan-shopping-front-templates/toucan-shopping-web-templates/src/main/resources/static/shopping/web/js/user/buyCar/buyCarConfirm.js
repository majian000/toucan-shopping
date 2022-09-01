
var g_buy_car_item_req = 0;

var requestCompleted=2;

var g_consigneeAddress;
var g_updateConsigneeAddressStatus=0;

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
            url: basePath + "/api/user/consigneeAddress/update",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify(params),
            dataType: "json",
            success: function (result) {
                if (result.code == 1) {

                    $(acobj).html("修改");
                    $(acobj).attr("attr-opt","1");
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