import React from 'react'
import { message, Row, Col } from 'antd';
import ServerCard from '../common/ServerCard';
import ApplyServerPrompt from '../common/ApplyServerPrompt';
import ApplyAndSearchServer from '../common/ApplyAndSearchServer';

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

    return (
      <>
        {
          (datas === undefined || datas.length === 0)
            ?
            <ApplyServerPrompt />
            :
            <div style={{ backgroundColor: '#FFFFFF' }}>
              <ApplyAndSearchServer />
              <Row gutter={24} style={{ paddingTop: '4px' }}>
                {this.renderServerCards()}
              </Row>
            </div>
        }

      </>
    )
  }
}