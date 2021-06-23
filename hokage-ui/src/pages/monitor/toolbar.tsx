import React from 'react'
import { Button, Col, DatePicker, Row, Switch } from 'antd'
import { ReloadOutlined } from '@ant-design/icons'
import moment, { Moment } from 'moment'
import { RangeValue } from 'rc-picker/lib/interface'

type ToolbarProp = {
  refreshData: (start?: number, end?: number) => void
}

type ToolbarState = {
  autoRefresh: boolean,
  start: number,
  end: number,

  interval: NodeJS.Timeout | null,
  timestamp: number,
  restSeconds: number,
}

// 自动更新时间间隔
const interval: number = 60 * 1000

export class Toolbar extends React.Component<ToolbarProp, ToolbarState> {

  state = {
    autoRefresh: false,
    start: 0,
    end: 0,
    interval: null,
    timestamp: 0,
    restSeconds: Math.floor(interval/1000),
  }

  componentDidMount() {
    this.checkRestSecondsInterval = setInterval(() => {
      const { autoRefresh, timestamp } = this.state
      if (!autoRefresh) {
        return
      }
      const restTimestamp = interval - ((moment().valueOf() - timestamp) % interval)
      const restSeconds = Math.floor(restTimestamp / 1000)
      this.setState({restSeconds: restSeconds })
    }, 1000)
  }

  componentWillUnmount() {
    const { interval } = this.state
    if (interval) {
      clearInterval(interval!)
    }
    clearInterval(this.checkRestSecondsInterval!)
  }

  private checkRestSecondsInterval: NodeJS.Timeout | null = null

  rangePickerChange = (values: RangeValue<Moment>, _: [string, string]) => {
    if (values && values[0] && values[1]) {
      this.setState({start: values[0]?.valueOf(), end: values[1]?.valueOf()})
    }
  }

  renderRangePicker = () => {
    return (
      <>
        <span>自定义时间: </span>
        <DatePicker.RangePicker ranges={{ 'Today': [moment(), moment()], }} showTime format="YYYY-MM-DD HH:mm" onChange={this.rangePickerChange} />
      </>
    )
  }

  onSwitchChange = (checked: boolean, _: MouseEvent): void => {
    if (checked) {
      const intervalTimeout: NodeJS.Timeout = setInterval(this.refreshData, interval)
      const timestamp = moment().valueOf()
      this.setState({start: 0, end: 0, autoRefresh: true, interval: intervalTimeout, timestamp: timestamp})
    } else {
      const { interval } = this.state
      if (interval) {
        clearInterval(interval!)
      }
      this.setState({autoRefresh: false})
    }
  }

  refreshData = () => {
    const { refreshData } = this.props
    const { start, end } = this.state
    refreshData(start, end)
  }

  render() {
    const { autoRefresh, restSeconds } = this.state
    return (
      <Row gutter={24} align="middle" style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', margin: '0px 0px', marginBottom: '10px', padding: '2px 2px' }}>
        <Col span={16} style={{padding: '0px 0px'}}>
          {this.renderRangePicker()}
        </Col>
        <Col span={8} style={{padding: '0px 0px'}}>
            <span style={{ float: 'right' }}>
              <span style={{paddingRight: '50px'}}>
                {autoRefresh && restSeconds > 0 ? `${restSeconds}秒后自动更新: ` : '自动更新: '}
                <Switch defaultChecked={autoRefresh} onChange={this.onSwitchChange} /></span>
              <Button onClick={this.refreshData}><ReloadOutlined translate />刷新</Button>
            </span>
        </Col>
      </Row>
    )
  }
}
