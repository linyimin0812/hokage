import React from 'react'
import { MetricMetaVO } from '../../../axios/action/monitor/monitor-type'
import { Line } from '@antv/g2plot'

type AverageLoadProps = {
  id: string | number,
  data: MetricMetaVO[]
}

export default class AverageLoad extends React.Component<AverageLoadProps> {
  componentDidMount() {
    const {id, data} = this.props
    this.line = new Line(`average-load-${id}`, {
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
        <h3>平均负载</h3>
        <div id={`average-load-${id}`} />
      </>
    )
  }
}
