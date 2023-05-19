

$(function(){

    $(".s_btn").bind("click", function () {
        doSearch();
    });

    $(".q_labels").bind("click", function () {
        // $(".s_form .s_ipt").val($(this).text());
        // $(".s_form").attr("action","/api/product/g/search");
        // $(".s_form").submit();
        window.location.href=searchGetPath+"?keyword="+$(this).text();
    });

});


function doSearch()
{
    // $(".s_form").attr("action","/api/product/g/search");
    // $(".s_form").submit();
    var keywrd=$(".s_ipt").val();
    var qkeyword=$("#qkeyword").val();
    var cid=$("#cid").val();
    var ebids=$("#ebids").val();
    var qbs=$("#qbs").val();
    var params ="?keyword="+keywrd;
    if(cid!=null&&cid!="")
    {
        params+="&cid="+cid;
    }
    if(ebids!=null&&ebids!="")
    {
        params+="&ebids="+ebids;
    }
    if(qkeyword!=keywrd)
    {
        qbs='t';
    }
    if(qbs!=null&&qbs!='')
    {
        params+="&qbs="+qbs;
    }

    window.location.href=searchGetPath+params;
}