import * as echarts from "echarts";

// 柱状图1-相关配置
const BAR_OPTION1 = {
  title: {
    text: '柱状图-就业行业',
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
  },
  grid: {
    left: '0%',
    right: '0%',
    bottom: '4%',
    containLabel: true,
  },
  xAxis: {
    type: 'category',
    data: [
      '旅游行业',
      '教育培训',
      '游戏行业',
      '医疗行业',
      '电商行业',
      '社交行业',
      '金融行业',
    ],
    // 不展示x轴刻度
    axisTick: {
      show: false,
    },
    axisLabel: {
      color: 'rgba(255,255,255,.6)',
      fontSize: '12px',
    },
  },
  yAxis: {
    type: 'value',
    // 不展示y轴刻度
    axisTick: {
      show: false,
    },
    // 设置y轴文本颜色
    axisLabel: {
      color: 'rgba(255,255,255,.6)',
      fontSize: '12px',
    },
    // y轴的线条改为了 2像素
    axisLine: {
      lineStyle: {
        color: 'rgba(255,255,255,.1)',
        width: 2,
      },
    },
    // 设置y轴分割线颜色
    splitLine: {
      lineStyle: {
        color: 'rgba(255,255,255,.1)',
      },
    },
  },
  series: [
    {
      data: [200, 300, 300, 900, 1500, 1200, 600],
      type: 'bar',
      // 柱子宽度占比
      barWidth: "35%",
      itemStyle: {
        // 修改柱子圆角
        borderRadius : 5
      }
    },
  ],
}

// 柱状图2-相关配置
const BAR_OPTION2 = {
  title: {
    text: '柱状图-技术占比',
    left: 'center',
    top: '10px',
    textStyle: {
      color: 'rgba(255,255,255)',
      fontSize: '16px',
    },
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'value',
    // 不展示x轴刻度
    axisTick: {
      show: false,
    },
    axisLabel: {
      show: false,
      color: 'rgb(255,255,255)',
      fontSize: '12px',
    },
    splitLine: {
      show: false,
    }
  },
  // 显示两条y轴的方式，数组，一条y轴，通过对象形式
  yAxis: [
    {
      type: 'category',
      data: ["HTML5", "CSS3", "javascript", "VUE", "NODE"],
      axisTick: {
        show: false,
      },
      axisLabel: {
        color: 'rgb(255,255,255)',
        fontSize: '12px',
      },
      // 不展示轴
      axisLine: {
        show: false
      }
    },
    {
    data: [702, 350, 610, 793, 664],
    inverse: true,
    // 不显示y轴的线
    axisLine: {
      show: false
    },
    // 不显示刻度
    axisTick: {
      show: false
    },
    // 把刻度标签里面的文字颜色设置为白色
    axisLabel: {
      color: "#fff"
    }
  }],
  series: [
    {
      name: "条",
      type: "bar",
      barWidth: 15, // 柱子宽度
      yAxisIndex: 0,  // 控制不同系列的柱状图位置关的操作，比如将这个放入下面那个上，具体可以看代码效果
      itemStyle: {
        // 通过回调的方式设置不同的颜色
        color: (params)=>["#1089E7", "#F57474", "#56D0E3", "#F8B448", "#8B78F6"][params.dataIndex],
        borderRadius: 20,
      },
      label: {
        position: 'inside' , // 显示在柱子内部
        show: true,
        formatter: '{c}%',
        color: "#fff"
      },
      data: [70, 34, 60, 78, 69],
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
        borderWidth: 3,
        borderRadius: 15
      },
      data: [100, 100, 100, 100, 100],
    }
  ]
}

// 折线图1-相关配置
const LINE_OPTION1 = {
  // 整体控制折线的样式
  color: ["#00f2f1", "#ed3f35"],
  title: {
    text: '折线图-人员变化',
    textStyle: {
      color: 'rgba(255,255,255)',
      fontSize: '16px',
    },
    // 控制标题的位置
    left: 'center',
  },
  tooltip: {
    trigger: 'axis'
  },
  // 控制图形的位置
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    show: true, // 显示边框
    borderColor: "#012f4a", // 边框颜色
    containLabel: true // 包含刻度文字在内
  },
  legend: {
    right: '10px',
    top: '25px',
    // 控制图例文本相关的样式
    textStyle: {
      color: 'rbg(36, 75, 145)',
      fontSize: '12px',
    }
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: [
        "1月",
        "2月",
        "3月",
        "4月",
        "5月",
        "6月",
        "7月",
        "8月",
        "9月",
        "10月",
        "11月",
        "12月"
    ],
    // 控制刻度相关的显示效果
    axisTick: {
      show: false,
    },
    // 控制文本相关的显示效果
    axisLabel: {
      color: "#4c9bfd"
    },
    axisLine: {
      show: false
    }
  },
  yAxis: {
    type: 'value',
    // 控制文本相关的显示效果
    axisLabel: {
      color: "#4c9bfd"
    },
    // 设置y轴分割线相关样式
    splitLine: {
      lineStyle: {
        color: "#012f4a"
      }
    },
    axisLine: {
      show: false
    }
  },
  series: [
    {
      name: "新增粉丝", // 年份
      type: "line",
      // 控制折线平滑显示
      smooth: true,
      data: [
        40, 64, 191, 324, 290, 330, 310, 213, 180, 200, 180, 79
      ]
    },
    {
      name: "新增游客", // 年份
      type: "line",
      // 控制折线平滑显示
      smooth: true,
      data: [
        143, 131, 165, 123, 178, 21, 82, 64, 43, 60, 19, 34
      ]
    }
  ]
}

// 折线图2-相关配置
const LINE_OPTION2 = {
  // 整体控制折线的样式
  color: ["#00f2f1", "#ed3f35"],
  title: {
    text: '折线图-播放量',
    textStyle: {
      color: 'rgba(255,255,255)',
      fontSize: '16px',
    },
    // 控制标题的位置
    left: 'center',
  },
  tooltip: {
    trigger: 'axis'
  },
  // 控制图形的位置
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true // 包含刻度文字在内
  },
  legend: {
    left: 'center',
    top: '25px',
    // 控制图例文本相关的样式
    textStyle: {
      color: 'rbg(36, 75, 145)',
      fontSize: '12px',
    }
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: [
      "01",
      "02",
      "03",
      "04",
      "05",
      "06",
      "07",
      "08",
      "09",
      "10",
      "11",
      "12",
      "13",
      "14",
      "15",
      "16",
      "17",
      "18",
      "19",
      "20",
      "21",
      "22",
      "23",
      "24",
      "25",
      "26",
      "26",
      "28",
      "29",
      "30"
    ],
    // 控制刻度相关的显示效果
    axisTick: {
      show: false,
    },
    // 控制文本相关的显示效果
    axisLabel: {
      color: "rgba(255,255,255,.5)"
    },
    axisLine: {
      show: false
    }
  },
  yAxis: {
    type: 'value',
    // 控制文本相关的显示效果
    axisLabel: {
      color: "rgba(255,255,255,.5)"
    },
    // 设置y轴分割线相关样式
    splitLine: {
      lineStyle: {
        color: "rgba(255,255,255,.1)"
      }
    },
  },
  series: [
    {
      name: "邮件营销", // 年份
      type: "line",
      showSymbol: false,  // 不展示圆点
      smooth: true, // 控制折线平滑显示
      // 设置拐点 小圆点
      symbol: "circle",
      // 拐点大小
      symbolSize: 5,
      // 设置拐点颜色以及边框
      itemStyle: {
        color: "#0184d5",
        borderColor: "rgba(221, 220, 107, .1)",
        borderWidth: 12
      },
      data: [
        30,
          40,
          30,
          40,
          30,
          40,
          30,
          60,
          20,
          40,
          30,
          40,
          30,
          40,
          30,
          40,
          30,
          60,
          20,
          40,
          30,
          40,
          30,
          40,
          30,
          40,
          20,
          60,
          50,
          40
      ]
    },
    {
      name: "联盟广告", // 年份
      type: "line",
      showSymbol: false,  // 不展示圆点
      smooth: true, // 控制折线平滑显示
      lineStyle: {
        color: "#00d887",
        width: 2
      },
       // 展示区域颜色
      areaStyle: {
        color: new echarts.graphic.LinearGradient(
          0,
          0,
          0,
          1,
          [
            {
              offset: 0,
              color: "rgba(0, 216, 135, 0.4)"
            },
            {
              offset: 0.8,
              color: "rgba(0, 216, 135, 0.1)"
            }
          ],
          false
        ),
        shadowColor: "rgba(0, 0, 0, 0.1)"
      },
      // 设置拐点 小圆点
      symbol: "circle",
      // 拐点大小
      symbolSize: 5,
      // 设置拐点颜色以及边框
      itemStyle: {
        color: "#00d887",
        borderColor: "rgba(221, 220, 107, .1)",
        borderWidth: 12
      },
      data: [
        130,
        10,
        20,
        40,
        30,
        40,
        80,
        60,
        20,
        40,
        90,
        40,
        20,
        140,
        30,
        40,
        130,
        20,
        20,
        40,
        80,
        70,
        30,
        40,
        30,
        120,
        20,
        99,
        50,
        20
      ]
    }
  ]
}

// 饼图1-相关配置
const PIE_OPTION1 =  {
  // 每个条目的配置
  color: ["#065aab", "#066eab", "#0682ab", "#0696ab", "#06a0ab"],
  title: {
    text: '饼图-年龄分布',
    textStyle: {
      color: 'rgba(255,255,255)',
      fontSize: '16px',
    },
    // 控制标题的位置
    left: 'center',
  },
  tooltip: {
    trigger: 'item',
    // a 是 series 对应的name b 数据对应的name c 数据对应的value d 数据占的百分比
    formatter: "{a} <br/>{b}: {c} ({d}%)"
  },
  legend: {
    bottom: '10px',
    left: 'center',
    textStyle: {
      color: "rgba(255,255,255,.5)",
      fontSize: "12"
    }
  },
  series: [
    {
      name: '年龄分布',
      type: 'pie',
      // 设置 饼图的半径
      radius: ['40%', '60%'],
      // 设置圆心位置
      center: ["50%", "45%"],
      avoidLabelOverlap: false,
      label: {
        show: false,
        position: 'center'
      },
      labelLine: {
        show: false
      },
      data: [
        { value: 1, name: "0岁以下" },
        { value: 4, name: "20-29岁" },
        { value: 2, name: "30-39岁" },
        { value: 2, name: "40-49岁" },
        { value: 1, name: "50岁以上" }
      ]
    }
  ]
}

// 饼图2-相关配置
const PIE_OPTION2 = {
  // 每个条目的顔色配置
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
    text: '饼图地图-地区分布',
    textStyle: {
      color: 'rgba(255,255,255)',
      fontSize: '16px',
    },
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b} : {c} ({d}%)'
  },
  legend: {
    bottom: "0%",
    // 设置图例, 前面那块的长宽
    itemWidth: 10,
    itemHeight: 10,
    textStyle: {
      color: "rgba(255,255,255,.5)",
      fontSize: "12"
    }
  },
  series: [
    {
      name: '地区分布',
      type: 'pie',
      radius: [20, 90],
      center: ['50%', '50%'],
      roseType: "radius",
      label: {
        // 让文本显示的颜色与系列色一样
        color: "inherit",
      },
      itemStyle: {
        // 每块扇形的圆角
        borderRadius: 0
      },
      // 设置视觉引导线的样式
      labelLine: {
        length: 6,  // 第一段的长度
        length2: 8, // 第二段的长度
      },
      data: [
        { value: 20, name: "云南" },
        { value: 26, name: "北京" },
        { value: 24, name: "山东" },
        { value: 25, name: "河北" },
        { value: 20, name: "江苏" },
        { value: 25, name: "浙江" },
        { value: 30, name: "四川" },
        { value: 42, name: "湖北" }
      ]
    }
  ]
}

export { BAR_OPTION1, BAR_OPTION2, LINE_OPTION1, LINE_OPTION2, PIE_OPTION1, PIE_OPTION2 }