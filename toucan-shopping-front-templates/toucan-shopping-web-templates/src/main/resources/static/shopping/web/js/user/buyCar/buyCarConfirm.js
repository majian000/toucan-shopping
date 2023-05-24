
var g_buy_car_item_req = 0;

var requestCompleted=2;

var g_consigneeAddress; //收货人信息
var g_updateConsigneeAddressStatus=0; //修改收货人信息状态

var g_ca_cpage=1; //收货人当前页

var g_selectConsigneeAddressPageData = null;
var g_sleectConsigneeAddressDialogHandler = null;

var g_cache_buy_items=null;
var g_freight_money = new BigNumber(0); //运费总价
var g_product_price_money=new BigNumber(0); //商品总价
var g_cache_buy_item_select_freight=new Map(); //选择运费模板

function loadBuyCarConfirmPage() {
    startLoadding();
    loadDefaultConsigneeAddress();

    $('#order_remark').bind('input propertychange',function(){
        $('.or_max_tips').html($(this).val().length+"/"+$(this).attr("maxlength"));
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

        if(!validConsigneeAddressForm())
        {
            $.message({
                message: "请完善收货人信息",
                type: 'error'
            });
        }

    });


    $(".mac_modify").click(function(){
        var opt = $(this).attr("attr-opt");
        if(opt!=null&&opt=="1") {
            $(this).html("保存");
            $(this).attr("attr-opt","2");
            $(".mac_close").show();
            drawConsigneeAddressEditControl();
            g_updateConsigneeAddressStatus=1;
            $(".ca_select").hide();
        }else{
            updateConsigneeAddress(this);
        }
    });

    $(".mac_close").click(function(){
        $(".mac_close").hide();
        $(".mac_modify").html("修改");
        $(".mac_modify").attr("attr-opt","1");
        //隐藏修改表单
        $("#ca_edit_form").hide();
        $("#ca_form").show();
        g_updateConsigneeAddressStatus=0;
        $(".ca_select").show();
    });


    $(".ca_select").click(function(){
        openSelectConsigneeAdddressDialog();
        queryConsignessAddressList(1);
    });


    bindPaymentBtnEvent();

}

function bindPaymentBtnEvent()
{

    $(".confirm_payment_btn").click(function(){
        paymentEvent();
    });
}

/**
 * 校验收货人表单
 */
function validConsigneeAddressForm()
{
    var caName = $("#ca_name").html();
    if(caName==null||caName=="")
    {
        return false;
    }
    var caPhone = $("#ca_phone").html();
    if(caPhone==null||caPhone=="")
    {
        return false;
    }
    var caProvinceName = $("#ca_provice_name").html();
    if(caProvinceName==null||caProvinceName=="")
    {
        return false;
    }
    var caAreaName = $("#ca_area_name").html();
    if(caAreaName==null||caAreaName=="")
    {
        return false;
    }
    var caAddress = $("#ca_address").html();
    if(caAddress==null||caAddress=="")
    {
        return false;
    }

    return true;
}


function startLoadding()
{
    loading.showLoading({
        type:1,
        tip:"查询中..."
    });
    $(".confirm_btns").hide();
}

function hideLoadding()
{
    requestCompleted--;
    if(requestCompleted<=0)
    {
        requestCompleted=0;
        loading.hideLoading();
        $(".confirm_btns").show();
    }
}

function drawFreightTemplateOption(datas)
{
    for(var i=0;i<datas.length;i++) {
        var obj = datas[i];
        if (obj.freightTemplateVO != null && obj.freightTemplateVO.freightStatus == 2) {
            $(".bif_" + obj.id).html("包邮");
            $(".bifm_" + obj.id).html("0");
        } else {
            if (obj.isMergeRow == true) {
                $(".bif_" + obj.id).attr("rowspan", obj.mergeRowCount+1);
                var transportModelArray = obj.freightTemplateVO.transportModel.split(",");
                var freightTemplateOptionHtml="<div style='width:100%;height:100%'>";
                for(var j=0;j<transportModelArray.length;j++)
                {
                    var transportModelItem = transportModelArray[j];
                    if(transportModelItem=="1")
                    {
                        freightTemplateOptionHtml+="<div style='overflow: hidden;'><input id='bcy_fto_"+obj.id+"_1' class='bcy_fto' name='bcy_fto_group_"+obj.id+"' attr-eid='"+obj.id+"' type='radio' value='1'  /><label for='bcy_fto_"+obj.id+"_1'>快递</label></div>"
                    }else if(transportModelItem=="2")
                    {
                        freightTemplateOptionHtml+="<div style='overflow: hidden;'><input id='bcy_fto_"+obj.id+"_2' class='bcy_fto' name='bcy_fto_group_"+obj.id+"' attr-eid='"+obj.id+"' type='radio' value='2' /><label for='bcy_fto_"+obj.id+"_2'>EMS</label></div>"
                    }else if(transportModelItem=="3")
                    {
                        freightTemplateOptionHtml+="<div style='overflow: hidden;'><input id='bcy_fto_"+obj.id+"_3' class='bcy_fto' name='bcy_fto_group_"+obj.id+"' attr-eid='"+obj.id+"' type='radio' value='3' /><label for='bcy_fto_"+obj.id+"_3'>平邮</label></div>"
                    }
                }
                freightTemplateOptionHtml+="</div>";
                $(".bif_" + obj.id).html(freightTemplateOptionHtml);
                //运费
                $(".bifm_" + obj.id).attr("rowspan", obj.mergeRowCount+1);
                $(".bifm_" + obj.id).html("0");
            }else{
                $(".bif_" + obj.id).remove();
                $(".bifm_" + obj.id).remove();
            }
        }
    }


    $(".bcy_fto").unbind();
    $(".bcy_fto").click(function() {
        var attrId = $(this).attr("attr-eid");
        calculateFreight(attrId,this);
    });

    for(var i=0;i<datas.length;i++) {
        var obj =  datas[i];
        freightOptionCalculate(obj.id);
    }
}

/**
 * 运费项计算
 * @param groupId
 */
function freightOptionCalculate(groupId)
{
    if($("input[name='bcy_fto_group_"+groupId+"']")!=null) {
        if(g_cache_buy_item_select_freight.get(groupId)==null) {
            $("input[name='bcy_fto_group_" + groupId + "']:first").click();
        }else{
            $("#"+g_cache_buy_item_select_freight.get(groupId)).click();
        }
    }
}

/**
 * 查询运费规则
 */
function findFreightTemplateRule(transportModel,freightTemplate,cityCode)
{
    if(transportModel=="1") //快递
    {
        if(freightTemplate.expressAreaRules!=null&&freightTemplate.expressAreaRules.length>0)
        {
            //查询地区规则
            for(var i=0;i<freightTemplate.expressAreaRules.length;i++){
                var rowAreaRule = freightTemplate.expressAreaRules[i];
                if(rowAreaRule.selectItems!=null&&rowAreaRule.selectItems.length>0)
                {
                    for(var j=0;j<rowAreaRule.selectItems.length;j++)
                    {
                        var areaRule = rowAreaRule.selectItems[j];
                        if(areaRule.cityCode==cityCode)
                        {
                            areaRule.type=2;
                            return areaRule;
                        }
                    }
                }
            }
        }

        //查询默认规则
        freightTemplate.expressDefaultRule.type=1;
        return freightTemplate.expressDefaultRule;
    }else if(transportModel=="2") //EMS
    {
        if(freightTemplate.emsAreaRules!=null&&freightTemplate.emsAreaRules.length>0) {
            //查询地区规则
            for (var i = 0; i < freightTemplate.emsAreaRules.length; i++) {
                var rowAreaRule = freightTemplate.emsAreaRules[i];
                if (rowAreaRule.selectItems != null && rowAreaRule.selectItems.length > 0) {
                    for (var j = 0; j < rowAreaRule.selectItems.length; j++) {
                        var areaRule = rowAreaRule.selectItems[j];
                        if (areaRule.cityCode == cityCode) {
                            areaRule.type=2;
                            return areaRule;
                        }
                    }
                }
            }
        }

        //查询默认规则
        freightTemplate.emsDefaultRule.type=1;
        return freightTemplate.emsDefaultRule;

    }else if(transportModel=="3") //平邮
    {
        if(freightTemplate.ordinaryMailAreaRules!=null&&freightTemplate.ordinaryMailAreaRules.length>0) {
            //查询地区规则
            for (var i = 0; i < freightTemplate.ordinaryMailAreaRules.length; i++) {
                var rowAreaRule = freightTemplate.ordinaryMailAreaRules[i];
                if (rowAreaRule.selectItems != null && rowAreaRule.selectItems.length > 0) {
                    for (var j = 0; j < rowAreaRule.selectItems.length; j++) {
                        var areaRule = rowAreaRule.selectItems[j];
                        if (areaRule.cityCode == cityCode) {
                            areaRule.type=2;
                            return areaRule;
                        }
                    }
                }
            }
        }

        //查询默认规则
        freightTemplate.ordinaryMailDefaultRule.type=1;
        return freightTemplate.ordinaryMailDefaultRule;
    }
    return null;
}


/**
 * 根据指定运送方式 计算运费
 * @param rid
 */
function calculateFreight(rid,clickObj)
{
    if(g_cache_buy_items!=null) {
        for (var i = 0; i < g_cache_buy_items.length; i++) {
            var obj = g_cache_buy_items[i];
            //忽略其他购物项分组
            if(obj.id!=rid)
            {
                continue;
            }
            //忽略包邮
            if(obj.freightTemplateVO != null && obj.freightTemplateVO.freightStatus == 2)
            {
                continue;
            }
            //忽略下架、售罄
            if(!obj.isAllowedBuy)
            {
                continue;
            }
            if(obj.isMergeRow)
            {
                var transportModel = $("input[name='bcy_fto_group_"+obj.id+"']:checked").val();
                var itemGroups = new Array();
                itemGroups.push(obj);
                //如果没有选择运送方式 直接跳过
                if(transportModel==null)
                {
                    continue;
                }
                obj.transportModel = transportModel;//保存选择的运送方式
                var freightTemplateRule=null; //运费计算规则
                if(g_consigneeAddress==null)
                {
                    calculateFreightTotal();
                    return;
                }
                if(g_consigneeAddress.cityCode==null||g_consigneeAddress.cityCode=="") //直辖市、自治区
                {
                    freightTemplateRule = findFreightTemplateRule(transportModel,obj.freightTemplateVO,g_consigneeAddress.provinceCode);
                }else{
                    freightTemplateRule = findFreightTemplateRule(transportModel,obj.freightTemplateVO,g_consigneeAddress.cityCode);
                }
                //找到同运费模板下相邻的商品
                for(var j=i+1;j<g_cache_buy_items.length;j++)
                {
                    var nextObj = g_cache_buy_items[j];
                    if(nextObj.freightTemplateId==obj.freightTemplateId)
                    {
                        nextObj.transportModel = transportModel;//保存选择的运送方式
                        itemGroups.push(nextObj);
                    }else{
                        i=j-1; //将运费模板ID不相等的设为下一个分组起始位置
                        break;
                    }
                }
                //计算同运费模板下相邻的商品
                if(freightTemplateRule != null)
                {
                    var buyCount=0; //购买总件数
                    var roughWeightTotal=new BigNumber("0");
                    itemGroups.forEach(function (item) {
                        buyCount+=parseInt(item.buyCount);
                        var itemBuyCountBigNumber=new BigNumber(item.buyCount);
                        var itemRoughWeightTotalBigNumber=new BigNumber(item.roughWeight!=null?item.roughWeight:0);
                        roughWeightTotal = roughWeightTotal.plus(itemBuyCountBigNumber.times(itemRoughWeightTotalBigNumber)); //毛重总量=数量*毛重
                    });

                    buyCount = new BigNumber(buyCount);
                    var firstWeight; //首重、首件
                    var firstMoney; //首件价格
                    var appendWight; //续重、续件
                    var appendMoney; //续件价格
                    //默认运费规则
                    if(freightTemplateRule.type==1)
                    {
                        firstWeight = new BigNumber(freightTemplateRule.defaultWeight);
                        firstMoney = new BigNumber(freightTemplateRule.defaultWeightMoney);
                        appendWight = new BigNumber(freightTemplateRule.defaultAppendWeight);
                        appendMoney = new BigNumber(freightTemplateRule.defaultAppendWeightMoney);
                    }else{ //地区运费规则
                        firstWeight = new BigNumber(freightTemplateRule.firstWeight);
                        firstMoney = new BigNumber(freightTemplateRule.firstWeightMoney);
                        appendWight = new BigNumber(freightTemplateRule.appendWeight);
                        appendMoney = new BigNumber(freightTemplateRule.appendWeightMoney);
                    }

                    var freightMoney=0;
                    //按件
                    if(obj.freightTemplateVO.valuationMethod==1)
                    {
                        //小于等于
                        if(buyCount.lte(firstWeight))
                        {
                            //默认运费
                            firstMoney = firstMoney.toFixed(2);
                            $(".bifm_"+obj.id).html(firstMoney);
                            $(".bifm_"+obj.id).append("<input type='hidden'  id='bifm_money_hids_"+obj.id+"' class='bifm_money_hids' value='"+firstMoney+"'/>");
                        }else{
                            //(购买数量-首件)/续件*续件金额
                            freightMoney= new BigNumber(((buyCount.minus(firstWeight)).div(appendWight)).toFixed(2)).times(appendMoney);
                            freightMoney= freightMoney<0?new BigNumber(0):freightMoney;
                            freightMoney=freightMoney.plus(firstMoney); //加上首件金额
                            freightMoney = freightMoney.toFixed(2);
                            $(".bifm_"+obj.id).html(freightMoney);
                            $(".bifm_"+obj.id).append("<input type='hidden'  id='bifm_money_hids_"+obj.id+"' class='bifm_money_hids' value='"+freightMoney+"'/>");
                        }
                    }else if(obj.freightTemplateVO.valuationMethod==2) //按重量
                    {
                        //小于等于
                        if(roughWeightTotal.lte(firstWeight))
                        {
                            firstMoney = firstMoney.toFixed(2);
                            //默认运费
                            $(".bifm_"+obj.id).html(firstMoney);
                            $(".bifm_"+obj.id).append("<input type='hidden'  id='bifm_money_hids_"+obj.id+"' class='bifm_money_hids' value='"+firstMoney+"'/>");
                        }else{
                            //((购买数量-首件)/续件)*续件金额
                            freightMoney = new BigNumber((roughWeightTotal.minus(firstWeight)).div(appendWight).toFixed(2)).times(appendMoney);
                            freightMoney= freightMoney<0?new BigNumber(0):freightMoney;
                            freightMoney=freightMoney.plus(firstMoney); //加上首件金额
                            freightMoney = freightMoney.toFixed(2);
                            $(".bifm_"+obj.id).html(freightMoney);
                            $(".bifm_"+obj.id).append("<input type='hidden'  id='bifm_money_hids_"+obj.id+"' class='bifm_money_hids' value='"+freightMoney+"'/>");
                        }
                    }
                }
                //清空分组项
                itemGroups.splice(0,itemGroups.length);
            }
        }
    }

    if(clickObj!=null) {
        g_cache_buy_item_select_freight.set(rid, $(clickObj).attr("id"));
    }
    calculateFreightTotal();

}




/**
 * 计算总运费
 */
function calculateFreightTotal()
{
    var freightMoneyTotal=new BigNumber("0");
    var bifmms = $(".bifm_money_hids");
    if(bifmms!=null&&bifmms.length>0)
    {
        for(var i=0;i<bifmms.length;i++)
        {
            var bifm = parseFloat($(bifmms[i]).val()).toFixed(2);
            if(bifm!=null) {
                freightMoneyTotal = freightMoneyTotal.plus(bifm);
            }
        }
    }
    g_freight_money = new BigNumber(freightMoneyTotal.toFixed(2));
    $("#freightPriceTotal").html("￥"+parseFloat(g_freight_money.toFixed(2)));
    $(".order_freight_total").html("￥"+parseFloat(g_freight_money.toFixed(2)));
    $(".order_price_total").html("￥"+parseFloat((g_product_price_money.plus(g_freight_money)).toFixed(2)));

}


/**
 * 计算所有运费
 */
function calculateAllFreight()
{
    if(g_cache_buy_items!=null&&g_cache_buy_items.length>0)
    {
        g_cache_buy_items.forEach(function (item) {
            if(item.isMergeRow)
            {
                calculateFreight(item.id,null);
            }
        });
    }
}

/**
 * 查找出所有要合并行的数据
 */
function findMergeRow(datas)
{
    if(datas!=null&&datas.length>0)
    {

        for(var j=0;j<datas.length;j++) {
            datas[j].isMergeRow=false;
            datas[j].mergeRowCount=0;
            datas[j].isFindFirstRow=false; //找到了该分组首行
        }
        for(var i=0;i<datas.length;i++)
        {
            var row = datas[i];
            //忽略包邮情况
            if(row.freightTemplateVO!=null&&row.freightTemplateVO.freightStatus==2)
            {
                continue;
            }
            if(row.isFindFirstRow)
            {
                continue;
            }
            for(var j=i+1;j<datas.length;j++)
            {
                var nextRow = datas[j];
                if(row.freightTemplateId==nextRow.freightTemplateId)
                {
                    row.isMergeRow=true;
                    row.mergeRowCount++; //要合并的行
                    row.isFindFirstRow = true;
                    nextRow.isFindFirstRow = true;
                    continue;
                }else{
                    i=j-1; //还原到上一个项,让上面的i++进行移动
                    if(row.freightTemplateVO!=null&&row.freightTemplateVO.freightStatus!=2)
                    {
                        row.isMergeRow=true;
                    }
                    break;
                }
            }
            //最后一个项并且为不包邮的运费模板
            if(i+1>=datas.length)
            {
                if(row.freightTemplateVO!=null&&row.freightTemplateVO.freightStatus!=2)
                {
                    row.isMergeRow=true;
                }
            }
        }
    }
    return datas;
}

function loadBuyCarPanel(){

    loading.showLoading({
        type:1,
        tip:"加载中..."
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
                    "                    <td class=\"car_th\" style = \"width:10%\">配送方式</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:10%\">运费(元)</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:20%\">商品名称</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:15%\">属性</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:5%\">购买数量</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:20%\">小计</td>\n" +
                    "                </tr>";
                var productPriceTotal = new BigNumber(0);

                result.data = findMergeRow(result.data);
                g_cache_buy_items=result.data;
                for(var i=0;i<result.data.length;i++)
                {
                    var buyCarItem = result.data[i];

                    var noAllowedBuyDesc="";
                    if(!buyCarItem.isAllowedBuy)
                    {
                        noAllowedBuyDesc="&nbsp;"+buyCarItem.noAllowedBuyDesc;
                    }

                    productHtmls+="  <tr id=\"tr_"+buyCarItem.id+"\">\n" +
                        "<td class='bif_"+buyCarItem.id+"' align=\"center\"></td>"+
                        "<td class='bifm_"+buyCarItem.id+"' align=\"center\"></td>"+
                        "                <td>\n" +
                        "                    <div class=\"c_s_img\"><a href=\""+basePath+"/page/product/detail/"+buyCarItem.shopProductSkuId+"\" target='_blank' ><img src=\""+buyCarItem.httpProductImgPath+"\" title=\""+buyCarItem.productSkuName+"\" width=\"73\" height=\"73\" /></a></div>\n" ;
                    if(!buyCarItem.isAllowedBuy)
                    {
                        productHtmls+="                    <del>"+buyCarItem.productSkuName+noAllowedBuyDesc+"</del>\n" ;
                    }else{
                        productHtmls+="                    <a href=\""+basePath+"/page/product/detail/"+buyCarItem.shopProductSkuId+"\" target='_blank'>"+buyCarItem.productSkuName+"</a>\n" ;
                    }

                    productHtmls+="                </td>\n" +
                        "                <td align=\"center\">"+buyCarItem.attributePreview+"</td>\n" +
                        "                <td align=\"center\">\n" +buyCarItem.buyCount + "                </td>\n" +
                        "                <td align=\"center\" style=\"color:#ff4e00;\">￥<a id=\"buyItemTotal_"+buyCarItem.id+"\" style=\"color: #ff4e00;\">"+(parseFloat((new BigNumber(buyCarItem.buyCount).times(new BigNumber(buyCarItem.productPrice))).toFixed(2)))+"</a></td>\n" +
                        "            </tr>\n" +
                        "           ";
                    if(buyCarItem.isAllowedBuy) {
                        //单价*数量
                        productPriceTotal=productPriceTotal.plus(new BigNumber(buyCarItem.productPrice).times(new BigNumber(buyCarItem.buyCount)));
                    }
                }
                productHtmls+=" <tr>\n" +
                    "                    <td colspan=\"7\" align=\"right\" style=\"font-family:'Microsoft YaHei';\">\n" +
                    "                        运费：<a id=\"freightPriceTotal\" style='color:#ff4e00'>￥</a>&nbsp;&nbsp;&nbsp;\n" +
                    "                        商品总价：<a id=\"productPriceTotal\" style='color:#ff4e00'>￥"+parseFloat(productPriceTotal.toFixed(2))+"</a>\n" +
                    "                    </td>\n" +
                    "                </tr>";


                $(".mcar_tab").html(productHtmls);

                drawFreightTemplateOption(result.data);


                g_product_price_money = new BigNumber(productPriceTotal);
                $(".product_price_total").html("￥"+parseFloat(g_product_price_money.toFixed(2)));
                $(".order_price_total").html("￥"+parseFloat((g_product_price_money.plus(g_freight_money)).toFixed(2)));

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
        tip:"加载中..."
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
                    "                    <td class=\"car_th\" style = \"width:10%\">配送方式</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:10%\">运费(元)</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:20%\">商品名称</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:15%\">属性</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:5%\">购买数量</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:10%\">小计</td>\n" +
                    "                    <td class=\"car_th\" style = \"width:10%\">操作</td>\n" +
                    "                </tr>";
                var productPriceTotal = new BigNumber(0);

                result.data = findMergeRow(result.data);
                g_cache_buy_items=result.data;

                for(var i=0;i<result.data.length;i++)
                {
                    var buyCarItem = result.data[i];

                    var noAllowedBuyDesc="";
                    if(!buyCarItem.isAllowedBuy)
                    {
                        noAllowedBuyDesc="&nbsp;"+buyCarItem.noAllowedBuyDesc;
                    }

                    productHtmls+="  <tr id=\"tr_"+buyCarItem.id+"\">\n" +
                        "<td class='bif_"+buyCarItem.id+"' align=\"center\"></td>"+
                        "<td class='bifm_"+buyCarItem.id+"' align=\"center\"></td>"+
                        "                <td>\n" +
                         "                    <div class=\"c_s_img\"><a href=\""+basePath+"/page/product/detail/"+buyCarItem.shopProductSkuId+"\" target='_blank' ><img src=\""+buyCarItem.httpProductImgPath+"\" title=\""+buyCarItem.productSkuName+"\" width=\"73\" height=\"73\" /></a></div>\n" ;
                    if(!buyCarItem.isAllowedBuy)
                    {
                        productHtmls+="                    <del>"+buyCarItem.productSkuName+noAllowedBuyDesc+"</a>\n" ;
                    }else{
                        productHtmls+="                    <a href=\""+basePath+"/page/product/detail/"+buyCarItem.shopProductSkuId+"\" target='_blank'>"+buyCarItem.productSkuName+"</a>\n" ;
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
                        "                <td align=\"center\" style=\"color:#ff4e00;\">￥<a id=\"buyItemTotal_"+buyCarItem.id+"\" style=\"color: #ff4e00;\">"+(parseFloat((new BigNumber(buyCarItem.buyCount).times(new BigNumber(buyCarItem.productPrice))).toFixed(2)))+"</a></td>\n" +
                        "                <td align=\"center\"><a onclick=\"showRemoveBuyCar('"+buyCarItem.id+"','"+buyCarItem.productSkuName+"')\">删除</a></td>\n" +
                        "            </tr>\n" +
                        "           ";

                    if(buyCarItem.isAllowedBuy) {
                        productPriceTotal=productPriceTotal.plus(new BigNumber(buyCarItem.productPrice).times(new BigNumber(buyCarItem.buyCount)));
                    }
                }

                productHtmls+=" <tr>\n" +
                    "                    <td colspan=\"7\" align=\"right\" style=\"font-family:'Microsoft YaHei';\">\n" +
                    "                        运费：<a id=\"freightPriceTotal\" style='color:#ff4e00'>￥</a>&nbsp;&nbsp;&nbsp;\n" +
                    "                        商品总价：<a id=\"productPriceTotal\" style='color:#ff4e00'>￥"+parseFloat(productPriceTotal.toFixed(2))+"</a>\n" +
                    "                    </td>\n" +
                    "                </tr>";


                $(".mcar_tab").html(productHtmls);
                for(var i=0;i<result.data.length;i++) {
                    bindBuyCarIntInputKeyUp("num_"+result.data[i].id);
                }

                drawFreightTemplateOption(result.data);
                g_product_price_money = new BigNumber(productPriceTotal);
                $(".product_price_total").html("￥"+parseFloat(g_product_price_money.toFixed(2)));
                $(".order_price_total").html("￥"+parseFloat((g_product_price_money.plus(g_freight_money)).toFixed(2)));

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
        if(/[^\d]/.test(bnum)){//替换非数字字符
            var temp_amount=bnum.replace(/[^\d]/g,'');
            $(this).val(temp_amount);
            bnum = temp_amount;
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

    //卸载支付按钮事件
    $(".confirm_payment_btn").unbind();

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
                if(g_cache_buy_items!=null&&g_cache_buy_items.length>0)
                {
                    for(var i=0;i<g_cache_buy_items.length;i++)
                    {
                        if(g_cache_buy_items[i].id==cid)
                        {
                            g_cache_buy_items[i].buyCount = buyCarItem.buyCount;
                            freightOptionCalculate(g_cache_buy_items[i].id);
                            calculateFreightTotal();
                            break;
                        }
                    }
                }
                //绑定支付按钮事件
                bindPaymentBtnEvent();
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

/**
 * 计算商品总价
 */
function calculatePriceTotal()
{
    var pns = $(".mcar_pn"); //数量
    var pps = $(".mcar_pp"); //单价
    var productPriceTotal = new BigNumber(0);
    for(var i=0;i<pns.length;i++)
    {
        //数量*单价
        productPriceTotal=productPriceTotal.plus(new BigNumber(parseInt($(pns[i]).val())).times(new BigNumber(parseFloat($(pps[i]).val()))));
    }
    $("#productPriceTotal").html(parseFloat(productPriceTotal.toFixed(2)));

    g_product_price_money = new BigNumber(productPriceTotal);
    $(".product_price_total").html("￥"+parseFloat(g_product_price_money.toFixed(2)));
    $(".order_price_total").html("￥"+parseFloat((g_product_price_money.plus(g_freight_money)).toFixed(2)));
}




function showRemoveBuyCar(cid,cname)
{
    $("#removeBuyCarId").val(cid);
    $("#removeBuyCarProductName").html(cname);
    ShowDiv('removeBuyCar','fade');
}


/**
 * 移除购物车项删除事件
 */
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


/**
 * 加载默认收货人信息
 */
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

                drawReadOnlyConsigneeAddress(result.data);
            }else{
                drawConsigneeAddressEditControl(null);


                $(".mac_modify").html("保存");
                $(".mac_modify").attr("attr-opt","2");

                g_updateConsigneeAddressStatus=1;
            }

            //载入购物车
            loadBuyCarPanel();
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
                    $(".mac_close").hide();
                    //隐藏修改表单
                    $("#ca_edit_form").hide();
                    $("#ca_form").show();

                    drawReadOnlyConsigneeAddress(result.data);

                    g_updateConsigneeAddressStatus=0;

                    $(".ca_select").show();
                }else{
                    $.message({
                        message: result.msg,
                        type: 'error'
                    });
                }
            },
            error: function (result) {
            },
            complete: function (data, status) {
            }
        });
    }
}


/**
 * 绘制收货人只读表单
 * @param obj
 */
function drawReadOnlyConsigneeAddress(obj)
{

    $("#ca_name").html(obj.name);
    $("#ca_phone").html(obj.phone);
    $("#ca_provice_name").html(obj.provinceName);
    $("#ca_city_name").html(obj.cityName);
    $("#ca_area_name").html(obj.areaName);
    $("#ca_address").html(obj.address);


    calculateAllFreight();
}

/**
 * 绘制收货人可编辑表单
 * @param obj
 */
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


/**
 * 打开选择收货人对话框
 */
function openSelectConsigneeAdddressDialog()
{

    var consigneeAddressTableHtml="     <table border=\"0\" class=\"default_tab\" style=\"width:930px;margin-top: 20px;\" cellspacing=\"0\" cellpadding=\"0\">\n" +
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
        "                                    <td align=\"center\" style=\"width:20%\">操作</td>"+
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

    g_sleectConsigneeAddressDialogHandler = layer.open({
        type: 1,
        title:"选择收货人",
        area: ['55%', '50%'], //宽高
        content: consigneeAddressTableHtml
    });
}


/**
 * 查询收货人列表
 * @param cpage
 */
function queryConsignessAddressList(cpage)
{


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
                    g_selectConsigneeAddressPageData = result.data.list;
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
                                "                           "+(i+1)+"" +
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
                                "                        <td align=\"center\">" +
                                "           <a class=\"select_cap\" attr-id=\""+obj.id+"\" style=\"cursor:pointer;color:blue;\">选择</a>" +
                                "</td>\n" +
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

                        $(".select_cap").unbind();
                        $(".select_cap").click(function(){
                            var attrId =$(this).attr("attr-id");
                            if(g_selectConsigneeAddressPageData!=null&&g_selectConsigneeAddressPageData.length>0)
                            {
                                for(var i=0;i<g_selectConsigneeAddressPageData.length;i++)
                                {
                                    var selectObj = g_selectConsigneeAddressPageData[i];
                                    if(selectObj.id==attrId)
                                    {
                                        g_consigneeAddress = selectObj;
                                        drawReadOnlyConsigneeAddress(selectObj);
                                        layer.close(g_sleectConsigneeAddressDialogHandler);
                                    }
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
                if(total<=0)
                {
                    $("#consignee_address_pagination").html("<a style='font-size:20px;'>您暂时没有收货信息~</a>");
                }
            }

        });
    }

}


function selectProvinceEvent()
{
    if(g_consigneeAddress==null)
    {
        g_consigneeAddress = {};
    }
    g_consigneeAddress.provinceCode=$("#province_code").val();
    calculateAllFreight();
}


function selectCityEvent()
{
    g_consigneeAddress.cityCode=$("#city_code").val();
    calculateAllFreight();
}

function paymentEvent()
{

    var macAttr = $(".mac_modify").attr("attr-opt");
    if(macAttr=="2")
    {
        $.message({
            message:"请先保存收货信息",
            type: 'error'
        });
        return;
    }

    var orderRemark = $("#order_remark").val();
    if(orderRemark!=null&&orderRemark!=""&&orderRemark.length>255)
    {
        $.message({
            message:"订单备注长度最多255个汉字",
            type: 'error'
        });
        return;
    }

    loading.showLoading({
        type:1,
        tip:"提交中..."
    });


    g_cache_buy_items.forEach(function (item) {
        item.selectTransportModel = item.transportModel;
    });

    var params = {
        buyCarItems:g_cache_buy_items,
        consigneeAddress:g_consigneeAddress,
        payType:-1,
        remark:orderRemark
    };



    $.ajax({
        type: "POST",
        url: basePath + "/api/order/create",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(params),
        dataType: "json",
        success: function (result) {
            if(result.code!=1)
            {
                $.message({
                    message:result.msg,
                    type: 'error'
                });
                return;
            }

            window.location.href =basePath+"/page/user/buyCar/pay?mainOrderNo="+ result.data.mainOrder.orderNo;
        },
        error: function (result) {
        },
        complete:function(data,status){
            hideLoadding();
        }
    });
}