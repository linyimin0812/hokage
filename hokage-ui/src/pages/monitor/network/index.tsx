import React from 'react'
import { Button, Col, Divider, message, Row, Spin } from 'antd'
import InterfaceInfo from './interface-info'
import DownloadSpeed from './download-speed'
import UploadSpeed from './upload-speed'
import ArpCacheTable from './arpc-cache-table'
import ConnectionTable from './connection-table'
import { ServerVO } from '../../../axios/action/server/server-type'
import { ReloadOutlined } from '@ant-design/icons'
import { MonitorOperateForm, NetworkInfoVO } from '../../../axios/action/monitor/monitor-type'
import store from '../store'
import { MonitorAction } from '../../../axios/action/monitor/monitor-action'
import { observer } from 'mobx-react'
import { getHokageUid } from '../../../libs'

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
    this.acquireNetWorkInfo()
  }

  acquireNetWorkInfo = () => {
    store.loading = true
    MonitorAction.networkBasic(this.assembleOperateForm()).then(networkInfo => {
      this.setState({...networkInfo})
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
    const { interfaceIpInfo, arpInfo, connectionInfo } = this.state
    return (
      <Spin spinning={store.loading}>
        <Row gutter={24} align="middle" style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', margin: '0px 0px', padding: '2px 2px' }}>
          <Col span={16} style={{padding: '0px 0px'}} />
          <Col span={8} style={{padding: '0px 0px'}}>
            <span style={{ float: 'right' }}><Button onClick={this.acquireNetWorkInfo}><ReloadOutlined translate />刷新</Button></span>
          </Col>
        </Row>
        <Row gutter={12}>
          <Col span={6}><InterfaceInfo dataSource={interfaceIpInfo} /></Col>
          <Col span={8}><ArpCacheTable dataSource={arpInfo} /></Col>
          <Col span={10}><ConnectionTable dataSource={connectionInfo} /></Col>
        </Row>
        <Divider />
        <Row>
          <Col span={10}><DownloadSpeed /></Col>
          <Col span={2} />
          <Col span={10}><UploadSpeed /></Col>
          <Col span={2} />
        </Row>
      </Spin>
    )
  }
}
