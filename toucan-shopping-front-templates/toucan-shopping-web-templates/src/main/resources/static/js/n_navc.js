// JavaScript Document
var jq = jQuery.noConflict();

jQuery(document).ready(function(){
	$(".nav").hover(function(){
		$(this).find(".leftNavChild").show();
	},function(){
		$(this).find(".leftNavChild").hide();
	});
});	


