

$(function () {
    $(".buy_item_pay").click(function(){
        var buyItemPays = $(".buy_item_pay");
        for(var i=0;i<buyItemPays.length;i++)
        {
            $(buyItemPays[i]).removeClass("checked")
        }
        $(this).addClass("checked");
    });

});