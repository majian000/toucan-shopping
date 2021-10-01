$(function () {
    drawTable();
    $('#addsc').on('click', function(){
        layer.open({
            type: 2,
            title: '添加类别',
            fix: false,
            shadeClose: true,
            maxmin: true,
            area: ['500px', '260px'],
            content: basePath+"/page/shop/category/-1/add"
        });

    });
});


function addChildCategory(id)
{
    layer.open({
        type: 2,
        title: '添加类别',
        fix: false,
        shadeClose: true,
        maxmin: true,
        area: ['500px', '260px'],
        content: basePath+"/page/shop/category/"+id+"/add"
    });
}


function editCategory(id)
{
    layer.open({
        type: 2,
        title: '修改类别',
        fix: false,
        shadeClose: true,
        maxmin: true,
        area: ['500px', '260px'],
        content: basePath+"/page/shop/category/edit/"+id
    });
}

function deleteRow(id,name)
{
    layer.confirm('确定删除'+name+'?', {
        btn: ['确定','关闭'], //按钮
        title:'提示信息'
    }, function(index){
        $.ajax({
            type: "POST",
            url: basePath+'/api/shop/category/delete/'+id,
            contentType: "application/json;charset=utf-8",
            data:  null,
            dataType: "json",
            success: function (data) {
                if(data.code==1)
                {
                    layer.close(index);
                    drawTable();
                }
            },
            error: function (result) {
                drawTable();
            }
        });
    }, function(){

    });
}

function moveTop(id,parentId)
{
    $.ajax({
        type: "POST",
        url: basePath+'/api/shop/category/move/top',
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify({"id":id,"parentId":parentId}),
        dataType: "json",
        success: function (data) {
            if(data.code==1)
            {
                drawTable();
            }
        },
        error: function (result) {
            drawTable();
        }
    });
}

function moveBottom(id,parentId)
{
    $.ajax({
        type: "POST",
        url: basePath+'/api/shop/category/move/bottom',
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify({"id":id,"parentId":parentId}),
        dataType: "json",
        success: function (data) {
            if(data.code==1)
            {
                drawTable();
            }
        },
        error: function (result) {
            drawTable();
        }
    });
}


function moveUp(id,parentId)
{
    $.ajax({
        type: "POST",
        url: basePath+'/api/shop/category/move/up',
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify({"id":id,"parentId":parentId}),
        dataType: "json",
        success: function (data) {
            if(data.code==1)
            {
                drawTable();
            }
        },
        error: function (result) {
            drawTable();
        }
    });
}


function moveDown(id,parentId)
{
    $.ajax({
        type: "POST",
        url: basePath+'/api/shop/category/move/down',
        contentType: "application/json;charset=utf-8",
        data:  JSON.stringify({"id":id,"parentId":parentId}),
        dataType: "json",
        success: function (data) {
            if(data.code==1)
            {
                drawTable();
            }
        },
        error: function (result) {
            drawTable();
        }
    });
}
function drawTable()
{

    $.ajax({
        type: "POST",
        url: basePath+'/api/shop/category/list',
        contentType: "application/json;charset=utf-8",
        data:  null,
        dataType: "json",
        success: function (data) {
            if(data.code==1)
            {
                var tableData = "<tr>";
                tableData+="<th>分类名称</th>";
                /*tableData+="<th>分类图片</th>";*/
                tableData+="<th>移动</th>";
                tableData+="<th>操作</th>";
                tableData+="</tr>";
                if(data.data!=null&&data.data.length>0)
                {
                    for(var i=0;i<data.data.length;i++) {
                        var row = data.data[i];
                        var rowData = "<tr class='treegrid-"+row.id+" ";
                        rowData += " '>";
                        rowData += "<td>"+row.name+"</td>";
                        /*rowData += "<td>22</td>";*/
                        rowData += "<td><a href=\"#\" onclick=\"moveTop('"+row.id+"','"+row.parentId+"');\" >置顶</a> | <a href=\"#\" onclick=\"moveUp('"+row.id+"','"+row.parentId+"');\">向上</a> | <a href=\"#\" onclick=\"moveDown('"+row.id+"','"+row.parentId+"');\"  >向下</a> | <a href=\"#\" onclick=\"moveBottom('"+row.id+"','"+row.parentId+"');\" >置底</a></td>";
                        rowData += "<td>";
                        rowData += "<a href=\"#\" onclick=\"addChildCategory('"+row.id+"');\">添加子分类</a> |  ";
                        rowData += " <a href=\"#\" onclick=\"editCategory('"+row.id+"')\">修改</a> | <a href=\"#\" onclick=\"deleteRow('"+row.id+"','"+row.name+"');\">删除</a> ";
                        rowData += "</td>";
                        rowData += "</tr>";

                        tableData+=rowData;

                        if(row.children!=null&&row.children.length>0)
                        {
                            for(var j=0;j<row.children.length;j++) {
                                var child = row.children[j];
                                var childData = "<tr class='treegrid-" + child.id + " ";
                                childData+=" treegrid-parent-"+row.id+" ";
                                childData += " '>";
                                childData += "<td>" + child.name + "</td>";
                                /*childData += "<td>22</td>";*/
                                childData += "<td><a href=\"#\" onclick=\"moveTop('"+child.id+"','"+child.parentId+"');\" >置顶</a>| <a  href=\"#\" onclick=\"moveUp('"+child.id+"','"+child.parentId+"');\" >向上</a> | <a href=\"#\" onclick=\"moveDown('"+child.id+"','"+child.parentId+"');\"   >向下</a> | <a href=\"#\" onclick=\"moveBottom('"+child.id+"','"+child.parentId+"');\">置底</a></td>";
                                childData += "<td>";
                                childData += " <a href=\"#\" onclick=\"editCategory('"+child.id+"')\">修改</a> | <a href=\"#\" onclick=\"deleteRow('"+child.id+"','"+child.name+"');\">删除</a> ";
                                childData += "</td>";
                                childData += "</tr>";
                                tableData+=childData;
                            }
                        }


                    }
                }


                $("#sclist").html(tableData);


                $('.tree').treegrid();
            }
        }
    });
}