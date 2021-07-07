import React from 'react'
import { Line } from '@antv/g2plot'
import { MetricMetaVO } from '../../../axios/action/monitor/monitor-type'

type RamUsageProps = {
  id: string | number,
  data: MetricMetaVO[]
}

export default class RamUsage extends React.Component<RamUsageProps> {
  componentDidMount() {
    const {id, data} = this.props
    this.line = new Line(`ram-usage-${id}`, {
      data: data,
      xField: 'time',
      yField: 'value',
      seriesField: 'category',
      height: 300,
      legend: {
        layout: 'horizontal',
        position: 'bottom',
        flipPage: false
      }
    })
    this.line.render()
  }

  line: Line | null = null

  render() {
    const {id, data} = this.props
    if (this.line) {
      this.line.changeData(data)
    }
    return (
      <>
        <h3>内存使用量</h3>
        <div id={`ram-usage-${id}`} />
      </>
    )
  }
}
