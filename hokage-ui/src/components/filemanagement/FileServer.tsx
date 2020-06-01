import React from 'react'
import { Result, Button, message, Row, Col, Divider, Input } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import AddServer from '../server/AddServer';
import ServerCard from '../common/ServerCard';

const datas: any[] = [1,2,3,4,5,6,7,8,9,10]


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
  
  renderServerCards = () => {
    return datas.map(value => {
      console.log(value)
      return (
        <Col span={8}>
          <ServerCard serverType="test" serverIp={"192.182.92." + value} description="测试" action="文件管理" />
        </Col>
      )
    })
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
            <div style={{ backgroundColor: '#FFFFFF' }}>
              <Row
                gutter={24}
                style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', padding: '4px 0px' }}
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
              <Row gutter={24} style={{ paddingTop: '4px' }}>
                {this.renderServerCards()}
              </Row>
            </div>
        }

        <AddServer onModalOk={this.onModalOk} onModalCancel={this.onModalCancel} isModalVisible={isModalVisible} />

      </>
    )
  }
}