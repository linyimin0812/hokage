import React from 'react'
import { Button, Col, Divider, message, Row, Spin } from 'antd';
import AccountInfo from './account-info'
import BasicInfo from './basic-info'
import LoginAccountInfo from './login-account-info'
import { ServerVO } from '../../../axios/action/server/server-type'
import { ReloadOutlined } from '@ant-design/icons'
import { observer } from 'mobx-react'
import { BasicInfoVO, MonitorOperateForm } from '../../../axios/action/monitor/monitor-type'
import store from '../store'
import { MonitorAction } from '../../../axios/action/monitor/monitor-action'
import { getHokageUid } from '../../../libs'

type BasicInfoProp = {
  serverVO: ServerVO
}

type BasicInfoState = BasicInfoVO

@observer
export default class Index extends React.Component<BasicInfoProp, BasicInfoState>{

  constructor(props: BasicInfoProp) {
    super(props)
    this.acquireBasicInfo()
  }

  state = {
    cpuInfo: [],
    memInfo: [],
    accountInfo: [],
    lastLogInfo: [],
    generalInfo: []
  }

  acquireBasicInfo = () => {
    store.loading = true
    MonitorAction.basic(this.assembleOperateForm()).then(basicInfo => {
      this.setState({...basicInfo})
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
    const { cpuInfo, memInfo, accountInfo, lastLogInfo, generalInfo } = this.state
    return (
      <Spin spinning={store.loading}>
        <Row gutter={24} align="middle" style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', margin: '0px 0px', padding: '2px 2px' }}>
          <Col span={16} style={{padding: '0px 0px'}} />
          <Col span={8} style={{padding: '0px 0px'}}>
            <span style={{ float: 'right' }}><Button onClick={this.acquireBasicInfo}><ReloadOutlined translate />刷新</Button></span>
          </Col>
        </Row>
        <Row gutter={12}>
          <Col span={8}><BasicInfo dataSource={generalInfo} title={"基本信息"} /></Col>
          <Col span={8}><AccountInfo dataSource={accountInfo} /></Col>
          <Col span={8}><LoginAccountInfo dataSource={lastLogInfo} /></Col>
        </Row>
        <Divider />
        <Row gutter={12}>
          <Col span={8}><BasicInfo dataSource={cpuInfo} title={"CPU信息"} /></Col>
          <Col span={8}><BasicInfo dataSource={memInfo} title={"内存信息"} /></Col>
          <Col span={8}><BasicInfo dataSource={[]} title={"磁盘信息"} /></Col>
        </Row>
      </Spin>
    )
  }
}
