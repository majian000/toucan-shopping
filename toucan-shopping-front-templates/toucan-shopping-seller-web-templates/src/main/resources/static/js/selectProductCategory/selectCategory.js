/**
 * jquery.sort.js
 * 商品发布-选择分类
 * author: 锐不可挡
 * date: 2016-07-07
 **/



var expressP, expressC, expressD, expressArea, areaCont;
var arrow = " <font>&gt;</font> ";

/*初始化一级目录*/
function intRootCategory() {
	areaCont = "";
	for (var i=0; i<categoryList.length; i++) {
		areaCont += '<li onClick="selectCategoryLevel1(' + i + ');"><a href="javascript:void(0)">' + categoryList[i].title + '</a></li>';
	}
	$("#sort1").html(areaCont);
}
intRootCategory();

/*选择一级目录*/
function selectCategoryLevel1(p) {
	areaCont = "";
	for (var j=0; j<categoryList[p].children.length; j++) {
		areaCont += '<li onClick="selectCategoryLevel2(' + p + ',' + j + ');"><a href="javascript:void(0)">' + categoryList[p].children[j].title + '</a></li>';
	}
	$("#sort2").html(areaCont).show();
	$("#sort3").hide();
	$("#sort1 li").eq(p).addClass("active").siblings("li").removeClass("active");
	expressP = categoryList[p].title;
	$("#selectedSort").html(expressP);
	$("#releaseBtn").removeAttr("disabled");
}

/*选择二级目录*/
function selectCategoryLevel2(p,c) {
	areaCont = "";
	expressC = "";
	for (var k=0; k<categoryList[p].children[c].children.length; k++) {
		areaCont += '<li onClick="selectD(' + p + ',' + c + ',' + k + ');"><a href="javascript:void(0)">' + categoryList[p].children[c].children[k].title + '</a></li>';
	}
	$("#sort3").html(areaCont).show();
	$("#sort2 li").eq(c).addClass("active").siblings("li").removeClass("active");
	expressC = expressP + arrow + categoryList[p].children[c].title;
	$("#selectedSort").html(expressC);
}

/*选择三级目录*/
function selectD(p,c,d) {
	$("#sort3 li").eq(d).addClass("active").siblings("li").removeClass("active");
	expressD = expressC + arrow + categoryList[p].children[c].children[d].title;
	$("#selectedSort").html(expressD);
}

/*点击下一步*/
$("#releaseBtn").click(function() {
	var releaseS = $(this).prop("disabled");
	if (releaseS == false) {//未被禁用
		//location.href = "商品发布-详细信息.html";//跳转到下一页
	}
});
