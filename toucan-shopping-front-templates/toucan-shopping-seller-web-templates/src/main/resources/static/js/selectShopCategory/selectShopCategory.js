/**
 * jquery.sort.js
 * 商品发布-选择分类
 * author: 锐不可挡
 * date: 2016-07-07
 **/



var expressP, expressC, expressD, expressArea, areaCont;
var arrow = " <font>&gt;</font> ";

/*初始化一级目录*/
function intRootShopCategory() {
	areaCont = "";
	for (var i=0; i<shop_category_list.length; i++) {
		areaCont += '<li onClick="selectShopCategoryLevel1(' + i + ');"><a href="javascript:void(0)">' + shop_category_list[i].name + '</a></li>';
	}
	$("#shop_category_sort1").html(areaCont);
}
intRootShopCategory();

/*选择一级目录*/
function selectShopCategoryLevel1(p) {
	areaCont = "";
	for (var j=0; j<shop_category_list[p].children.length; j++) {
		areaCont += '<li onClick="selectShopCategoryLevel2(' + p + ',' + j + ');"><a href="javascript:void(0)">' + shop_category_list[p].children[j].name + '</a></li>';
	}
	$("#shop_category_sort2").html(areaCont).show();
	$("#shop_category_sort1 li").eq(p).addClass("active").siblings("li").removeClass("active");
	expressP = shop_category_list[p].name;
	$("#selectedShopCategorySort").html(expressP);
	$("#releaseBtn").removeAttr("disabled");
}

/*选择二级目录*/
function selectShopCategoryLevel2(p,c) {
	expressC = "";
	$("#shop_category_sort2 li").eq(c).addClass("active").siblings("li").removeClass("active");
	expressC = expressP + arrow + shop_category_list[p].children[c].name;
	$("#selectedShopCategorySort").html(expressC);
}


