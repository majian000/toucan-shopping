
$(function(){

    $.ajax({
        type: "GET",
        url: basePath+"/api/category/navigation/list",
        dataType: "json",
        success: function (result) {
            if(result.code>0)
            {
                var htmls = "";
                var categorys = result.data.categorys;
                if(categorys!=null&&categorys.length>0)
                {
                    for(var i=0;i<categorys.length;i++)
                    {
                        var category = categorys[i];
                        if(category.showStatus!=null && category.showStatus==1)
                        {
                            htmls+="<li>";
                            htmls+="<div class='fj'>";
                            htmls+="<span class='n_img'><span></span><img src='"+category+"' /></span>";
                            htmls+="<span class='fl'>"+category.rootLinks+"</span>";
                            htmls+="</div>";
                            htmls+="<div class='zj' style='"+category.pcIndexStyle+"' >";
                            htmls+="<div class='zj_l'>";
                            var categoryChildren = category.children;
                            if(categoryChildren!=null&&categoryChildren.length>0)
                            {
                                for(var j=0;j<categoryChildren.length;j++)
                                {
                                    var cc = categoryChildren[j];
                                    if(cc.showStatus!=null && cc.showStatus==1) {
                                        htmls +=" <div class='zj_l_c'>";
                                        if(cc.href==null)
                                        {
                                            cc.href = "#";
                                        }
                                        if(cc.name==null)
                                        {
                                            cc.name="";
                                        }
                                        htmls += "<h2><a href='"+cc.href+"'>"+cc.name+"</a></h2>";
                                        htmls+="<div style='padding-left: 10px;'>";
                                        var categoryChildChildren = cc.children;
                                        if(categoryChildChildren!=null&&categoryChildChildren.length>0) {
                                            for (var p = 0; p < categoryChildChildren.length; p++) {
                                                var ccc = categoryChildChildren[p];
                                                if(ccc.showStatus!=null && ccc.showStatus==1)
                                                {
                                                    if(ccc.href==null)
                                                    {
                                                        ccc.href = "#";
                                                    }
                                                    if(ccc.name==null)
                                                    {
                                                        ccc.name="";
                                                    }
                                                    htmls+="<a href='"+ccc.href+"' class='ac' >"+ccc.name+"</a>|";
                                                }
                                            }
                                            htmls+="</div>";
                                            htmls+="</div>";
                                        }
                                    }
                                }
                            }
                            htmls+="</div>";
                            htmls+="<div class='zj_r'>";
                            htmls+="<a href='#'><img src='${basePath}/static/images/n_img1.jpg' width='236' height='200' /></a>";
                            htmls+="<a href='#'><img src='${basePath}/static/images/n_img2.jpg' width='236' height='200' /></a>";
                            htmls+="</div>";
                            htmls+="</div>";
                            htmls+="</li>";
                        }



                    }
                }
            }
        }
    });

});