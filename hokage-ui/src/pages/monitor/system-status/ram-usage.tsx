import React from 'react'
import ReactEcharts from 'echarts-for-react'
import store from '../store';

export default class RamUsage extends React.Component {
  renderOption = () => {
    return {
      title: { text: '内存使用量' },
      tooltip: { trigger: 'axis' },
      legend: { data: store.memStatMetric.legendList, orient: "horizontal", x: "center", y: "bottom" },
      grid: { left: '3%', right: '4%', bottom: '10%', containLabel: true },
      toolbox: { feature: { saveAsImage: {} } },
      xAxis: { type: 'category', boundaryGap: false, data: store.memStatMetric.timeList },
      yAxis: { type: 'value' },
      series: store.memStatMetric.series
    }
  }
  render() {
    return (
      <ReactEcharts option={this.renderOption() as any} style={{width: "400px"}} />
    )
  }
}
