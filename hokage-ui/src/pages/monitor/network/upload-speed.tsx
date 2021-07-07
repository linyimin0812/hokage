import React from 'react'
import { Line } from '@antv/g2plot'
import { MetricMetaVO } from '../../../axios/action/monitor/monitor-type'

type UploadSpeedProps = {
  id: string | number,
  data: MetricMetaVO[]
}

export default class UploadSpeed extends React.Component<UploadSpeedProps> {
  componentDidMount() {
    const {id, data} = this.props
    this.line = new Line(`upload-speed-${id}`, {
      data: data,
      xField: 'time',
      yField: 'value',
      seriesField: 'category',
      height: 300,
      yAxis: {
        title: { text: '单位(byte)' }
      },
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
        <h3>上传速率</h3>
        <div id={`upload-speed-${id}`} />
      </>
    )
  }
}
