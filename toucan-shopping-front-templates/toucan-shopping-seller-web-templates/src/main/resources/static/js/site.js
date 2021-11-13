$(function(){
    
    //商家中心左侧菜单
    $(".active").parent().parent().addClass('nav-show');
    $('.nav-item>a').on('click',function(){
        $('.nav-item').children('ul').slideUp(300);
        if($(this).next().css('display') == "none") {
            $(this).next('ul').slideDown(300);
            $(this).parent('li').addClass('nav-show').siblings('li').removeClass('nav-show');
        }else{
            $(this).next('ul').slideUp(300);
            $('.nav-item.nav-show').removeClass('nav-show');
        }
    });

    //主页最新审核产品
	$(".to_new_product li").hover(function(){
        $(".btn",this).stop().animate({bottom:'10px'},{queue:false,duration:100});
        $("img",this).stop().animate({opacity:'.95'},{queue:false,duration:100});
	}, function(){
        $(".btn",this).stop().animate({bottom:'-36px'},{queue:false,duration:100});
        $("img",this).stop().animate({opacity:'1'},{queue:false,duration:100});
    });

    //设置活动产品
	$(".pic_box").hover(function(){
        $(".btn",this).stop().animate({bottom:'10px'},{queue:false,duration:100});
        $("img",this).stop().animate({opacity:'.95'},{queue:false,duration:100});
	}, function(){
        $(".btn",this).stop().animate({bottom:'-36px'},{queue:false,duration:100});
        $("img",this).stop().animate({opacity:'1'},{queue:false,duration:100});
    });

    //支付密码框监听    
    $(".pay_pass").keyup(function(){
        var len = $(this).val().length;
        if(len == 6){
            $(this).blur();
        }
    });

});

//通用TAB
function cutover(id,etype) {
	var tabContainer = $("#" + id).find("#tabContainer");
	var panelContainer = $("#" + id).find(".panelContainer");
	tabContainer.find("ul>li").each(function(i, e) {
		if(etype == "1") {
			$(e).hover(function() {
				tabContainer.find("ul>li").removeClass("active");
				$(e).addClass("active");
				panelContainer.children("div").hide();
				panelContainer.children("div").eq(i).show();
			});
		} else {
			$(e).click(function() {
				tabContainer.find("ul>li").removeClass("active");
				$(e).addClass("active");
				panelContainer.children("div").hide();
				panelContainer.children("div").eq(i).show();
			});
		}
	});
};