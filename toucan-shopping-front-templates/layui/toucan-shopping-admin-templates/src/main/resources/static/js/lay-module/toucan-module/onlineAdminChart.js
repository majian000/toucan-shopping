/**
 * 管理员在线统计
 */
layui.define(['layer','echarts'],function (exports) {
    var obj = {
        echartsRecords:null,
        resize:function(){
            if(echartsRecords!=null) {
                echartsRecords.resize();
            }
        },
        draw:function() {
            var $ = layui.jquery;
            var layer = layui.layer;
            var echarts = layui.echarts;
            var onlineUserChartLoadding;

            function drawOnlineUserCharts(optionRecords) {
                onlineUserChartLoadding = layer.load();
                $.ajax({
                    url: basePath+"/online/user/chart/queryAppLoginUserCountList",
                    contentType: "application/json; charset=utf-8",
                    type: 'POST',
                    data: JSON.stringify({}),
                    success: function (data) {
                        layer.close(onlineUserChartLoadding);
                        if (data.code == 0) {
                            layer.msg(data.msg);
                            return false;
                        }
                        optionRecords.xAxis.data = [];
                        optionRecords.series[0].data = [];
                        if (data.data != null && data.data.length > 0) {
                            for (var i = 0; i < data.data.length; i++) {
                                var obj = data.data[i];
                                optionRecords.xAxis.data.push(obj.appName);
                                optionRecords.series[0].data.push(obj.loginCount);
                            }
                        }
                        echartsRecords.clear();
                        echartsRecords.setOption(optionRecords);
                    },
                    complete: function () {
                        layer.close(onlineUserChartLoadding);
                    }
                });
            }


            var onlineUserChartsObj = $("#online-user-charts-dev");
            if(onlineUserChartsObj!=null&&onlineUserChartsObj.html()!=null&&onlineUserChartsObj.html()!="") {
                /**
                 * 报表功能
                 */
                echartsRecords = echarts.init(document.getElementById('online-user-charts'));
                var optionRecords = {
                    xAxis: {
                        type: 'category',
                        data: []
                    },
                    yAxis: {
                        type: 'value',
                        minInterval: 1 //只显示整数
                    },
                    series: [
                        {
                            type: 'bar',
                            data: [],
                            showBackground: true,
                            backgroundStyle: {
                                color: 'rgba(111, 162, 135, 0.2)'
                            },
                            itemStyle: {
                                normal: {
                                    //这里是颜色
                                    color: function(params) {
                                        //注意，如果颜色太少的话，后面颜色不会自动循环，最好多定义几个颜色
                                        var colorList = ['#00A3E0','#FFA100', '#ffc0cb', '#CCCCCC', '#BBFFAA','#749f83', '#ca8622'];
                                        return colorList[params.dataIndex]
                                    }
                                }
                            }
                        }
                    ],
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'cross',
                            label: {
                                backgroundColor: '#6a7985'
                            }
                        }
                    }
                };

                $("#refershOnlineUserCharts").bind("click", function () {
                    drawOnlineUserCharts(optionRecords);
                });

                drawOnlineUserCharts(optionRecords);
            }
        }
    };
    exports("onlineAdminChart",obj);
});