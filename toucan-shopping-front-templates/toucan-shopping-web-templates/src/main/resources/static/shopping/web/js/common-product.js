function getProductId()
{
    var urlParam = window.location.href;
    var id_op=urlParam.substring(urlParam.lastIndexOf("/")+1, urlParam.length);
    if(id_op.indexOf("#")!=-1)
    {
        return id_op.substring(0,id_op.indexOf("#"));
    }
    if(id_op.indexOf("_")!=-1)
    {
        return id_op.substring(0,id_op.indexOf("_"));
    }
    return id_op;
}