/**
 * 商品SKU统计
 */
layui.define(['layer','echarts'],function (exports) {
    var obj = {
        draw:function() {
            var $ = layui.jquery;
            var layer = layui.layer;
            var productSkuStatisticPanelObj = $(".productSkuStatisticPanel");
            if(productSkuStatisticPanelObj!=null&&productSkuStatisticPanelObj.html()!=null&&productSkuStatisticPanelObj.html()!="") {
                $.ajax({
                    url: basePath+"/productSkuStatistic/queryTotalAndTodayAndCurrentMonthAndCurrentYear",
                    contentType: "application/json; charset=utf-8",
                    type: 'POST',
                    data: JSON.stringify({}),
                    success: function (data) {
                        if(data.code==0)
                        {
                            layer.msg(data.msg);
                            return false;
                        }

                        $(".userStatisticPanelTotal").html(data.data.total);
                        $(".userStatisticPanelOther").html("<small>今日新增:</small>"+data.data.todayCount+"<small>,本月新增:</small>"+data.data.curMonthCount+"<small>,本年新增:</small>"+data.data.curYearCount);
                    },
                    complete: function () {
                    }
                });
            }
        }
    };
    exports("productSkuStatistic",obj);
});