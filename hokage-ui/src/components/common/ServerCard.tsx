import React from 'react'
import { Card, Row, Col, Avatar, Divider } from 'antd'

interface ServerCardPropsType {
  serverType: string,
  serverIp: string, // 服务器IP或者服务器域名
  description: string, //服务器描述
  action: string, // 触发的操作
}

export default class ServerCard extends React.Component<ServerCardPropsType> {
  render() {
    const { serverType, serverIp, description, action } = this.props
    return (
      <div>
        <Card>
          <Row gutter={24}>
            <Col span={20}>
              <div>
                <Card.Meta
                  avatar={
                    <Avatar
                      style={{
                        backgroundColor: "#F56A00",
                        verticalAlign: "middle"
                      }}
                      size="large"
                    >
                      {serverType}
                    </Avatar>
                  }
                  title={serverIp}
                  description={description}
                />
              </div>
            </Col>
            <Col
              span={4}
              style={{
                display: "flex",
                alignItems: "center",
                justifyContent: "center"
              }}
            >
              <a href="/#/app/index">{action}</a>
            </Col>
          </Row>
        </Card>
        <Divider style={{margin: '4px 0px'}} />
      </div>
    )
  }
}