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
	for (var i=0; i<category_list.length; i++) {
		areaCont += '<li onClick="selectCategoryLevel1(' + i + ');"><a href="javascript:void(0)">' + category_list[i].name + '</a></li>';
	}
	$("#sort1").html(areaCont);
}
intRootCategory();

/*选择一级目录*/
function selectCategoryLevel1(p) {
	areaCont = "";
	for (var j=0; j<category_list[p].children.length; j++) {
		areaCont += '<li onClick="selectCategoryLevel2(' + p + ',' + j + ');"><a href="javascript:void(0)">' + category_list[p].children[j].name + '</a></li>';
	}
	$("#sort2").html(areaCont).show();
	$("#sort3").hide();
	$("#sort1 li").eq(p).addClass("active").siblings("li").removeClass("active");
	expressP = category_list[p].name;
	$("#selectedSort").html(expressP);
	$("#releaseBtn").removeAttr("disabled");
}

/*选择二级目录*/
function selectCategoryLevel2(p,c) {
	areaCont = "";
	expressC = "";
	for (var k=0; k<category_list[p].children[c].children.length; k++) {
		areaCont += '<li onClick="selectD(' + p + ',' + c + ',' + k + ');"><a href="javascript:void(0)">' + category_list[p].children[c].children[k].name + '</a></li>';
	}
	$("#sort3").html(areaCont).show();
	$("#sort2 li").eq(c).addClass("active").siblings("li").removeClass("active");
	expressC = expressP + arrow + category_list[p].children[c].name;
	$("#selectedSort").html(expressC);
}

/*选择三级目录*/
function selectD(p,c,d) {
	$("#sort3 li").eq(d).addClass("active").siblings("li").removeClass("active");
	expressD = expressC + arrow + category_list[p].children[c].children[d].name;
	$("#selectedSort").html(expressD);
}

/*点击下一步*/
$("#releaseBtn").click(function() {
	var releaseS = $(this).prop("disabled");
	if (releaseS == false) {//未被禁用
		$("#step1").hide();
		$("#step2").show();
	}
});
