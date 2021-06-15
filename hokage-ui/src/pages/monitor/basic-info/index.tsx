import React from 'react'
import { Button, Col, Divider, Row } from 'antd'
import AccountInfo from './account-info'
import BasicInfo from './basic-info'
import LoginAccountInfo from './login-account-info'
import { ServerVO } from '../../../axios/action/server/server-type'
import { ReloadOutlined } from '@ant-design/icons'

type BasicInfoProp = {
  serverVO: ServerVO
}

export default class Index extends React.Component<BasicInfoProp>{
  render() {
    return (
      <div>
        <Row gutter={24} align="middle" style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', margin: '0px 0px', padding: '2px 2px' }}>
          <Col span={16} style={{padding: '0px 0px'}} />
          <Col span={8} style={{padding: '0px 0px'}}>
            <span style={{ float: 'right' }}><Button onClick={() => {}}><ReloadOutlined translate />刷新</Button></span>
          </Col>
        </Row>
        <Row gutter={12}>
          <Col span={8}><BasicInfo title={"基本信息"} /></Col>
          <Col span={8}><AccountInfo /></Col>
          <Col span={8}><LoginAccountInfo /></Col>
        </Row>
        <Divider />
        <Row gutter={12}>
          <Col span={8}><BasicInfo title={"CPU信息"} /></Col>
          <Col span={8}><BasicInfo title={"内存信息"} /></Col>
          <Col span={8}><BasicInfo title={"磁盘信息"} /></Col>
        </Row>
      </div>
    )
  }
}
