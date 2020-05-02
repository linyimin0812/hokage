import React from 'react'
import { Table } from 'antd'
import OrdinaryUser from './OrdinaryUser'

const columns = [
  { title: 'id', dataIndex: 'id', key: 'id' },
  { title: '姓名', dataIndex: 'name', key: 'name' },
  { title: '负责的服务器', dataIndex: 'numOfServer', key: 'numOfServer' },
  { title: '标签', dataIndex: 'serverTag', key: 'serverTag' },
  { title: '操作', dataIndex: 'action', key: 'action' }
]

export default class Operator extends React.Component {
  expandedRowRender = () => {
    const columns = [
    {title: '主机名', dataIndex: 'hostname', key: 'hostname'},
    {title: '域名', dataIndex: 'domainName', key: 'domainName'},
    {title: '标签', dataIndex: 'serverTag', key: 'serverTag'},
    {title: '人数', dataIndex: 'numberOfUser', key: 'numOfUser'},
    {title: '状态', dataIndex: 'status', key: 'status'},
  ]
    return <Table columns={columns} dataSource={[]} pagination={false} />;
  }
    render() {
      return (
        <Table 
          columns={columns}
        />
      )
    }
}