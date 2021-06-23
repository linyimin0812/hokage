import React from 'react'
import { Col, Divider, message, Row, Spin } from 'antd'
import AverageLoad from './average-load'
import CpuUtilization from './cpu-utilization'
import RamUsage from './ram-usage'
import Process from './process'
import DiskPartition from './disk-partition'
import { ServerVO } from '../../../axios/action/server/server-type'
import { MonitorOperateForm, SystemInfoVO } from '../../../axios/action/monitor/monitor-type'
import store from '../store'
import { observer } from 'mobx-react'
import { MonitorAction } from '../../../axios/action/monitor/monitor-action'
import { getHokageUid } from '../../../libs'
import { Toolbar } from '../toolbar'

type SystemStatusProp = {
  serverVO: ServerVO
}

type SystemStatusState = SystemInfoVO

@observer
export default class Index extends React.Component<SystemStatusProp, SystemStatusState> {

  state = {
    processInfo: [],
    diskInfo: []
  }

  componentDidMount() {
    this.refreshData()
  }

  refreshData = (start?: number, end?: number) => {
    this.acquireSystemInfo()
    this.acquireMetric(start, end)
  }

  acquireSystemInfo = () => {
    store.loading = true
    MonitorAction.system(this.assembleOperateForm()).then(systemInfo => {
      this.setState({ ...systemInfo })
    }).catch(e => message.error(e))
      .finally(() => store.loading = false)
  }

  acquireMetric = (start?: number, end?: number) => {
    const form = this.assembleOperateForm()
    form.start = start ? start : new Date().getTime() - 60 * 60 * 1000
    form.end = end ? end : new Date().getTime()
    store.acquireSystemStat(form)
  }

  assembleOperateForm = () => {
    const { ip, sshPort, account } = this.props.serverVO
    const form: MonitorOperateForm = {
      operatorId: getHokageUid(),
      ip: ip,
      sshPort: sshPort,
      account: account
    }
    return form
  }

  render() {

    const { processInfo, diskInfo } = this.state

    return (
      <Spin spinning={store.loading}>
        <Toolbar refreshData={this.refreshData} />
        <Row gutter={12} align="middle" justify={"center"} >
          <Col span={8}><AverageLoad /></Col>
          <Col span={8}><CpuUtilization /></Col>
          <Col span={8}><RamUsage /></Col>
        </Row>
        <Divider />
        <Row gutter={12} >
          <Col span={16}><Process dataSource={processInfo} serverVO={this.props.serverVO} /></Col>
          <Col span={8}><DiskPartition dataSource={diskInfo} /></Col>
        </Row>
      </Spin>
    )
  }
}
