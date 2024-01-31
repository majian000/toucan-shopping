/**
 * 订单统计
 */
layui.define(['layer','echarts'],function (exports) {
    var obj = {
        draw:function() {
            var $ = layui.jquery;
            var layer = layui.layer;
            var orderStatisticPanelObj = $(".orderStatisticPanel");
            if(orderStatisticPanelObj!=null&&orderStatisticPanelObj.html()!=null&&orderStatisticPanelObj.html()!="") {
                $.ajax({
                    url: basePath+"/orderStatistic/queryTotalAndTodayAndCurrentMonthAndCurrentYear",
                    contentType: "application/json; charset=utf-8",
                    type: 'POST',
                    data: JSON.stringify({}),
                    success: function (data) {
                        if(data.code==0)
                        {
                            layer.msg(data.msg);
                            return false;
                        }

                        $(".orderStatisticPanelTotal").html(data.data.total);
                        // $(".orderStatisticPanelOther").html("<small>今日新增:</small>"+data.data.todayCount+"<small>,本月新增:</small>"+data.data.curMonthCount+"<small>,本年新增:</small>"+data.data.curYearCount);
                    },
                    complete: function () {
                    }
                });
            }
        }
    };
    exports("orderStatistic",obj);
});