

$(function () {

    $("#orderState").mouseover(function() {
        $(this).addClass("deal-state-hover");
    });

    $("#orderState").mouseout(function() {
        $(this).removeClass("deal-state-hover");
    });

    $(".consignee .txt").mouseover(function() {
        var attrIndex=$(this).attr("attr-index");
        $(".consignee-detail-"+attrIndex).css("display","block");
    });

    $(".consignee .txt").mouseout(function() {
        var attrIndex=$(this).attr("attr-index");
        $(".consignee-detail-"+attrIndex).css("display","none");
    });
});