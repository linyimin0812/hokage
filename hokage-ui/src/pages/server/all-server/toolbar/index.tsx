import React from 'react'
import { Button, Col, message, Row } from 'antd'
import { PlusOutlined } from '@ant-design/icons'
import AddServer from '../../common/add-server'
import store from '../store'
import { ServerForm } from '../../../../axios/action/server/server-type'
import { getHokageUid } from '../../../../libs'
import { ServiceResult } from '../../../../axios/common'
import { ServerAction } from '../../../../axios/action/server/server-action'
import { observer } from 'mobx-react'

@observer
export default class Toolbar extends React.Component {

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
      store.fetchRecords({operatorId: getHokageUid()})
    }).catch(e => message.error(e))
  }

  onModalCancel = () => {
    store.addServerModalVisible = false
  }

  render() {
    return (
      <Row gutter={24} style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', margin: '0 0' }}>
        <Col span={4} style={{ display: 'flex', alignItems: 'center' }} />
        <Col span={20} style={{padding: '0 0'}}>
          <span style={{ float: 'right' }}>
            <Button icon={<PlusOutlined translate="true" />} onClick={this.addServer}>添加</Button>
            <AddServer onModalOk={this.onModalOk} onModalCancel={this.onModalCancel} isModalVisible={store.addServerModalVisible} />
          </span>
        </Col>
      </Row>
    )
  }
}
