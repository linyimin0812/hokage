import React from 'react'
import { Button, Col, Divider, message, Row, Spin } from 'antd';
import AverageLoad from './average-load'
import CpuUtilization from './cpu-utilization'
import RamUsage from './ram-usage'
import Process from './process'
import DiskPartition from './disk-partition'
import { ServerVO } from '../../../axios/action/server/server-type'
import { ReloadOutlined } from '@ant-design/icons'
import { MonitorOperateForm, SystemInfoVO } from '../../../axios/action/monitor/monitor-type'
import store from '../store'
import { observer } from 'mobx-react'
import { MonitorAction } from '../../../axios/action/monitor/monitor-action'
import { getHokageUid } from '../../../libs'

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
    this.acquireSystemInfo()
  }

  acquireSystemInfo = () => {
    store.loading = true
    MonitorAction.system(this.assembleOperateForm()).then(systemInfo => {
      this.setState({ ...systemInfo })
    }).catch(e => message.error(e))
      .finally(() => store.loading = false)
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
        <Row gutter={24} align="middle" style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', margin: '0px 0px', padding: '2px 2px' }}>
          <Col span={16} style={{padding: '0px 0px'}} />
          <Col span={8} style={{padding: '0px 0px'}}>
            <span style={{ float: 'right' }}><Button onClick={this.acquireSystemInfo}><ReloadOutlined translate />刷新</Button></span>
          </Col>
        </Row>
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
