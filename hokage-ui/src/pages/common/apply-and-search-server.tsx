import React, { ReactText } from 'react'
import { Button, Col, Divider, Input, message, Row } from 'antd'
import { InfoCircleOutlined, MinusOutlined, PlusOutlined } from '@ant-design/icons/lib'
import AddServer from '../server/common/add-server'

interface ApplyAndSearchServerStateType {
  isModalVisible: boolean
}

interface ApplyAndSearchServerPropsType {
  selectionKeys?: ReactText[]
}

export default class ApplyAndSearchServer extends React.Component<ApplyAndSearchServerPropsType, ApplyAndSearchServerStateType> {

  state = {
    isModalVisible: false,
  };

  applyServer = () => {
    window.location.href = '/app/server/all'
  };

  add = () => {
    this.setState({ isModalVisible: true })
    console.log('hahahahaha')
  };

  delete = () => {
    const { selectionKeys } = this.props;
    alert(`delete keys: ${JSON.stringify(selectionKeys)}`)
  };

  onModalOk = (value: any) => {
    console.log(value)
    this.setState({ isModalVisible: false })
    message.loading({ content: 'Loading...', key: 'addServer' })
    // TODO 保存服务器信息
    setTimeout(() => {
      message.success({ content: 'Loaded!', key: 'addServer', duration: 2 })
    }, 2000);
  };

  onModalCancel = () => {
    this.setState({ isModalVisible: false });
    message.warning({ content: '添加用户已经取消!', key: 'addUser', duration: 2 })
  };

  render() {

    const { isModalVisible } = this.state
    const { selectionKeys } = this.props

    return (
      <>
        <Row gutter={24} style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', padding: '4px 0px' }}>
          <Col span={8} style={{ display: 'flex', alignItems: 'center' }}>
            {
              selectionKeys !== undefined ? (<span>
                <InfoCircleOutlined translate="true" style={{ color: '#1890ff' }} />
                已选择{<span style={{ color: 'blue' }}>{selectionKeys.length}</span>}项
              </span>) : null
            }
          </Col>
          <Col span={16}>
            <span style={{ float: 'right' }}>
              {
                (selectionKeys || []).length > 0 ? (
                  <>
                    <Button icon={<MinusOutlined translate="true" />} onClick={this.delete}>批量删除</Button>
                    <Divider type="vertical" />
                  </>
                ) : (
                  <>
                    <Button key="3" icon={<PlusOutlined translate="true" />} onClick={this.applyServer}>申请服务器</Button>
                    <Divider type="vertical" />
                    <Button key="4" icon={<PlusOutlined translate="true" />} onClick={this.add}>添加服务器</Button>
                    <Divider type="vertical" />
                    <Input.Search placeholder="服务器地址" onSearch={value => console.log(value)} style={{ width: '280px' }} />
                  </>
                )
              }
            </span>
          </Col>
        </Row>
          <AddServer
            onModalOk={this.onModalOk}
            onModalCancel={this.onModalCancel}
            isModalVisible={isModalVisible}
          />
      </>
    )
  }
}
