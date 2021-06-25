import React from 'react'
import { Col, Divider, message, Row, Spin } from 'antd'
import InterfaceInfo from './interface-info'
import DownloadSpeed from './download-speed'
import UploadSpeed from './upload-speed'
import ArpCacheTable from './arpc-cache-table'
import ConnectionTable from './connection-table'
import { ServerVO } from '../../../axios/action/server/server-type'
import { MonitorOperateForm, NetworkInfoVO } from '../../../axios/action/monitor/monitor-type'
import store from '../store'
import { MonitorAction } from '../../../axios/action/monitor/monitor-action'
import { observer } from 'mobx-react'
import { getHokageUid } from '../../../libs'
import { Toolbar } from '../toolbar'

type NetworkProp = {
  serverVO: ServerVO
}

type NetworkState = NetworkInfoVO

@observer
export default class Index extends React.Component<NetworkProp, NetworkState>{

  state = {
    interfaceIpInfo: [],
    arpInfo: [],
    connectionInfo: []
  }

  componentDidMount() {
    this.refreshData()
  }

  refreshData = () => {
    this.acquireNetWorkInfo()
    this.acquireMetric()
  }

  acquireNetWorkInfo = () => {
    store.basicLoading = true
    MonitorAction.networkBasic(this.assembleOperateForm()).then(networkInfo => {
      this.setState({...networkInfo})
    }).catch(e => message.error(e))
      .finally(() => store.basicLoading = false)
  }

  acquireMetric = () => {
    const form = this.assembleOperateForm()
    form.start = new Date().getTime() - 60 * 60 * 1000
    form.end = new Date().getTime()
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
    const { interfaceIpInfo, arpInfo, connectionInfo } = this.state
    return (
      <Spin spinning={store.basicLoading}>
        <Toolbar refreshData={this.refreshData} />
        <Row>
          <Col span={10}><DownloadSpeed /></Col>
          <Col span={2} />
          <Col span={10}><UploadSpeed /></Col>
          <Col span={2} />
        </Row>
        <Divider />
        <Row gutter={12}>
          <Col span={6}><InterfaceInfo dataSource={interfaceIpInfo} /></Col>
          <Col span={8}><ArpCacheTable dataSource={arpInfo} /></Col>
          <Col span={10}><ConnectionTable dataSource={connectionInfo} /></Col>
        </Row>
      </Spin>
    )
  }
}
