function page_ctrl(b,callback) {
    var c = (b.obj_box !== undefined) ? b.obj_box: function() {
        return
    };
    var f = (b.total_item !== undefined) ? parseInt(b.total_item) : 0;
    var e = (b.per_num !== undefined) ? parseInt(b.per_num) : 10;
    var a = (b.current_page !== undefined) ? parseInt(b.current_page) : 1;
    var g = Math.ceil(f / e);
    if (g < 2) {
        return
    }
    $(c).append('<div class="page_content"></div>');
    $(c).append('<div class="page_ctrl"></div>');
    function d() {
        var l = (a == g) ? 1 : a + 1;
        var h = '<button class="prev_page">上一页</button>';
        for (var k = 0; k < g - 1; k++) {
            if (g > 8 && a > 6 && k < a - 3) {
                if (k < 2) {
                    h += '<button class="page_num">' + (k + 1) + "</button>"
                } else {
                    if (k == 2) {
                        h += '<span class="page_dot">•••</span>'
                    }
                }
            } else {
                if (g > 8 && a < g - 3 && k > a + 1) {
                    if (a > 6 && k == a + 2) {
                        h += '<span class="page_dot">•••</span>'
                    } else {
                        if (a < 7) {
                            if (k < 8) {
                                h += '<button class="page_num">' + (k + 1) + "</button>"
                            } else {
                                if (k == 8) {
                                    h += '<span class="page_dot">•••</span>'
                                }
                            }
                        }
                    }
                } else {
                    if (k == a - 1) {
                        h += '<button class="page_num current_page">' + (k + 1) + "</button>"
                    } else {
                        h += '<button class="page_num">' + (k + 1) + "</button>"
                    }
                }
            }
        }
        if (a == g) {
            h += '<button class="page_num current_page">' + (k + 1) + "</button>"
        } else {
            h += '<button class="page_num">' + (k + 1) + "</button>"
        }
        h += '<button class="next_page">下一页</button><span class="page_total">共 ' + g + ' 页, 到第</span><input class="input_page_num" type="text" value="' + l + '"><span class="page_text">页</span><button class="to_page_num">确定</button>';
        $(c).children(".page_ctrl").append(h);
        if (a == 1) {
            $(c + " .page_ctrl .prev_page").attr("disabled", "disabled").addClass("btn_dis")
        } else {
            $(c + " .page_ctrl .prev_page").removeAttr("disabled").removeClass("btn_dis")
        }
        if (a == g) {
            $(c + " .page_ctrl .next_page").attr("disabled", "disabled").addClass("btn_dis")
        } else {
            $(c + " .page_ctrl .next_page").removeAttr("disabled").removeClass("btn_dis")
        }
    }
    d();
    $(c + " .page_ctrl").on("click", "button",
        function() {
            var h = $(this);
            if (h.hasClass("prev_page")) {
                if (a != 1) {
                    a--;
                    h.parent(".page_ctrl").html("");
                    d()
                }
            } else {
                if (h.hasClass("next_page")) {
                    if (a != g) {
                        a++;
                        h.parent(".page_ctrl").html("");
                        d()
                    }
                } else {
                    if (h.hasClass("page_num") && !h.hasClass("current_page")) {
                        a = parseInt(h.html());
                        h.parent(".page_ctrl").html("");
                        d()
                    } else {
                        if (h.hasClass("to_page_num")) {
                            a = parseInt(h.siblings(".input_page_num").val());
                            h.parent(".page_ctrl").html("");
                            d()
                        }
                    }
                }
            }
            b.current_page = a;
            if(callback!=null) {
                callback();
            }
        })
}