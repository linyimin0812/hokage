import React from 'react'
import { Table, Result, Button, Tag } from 'antd';
import BreadcrumbCustom, { BreadcrumbPrpos } from '../BreadcrumbCustom';

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

const columns = [
  {
    title: "id",
    dataIndex: "id",
    key: "id"
  },
  {
    title: "主机名",
    dataIndex: "hostname",
    key: "hostname"
  },
  {
    title: "域名",
    dataIndex: "domainName",
    key: "domainName"
  },
  {
    title: "登录账号",
    dataIndex: "account",
    key: "account"
  },
  {
    title: '服务器状态',
    dataIndex: 'serverStatus',
    key: 'serverStatue',
    render: renderStatus
  },
  {
    title: '我的状态',
    dataIndex: 'myStatus',
    key: 'myStatus',
    render: renderStatus
  },
  {
    title: "备注",
    dataIndex: "remark",
    key: "remark"
  },
  {
    title: "操作",
    dataIndex: "action",
    key: "action"
  },
]
const datas: any[] = []
for (let i = 0; i < 5; i++) {
  const data = {
    key: i + 1,
    id: 'id_' + i,
    hostname: 'master_' + i + ".pcncad.club",
    domainName: 'name_' + i + ".pcncad.club",
    account: "banzhe_" + i,
    serverStatus: i % 2 == 0 ? '掉线' : '在线',
    myStatus: i % 2 == 0 ? '退出' : '登录',
    action: 'Web SSH | 文件管理 | 远程命令 | 更多'
  }
  datas.push(data) 
}



const breadcrumProps: BreadcrumbPrpos[] = [
  {
    name: '首页',
    link: '/app/index'
  },
  {
    name: '我的服务器'
  },
  {
    name: '我使用的服务器'
  }
]
export default class MyServer extends React.Component {

  applyServer = () => {
    window.location.href="/#/app/server/all"
  }
  render() {
    return(
      <>
      <BreadcrumbCustom breadcrumProps={breadcrumProps} />
      {
        (datas === undefined || datas.length === 0) 
        ? 
        <Result
          title="你还没有可用服务器哦,请点击申请按钮进行申请"
          extra={
            <Button type="primary" onClick={ this.applyServer }>
              申请
            </Button>
          }
        />
        :
        <Table columns={columns} dataSource={datas} />                
      }
      </>
    ) 
  }
}