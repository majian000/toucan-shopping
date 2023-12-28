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
                renderBannerView(componentView);
            }else if(componentView.type=="shopCategoryView") {
                renderCategoryView(componentView);
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
 * 渲染轮播图视图
 * @param componentView
 */
function renderBannerView(componentView){

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
    $(".com-block-"+componentView.y+"-"+componentView.x).append(bannerHtml);
    initBannerPluginSliders();
}

/**
 * 渲染分类视图
 * @param componentView
 */
function renderCategoryView(componentView){
    var width = componentView.width*100;
    var height = componentView.height*60;
    var categoryMenuHtml="<div class=\"shopCategory\" style=\"width:"+width+"%;height:"+height+"%\">\n" +
        "            <ul class=\"menu-top\">\n" +
        "            </ul>\n" +
        "        </div>";
    $(".com-block-"+componentView.y+"-"+componentView.x).append(categoryMenuHtml);
    loadCategoryMenu();
}

$(function(){
    drawComponentBlock();
    drawPage();
    drawComponents();
});