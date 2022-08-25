$(function () {
    $("#ms_city").click(function (e) {
        SelCity(this,e);
    });
    $(".msc_l").click(function (e) {
        SelCity(document.getElementById("ms_city"),e);
    });

});