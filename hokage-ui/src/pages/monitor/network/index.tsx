import React from 'react'
import { Button, Col, Divider, Row } from 'antd';
import InterfaceInfo from './interface-info'
import DownloadSpeed from './download-speed'
import UploadSpeed from './upload-speed'
import ARPCacheTable from './arpc-cache-table'
import ConnectionInfo from './connection-info'
import { ServerVO } from '../../../axios/action/server/server-type'
import { ReloadOutlined } from '@ant-design/icons';

type NetworkProp = {
  serverVO: ServerVO
}

export default class Index extends React.Component<NetworkProp>{
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
          <Col span={8}><InterfaceInfo /></Col>
          <Col span={8}><ARPCacheTable /></Col>
          <Col span={8}><ConnectionInfo /></Col>
        </Row>
        <Divider />
        <Row>
          <Col span={10}><DownloadSpeed /></Col>
          <Col span={2} />
          <Col span={10}><UploadSpeed /></Col>
          <Col span={2} />
        </Row>
      </div>
    )
  }
}
