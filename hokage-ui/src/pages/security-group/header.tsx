import React from 'react'
import { Button, Col, Input, Row } from 'antd'

interface HeaderPropsType {
  onClick: () => void,
  onSearch: (address: string) => void
}

export default class Header extends React.Component<HeaderPropsType> {

  render() {
    return (
      <div>
        <Row gutter={24} style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', padding: '4px 0px' }}>
          <Col span={12} style={{ display: 'flex', alignItems: 'center' }}>
            <Button onClick={this.props.onClick}>添加安全组</Button>
          </Col>
          <Col span={12}>
            <Input.Search placeholder="服务器地址" onSearch={this.props.onSearch} style={{ width: '280px', float: "right" }} />
          </Col>
        </Row>
      </div>
    )
  }
}
