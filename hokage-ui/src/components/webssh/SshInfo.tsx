import React, { ReactText } from 'react'
import { Tag, Result, Button, message, Row, Col, Divider, Table, Input } from 'antd';
import { InfoCircleOutlined, MinusOutlined, PlusOutlined } from '@ant-design/icons';
import AddServer from '../server/AddServer';


const renderStatus = (text: string, _: any, __: any) => {
  let color: string = ''
  switch (text) {
    case '在线':
    case '登录':
      color = 'green'
      break;
    case '掉线':
    case '退出':
      color = 'red'
      break
    default:
      color = 'red'
      break
  }
  return <Tag color={color}> {text} </Tag>
}

const column = [
  {
    title: '服务器地址',
    dataIndex: 'serverIp',
    key: 'serverIp'
  },
  {
    title: '账号',
    dataIndex: 'account',
    key: 'account'
  },
  {
    title: '登录方式',
    dataIndex: 'loginForm',
    keyIndex: 'loginForm'
  },
  {
    title: '我的状态',
    dataIndex: 'status',
    key: 'status',
    render: renderStatus
  },
  {
    title: '备注',
    dataIndex: 'remark',
    key: 'remark'
  },
  {
    title: '操作',
    dataIndex: 'action',
    key: 'action'
  },
]

const datas: any[] = []
for (let i = 0; i < 11; i++) {
  const data = {
    serverIp: '10.108.210' + (i + 5),
    loginForm: (i % 2 === 0) ? '私钥' : '密码',
    status: (i % 2 === 0) ? '退出' : '登录',
    account: "banzhe_" + i,
    remark: '测试',
    action: '登录 | 退出 | 编辑 | 删除',
    key: i
  }
  datas.push(data)
}

type SshInfoState = {
  selectedRowKeys: ReactText[],
  isModalVisible: boolean
}

export default class SshInfo extends React.Component<any, SshInfoState> {
  
  state = {
    selectedRowKeys: [],
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

  onSelectChange = (selectedRowKeys: ReactText[], selectedRows: any[]) => {
    this.setState({ selectedRowKeys })
    // TODO: 从selectRows中获取选择的目标数据,然后进行相关操作
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
  
  render() {
    
    const { selectedRowKeys, isModalVisible } = this.state
    
    const rowSelection = {
      selectedRowKeys,
      onChange: this.onSelectChange,
      
    };
    
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
                  <Col span={12} style={{ display: 'flex', alignItems: 'center' }}>
                    <span>
                      <InfoCircleOutlined
                        translate="true"
                        style={{ color: "#1890ff" }}
                      />
                      已选择{<span style={{ color: "blue" }}>{selectedRowKeys.length}</span>}项
                    </span>
                  </Col>
                  <Col span={12} >
                    <span style={{ float: 'right' }}>
                      {
                        selectedRowKeys.length > 0 ? ([
                          <Button
                            icon={<MinusOutlined translate="true" />}
                            onClick={this.delete}
                          >
                            批量删除
                          </Button>,
                          <Divider type="vertical" />
                        ]) : (
                            null
                          )
                      }
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
                        添加SSH信息
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
                <Table
                  columns={column}
                  dataSource={datas}
                  rowSelection={rowSelection}
                />
              </div>
            </>
        }
        
        <AddServer onModalOk={this.onModalOk} onModalCancel={this.onModalCancel} isModalVisible={isModalVisible} />                      
        
      </>
    )
  }
}