import React from 'react'
import { Button, Col, Row } from 'antd'

export class FileOperation extends React.Component {
  render() {
    return (
      <Row
        gutter={24}
        align="middle"
        style={{ backgroundColor: '#FFFFFF', border: '#FFFFFF', margin: '0px 0px', padding: '8px 8px' }}
      >
        <Col span={16}>
          <span style={{paddingRight: '8px'}}>
            <Button>上传</Button>
          </span>
          <span style={{paddingRight: '8px'}}>
            <Button>新建</Button>
          </span>
          <span style={{paddingRight: '8px'}}>
            <Button>上一步</Button>
          </span>
          <span style={{paddingRight: '8px'}}>
            <Button>分享</Button>
          </span>
          <span style={{paddingRight: '8px'}}>
            <Button>工作目录</Button>
          </span>
        </Col>
        <Col span={8}>
          <span style={{ float: 'right' }}>
            <Button>回收站</Button>
          </span>
        </Col>
      </Row>
    )
  }
}
