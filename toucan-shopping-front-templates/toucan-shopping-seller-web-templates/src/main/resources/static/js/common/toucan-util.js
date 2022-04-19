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