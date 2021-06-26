import React from 'react';
import { Button, Col, DatePicker, Divider, Radio, Row, Switch } from 'antd';
import { ReloadOutlined } from '@ant-design/icons';
import moment, { Moment } from 'moment';
import { RangeValue } from 'rc-picker/lib/interface';
import { RadioChangeEvent } from 'antd/lib/radio/interface';
import store from './store';
import { observer } from 'mobx-react';

type ToolbarProp = {
  refreshData: (start?: number, end?: number) => void
}

// 自动更新时间间隔
const interval: number = 60 * 1000
@observer
export class Toolbar extends React.Component<ToolbarProp> {

  componentDidMount() {
    this.checkRestSecondsInterval = setInterval(() => {
      if (!store.autoRefresh) {
        return
      }
      const restTimestamp = interval - ((moment().valueOf() - store.timestamp) % interval)
      store.restSeconds = Math.floor(restTimestamp / 1000)
    }, 1000)
  }

  componentWillUnmount() {
    if (store.interval) {
      clearInterval(store.interval!)
    }
    clearInterval(this.checkRestSecondsInterval!)
  }

  private checkRestSecondsInterval: NodeJS.Timeout | null = null

  rangePickerChange = (values: RangeValue<Moment>, _: [string, string]) => {
    if (values && values[0] && values[1]) {
      store.start = values[0]?.valueOf()
      store.end = values[1]?.valueOf()
    }
  }

  onTimeTYpeChange = (e: RadioChangeEvent) => {

    const timeType = e.target.value
    if (timeType === 'customer') {
      store.timeType = timeType
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

    store.timeType = timeType
    store.start = start
    store.end = end

  }

  renderRangePicker = () => {
    return (
      <>
        <Radio.Group value={store.timeType} onChange={this.onTimeTYpeChange}>
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
          store.timeType === 'customer' ? (
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
      store.autoRefresh = true
      store.interval = intervalTimeout
      store.timestamp = timestamp
      store.restSeconds = Math.floor(interval / 1000)
    } else {
      if (store.interval) {
        clearInterval(store.interval!)
      }
      store.autoRefresh = false
    }
  }

  refreshData = () => {
    const { refreshData } = this.props
    refreshData(store.start, store.end)
  }

  render() {
    return (
      <Row gutter={24} align="middle" style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', margin: '0px 0px', marginBottom: '10px', padding: '2px 2px' }}>
        <Col span={16} style={{padding: '0px 0px'}}>
          {this.renderRangePicker()}
        </Col>
        <Col span={8} style={{padding: '0px 0px'}}>
            <span style={{ float: 'right' }}>
              <span style={{paddingRight: '50px'}}>
                {store.autoRefresh && store.restSeconds > 0 ? `${store.restSeconds}秒后自动更新: ` : '自动更新: '}
                <Switch defaultChecked={store.autoRefresh} onChange={this.onSwitchChange} /></span>
              <Button onClick={this.refreshData}><ReloadOutlined translate />刷新</Button>
            </span>
        </Col>
      </Row>
    )
  }
}
