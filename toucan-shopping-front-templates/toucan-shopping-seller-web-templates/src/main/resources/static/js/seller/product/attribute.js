/**
 * Created by Administrator on 14-12-01.
 * 模拟淘宝SKU添加组合
 * 页面注意事项：
 *      1、 .rp_attr_div   这个类变化这里的js单击事件类名也要改
 *      2、 .rp_attr_kt      这个类作用是取到所有标题的值，赋给表格，如有改变JS也应相应改动
 *      3、 .rpai       这个类作用是取类型组数，有多少类型就添加相应的类名：如: rpai1、rpai2、rpai3 ...
 */

var g_sku_pos=0;
function stockInputKeyUp(o)
{
    $(o).keyup(function(){
        var c=$(this);
        if(/[^\d]/.test(c.val())){//替换非数字字符
            var temp_amount=c.val().replace(/[^\d]/g,'');
            $(this).val(temp_amount);
        }
    });
}

function skuUploadPreview(pos)
{
    $("#skuProductProview"+pos).on("change", function(){
        // Get a reference to the fileList
        var files = !!this.files ? this.files : [];

        // If no files were selected, or no FileReader support, return
        if (!files.length || !window.FileReader) {
            $("#skuPreview"+pos).attr("src","/static/lib/tupload/images/imgadd.png");
            return;
        }

        // Only proceed if the selected file is an image
        if (/^image/.test( files[0].type)){
            // Create a new instance of the FileReader
            var reader = new FileReader();

            // Read the local file as a DataURL
            reader.readAsDataURL(files[0]);

            // When loaded, set image data as background of div
            reader.onloadend = function(){
                $("#skuPreview"+pos).attr("src",this.result);
            }

        }

    });
}

function bindAttLabelEvent()
{
    $(".rp_attr_div label").unbind("change");
    //SKU信息
    $(".rp_attr_div label").bind("change", function () {
        attributeControl.Creat_Table();
        //重新计算库存总数
        inputStock();
        //绑定图片预览事件
        for(var i=0;i<g_sku_pos;i++)
        {
            skuUploadPreview(i);
        }
    });
}

var attributeControl = {
    imgUploadTitle:"<span class='red'>*</span>图片上传",
    //SKU信息组合
    Creat_Table: function () {
        attributeControl.hebingFunction();
        var SKUObj = $(".rp_attr_kt");
        //var skuCount = SKUObj.length;//
        var arrayTile = new Array();//标题组数
        var arraySelectTitle = new Array(); //所选标题数组
        var arrayInfor = new Array();//盛放每组选中的CheckBox值的对象
        var arrayColumn = new Array();//指定列，用来合并哪些列
        var columnIndex = 0;
        $.each(SKUObj, function (i, item){
            var itemName = "rpai" + i;
            //查询所有已选择属性的名称
            var selectAttributeControls = $("." + itemName + " input[type=checkbox]:checked");
            if(selectAttributeControls!=null&&selectAttributeControls.length>0) {
                arrayColumn.push(columnIndex);
                columnIndex++;
                arrayTile.push(SKUObj.find("li").eq(i).html().replace("：", ""));
                //选中的CHeckBox取值
                var order = new Array();
                $("." + itemName + " input[type=checkbox]:checked").each(function () {
                    order.push($(this).val());
                    var attributeName = $("." + itemName).attr("attr-group-name");
                    var isFind = false;
                    for (var sti = 0; sti < arraySelectTitle.length; sti++) {
                        if (arraySelectTitle[sti] == attributeName) {
                            isFind = true;
                        }
                    }
                    if (!isFind) {
                        arraySelectTitle.push(attributeName);
                    }
                });
                if (order != null && order.length > 0) {
                    arrayInfor.push(order);
                }
                //var skuValue = SKUObj.find("li").eq(index).html();
            }
        });
        if(arrayInfor!=null&&arrayInfor.length>0) {
            //开始创建Table表
            var RowsCount = 0;
            $("#tspSkuAttributeTable").html("");
            var table = $("<table id=\"process\"  cellpadding=\"1\" cellspacing=\"0\"  class='skuTable' border='1' ></table>");
            $("#tspSkuAttributeTable").html("<a>销售规格</a>");
            table.appendTo($("#tspSkuAttributeTable"));
            var thead = $("<thead></thead>");
            thead.appendTo(table);
            var trHead = $("<tr></tr>");
            trHead.appendTo(thead);
            //创建表头
            $.each(arrayTile, function (index, item) {
                var td = $("<th >" + item + "</th>");
                td.appendTo(trHead);
            });
            var itemColumHead = $("<th  style=\"width:70px;\"><span class='red'>*</span>价格</th><th style=\"width:70px;\"><span class='red'>*</span>库存</th> <th  style=\"width:70px;\">"+this.imgUploadTitle+"</th> <th  style=\"width:70px;\"><span class='red'>*</span>图片预览</th>");
            itemColumHead.appendTo(trHead);
            //var itemColumHead2 = $("<td >商家编码</td><td >商品条形码</td>");
            //itemColumHead2.appendTo(trHead);
            var tbody = $("<tbody></tbody>");
            tbody.appendTo(table);
            ////生成组合
            var zuheDate = attributeControl.doExchange(arrayInfor);
            if (zuheDate.length > 0) {
                g_sku_pos=0;
                //创建行
                $.each(zuheDate, function (index, item) {
                    var td_array = item.split(",");
                    var tr = $("<tr id='sku_row_"+g_sku_pos+"'></tr>");
                    tr.appendTo(tbody);
                    var sku_attribute_json="{";
                    $.each(td_array, function (i, values) {
                        var td = $("<td>" + values + "</td>");
                        td.appendTo(tr);

                        sku_attribute_json+="\""+arrayTile[i]+"\":\""+values+"\"";
                        if(i+1<td_array.length)
                        {
                            sku_attribute_json+=",";
                        }
                    });
                    sku_attribute_json+="}";
                    var td1 = $("<td ><input name=\"productSkuVOList["+g_sku_pos+"].price\" id=\"productSkuVOList_"+g_sku_pos+"_price\" class=\"releaseProductInputText\" type=\"text\" value=\"\" lay-verify=\"required|money\" style=\"width:80%\"  placeholder='请输入价格'></td>");
                    td1.appendTo(tr);
                    var td2 = $("<td ><input name=\"productSkuVOList["+g_sku_pos+"].stockNum\" id=\"productSkuVOList_"+g_sku_pos+"_stockNum\" class=\"releaseProductInputText skuStockInput\" type=\"text\" value=\"\" lay-verify=\"required|productCount\" style=\"width:80%\"  onchange='inputStock(this);' onkeyup='stockInputKeyUp(this);' placeholder='请输入库存数量'></td>");
                    td2.appendTo(tr);
                    var td3 = $("<td ><input type='file' class='skuTablePhotos skuTableUploadFile' name='productSkuVOList["+g_sku_pos+"].mainPhotoFile' id='skuProductProview"+g_sku_pos+"' /></td>");
                    td3.appendTo(tr);
                    var td4 = $("<td ><img id='skuPreview"+g_sku_pos+"' src='"+basePath+"/static/lib/tupload/images/imgadd.png' style='width:100%;height:100%'></td>");
                    td4.appendTo(tr);
                    var td5 = $("<input type='hidden' name='productSkuVOList["+g_sku_pos+"].attributes' id='productSkuVOList"+g_sku_pos+"_attributes'  class='productSkuAttributeHidden' attr-row-id='sku_row_"+g_sku_pos+"' attr-row-index='"+g_sku_pos+"' />");
                    td5.appendTo(tr);
                    var td6 = $("<input type='hidden'  id='productSkuVOList"+g_sku_pos+"_attributes_value'   class='productSkuAttributeValueHidden' attr-row-id='sku_row_"+g_sku_pos+"' attr-row-index='"+g_sku_pos+"' />");
                    td6.appendTo(tr);
                    var td7 = $("<input type='hidden' name=\"productSkuVOList["+g_sku_pos+"].id\"  id='productSkuVOList_"+g_sku_pos+"_id'  />");
                    td7.appendTo(tr);
                    var td8 = $("<input type='hidden' class='skuTableImgPaths' id='skuPreviewPath_"+g_sku_pos+"'  />");
                    td8.appendTo(tr);



                    $("#productSkuVOList"+g_sku_pos+"_attributes").val(sku_attribute_json);
                    $("#productSkuVOList"+g_sku_pos+"_attributes_value").val(td_array.join("_"));

                    g_sku_pos++;
                    //var td3 = $("<td ><input name=\"Txt_NumberSon\" class=\"l-text\" type=\"text\" value=\"\"></td>");
                    //td3.appendTo(tr);
                    //var td4 = $("<td ><input name=\"Txt_SnSon\" class=\"l-text\" type=\"text\" value=\"\"></td>");
                    //td4.appendTo(tr);
                });

            }
            //结束创建Table表
            arrayColumn.pop();//删除数组中最后一项
            //合并单元格
            $(table).mergeCell({
                // 目前只有cols这么一个配置项, 用数组表示列的索引,从0开始
                cols: arrayColumn
            });
        }else {
            //未全选中,清除表格
            document.getElementById('tspSkuAttributeTable').innerHTML="";
        }

        //保存发布的这个商品的所有属性
        var attribute_json="{";
        //遍历所有列
        $.each(arraySelectTitle, function (index, item) {
            attribute_json+="\""+item+"\":[";
            var attributeValueArray = new Array();
            //遍历所有行
            for(var zdi2=0;zdi2<zuheDate.length;zdi2++)
            {
                var item2=zuheDate[zdi2];
                var rowAttributeValue = item2.split(",");
                var isFind=false;
                for(var aa=0;aa<attributeValueArray.length;aa++)
                {
                    if(attributeValueArray[aa]==rowAttributeValue[index])
                    {
                        isFind=true;
                    }
                }
                if(!isFind) {
                    //保存这个行的指定列
                    attributeValueArray.push(rowAttributeValue[index]);
                }
            }
            attribute_json+="\""+attributeValueArray.join("\",\"")+"\"";
            attribute_json+="]";

            if(index+1<arraySelectTitle.length)
            {
                attribute_json+=",";
            }
        });
        attribute_json+="}";
        $("#attributes").val(attribute_json);

    },//合并行
    hebingFunction: function () {
        $.fn.mergeCell = function (options) {
            return this.each(function () {
                var cols = options.cols;
                for (var i = cols.length - 1; cols[i] != undefined; i--) {
                    // fixbug console调试
                    // console.debug(cols[i]);
                    mergeCell($(this), cols[i]);
                }
                dispose($(this));
            });
        };
        function mergeCell($table, colIndex) {
            $table.data('col-content', ''); // 存放单元格内容
            $table.data('col-rowspan', 1); // 存放计算的rowspan值 默认为1
            $table.data('col-td', $()); // 存放发现的第一个与前一行比较结果不同td(jQuery封装过的), 默认一个"空"的jquery对象
            $table.data('trNum', $('tbody tr', $table).length); // 要处理表格的总行数, 用于最后一行做特殊处理时进行判断之用
            // 进行"扫面"处理 关键是定位col-td, 和其对应的rowspan
            $('tbody tr', $table).each(function (index) {
                // td:eq中的colIndex即列索引
                var $td = $('td:eq(' + colIndex + ')', this);
                // 取出单元格的当前内容
                var currentContent = $td.html();
                // 第一次时走此分支
                if ($table.data('col-content') == '') {
                    $table.data('col-content', currentContent);
                    $table.data('col-td', $td);
                } else {
                    // 上一行与当前行内容相同
                    if ($table.data('col-content') == currentContent) {
                        // 上一行与当前行内容相同则col-rowspan累加, 保存新值
                        var rowspan = $table.data('col-rowspan') + 1;
                        $table.data('col-rowspan', rowspan);
                        // 值得注意的是 如果用了$td.remove()就会对其他列的处理造成影响
                        $td.hide();
                        // 最后一行的情况比较特殊一点
                        // 比如最后2行 td中的内容是一样的, 那么到最后一行就应该把此时的col-td里保存的td设置rowspan
                        if (++index == $table.data('trNum'))
                            $table.data('col-td').attr('rowspan', $table.data('col-rowspan'));
                    } else { // 上一行与当前行内容不同
                        // col-rowspan默认为1, 如果统计出的col-rowspan没有变化, 不处理
                        if ($table.data('col-rowspan') != 1) {
                            $table.data('col-td').attr('rowspan', $table.data('col-rowspan'));
                        }
                        // 保存第一次出现不同内容的td, 和其内容, 重置col-rowspan
                        $table.data('col-td', $td);
                        $table.data('col-content', $td.html());
                        $table.data('col-rowspan', 1);
                    }
                }
            });
        }
        // 同样是个private函数 清理内存之用
        function dispose($table) {
            $table.removeData();
        }
    },
    //组合数组
    doExchange: function (doubleArrays) {
        var len = doubleArrays.length;
        if (len >= 2) {
            var arr1 = doubleArrays[0];
            var arr2 = doubleArrays[1];
            var len1 = doubleArrays[0].length;
            var len2 = doubleArrays[1].length;
            var newlen = len1 * len2;
            var temp = new Array(newlen);
            var index = 0;
            for (var i = 0; i < len1; i++) {
                for (var j = 0; j < len2; j++) {
                    temp[index] = arr1[i] + "," + arr2[j];
                    index++;
                }
            }
            var newArray = new Array(len - 1);
            newArray[0] = temp;
            if (len > 2) {
                var _count = 1;
                for (var i = 2; i < len; i++) {
                    newArray[_count] = doubleArrays[i];
                    _count++;
                }
            }
            return attributeControl.doExchange(newArray);
        }
        else {
            return doubleArrays[0];
        }
    }
};


function bindInputAddEvent()
{
    $(".attributeTableAddBtn").unbind("click");
    //SKU信息
    $(".attributeTableAddBtn").bind("click", function () {
        var attId = $(this).attr("attr-data");
        var attVal = $("#attributeInput_"+attId).val();

        if(attVal!=null)
        {
            attVal = attVal.replace(/(^\s*)|(\s*$)/g, "");
        }
        var attributeValueRegex = /^[\u0391-\uFFE5a-zA-Z0-9\【\】\(\)\（\）\+\-\s\.]{1,25}$/;
        if(!attributeValueRegex.test(attVal))
        {
            $.message({
                message: "必须由1-25位的组成,不能包含特殊符号",
                type: 'error'
            });
            return;
        }
        var isFind = false;
        $("#rpai"+attId).find('li').each(function() {
            var attLiText = $(this).text();
            if(attLiText!=null) {
                attLiText = attLiText.replace(/(^\s*)|(\s*$)/g, "");
            }

            if(attLiText==attVal)
            {
                isFind=true;
            }
        });

        if(!isFind) {
            $("#rpai" + attId).append("<li class='rpai_li'><label><input  type='checkbox' class='chcBox_Width' value='" + attVal + "' />" + attVal + "</label></li>");
            bindAttLabelEvent();
            $("#attributeInput_"+attId).val("");
        }else{
            $.message({
                message: "已存在该属性",
                type: 'error'
            });
        }
    });
}
function initAttributes(categoryId,callback,drawCallback)
{

    loading.showLoading({
        type:6,
        tip:"加载中..."
    });
    $.ajax({
        type: "GET",
        url: basePath+"/api/shop/product/approve/"+categoryId+"/attributes",
        contentType: "application/json;charset=utf-8",
        data: null,
        dataType: "json",
        success: function (result) {
            $(".rp_attr_div").html("");

            if(result!=null&&result.data!=null&&result.data.length>0)
            {
                var attributesHtml="";
                for(var i=0;i<result.data.length;i++)
                {
                    var attribute = result.data[i];
                    var attributeHtmls="<div class='item' style='clear:both;text-align: left;' >";
                    attributeHtmls+="<div style='padding-left: 6%;'>";
                    attributeHtmls+=" <a>"+attribute.attributeName+"</a>";
                    attributeHtmls+=" <input type='text' id='attributeInput_"+attribute.id+"' name='attributeValue' class='releaseProductInputText'  style='width:200px;' tabindex='5'  maxlength='25' placeholder='请输入自定义值' />";
                    attributeHtmls+="  <input type='button'  class='releaseProductButton attributeTableAddBtn' value='添加' attr-data='"+attribute.id+"'   />";
                    attributeHtmls+=" </div> ";
                    attributeHtmls+=" <div class='attributeList'> ";
                    attributeHtmls+=" <ul class='rp_attr_kt' style='display:none' ><li>"+attribute.attributeName+"</li></ul> ";
                    attributeHtmls+=" <div class='field' style='width:100%'> ";
                    attributeHtmls+=" <ul class='rpai"+i+" rpai_key_ul' id='rpai"+attribute.id+"' style='float:left;margin-left: 20px;' attr-group-name='"+attribute.attributeName+"' >";
                    if(attribute.values!=null&&attribute.values.length>0) {
                        for(var j=0;j<attribute.values.length;j++) {
                            var attributeValue = attribute.values[j];
                            if(attribute.attributeType==2) {
                                attributeHtmls += " <li class='rpai_li' att-value-name='"+attributeValue.attributeValue+"'><label><input  type='checkbox' class='chcBox_Width' value='" + attributeValue.attributeValue + "' /><div style='background-color:"+attributeValue.rgbColor+";width:15px;height:15px;float:right'></div>" + attributeValue.attributeValue + "</label></li>";
                            }else{
                                attributeHtmls += " <li class='rpai_li' att-value-name='"+attributeValue.attributeValue+"'><label><input  type='checkbox' class='chcBox_Width' value='" + attributeValue.attributeValue + "' />" + attributeValue.attributeValue + "</label></li>";
                            }
                        }
                    }
                    attributeHtmls+="</ul>";
                    attributeHtmls+="<div class='clearfloat'></div>";
                    attributeHtmls+="</div>";
                    attributeHtmls+="</div>";
                    attributeHtmls+="</div>";

                    attributesHtml+=attributeHtmls;
                }

                //SKU表格
                attributesHtml+="<div class='item' id=\"tspSkuAttributeTableDiv\" style='clear:both;text-align: left;' >";
                attributesHtml+="<div class='sku_attribute_list' style='padding-left: 6%;'>";
                attributesHtml+="<div id='tspSkuAttributeTable'></div>  ";
                attributesHtml+="</div>";
                attributesHtml+="</div>";
                $(".rp_attr_div").html(attributesHtml);

                bindInputAddEvent();
            }

            if(drawCallback!=null)
            {
                drawCallback();
            }
        },
        complete:function(data,status){
            bindAttLabelEvent();

            loading.hideLoading();
            if(callback!=null) {
                callback();
            }
        }
    });
}

function clearSkuTable()
{
    $("#tspSkuAttributeTable").html("");
}
