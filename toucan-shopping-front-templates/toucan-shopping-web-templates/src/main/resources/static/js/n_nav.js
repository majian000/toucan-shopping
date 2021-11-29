// JavaScript Document
var jq = jQuery.noConflict();

jQuery(document).ready(function(){
	$(".nav").hover(function(){
		$(this).find(".leftNav").show();
	},function(){
		$(this).find(".leftNav").hide();
	});
});	


