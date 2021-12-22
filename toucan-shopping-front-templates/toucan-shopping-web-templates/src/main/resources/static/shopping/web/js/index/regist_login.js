$(function () {
    var tss_uid = getCookie("tss_uid");
    var tss_lt = getCookie("tss_lt");
    if (tss_uid != null && tss_lt != null) {
        //查询信息
        $.post(basePath + "/api/user/login/info", {}, function (result) {
            if (result.code > 0 && result.data != null) {
                if (result.data.nickName != null) {
                    $("#unickname").text(result.data.nickName);
                    $("#unicknamec").text(result.data.nickName);
                }
                if (result.data.isVip != null) {
                    if (result.data.isVip == "0") {
                        $("#vip_0").show();
                    } else if (result.data.isVip == "1") {
                        $("#vip_1").show();
                        $("#vip_text_1").show();
                    }
                }
                if (result.data.httpHeadSculpture != null) {
                    $("#headImg").attr("src", result.data.httpHeadSculpture);
                }
                $("#userInfo").show();
                $("#messageCenterMenu").show();
            } else {

                $("#registOrLogin").show();
            }
        });
    } else {
        $("#registOrLogin").show();
    }

    $(".badge-list-next").click(function () {
        var widthValue = parseInt($(".badge-panel-main").css("width"));
        var leftValue = parseInt($(".badge-panel-main").css("left"));
        //判断是否是最后一页
        if (Math.abs(leftValue) < (widthValue - 148)) {
            $(".badge-panel-main").css("left", (leftValue - 148) + "px");
        }
    });

    $(".badge-list-prev").click(function () {
        var widthValue = parseInt($(".badge-panel-main").css("width"));
        var leftValue = parseInt($(".badge-panel-main").css("left"));
        //判断是否是第一页
        if (Math.abs(leftValue) > 0) {
            $(".badge-panel-main").css("left", (leftValue + 148) + "px");
        }
    });
});