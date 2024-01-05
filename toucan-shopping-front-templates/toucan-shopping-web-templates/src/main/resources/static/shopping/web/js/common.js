var appCode = "10001001";
var webName="犀鸟商城";

var productSkuBasePath="/page/product/detail/";
var orderPayPath="/page/user/buyCar/pay/";
var orderDetailPath="/page/order/detail";
var searchGetPath="/api/product/g/search";
var shopSearchGetPath="/api/shop/product/g/search";

/**
 * 模拟post表单提交
 * @param url
 * @param postData
 */
function formpost(url, postData) {
    var tempform = document.createElement("form");
    tempform.action = url;
    tempform.method = "post";
    tempform.style.display = "none";
    for (var x in postData) {
        var opt = document.createElement("textarea");
        opt.name = x;
        opt.value = postData[x];
        tempform.appendChild(opt);
    }
    document.body.appendChild(tempform);
    tempform.submit();
}