import React from 'react'
import ReactEcharts from 'echarts-for-react'
import store from '../store'

export default class DownloadSpeed extends React.Component {
  renderOption = () => {
    return {
      title: { text: '下行速率' },
      tooltip: { trigger: 'axis' },
      legend: { data: store.downloadStatMetric.legendList, orient: "horizontal", x: "center", y: "bottom" },
      grid: { left: '3%', right: '4%', bottom: '15%', containLabel: true },
      toolbox: { feature: { saveAsImage: {} } },
      xAxis: { type: 'category', boundaryGap: false, data: store.downloadStatMetric.timeList },
      yAxis: { type: 'value' },
      series: store.downloadStatMetric.series
    }
  }
  render() {
    return (
      <ReactEcharts option={this.renderOption() as any} />
    )
  }
}
