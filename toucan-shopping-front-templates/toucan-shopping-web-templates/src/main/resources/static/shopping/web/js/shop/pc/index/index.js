var blockUnit = 5;

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
            var componentBlockHtml="<div class=\"com-block com-block-"+i+"-"+j+"\" " +
                "data-row=\""+i+"\" data-col=\""+j+"\" " +
                "style=\"min-width: 40px; min-height: 40px; width:5%;height:5%;top:"+(i*blockUnit)+"%; left: "+(j*blockUnit)+"%; \" ></div>";
            componentBlockHtmls+=componentBlockHtml;
        }
    }
    pagePanel.append(componentBlockHtmls);
}

/**
 * 绘制组件
 */
function drawComponents(){
    if(pageView!=null&&pageView.componentViews!=null)
    {
        for(var i=0;i<pageView.componentViews.length;i++)
        {
            var componentView = pageView.componentViews[i];
            if(componentView.type=="shopBannerView") {
                drawBannerView(componentView);
            }
        }
    }
}

function drawPage(){
    if(pageView!=null) {
        if (pageView.backgroundColor != null && pageView.backgroundColor != "") {
            $(".shop-index-main-page").css("background-color", pageView.backgroundColor);
        }
        if(pageView.title!=null&&pageView.title!="")
        {
            $("title").text(pageView.title);
        }
    }
}

/**
 * 绘制轮播图
 * @param componentView
 */
function drawBannerView(componentView){

    var width = componentView.width*100;
    var height = componentView.height*100;
    var bannerHtml="  <div id=\"slider\" class=\"tslider\" style=\"width:"+width+"%;height:"+height+"%\">\n" +
        "            <button type=\"button\" class=\"preBtn\" id=\"preBtn\">\n" +
        "                &lt;\n" +
        "            </button>\n" +
        "            <button type=\"button\" class=\"nextBtn\" id=\"nextBtn\">\n" +
        "                &gt; >\n" +
        "            </button>\n" +
        "            <ul class=\"pointer\" id=\"defaultBannerPointer\">\n" +
        "            </ul>\n" +
        "        </div>";
    $(".com-block-"+componentView.x+"-"+componentView.y).append(bannerHtml);
    initBannerPluginSliders();
}

$(function(){
    drawComponentBlock();
    drawPage();
    drawComponents();
});