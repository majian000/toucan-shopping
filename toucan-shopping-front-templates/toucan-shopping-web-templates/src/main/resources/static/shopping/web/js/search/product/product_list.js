

$(function(){


    drawPageJumpBtns();


});


function drawPageJumpBtns()
{
    var pageTotal = parseInt($("#pageTotal").val());
    var page = parseInt($("#page").val());
    var pageHtml="<a  class=\"p_pre pages_a\">上一页</a>";
    pageHtml+="<a  class=\"cur pages_a\" attr-val=\""+page+"\">"+page+"</a>";
    for(var i=page+1;i<5;i++)
    {
        pageHtml += "<a  class=\"p_pn pages_a\" attr-val=\""+i+"\">"+i+"</a>";
    }
    pageHtml+="<a class=\"p_next pages_a\">下一页</a> <a>共"+pageTotal+"页 跳转到<input type=\"text\" class=\"l_ipt\" style=\"width:30px;height: 30px; margin-top: -5px;\" />页 </a><a  class=\"p_jump pages_a\">跳转</a>";
    $(".pages").html(pageHtml);

}