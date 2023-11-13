/**
 * 比较两个集合是否相等(无关顺序)
 * @param srcArray
 * @param destArray
 * @returns {boolean}
 */
function toucan_compareArray(srcArray,destArray)
{
    var findCount = 0 ;
    if(srcArray.length!=destArray.length)
    {
        return false;
    }
    for(var i=0;i<srcArray.length;i++)
    {
        for(var j=0;j<destArray.length;j++)
        {
            if(srcArray[i]==destArray[j])
            {
                findCount++;
            }
        }
    }
    if(findCount==srcArray.length)
    {
        return true;
    }
    return false;
}

/**
 * 两个对象进行深拷贝
 * @param destObj
 * @param srcObj
 * @returns {*|{}}
 */
function objectCopy(destObj,srcObj){
    var destObj = destObj || {};
    for (var i in srcObj) {
        if (typeof srcObj[i] === 'object') {
            destObj[i] = (srcObj[i].constructor === Array) ? [] : {};
            objectCopy(destObj[i], srcObj[i]);
        } else {
            destObj[i] = srcObj[i];
        }
    }
    return destObj;
}