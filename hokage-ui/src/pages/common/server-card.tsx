import React from 'react';
import { Avatar, Button, Card, Col, Divider, Row } from 'antd';
import { AccountTypeEnum, ServerVO } from '../../axios/action/server/server-type';

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
    let accountType = '普通账号'
    if (serverVO.accountType === AccountTypeEnum.ordinary) {
      accountType = '普通账号'
    } else if (serverVO.accountType === AccountTypeEnum.admin) {
      accountType = '管理账号'
    }
    return (
      <Card.Meta
        avatar={<Avatar style={{ backgroundColor: "#F56A00", verticalAlign: "middle" }} size="large">{serverVO.account}</Avatar>}
        title={<span>{serverVO.ip} - {accountType}</span>}
        description={serverVO.description}
        style={{wordBreak: 'break-all'}}
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
