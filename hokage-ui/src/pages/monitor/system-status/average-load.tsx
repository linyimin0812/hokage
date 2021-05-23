import React from 'react'
import ReactEcharts from 'echarts-for-react'

export default class AverageLoad extends React.Component {
  state = {
    AvgLoadOption:  {
      title: { text: 'CPU平均负载' },
      tooltip: { trigger: 'axis' },
      legend: { data: ['1_min_avg', '5_min_avg', '15_min_avg'], orient: "horizontal", x: "center", y: "bottom" },
      grid: { left: '3%', right: '4%', bottom: '10%', containLabel: true },
      toolbox: { feature: { saveAsImage: {} } },
      xAxis: { type: 'category', boundaryGap: false, data: ['20:00', '20:10', '20:20', '20:30', '20:40', '20:50', '21:00'] },
      yAxis: { type: 'value' },
      series: [
        { name: '1_min_avg', type: 'line', stack: '总量', data: [150, 232, 201, 154, 190, 330, 410] },
        { name: '5_min_avg', type: 'line', stack: '总量', data: [320, 332, 301, 334, 390, 330, 320] },
        { name: '15_min_avg', type: 'line', stack: '总量', data: [820, 932, 901, 934, 1290, 1330, 1320] }
      ]
    }
  }
  render() {
    return (
      <ReactEcharts option={this.state.AvgLoadOption as any} />
    )
  }
}
