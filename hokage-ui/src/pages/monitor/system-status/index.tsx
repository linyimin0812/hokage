import React from 'react'
import { Col, Divider, message, Row, Spin } from 'antd'
import AverageLoad from './average-load'
import CpuUtilization from './cpu-utilization'
import RamUsage from './ram-usage'
import Process from './process'
import DiskPartition from './disk-partition'
import { ServerVO } from '../../../axios/action/server/server-type'
import { MetricVO, MonitorOperateForm, SystemInfoVO } from '../../../axios/action/monitor/monitor-type';
import store from '../store'
import { observer } from 'mobx-react'
import { MonitorAction } from '../../../axios/action/monitor/monitor-action'
import { getHokageUid } from '../../../libs'
import { Toolbar } from '../toolbar'

type SystemStatusProp = {
  serverVO: ServerVO
}

type SystemStatusState = {
  systemInfo: SystemInfoVO,
  systemMetric: MetricVO
}

@observer
export default class Index extends React.Component<SystemStatusProp, SystemStatusState> {

  state = {
    systemInfo: {
      processInfo: [],
      diskInfo: []
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
    this.acquireSystemInfo()
    this.acquireMetric(start, end)
  }

  acquireSystemInfo = () => {
    store.basicLoading = true
    MonitorAction.system(this.assembleOperateForm()).then(systemInfo => {
      this.setState({ systemInfo: systemInfo })
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

    const { processInfo, diskInfo } = this.state.systemInfo
    const { cpuStatMetric, loadAvgMetric, memStatMetric } = this.state.systemMetric
    const { serverVO } = this.props

    return (
      <>
        <Spin spinning={store.metricLoading}>
          <Toolbar refreshData={this.refreshData} />
          <Row gutter={12} align="middle" justify={"center"} >
            <Col span={8}><AverageLoad data={loadAvgMetric} id={serverVO.id} /></Col>
            <Col span={8}><CpuUtilization data={cpuStatMetric} id={serverVO.id} /></Col>
            <Col span={8}><RamUsage data={memStatMetric} id={serverVO.id} /></Col>
          </Row>
        </Spin>
        <Divider />
        <Spin spinning={store.basicLoading}>
        <Row gutter={12} >
          <Col span={16}><Process dataSource={processInfo} serverVO={this.props.serverVO} /></Col>
          <Col span={8}><DiskPartition dataSource={diskInfo} /></Col>
        </Row>
        </Spin>
      </>
    )
  }
}
