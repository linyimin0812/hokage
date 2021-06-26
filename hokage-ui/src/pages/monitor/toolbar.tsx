import React from 'react'
import { Button, Col, DatePicker, Divider, Radio, Row, Switch } from 'antd';
import { ReloadOutlined } from '@ant-design/icons'
import moment, { Moment } from 'moment'
import { RangeValue } from 'rc-picker/lib/interface'
import { RadioChangeEvent } from 'antd/lib/radio/interface'

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

  timeType: string
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
    timeType: '10min'
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

  onTimeTYpeChange = (e: RadioChangeEvent) => {

    const timeType = e.target.value
    if (timeType === 'customer') {
      this.setState({timeType: timeType})
      return
    }

    const end = moment().valueOf()
    let start = end - 10 * 60 * 1000
    if (timeType === '30min') {
      start = end - 30 * 60 * 1000
    } else if (timeType === '60min') {
      start = end - 60 * 60 * 1000
    } else if (timeType === '1day') {
      start = end - 24 * 60 * 60 * 1000
    }

    this.props.refreshData(start, end)

    this.setState({timeType: timeType, start: start, end: end})

  }

  renderRangePicker = () => {
    const { timeType } = this.state
    return (
      <>
        <Radio.Group value={timeType} onChange={this.onTimeTYpeChange}>
          <Radio.Button value={'10min'}>10分钟</Radio.Button>
          <Divider type={'vertical'} />
          <Radio.Button value={'30min'}>30分钟</Radio.Button>
          <Divider type={'vertical'} />
          <Radio.Button value={'60min'}>3小时</Radio.Button>
          <Divider type={'vertical'} />
          <Radio.Button value={'1day'}>1天</Radio.Button>
          <Divider type={'vertical'} />
          <Radio.Button value={'customer'}>自定义</Radio.Button>
        </Radio.Group>
        {
          timeType === 'customer' ? (
            <>
              <Divider type={'vertical'} />
              <DatePicker.RangePicker ranges={{ 'Today': [moment(), moment()], }} showTime format="YYYY-MM-DD HH:mm" onChange={this.rangePickerChange} />
            </>
          ) : null
        }
      </>
    )
  }

  onSwitchChange = (checked: boolean, _: MouseEvent): void => {
    if (checked) {
      const intervalTimeout: NodeJS.Timeout = setInterval(this.refreshData, interval)
      const timestamp = moment().valueOf()
      this.setState({ autoRefresh: true, interval: intervalTimeout, timestamp: timestamp})
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
