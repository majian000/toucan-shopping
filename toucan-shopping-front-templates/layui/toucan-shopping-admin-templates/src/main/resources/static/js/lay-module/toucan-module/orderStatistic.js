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

                        $(".orderStatisticPanelTotal").html(data.data.totalMoney);
                        $(".orderStatisticPanelOther").html("<small>今日金额:</small>"+data.data.todayMoney+"<small>,本月金额:</small>"+data.data.curMonthMoney+"<small>,本年金额:</small>"+data.data.curYearMoney);
                    },
                    complete: function () {
                    }
                });
            }
        }
    };
    exports("orderStatistic",obj);
});