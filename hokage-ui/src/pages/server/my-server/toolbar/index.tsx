import React from 'react'
import { Button, Col, Divider, message, Row } from 'antd';
import { InfoCircleOutlined, MinusOutlined, PlusOutlined } from '@ant-design/icons'
import AddServer from '../../common/add-server'
import AddAccount from '../../common/add-account'
import store from '../store'
import { observer } from 'mobx-react'
@observer
export default class Toolbar extends React.Component {
  delete = () => {
    alert("delete operators bat")
  }

  applyServerClick = () => {
    window.location.href = '/app/server/all'
  }

  onAddServerModalOk = (value: any) => {
    setTimeout(() => {
      message.success({ content: 'Loaded!', key: 'addUser', duration: 2 });
    }, 2000);
    store.isAddServerModalVisible =false
  }

  onAddServerModalCancel = () => {
    store.isAddServerModalVisible = false
  }

  onAddAccountModalOk = (value: any) => {
    console.log(value)
  }

  onAddAccountModalCancel = () => {
    store.isAddAccountModalVisible = false
  }

  render() {
    return (
      <Row gutter={24} style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', margin: '0 0' }}>
        <Col span={8} style={{ display: 'flex', alignItems: 'center' }}>
          <span>
          <InfoCircleOutlined translate="true" style={{ color: "#1890ff" }} />
            已选择{<span style={{ color: "blue" }}>{store.selectedRowKeys.length}</span>}项
          </span>
        </Col>
        <Col span={16} style={{padding: '0 0'}} >
          <span style={{ float: 'right' }}>
            {store.selectedRowKeys.length > 0 ? ([
                <Button icon={<MinusOutlined translate="true" />} onClick={this.delete}>批量删除</Button>, <Divider type="vertical" />]) : null}
            <Button icon={<PlusOutlined translate="true" />} onClick={this.applyServerClick}>
              申请服务器
            </Button>
            <AddServer onModalOk={this.onAddServerModalOk} onModalCancel={this.onAddServerModalCancel} isModalVisible={store.isAddServerModalVisible} />
            <AddAccount onModalOk={this.onAddAccountModalOk} onModalCancel={this.onAddAccountModalCancel} isModalVisible={store.isAddAccountModalVisible} />
            <Divider type="vertical" />
            <Button icon={<PlusOutlined translate="true" />} onClick={() => store.isAddServerModalVisible = true}>
              添加服务器
            </Button>
            <Divider type="vertical" />
            <Button icon={<PlusOutlined translate="true" />} onClick={() => store.isAddAccountModalVisible = true}>
              添加账号
            </Button>
          </span>
        </Col>
      </Row>
    )
  }
}
