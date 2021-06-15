import React from 'react'
import { Button, Col, Divider, Row } from 'antd';
import AverageLoad from './average-load'
import CpuUtilization from './cpu-utilization'
import RamUsage from './ram-usage'
import CpuProcess from './cpu-process'
import RamProcess from './ram-process'
import DiskPartition from './disk-partition'
import { ServerVO } from '../../../axios/action/server/server-type'
import { ReloadOutlined } from '@ant-design/icons';

type SystemStatusProp = {
  serverVO: ServerVO
}

export default class Index extends React.Component<SystemStatusProp> {
  render() {
    return (
      <div>
        <Row gutter={24} align="middle" style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', margin: '0px 0px', padding: '2px 2px' }}>
          <Col span={16} style={{padding: '0px 0px'}} />
          <Col span={8} style={{padding: '0px 0px'}}>
            <span style={{ float: 'right' }}><Button onClick={() => {}}><ReloadOutlined translate />刷新</Button></span>
          </Col>
        </Row>
        <Row gutter={12} align="middle" justify={"center"} >
          <Col span={8}><AverageLoad /></Col>
          <Col span={8}><CpuUtilization /></Col>
          <Col span={8}><RamUsage /></Col>
        </Row>
        <Divider />
        <Row gutter={12} >
          <Col span={8}><CpuProcess /></Col>
          <Col span={8}><RamProcess /></Col>
          <Col span={8}><DiskPartition /></Col>
        </Row>
      </div>
    )
  }
}
