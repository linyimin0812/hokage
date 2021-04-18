import React from 'react'
import { Button, Col, Divider, message, Row } from 'antd'
import { InfoCircleOutlined, MinusOutlined, PlusOutlined } from '@ant-design/icons'
import ApplyServer from '../../common/apply-server'
import store from '../store'
import { observer } from 'mobx-react';

@observer
export default class Toolbar extends React.Component {

  onModalOk = (value: any) => {
    store.isModalVisible = true
    setTimeout(() => {
      message.success({ content: 'Loaded!', key: 'addUser', duration: 2 });
    }, 2000);
    store.isModalVisible = false
  }

  onModalCancel = () => {
    store.isModalVisible = false
  }

  delete = () => {
    alert("delete operators bat")
  }

  applyServerClick = () => {
    store.isModalVisible = true
  }

  render() {
    return (
      <Row
        gutter={24}
        style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', margin: '0 0' }}
      >
        <Col span={12} style={{ display: 'flex', alignItems: 'center' }}>
          <span>
            <InfoCircleOutlined translate="true" style={{ color: "#1890ff" }} />
            已选择{<span style={{ color: "blue" }}>{store.selectedRowKeys.length}</span>}项
          </span>
        </Col>
        <Col span={12} style={{padding: '0 0'}} >
          <span style={{ float: 'right' }}>
            {store.selectedRowKeys.length > 0 ? ([
              <Button icon={<MinusOutlined translate="true" />} onClick={this.delete}>
                批量删除
              </Button>,
              <Divider type="vertical" />]) : null
            }
            <Button icon={<PlusOutlined translate="true" />} onClick={this.applyServerClick} >
              申请
            </Button>
            <ApplyServer onModalOk={this.onModalOk} onModalCancel={this.onModalCancel} isModalVisible={store.isModalVisible} />
          </span>
        </Col>
      </Row>
    )
  }
}
