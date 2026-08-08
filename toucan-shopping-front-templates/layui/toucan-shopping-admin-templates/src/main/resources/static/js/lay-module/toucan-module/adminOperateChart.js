/**
 * 管理员操作统计
 */
layui.define(['layer','echarts'],function (exports) {
    var obj = {
        operateEchartsRecords:null,
        resize:function(){
            if(operateEchartsRecords!=null) {
                operateEchartsRecords.resize();
            }
        },
        draw:function() {
            var $ = layui.jquery;
            var layer = layui.layer;
            var echarts = layui.echarts;
            var operateChartLoadding;

            /**
             *  操作统计
             */
            function drawOperateCharts(optionRecords) {
                operateChartLoadding = layer.load();
                $.ajax({
                    url: basePath+"/operate/log/chart/queryOperateChart",
                    contentType: "application/json; charset=utf-8",
                    type: 'POST',
                    data: JSON.stringify({}),
                    success: function (data) {
                        layer.close(operateChartLoadding);
                        if (data.code == 0) {
                            layer.msg(data.msg);
                            return false;
                        }
                        optionRecords.legend.data=data.data.appNames;
                        optionRecords.xAxis.data = data.data.categorys;
                        optionRecords.series = [];
                        if (data.data != null && data.data.datas!=null && data.data.datas.length > 0) {
                            for (var i = 0; i < data.data.datas.length; i++) {
                                var obj = data.data.datas[i];
                                obj.name=obj.appName;
                                obj.type = "line";
                                obj.data = obj.values;
                                optionRecords.series.push(obj);
                            }
                        }
                        operateEchartsRecords.clear();
                        operateEchartsRecords.setOption(optionRecords);
                    },
                    complete: function () {
                        layer.close(operateChartLoadding);
                    }
                });
            }
            var operateChartsObj = $("#operate-charts-dev");
            if(operateChartsObj!=null&&operateChartsObj.html()!=null&&operateChartsObj.html()!="") {
                /**
                 * 操作报表功能
                 */
                operateEchartsRecords = echarts.init(document.getElementById('operate-charts'), 'walden');
                var operateOptionRecords = {
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data:[]
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    toolbox: {
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: []
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [

                    ]
                };

                drawOperateCharts(operateOptionRecords);
            }

        }
    };
    exports("adminOperateChart",obj);
});