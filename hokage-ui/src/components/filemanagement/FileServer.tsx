import React from 'react'
import { Tag, Result, Button, message, Row, Col, Divider, Table, Input, List, Avatar } from 'antd';
import { PlusOutlined, FolderFilled } from '@ant-design/icons';
import AddServer from '../server/AddServer';

const datas: any[] = []
for (let i = 0; i < 11; i++) {
  const data = {
    hostname: 'master_' + i,
    serverIp: '10.108.210.' + (i + 5),
    account: "banzhe_" + i,
    status: (i % 2 === 0) ? '掉线' : '在线',
    remark: '测试',
    action: '进入文件管理',
  }
  datas.push(data)
}

type FileServerState = {
  isModalVisible: boolean
}

export default class FileServer extends React.Component<any, FileServerState> {

  state = {
    isModalVisible: false
  }

  applyServer = () => {
    window.location.href = "/#/app/server/all"
  }

  onFinish = (value: any) => {
    console.log(value)
  }

  resetFields = () => {
    console.log("reset")
  }

  add = () => {
    console.log(this.state)
    this.setState({ ...this.state, isModalVisible: true })
    console.log('hahahahaha')
  }

  delete = () => {
    alert("delete operators bat")
  }

  sync = () => {
    alert("sync operator")
  }

  onModalOk = (value: any) => {
    console.log(value)
    this.setState({ ...this.state, isModalVisible: false })
    message.loading({ content: 'Loading...', key: 'addUser' });
    setTimeout(() => {
      message.success({ content: 'Loaded!', key: 'addUser', duration: 2 });
    }, 2000);
  }

  onModalCancel = () => {
    this.setState({ ...this.state, isModalVisible: false })
    message.warning({ content: '添加用户已经取消!', key: 'addUser', duration: 2 });
  }
  
  enterFileManagement = () => {
    alert('enter file management')
  } 

  render() {

    const { isModalVisible } = this.state

    return (
      <>
        {
          (datas === undefined || datas.length === 0)
            ?
            <Result
              title="你还没有可用服务器哦,请点击申请按钮进行申请,或者点击添加按钮进行添加"
              extra={[
                <Button
                  key="1"
                  icon={<PlusOutlined translate="true" />}
                  onClick={this.applyServer}
                >
                  申请
                </Button>,
                <Divider type="vertical" />,
                <Button
                  key="2"
                  icon={<PlusOutlined translate="true" />}
                  onClick={this.add}
                >
                  添加
                </Button>
              ]}
            />
            :
            <>
              <div style={{ backgroundColor: '#FFFFFF' }}>
                <Row
                  gutter={24}
                  style={{ backgroundColor: '#e6f7ff', border: '#91d5ff' }}
                >
                  <Col span={12} />
                  <Col span={12} >
                    <span style={{ float: 'right' }}>
                      <Button
                        key="3"
                        icon={<PlusOutlined translate="true" />}
                        onClick={this.applyServer}
                      >
                        申请服务器
                      </Button>
                      <Divider type="vertical" />
                      <Button
                        key="4"
                        icon={<PlusOutlined translate="true" />}
                        onClick={this.add}
                      >
                        添加服务器
                      </Button>
                      <Divider type="vertical" />
                      <Input.Search
                        placeholder="服务器地址"
                        onSearch={value => console.log(value)}
                        style={{ width: "280px" }}
                      />
                    </span>
                  </Col>
                </Row>
                <List
                  grid={{ column: 2}}
                  itemLayout="vertical"
                  dataSource={[
                    {
                      name: '10.108.210.194',
                      id: '1'
                    },
                    {
                      name: '10.108.210.136',
                      id: '2'
                    },
                  ]}
                  bordered
                  renderItem={item => (
                    <List.Item
                      key={item.id}
                      actions={[
                        <a onClick={this.enterFileManagement} key={`a-${item.id}`}>
                          查看
                        </a>,
                      ]}
                    >
                      <List.Item.Meta
                        avatar={
                          <Avatar> <FolderFilled translate="true" /> </Avatar>
                        }
                        title={<a href="https://ant.design/index-cn">{item.name}</a>}
                        description="Progresser XTech"
                      />
                    </List.Item>
                  )}
                />
              </div>
            </>
        }

        <AddServer onModalOk={this.onModalOk} onModalCancel={this.onModalCancel} isModalVisible={isModalVisible} />

      </>
    )
  }
}