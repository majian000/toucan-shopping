

function initBrandListControl(brandPageId,categoryId)
{
    //定义数组，在服务端返回的数据也以该格式返回：Array[{Object},{...}]
    var tag_data = [
        {id:1 ,name:'Chicago Bulls',desc:'芝加哥公牛'},
        {id:2 ,name:'Cleveland Cavaliers',desc:'克里夫兰骑士'},
        {id:3 ,name:'Detroit Pistons',desc:'底特律活塞'},
        {id:4 ,name:'Indiana Pacers',desc:'印第安纳步行者'}
    ];
    //初始化插件
    $('#'+brandPageId).selectPage({
        showField : 'desc',
        keyField : 'id',
        data : tag_data
    });
}