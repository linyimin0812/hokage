import React from 'react'
import ReactEcharts from 'echarts-for-react'
import store from '../store'

export default class UploadSpeed extends React.Component {
  renderOption = () => {
    return {
      title: { text: '上行速率' },
      tooltip: { trigger: 'axis' },
      legend: { data: store.uploadStatMetric.legendList, orient: "horizontal", x: "center", y: "bottom" },
      grid: { left: '3%', right: '4%', bottom: '15%', containLabel: true },
      toolbox: { feature: { saveAsImage: {} } },
      xAxis: { type: 'category', boundaryGap: false, data: store.uploadStatMetric.timeList },
      yAxis: { type: 'value' },
      series: store.uploadStatMetric.series
    }
  }
  render() {
    return (
      <ReactEcharts option={this.renderOption() as any} />
    )
  }
}
