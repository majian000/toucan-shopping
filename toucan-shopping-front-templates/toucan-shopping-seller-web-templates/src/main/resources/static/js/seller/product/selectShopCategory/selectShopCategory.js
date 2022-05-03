/**
 * jquery.sort.js
 * 商品发布-选择分类
 * author: 锐不可挡
 * date: 2016-07-07
 **/



var expressShopCategoryP, expressShopCategoryC,  areaShopCategoryCont;
var arrow = " <font>&gt;</font> ";

/*初始化一级目录*/
function intRootShopCategory() {
	areaShopCategoryCont = "";
	for (var i=0; i<shop_category_list.length; i++) {
		areaShopCategoryCont += "<li onClick=\"selectShopCategoryLevel1(" + i + "); \" id=\"shop_category_"+shop_category_list[i].id+"\"><a href=\"javascript:void(0)\">" + shop_category_list[i].name + "</a></li>";
	}
	$("#shop_category_sort1").html(areaShopCategoryCont);
}


/*选择一级目录*/
function selectShopCategoryLevel1(p) {
	areaShopCategoryCont = "";
	for (var j=0; j<shop_category_list[p].children.length; j++) {
		areaShopCategoryCont += "<li onClick=\"selectShopCategoryLevel2(" + p + "," + j + ");\" id=\"shop_category_"+shop_category_list[p].children[j].id+"\"><a href=\"javascript:void(0)\">" + shop_category_list[p].children[j].name + "</a></li>";
	}
	$("#shop_category_sort2").html(areaShopCategoryCont).show();
	if($("#shop_category_sort1 li").eq(p).hasClass("active"))
	{
		$("#shop_category_sort1 li").eq(p).removeClass("active");
		$("#selectedShopCategorySort").html("");
	}else{
		$("#shop_category_sort1 li").eq(p).addClass("active").siblings("li").removeClass("active");
		expressShopCategoryP = shop_category_list[p].name;
		$("#selectedShopCategorySort").html(expressShopCategoryP);
		$("#shopCategoryId").val(shop_category_list[p].id);
	}
}

/*选择二级目录*/
function selectShopCategoryLevel2(p,c) {
	expressShopCategoryC = "";
	if($("#shop_category_sort2 li").eq(c).hasClass("active"))
	{
		$("#shop_category_sort2 li").eq(c).removeClass("active");
		expressShopCategoryC=expressShopCategoryP;
		$("#shopCategoryId").val(shop_category_list[p].id);
	}else{
		$("#shop_category_sort2 li").eq(c).addClass("active").siblings("li").removeClass("active");
		expressShopCategoryC = expressShopCategoryP + arrow + shop_category_list[p].children[c].name;
		$("#shopCategoryId").val(shop_category_list[p].children[c].id);
	}
	$("#selectedShopCategorySort").html(expressShopCategoryC);
}


