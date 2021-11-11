

function initBrandListControl(brandDiv,brandPageId,categoryId)
{
    //定义数组，在服务端返回的数据也以该格式返回：Array[{Object},{...}]
    $("#"+brandDiv).empty();
    $("#"+brandDiv).append("<input type=\"text\" id=\""+brandPageId+"\" >");

    //初始化插件
    $('#'+brandPageId).selectPage({
        showField : 'name',
        keyField : 'id',
        data : basePath+'/api/product/brand/list',
        params : function(){
            return {'categoryId':categoryId};
        },
        //ajax请求后服务端返回的数据格式处理
        //返回的数据里必须包含list（Array）和totalRow（number|string）两个节点
        eAjaxSuccess : function(d){
            var result;
            if(d) {
                result = d.values.gridResult;
            }else {
                result = undefined;
            }
            return result;
        }
    });
}