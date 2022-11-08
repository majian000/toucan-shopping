$(function () {

    loadBuyCarPanel();

    $(".mcar_remove").click(function(){
        removeBuyCar();
    });

    $(".mcar_clear").click(function(){
        clearBuyCar();
    });
});




function loadBuyCarPanel(){
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
                var productHtmls="";
                var productPriceTotal = 0;
                var dataLength = 0;
                if(result.data!=null)
                {
                    dataLength = result.data.length;
                }
                for(var i=0;i<result.data.length;i++)
                {
                    var buyCarItem = result.data[i];
                    var noAllowedBuyDesc="";
                    if(!buyCarItem.isAllowedBuy)
                    {
                        noAllowedBuyDesc="&nbsp;"+buyCarItem.noAllowedBuyDesc;
                    }
                    productHtmls+="  <tr>\n" +
                        "                <td>\n" +
                        "                    <div class=\"c_s_img\"><a href=\""+basePath+"/page/product/detail/"+buyCarItem.shopProductSkuId+"\" target='_blank' ><img src=\""+buyCarItem.httpProductImgPath+"\" title=\""+buyCarItem.productSkuName+"\" width=\"73\" height=\"73\" /></a></div>\n" ;
                        if(!buyCarItem.isAllowedBuy)
                        {
                            productHtmls+=    "                    <del>"+buyCarItem.productSkuName+noAllowedBuyDesc+"</del>\n" ;
                        }else{
                            productHtmls+=    "                    <a href=\""+basePath+"/page/product/detail/"+buyCarItem.shopProductSkuId+"\" target='_blank'>"+buyCarItem.productSkuName+"</a>\n" ;
                        }
                        productHtmls+="                </td>\n" +
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
                    if(buyCarItem.isAllowedBuy) {
                        productPriceTotal += (buyCarItem.productPrice * buyCarItem.buyCount);
                    }
                }
                productHtmls+="<tr height=\"70\">\n" +
                    "                <td colspan=\"6\" style=\"font-family:'Microsoft YaHei'; border-bottom:0;\">\n" +
                    "                    <label class=\"r_rad\" style=\"padding-top: 5px;\"><input type=\"checkbox\" name=\"clear\"  class=\"clear_buy_car\" /></label><label class=\"r_txt\"><a style=\"cursor:pointer;\" class=\"clear_buy_car\" >清空购物车</a></label>\n" +
                    "                    <span class=\"fr\">商品总价：<b style=\"font-size:22px; color:#ff4e00;\">￥<a id=\"productPriceTotal\" style=\"color: #ff4e00;\">"+productPriceTotal+"</a></b></span>\n" +
                    "                </td>\n" +
                    "            </tr>\n" +
                    "            <tr valign=\"top\" height=\"150\">\n" +
                    "                <td colspan=\"6\" align=\"right\">\n" +
                    "                    <a style=\"cursor:pointer;\" class=\"refersh_buy_car\" onclick=\"refershBuyCar(1,0);\"><img src=\""+basePath+"/static/images/buy1.gif\" /></a>&nbsp; &nbsp; <a style=\"cursor:pointer;\" onclick=\"refershBuyCar(2,"+dataLength+");\" ><img src=\""+basePath+"/static/images/buy2.gif\" /></a>\n" +
                    "                </td>\n" +
                    "            </tr>";


                $(".mcar_tab").append(productHtmls);

                bindBuyItemNumEvent();
                bindClearBuyCar();
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

function refershBuyCar(srcType,itemCount)
{
    //继续购物
    if(srcType==1)
    {
        window.location.href=basePath+"/";
    }else if(srcType==2) //确认付款
    {
        if(itemCount<=0)
        {
            $.message({
                message: "您的购物车是空的,快去购物吧",
                type: 'success'
            });
            return;
        }
        window.location.href=basePath+"/page/user/buyCar/confirm";
    }
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
                window.location.reload();
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

function clearBuyCar()
{
    CloseDiv_1('buyCarClear','fadeClear');
    loading.showLoading({
        type:1,
        tip:"提交中..."
    });
    $.ajax({
        type: "POST",
        url: basePath+"/api/user/buyCar/clear",
        contentType: "application/json;charset=utf-8",
        data: {},
        dataType: "json",
        success: function (result) {
            if(result.code==1)
            {
                window.location.reload();
            }else{
                $.message({
                    message: "清空失败,请稍后重试",
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

function updateRow(cid,bnum)
{
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

function bindClearBuyCar()
{
    $(".clear_buy_car").click(function(){
        ShowDiv('buyCarClear','fadeClear');
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

}