import React, { ReactText } from 'react'
import { Tag, Table } from 'antd'
import ApplyServerPrompt from '../common/ApplyServerPrompt'
import ApplyAndSearchServer from '../common/ApplyAndSearchServer'


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
  selectedRowKeys: ReactText[]
}

export default class SshInfo extends React.Component<any, SshInfoState> {
  
  state = {
    selectedRowKeys: [],
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
  sync = () => {
    alert("sync operator")
  }
  
  render() {
    
    const { selectedRowKeys } = this.state
    
    const rowSelection = {
      selectedRowKeys,
      onChange: this.onSelectChange,
      
    };
    
    return (
      <>
        {
          (datas === undefined || datas.length === 0)
            ? <ApplyServerPrompt />
            :
            <>
              <div style={{ backgroundColor: '#FFFFFF' }}>
                <ApplyAndSearchServer selectionKeys={selectedRowKeys} />
                <Table
                  columns={column}
                  dataSource={datas}
                  rowSelection={rowSelection}
                />
              </div>
            </>
        }
      </>
    )
  }
}