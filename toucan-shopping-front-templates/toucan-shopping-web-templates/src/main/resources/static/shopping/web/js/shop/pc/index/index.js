/**
 * 绘制组件格子
 */
function drawComponentBlock() {
    //20*20的页面
    var col = 20;
    var row = 20;
    var pagePanel = $(".shop-index-main");
    var componentBlockHtmls="";
    for (var i = 0; i < row;i++){
        for (var j = 0; j < col; j++) {
            var componentBlockHtml="<div class=\"com-block\" " +
                "data-row=\""+i+"\" data-col=\""+j+"\" " +
                "style=\"min-width: 40px; min-height: 40px; width:5%;height:5%;top:"+(i*5)+"%; left: "+(j*5)+"%; \" >"+(i+":"+j)+"</div>";
            componentBlockHtmls+=componentBlockHtml;
        }
    }
    pagePanel.append(componentBlockHtmls);
}

$(function(){
    drawComponentBlock();
    // if(pageView!=null) {
    //     if (pageView.backgroundColor != null && pageView.backgroundColor != "") {
    //         $(".shop-index-main-page").css("background-color", pageView.backgroundColor);
    //     }
    //     if(pageView.title!=null&&pageView.title!="")
    //     {
    //         $("title").text(pageView.title);
    //     }
    // }
});