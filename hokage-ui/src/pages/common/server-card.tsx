import React from 'react'
import { Card, Row, Col, Avatar, Divider, Button } from 'antd'
import { ServerVO } from '../../axios/action/server/server-type'

export interface ActionPanesType {
  title: string,
  content: JSX.Element,
  key: string,
  closable?: boolean
}

interface ServerCardPropsType {
  serverVO: ServerVO
  actionName: string, // 动作名称
  action: (serverVO: ServerVO) => void, // 触发的操作
}

export default class ServerCard extends React.Component<ServerCardPropsType> {

  renderCardMeta = () => {
    const { serverVO } = this.props
    return (
      <Card.Meta
        avatar={<Avatar style={{ backgroundColor: "#F56A00", verticalAlign: "middle" }} size="large">{serverVO.account}</Avatar>}
        title={serverVO.ip}
        description={serverVO.description}
      />
    )
  }

  render() {
    const { serverVO, action, actionName } = this.props
    return (
      <div>
        <Card>
          <Row gutter={24}>
            <Col span={16}><div>{ this.renderCardMeta() }</div></Col>
            <Col span={8} style={{ display: "flex", alignItems: "center", justifyContent: "center" }}>
              <Button type={'link'} onClick={ () => { action(serverVO) } }>{actionName}</Button>
            </Col>
          </Row>
        </Card>
        <Divider style={{margin: '4px 0px'}} />
      </div>
    )
  }
}
