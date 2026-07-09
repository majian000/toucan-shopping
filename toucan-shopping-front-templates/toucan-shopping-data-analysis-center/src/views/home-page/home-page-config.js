import * as echarts from "echarts";

// ==================== 左侧图表 ====================

// 柱状图1 - 类目销售额
const BAR_OPTION1 = {
  title: {
    text: '类目销售额 TOP7',
    left: 'center',
    top: '10px',
    textStyle: {
      color: 'rgba(255,255,255)',
      fontSize: '16px',
    },
  },
  color: ['#2f89cf'],
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow',
    },
    formatter: function(params) {
      return params[0].name + '<br/>销售额：¥' + params[0].value + ' 万元';
    }
  },
  grid: {
    left: '0%',
    right: '5%',
    bottom: '4%',
    containLabel: true,
  },
  xAxis: {
    type: 'category',
    data: [
      '服装鞋包',
      '数码电子',
      '家居用品',
      '美妆护肤',
      '食品饮料',
      '运动户外',
      '母婴用品',
    ],
    axisTick: {
      show: false,
    },
    axisLabel: {
      color: 'rgba(255,255,255,.6)',
      fontSize: '11px',
      rotate: 15,
    },
  },
  yAxis: {
    type: 'value',
    name: '万元',
    nameTextStyle: {
      color: 'rgba(255,255,255,.6)',
      fontSize: '12px',
    },
    axisTick: {
      show: false,
    },
    axisLabel: {
      color: 'rgba(255,255,255,.6)',
      fontSize: '12px',
    },
    axisLine: {
      lineStyle: {
        color: 'rgba(255,255,255,.1)',
        width: 2,
      },
    },
    splitLine: {
      lineStyle: {
        color: 'rgba(255,255,255,.1)',
      },
    },
  },
  series: [
    {
      data: [520, 480, 360, 310, 280, 220, 190],
      type: 'bar',
      barWidth: "35%",
      itemStyle: {
        borderRadius: [5, 5, 0, 0],
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#2f89cf' },
          { offset: 1, color: '#0b3d6b' }
        ])
      },
      label: {
        show: true,
        position: 'top',
        color: '#2f89cf',
        fontSize: '10px',
        formatter: '{c}'
      }
    },
  ],
}

// 折线图1 - 月度销售额趋势
const LINE_OPTION1 = {
  color: ["#ed3f35", "#00f2f1"],
  title: {
    text: '月度销售额趋势',
    textStyle: {
      color: 'rgba(255,255,255)',
      fontSize: '16px',
    },
    left: 'center',
  },
  tooltip: {
    trigger: 'axis',
    formatter: function(params) {
      let result = params[0].axisValue + '<br/>';
      params.forEach(function(item) {
        result += item.marker + item.seriesName + '：¥' + item.value + ' 万元<br/>';
      });
      return result;
    }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    show: true,
    borderColor: "#012f4a",
    containLabel: true
  },
  legend: {
    right: '10px',
    top: '25px',
    textStyle: {
      color: "rgba(255,255,255,.5)",
      fontSize: "12"
    }
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
    axisLabel: {
      color: 'rgba(255,255,255,.6)',
      fontSize: '12px',
    },
    axisTick: {
      show: false,
    },
  },
  yAxis: {
    type: 'value',
    name: '万元',
    axisLabel: {
      color: 'rgba(255,255,255,.6)',
      fontSize: '12px',
    },
    splitLine: {
      lineStyle: {
        color: 'rgba(255,255,255,.1)',
      }
    }
  },
  series: [
    {
      name: "2023年",
      type: 'line',
      smooth: true,
      showSymbol: false,
      lineStyle: {
        width: 2,
      },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: "rgba(237, 63, 53, 0.3)" },
          { offset: 1, color: "rgba(237, 63, 53, 0.02)" }
        ])
      },
      data: [320, 280, 350, 380, 420, 450, 480, 460, 500, 520, 580, 620],
    },
    {
      name: "2024年",
      type: 'line',
      smooth: true,
      showSymbol: false,
      lineStyle: {
        width: 2,
      },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: "rgba(0, 242, 241, 0.3)" },
          { offset: 1, color: "rgba(0, 242, 241, 0.02)" }
        ])
      },
      data: [400, 360, 430, 470, 510, 540, 580, 560, 610, 630, 700, 750],
    },
  ],
}

// 饼图1 - 支付方式分布
const PIE_OPTION1 = {
  color: ["#006cff", "#60cda0", "#ed8884", "#ff9f7f", "#9fe6b8", "#32c5e9"],
  title: {
    text: '支付方式分布',
    textStyle: {
      color: 'rgba(255,255,255)',
      fontSize: '16px',
    },
    left: 'center',
  },
  tooltip: {
    trigger: 'item',
    formatter: "{a} <br/>{b}: {c} ({d}%)"
  },
  legend: {
    bottom: '10px',
    left: 'center',
    textStyle: {
      color: "rgba(255,255,255,.5)",
      fontSize: "11"
    }
  },
  series: [
    {
      name: '支付方式',
      type: 'pie',
      radius: ['40%', '65%'],
      center: ["50%", "45%"],
      avoidLabelOverlap: false,
      label: {
        show: true,
        position: 'outside',
        color: 'rgba(255,255,255,.7)',
        fontSize: '10px',
        formatter: '{b}\n{d}%'
      },
      labelLine: {
        lineStyle: {
          color: 'rgba(255,255,255,.3)'
        }
      },
      data: [
        { value: 45, name: "微信支付" },
        { value: 30, name: "支付宝" },
        { value: 12, name: "银行卡" },
        { value: 8, name: "花呗分期" },
        { value: 3, name: "京东白条" },
        { value: 2, name: "其他" }
      ]
    }
  ]
}

// ==================== 右侧图表 ====================

// 柱状图2 - 热销商品TOP5
const BAR_OPTION2 = {
  title: {
    text: '热销商品 TOP5',
    left: 'center',
    top: '10px',
    textStyle: {
      color: 'rgba(255,255,255)',
      fontSize: '16px',
    },
  },
  grid: {
    left: '3%',
    right: '8%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'value',
    name: '万元',
    nameTextStyle: {
      color: 'rgba(255,255,255,.6)',
      fontSize: '11px',
    },
    axisTick: {
      show: false,
    },
    axisLabel: {
      show: false,
    },
    splitLine: {
      show: false,
    }
  },
  yAxis: [
    {
      type: 'category',
      data: ["iPhone 15 Pro", "戴森吸尘器V15", "SK-II神仙水", "Nike Air Max", "茅台飞天53°"],
      axisTick: {
        show: false,
      },
      axisLabel: {
        color: 'rgb(255,255,255)',
        fontSize: '11px',
      },
      axisLine: {
        show: false
      }
    },
    {
      data: [280, 240, 200, 180, 160],
      inverse: true,
      axisLine: {
        show: false
      },
      axisTick: {
        show: false
      },
      axisLabel: {
        color: "#fff",
        fontSize: '11px',
      }
    }
  ],
  series: [
    {
      name: "条",
      type: "bar",
      barWidth: 15,
      yAxisIndex: 0,
      itemStyle: {
        color: (params) => ["#F57474", "#1089E7", "#F8B448", "#56D0E3", "#8B78F6"][params.dataIndex],
        borderRadius: [0, 10, 10, 0],
      },
      label: {
        position: 'right',
        show: true,
        formatter: '{c}万',
        color: "#fff",
        fontSize: '11px',
      },
      data: [280, 240, 200, 180, 160],
    },
    {
      name: "框",
      type: "bar",
      barGap: 0,
      barCategoryGap: 50,
      barWidth: 15,
      yAxisIndex: 1,
      itemStyle: {
        color: "none",
        borderColor: "#00c1de",
        borderWidth: 2,
        borderRadius: [0, 10, 10, 0],
      },
      data: [300, 300, 300, 300, 300],
    }
  ]
}

// 折线图2 - 用户访问量趋势
const LINE_OPTION2 = {
  color: ["#1089E7", "#00d887"],
  title: {
    text: '平台访问量趋势',
    left: 'center',
    textStyle: {
      color: 'rgba(255,255,255)',
      fontSize: '16px',
    },
  },
  tooltip: {
    trigger: 'axis',
    formatter: function(params) {
      let result = params[0].axisValue + '<br/>';
      params.forEach(function(item) {
        result += item.marker + item.seriesName + '：' + item.value + ' 人次<br/>';
      });
      return result;
    }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    show: true,
    borderColor: "#012f4a",
    containLabel: true
  },
  legend: {
    right: '10px',
    top: '25px',
    textStyle: {
      color: "rgba(255,255,255,.5)",
      fontSize: "12"
    }
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: ['00:00','02:00','04:00','06:00','08:00','10:00','12:00','14:00','16:00','18:00','20:00','22:00'],
    axisLabel: {
      color: 'rgba(255,255,255,.6)',
      fontSize: '11px',
    },
    axisTick: { show: false },
  },
  yAxis: {
    type: 'value',
    name: '人次',
    nameTextStyle: {
      color: 'rgba(255,255,255,.6)',
      fontSize: '11px',
    },
    axisLabel: {
      color: 'rgba(255,255,255,.6)',
      fontSize: '11px',
    },
    splitLine: {
      lineStyle: {
        color: 'rgba(255,255,255,.1)',
      }
    }
  },
  series: [
    {
      name: "PC端",
      type: "line",
      showSymbol: false,
      smooth: true,
      lineStyle: {
        color: "#1089E7",
        width: 2
      },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: "rgba(16, 137, 231, 0.3)" },
          { offset: 1, color: "rgba(16, 137, 231, 0.02)" }
        ])
      },
      symbol: "circle",
      symbolSize: 4,
      itemStyle: {
        color: "#1089E7",
      },
      data: [20, 10, 5, 30, 120, 280, 320, 260, 350, 420, 380, 180]
    },
    {
      name: "移动端",
      type: "line",
      showSymbol: false,
      smooth: true,
      lineStyle: {
        color: "#00d887",
        width: 2
      },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: "rgba(0, 216, 135, 0.3)" },
          { offset: 1, color: "rgba(0, 216, 135, 0.02)" }
        ])
      },
      symbol: "circle",
      symbolSize: 4,
      itemStyle: {
        color: "#00d887",
      },
      data: [30, 15, 8, 50, 180, 400, 450, 380, 500, 600, 520, 260]
    }
  ]
}

// 饼图2-玫瑰图 - 用户地域分布
const PIE_OPTION2 = {
  color: [
    "#006cff",
    "#60cda0",
    "#ed8884",
    "#ff9f7f",
    "#0096ff",
    "#9fe6b8",
    "#32c5e9",
    "#1d9dff"
  ],
  title: {
    text: '用户地域分布',
    textStyle: {
      color: 'rgba(255,255,255)',
      fontSize: '16px',
    },
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b} : {c} 万人 ({d}%)'
  },
  legend: {
    bottom: "0%",
    itemWidth: 10,
    itemHeight: 10,
    textStyle: {
      color: "rgba(255,255,255,.5)",
      fontSize: "11"
    }
  },
  series: [
    {
      name: '用户地域',
      type: 'pie',
      radius: [20, 85],
      center: ['50%', '50%'],
      roseType: "radius",
      label: {
        color: "inherit",
        fontSize: '10px',
      },
      itemStyle: {
        borderRadius: 2
      },
      labelLine: {
        length: 5,
        length2: 8,
      },
      data: [
        { value: 42, name: "广东" },
        { value: 38, name: "浙江" },
        { value: 35, name: "江苏" },
        { value: 30, name: "上海" },
        { value: 28, name: "北京" },
        { value: 25, name: "四川" },
        { value: 22, name: "山东" },
        { value: 20, name: "湖北" }
      ]
    }
  ]
}

export { BAR_OPTION1, BAR_OPTION2, LINE_OPTION1, LINE_OPTION2, PIE_OPTION1, PIE_OPTION2 }
