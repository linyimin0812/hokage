import React from 'react'
import { Button, Col, Row } from 'antd'
import { UserAddOutlined, } from '@ant-design/icons'
import AddOperator from './add-operator'
import { observer } from 'mobx-react'
import store from '../store';
import { UserAction } from '../../../../axios/action'
import { getHokageUid } from '../../../../libs'

@observer
export class Toolbar extends React.Component {

  delete = () => {
    alert("delete operators bat")
  }

  add = () => {
    store.fetchUserOptions()
  }

  onModalOk = (value: any) => {
    UserAction.addSupervisor({
      operatorId: getHokageUid(),
      serverIds: [],
      userIds: value.userIds || []
    }).finally(() => store.isModalVisible = false)
  }

  onModalCancel = () => {
    store.isModalVisible = false
  }

  render() {
    return (
      <Row gutter={24} style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', margin: '0 0' }}>
        <Col span={12} style={{ display: 'flex', alignItems: 'center' }} />
        <Col span={12} style={{padding: '0 0'}}>
          <span style={{ float: 'right' }}>
            <Button icon={<UserAddOutlined translate="true" />} onClick={this.add}>添加</Button>
            <AddOperator onModalOk={this.onModalOk} onModalCancel={this.onModalCancel} />
          </span>
        </Col>
      </Row>
    )
  }
}
