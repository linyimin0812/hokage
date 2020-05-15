import React from 'react'
import { Col, Row } from 'antd';
import ServerInfoCard from '../common/ServerInfoCard';

export default class ServerInfo extends React.Component {
  renderServerInfoCards = () => {
    const datas = [1,2,3,4,5]
    return datas.map(_ => {
      return (
        <Col span={12}>
          <ServerInfoCard />
        </Col>
      )
    })
  }
  
  render() {
    return (
      <Row gutter={24}>
        {this.renderServerInfoCards()}
      </Row>
    )
  }
}