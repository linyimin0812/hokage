import React from 'react'
import { Table } from 'antd';

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

export default class MyServer extends React.Component {
  render() {
    return(
      <Table columns={columns} dataSource={datas} />
    ) 
  }
}