import React from 'react'
import { Col, Divider, message, Row, Spin } from 'antd'
import InterfaceInfo from './interface-info'
import DownloadSpeed from './download-speed'
import UploadSpeed from './upload-speed'
import ArpCacheTable from './arpc-cache-table'
import ConnectionTable from './connection-table'
import { ServerVO } from '../../../axios/action/server/server-type'
import { MetricVO, MonitorOperateForm, NetworkInfoVO } from '../../../axios/action/monitor/monitor-type'
import store from '../store'
import { MonitorAction } from '../../../axios/action/monitor/monitor-action'
import { observer } from 'mobx-react'
import { getHokageUid } from '../../../libs'
import { Toolbar } from '../toolbar'

type NetworkProp = {
  serverVO: ServerVO
}

type NetworkState = {
  networkInfo: NetworkInfoVO,
  systemMetric: MetricVO
}

@observer
export default class Index extends React.Component<NetworkProp, NetworkState>{

  state = {
    networkInfo: {
      interfaceIpInfo: [],
      arpInfo: [],
      connectionInfo: []
    },
    systemMetric: {
      loadAvgMetric: [],
      cpuStatMetric: [],
      memStatMetric: [],

      uploadStatMetric: [],
      downloadStatMetric: []
    }
  }

  componentDidMount() {
    this.refreshData(store.start, store.end)
  }

  refreshData = (start?: number, end?: number) => {
    this.acquireNetWorkInfo()
    this.acquireMetric(start, end)
  }

  acquireNetWorkInfo = () => {
    store.basicLoading = true
    MonitorAction.networkBasic(this.assembleOperateForm()).then(networkInfo => {
      this.setState({networkInfo: networkInfo})
    }).catch(e => message.error(e))
      .finally(() => store.basicLoading = false)
  }

  acquireMetric = (start?: number, end?: number) => {
    const form = this.assembleOperateForm()
    form.start = start ? start : new Date().getTime() - 10 * 60 * 1000
    form.end = end ? end : new Date().getTime()
    store.metricLoading = true
    MonitorAction.metric(form).then(metric => {
      if (!metric) {
        return
      }
      this.setState({systemMetric: metric})

    }).catch(e => message.error(e))
      .finally(() => store.metricLoading = false)
  }

  assembleOperateForm = () => {
    const { ip, sshPort, account, id } = this.props.serverVO
    const form: MonitorOperateForm = {
      operatorId: getHokageUid(),
      serverId: id,
      ip: ip,
      sshPort: sshPort,
      account: account
    }
    return form
  }

  render() {
    const { interfaceIpInfo, arpInfo, connectionInfo } = this.state.networkInfo
    const {downloadStatMetric, uploadStatMetric} = this.state.systemMetric
    const { serverVO } = this.props
    return (
      <>
        <Spin spinning={store.metricLoading}>
          <Toolbar refreshData={this.refreshData} />
          <Row>
            <Col span={10}><DownloadSpeed id={serverVO.id} data={downloadStatMetric} /></Col>
            <Col span={2} />
            <Col span={10}><UploadSpeed id={serverVO.id} data={uploadStatMetric} /></Col>
            <Col span={2} />
          </Row>
        </Spin>
        <Divider />
        <Spin spinning={store.basicLoading}>
          <Row gutter={12}>
            <Col span={6}><InterfaceInfo dataSource={interfaceIpInfo} /></Col>
            <Col span={8}><ArpCacheTable dataSource={arpInfo} /></Col>
            <Col span={10}><ConnectionTable dataSource={connectionInfo} /></Col>
          </Row>
        </Spin>
      </>
    )
  }
}
