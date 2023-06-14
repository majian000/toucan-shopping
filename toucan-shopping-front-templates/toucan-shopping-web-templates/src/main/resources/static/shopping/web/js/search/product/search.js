

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


function doSearch(extParams)
{
    // $(".s_form").attr("action","/api/product/g/search");
    // $(".s_form").submit();
    var keywrd=$(".s_ipt").val();
    var qkeyword=$("#qkeyword").val();
    var cid=$("#cid").val();
    var ebids=$("#ebids").val();
    var qbs=$("#qbs").val();
    var ab = $("#ab").val();
    var abids = $("#abids").val();
    var bid = $("#bid").val();
    var bn = $("#bn").val();
    var ps = $("#ps").val();
    var pe = $("#pe").val();
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
    if(ab!=null&&ab!='') {
        params += "&ab=" + ab;
    }
    if(abids!=null&&abids!='') {
        params += "&abids=" + abids;
    }
    if(bid!=null&&bid!='')
    {
        params += "&bid=" + bid;
    }
    if(bn!=null&&bn!='')
    {
        params += "&bn=" + bn;
    }
    if(ps!=null&&ps!='')
    {
        params += "&ps=" + ps;
    }
    if(pe!=null&&pe!='')
    {
        params += "&pe=" + pe;
    }


    if(extParams!=null&&extParams!='')
    {
        params+=extParams;
    }

    window.location.href=searchGetPath+params;
}