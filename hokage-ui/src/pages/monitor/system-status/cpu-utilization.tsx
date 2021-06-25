import React from 'react'
import ReactEcharts from 'echarts-for-react'
import { observer } from 'mobx-react'
import store from '../store'

@observer
export default class CpuUtilization extends React.Component {

  renderOption = () => {
    return {
      title: { text: 'CPU利用率' },
      tooltip: { trigger: 'axis' },
      legend: { orient: "horizontal", x: "center", y: "bottom" },
      grid: { left: '3%', right: '4%', bottom: '10%', containLabel: true },
      toolbox: { feature: { saveAsImage: {} } },
      xAxis: { type: 'category', boundaryGap: false, data: store.metric.cpuStatMetric.timeList },
      yAxis: { type: 'value' },
      series: store.metric.cpuStatMetric.series
    }
  }
  render() {
    return (
      <ReactEcharts option={this.renderOption() as any} />
    )
  }
}
