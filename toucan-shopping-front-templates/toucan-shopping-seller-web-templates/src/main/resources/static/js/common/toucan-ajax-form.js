function getAjaxFormData(id)
{
    var fields = $(id).serializeArray();
    var params = {}; //声明一个对象
    $.each(fields, function(index, field) {
        params[field.name] = field.value; //通过变量，将属性值，属性一起放到对象中
    });
    return JSON.stringify(params);
}