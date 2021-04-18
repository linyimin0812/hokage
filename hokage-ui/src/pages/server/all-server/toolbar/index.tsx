import React from 'react'
import { Button, Col, Divider, message, Row } from 'antd'
import { InfoCircleOutlined, LaptopOutlined, MinusOutlined, PlusOutlined, UserOutlined } from '@ant-design/icons'
import AddServer from '../../common/add-server'
import store from '../store'
import { ServerForm } from '../../../../axios/action/server/server-type'
import { getHokageUid } from '../../../../libs'
import { ServiceResult } from '../../../../axios/common'
import { ServerAction } from '../../../../axios/action/server/server-action'
import { searchServer } from '../../util'
import { observer } from 'mobx-react'

@observer
export default class Toolbar extends React.Component {

  batDelete = () => {
    alert(JSON.stringify(store.selectedRowKeys))
  }

  addServer = () => {
    store.addServerModalVisible = true
  }

  onModalOk = (value: ServerForm) => {
    value.operatorId = getHokageUid()
    if (value.passwd && !(typeof value.passwd === 'string')) {
      const uploadResponse = value.passwd.file.response as ServiceResult<string>
      if (uploadResponse.success) {
        value.passwd = uploadResponse.data!
      } else {
        message.error('密钥文件上传失败, 请重试！')
      }
    }
    if (!value.loginType) {
      value.loginType = 0
    }
    ServerAction.saveServer(value).then(() => {
      store.addServerModalVisible = false
      searchServer(this)
    }).catch(e => message.error(e))
  }

  onModalCancel = () => {
    store.addServerModalVisible = false
  }

  render() {
    return (
      <Row gutter={24} style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', margin: '0 0' }}>
        <Col span={4} style={{ display: 'flex', alignItems: 'center' }}>
          <span>
            <InfoCircleOutlined translate="true" style={{ color: "#1890ff" }} />
            已选择{<span style={{ color: "blue" }}>{store.selectedRowKeys.length}</span>}项
          </span>
        </Col>
        <Col span={20} style={{padding: '0 0'}}>
          <span style={{ float: 'right' }}>
            {store.selectedRowKeys.length > 0 ? ([
              <Button icon={<UserOutlined translate="true" />} onClick={this.batDelete}>指定管理员</Button>,
              <Divider type="vertical" />,
              <Button icon={<LaptopOutlined translate="true" />} onClick={this.batDelete}>申请服务器</Button>,
              <Divider type="vertical" />,
              <Button icon={<UserOutlined translate="true" />} onClick={this.batDelete}>申请管理员</Button>,
              <Divider type="vertical" />,
              <Button icon={<MinusOutlined translate="true" />} onClick={this.batDelete}>删除</Button>])
              : <Button icon={<PlusOutlined translate="true" />} onClick={this.addServer}>添加</Button>}
            <AddServer onModalOk={this.onModalOk} onModalCancel={this.onModalCancel} isModalVisible={store.addServerModalVisible} />
          </span>
        </Col>
      </Row>
    )
  }
}
