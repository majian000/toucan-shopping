

$(function(){

    $(".s_btn").bind("click", function () {
        clearSearchParamsBySearch();
        doSearch();
    });

    var curspid = $("#curspid").val();
    if(curspid!=null&&curspid!="") {
        $(".ss_btn").show();
        $(".ss_btn").bind("click", function () {
            clearSearchParamsByShopSearch();
            doShopSearch();
        });
    }

    $(".q_labels").bind("click", function () {
        // $(".s_form .s_ipt").val($(this).text());
        // $(".s_form").attr("action","/api/product/g/search");
        // $(".s_form").submit();
        window.location.href=searchGetPath+"?keyword="+$(this).text();
    });

    $(document).keyup(function(event){
        if(event.keyCode ==13){
            $(".s_btn").click();
        }
    });

    $(".psfi").keydown(function(event){
        if(event.keyCode ==13){
            $(".s_btn").click();
            return false;
        }
    });

});

function clearSearchParamsBySearch()
{
    var psfhs = $(".psfh");
    if(psfhs!=null&&psfhs.length>0)
    {
        for(var i=0;i<psfhs.length;i++)
        {
            $(psfhs[i]).val("");
        }
    }
}

function clearSearchParamsByShopSearch()
{
    var psfhs = $(".psfh");
    if(psfhs!=null&&psfhs.length>0)
    {
        for(var i=0;i<psfhs.length;i++)
        {
            var psid = $(psfhs[i]).attr("id");
            if(psid!="sid") {
                $(psfhs[i]).val("");
            }
        }
    }
}

function doSearch(extParams)
{
    // $(".s_form").attr("action","/api/product/g/search");
    // $(".s_form").submit();

    var psfiMap = new Map();
    var psfi = $(".psfi");
    if(psfi!=null&&psfi.length>0)
    {
        for(var i=0;i<psfi.length;i++)
        {
            var id=$(psfi[i]).attr("id");
            var val=$(psfi[i]).val();
            if(val==null||val=="undefined"){
                val="";
            }
            psfiMap.set(id,val);
        }
    }

    var params ="?keyword="+(psfiMap.get("keyword")!=null?psfiMap.get("keyword"):"");
    if(psfiMap.get("qkeyword")!=psfiMap.get("keyword"))
    {
        psfiMap.set("qbs","t");
    }
    for(let item of psfiMap.entries()) {
        //0:key 1:value
        if(item[0]=="keyword")
        {
            continue;
        }
        if(item[0]=="qkeyword")
        {
            continue;
        }
        if(item[0]=="ebids")
        {
            if(item[1]!=null&&item[1]!="") {
                if (psfiMap.get("qbs") != "f") {
                    params += "&ebids=" + item[1];
                }
            }
            continue;
        }

        if(item[1]!=null&&item[1]!="")
        {
            params+="&"+item[0]+"="+item[1];
        }
    }


    if(extParams!=null&&extParams!='')
    {
        params+=extParams;
    }

    window.location.href=searchGetPath+params;
}


function doShopSearch(extParams)
{
    // $(".s_form").attr("action","/api/product/g/search");
    // $(".s_form").submit();

    var psfiMap = new Map();
    var psfi = $(".psfi");
    if(psfi!=null&&psfi.length>0)
    {
        for(var i=0;i<psfi.length;i++)
        {
            var id=$(psfi[i]).attr("id");
            var val=$(psfi[i]).val();
            if(val==null||val=="undefined"){
                val="";
            }
            psfiMap.set(id,val);
        }
    }

    var params ="?keyword="+(psfiMap.get("keyword")!=null?psfiMap.get("keyword"):"");
    if(psfiMap.get("qkeyword")!=psfiMap.get("keyword"))
    {
        psfiMap.set("qbs","t");
    }
    for(let item of psfiMap.entries()) {
        //0:key 1:value
        if(item[0]=="keyword")
        {
            continue;
        }
        if(item[0]=="qkeyword")
        {
            continue;
        }
        if(item[0]=="ebids")
        {
            if(item[1]!=null&&item[1]!="") {
                if (psfiMap.get("qbs") != "f") {
                    params += "&ebids=" + item[1];
                }
            }
            continue;
        }

        if(item[1]!=null&&item[1]!="")
        {
            params+="&"+item[0]+"="+item[1];
        }
    }


    if(extParams!=null&&extParams!='')
    {
        params+=extParams;
    }

    window.location.href=shopSearchGetPath+params;
}