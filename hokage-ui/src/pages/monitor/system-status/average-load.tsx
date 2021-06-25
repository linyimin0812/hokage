import React from 'react'
import ReactEcharts from 'echarts-for-react'
import store from '../store'

export default class AverageLoad extends React.Component {

  renderOption = () => {
    return {
      title: { text: 'CPU平均负载' },
      tooltip: { trigger: 'axis' },
      legend: { orient: "horizontal", x: "center", y: "bottom" },
      grid: { left: '3%', right: '4%', bottom: '10%', containLabel: true },
      toolbox: { feature: { saveAsImage: {} } },
      xAxis: { type: 'category', boundaryGap: false, data: store.metric.loadAvgMetric.timeList },
      yAxis: { type: 'value' },
      series: store.metric.loadAvgMetric.series
    }
  }

  render() {
    return (
      <ReactEcharts option={this.renderOption() as any} />
    )
  }
}
