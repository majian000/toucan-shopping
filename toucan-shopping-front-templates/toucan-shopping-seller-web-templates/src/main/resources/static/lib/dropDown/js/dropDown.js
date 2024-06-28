/**
 * 绑定下拉事件
 */
function bindMoreBtnEvent(){
    /*----------------------------------
          点击展开移出区域隐藏列表
    ----------------------------------*/
    //外层moreBtn框
    var oDropDown = $('.moreBtn');
    //选择列表项
    var oChangeA = $('.moreBtnItems');
   oDropDown.click(function(){
       $(this).find('.moreBtnList').toggle().siblings('.xl-icon').toggleClass('sq-icon');
   });

    oDropDown.mouseleave(function(){
        $(this).find('.moreBtnList').hide().siblings('.xl-icon').removeClass('sq-icon');
    });

    oChangeA.click(function(){
        var oVal = $(this).text();
        // $(this).parents('.moreBtnItems-all').parents('.moreBtnList').siblings('.moreBtnVal').text(oVal).siblings('.xl-icon').toggleClass('sq-icon');
    });
    /*----------------------------------
        点击展开点击空白区域隐藏列表
     ----------------------------------*/
    var oNextS = $('.moreBtnNext');
    var oChangeANext = $('.moreBtnItemsNext');
    oNextS.click(function(){
        //如果页面存在多个下拉框，点击this把别的下拉框隐藏并且下拉icon向下
        $('.moreBtnListNext').hide();
        $('.xl-iconNext').removeClass('sq-icon');

        $(this).siblings('.moreBtnListNext').toggle().siblings('.moreBtnNext').find('.xl-iconNext').toggleClass('sq-icon');
    });
    //点击空白区域列表隐藏
    $(document).click(function(){
        $(".moreBtnListNext").hide();
        $('.xl-iconNext').removeClass('sq-icon');
    });
    //点击元素内不隐藏
    oNextS.click(function(event){
        event.stopPropagation();
    });
    oChangeANext.click(function(){
        var oVal = $(this).text();
        // $(this).parents('.moreBtnItems-allNext').parents('.moreBtnListNext').siblings('.moreBtnNext').find('.moreBtnValNext').text(oVal).siblings('.xl-iconNext').toggleClass('sq-icon');
    });
}