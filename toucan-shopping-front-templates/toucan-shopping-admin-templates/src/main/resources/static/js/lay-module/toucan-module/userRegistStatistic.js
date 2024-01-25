/**
 * 用户注册统计
 */
layui.define(['layer','echarts'],function (exports) {
    var obj = {
        draw:function() {
            var userStatisticPanelObj = $(".userStatisticPanel");
            if(userStatisticPanelObj!=null&&userStatisticPanelObj.html()!=null&&userStatisticPanelObj.html()!="") {
                $.ajax({
                    url: basePath+"/userStatistic/queryTotalAndTodayAndCurrentMonthAndCurrentYear",
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
    exports("userRegistStatistic",obj);
});