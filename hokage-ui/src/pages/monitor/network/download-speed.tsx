import React from 'react'
import { Line } from '@antv/g2plot'
import { MetricMetaVO } from '../../../axios/action/monitor/monitor-type'

type DownloadSpeedProps = {
  id: string | number,
  data: MetricMetaVO[]
}

export default class DownloadSpeed extends React.Component<DownloadSpeedProps> {
  componentDidMount() {
    const {id, data} = this.props
    this.line = new Line(`download-load-${id}`, {
      data: data,
      xField: 'time',
      yField: 'value',
      seriesField: 'category',
      height: 300,
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
      <div id={`download-load-${id}`} />
    )
  }
}
