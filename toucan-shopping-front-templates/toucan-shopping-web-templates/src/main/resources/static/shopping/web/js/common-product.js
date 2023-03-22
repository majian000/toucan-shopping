function getProductId()
{
    var urlParam = window.location.href;
    var id_op=urlParam.substring(urlParam.lastIndexOf("/")+1, urlParam.length);
    if(id_op.indexOf("#")!=-1)
    {
        return id_op.substring(0,id_op.indexOf("#"));
    }
    if(id_op.indexOf("_")!=-1)
    {
        return id_op.substring(0,id_op.indexOf("_"));
    }
    return id_op;
}

function getApprovePreviewHrefIngoreId()
{
    var urlParam = window.location.href;
    var currentLocation=urlParam.substring(0,urlParam.lastIndexOf("/"));
    if(currentLocation.indexOf("/paid")!=-1)
    {
        currentLocation = currentLocation.substring(0,currentLocation.indexOf("/paid"));
    }
    if(currentLocation.indexOf("/attrPath")!=-1)
    {
        currentLocation = currentLocation.substring(0,currentLocation.indexOf("/attrPath"));
    }
    return currentLocation;
}

function getShopProductPreviewHrefIngoreId()
{
    var urlParam = window.location.href;
    var currentLocation=urlParam.substring(0,urlParam.lastIndexOf("/"));
    if(currentLocation.indexOf("/pid")!=-1)
    {
        currentLocation = currentLocation.substring(0,currentLocation.indexOf("/pid"));
    }
    if(currentLocation.indexOf("/attrPath")!=-1)
    {
        currentLocation = currentLocation.substring(0,currentLocation.indexOf("/attrPath"));
    }
    return currentLocation;
}

function getProductPageUrl()
{
    return window.location.href;
}

/**
 * 判断是skuId查询还是商品id查询
 * @returns {number}
 */
function getPreviewType()
{
    var urlParam = window.location.href;
    if(urlParam.indexOf("/paid/")!=-1)
    {
        return 1;
    }
    return 2;
}

/**
 * 判断是skuId查询还是商品id查询
 * @returns {number}
 */
function getShopProductPreviewType()
{
    var urlParam = window.location.href;
    if(urlParam.indexOf("/pid/")!=-1) //查询第一个SKU
    {
        return 1;
    }
    if(urlParam.indexOf("/attrPath/")!=-1) //指定属性查询
    {
        return 3;
    }
    return 2;
}


/**
 * 只能输入整数
 * @param id
 */
function bindBuyCarIntInputKeyUp(id)
{
    $($("#"+id)).keyup(function(){
        var c=$(this);
        if(/[^\d]/.test(c.val())){//替换非数字字符
            var temp_amount=c.val().replace(/[^\d]/g,'');
            $(this).val(temp_amount);
        }
    });
}