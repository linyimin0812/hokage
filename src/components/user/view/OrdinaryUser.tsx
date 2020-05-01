import React from 'react'
import { Table } from 'antd';

const columns = [
  {title: '主机名', dataIndex: 'hostname', key: 'hostname'},
  {title: '域名', dataIndex: 'domainName', key: 'domainName'},
  {title: '标签', dataIndex: 'serverTag', key: 'serverTag'},
  {title: '人数', dataIndex: 'numberOfUser', key: 'numOfUser'},
  {title: '状态', dataIndex: 'status', key: 'status'},
]

export class OrdinaryUser extends React.Component {
  render() {
    return (
      <Table
        columns={columns}
        dataSource={[]}
        pagination={false}
        expandedRowRender={(record: any) => <p style={{margin: 0}}>{record.description}</p>}
        expandedRowKeys={columns.map(column => column.key)}
      />
    )
  }
}