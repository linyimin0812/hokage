import React from 'react'
import store from './store'
import { Table, Tag } from 'antd'
import { randomColor } from '../../../libs'
import { ServerUserVO, ServerVO } from '../../../axios/action/server/server-type'
import { Action } from '../../../component/Action'
import { observer } from 'mobx-react'

@observer
export default class OperatorServerTable extends React.Component {

  componentDidMount() {
    store.fetchRecords()
  }

  expandedRowRender = (record: ServerVO) => {
    const nestedRecords = record.userList || []
    return <Table rowKey={'id'} dataSource={nestedRecords} pagination={false}>
      <Table.Column title={'id'} dataIndex={'id'} />
      <Table.Column title={'姓名'} dataIndex={'username'} />
      <Table.Column title={'账号'} dataIndex={'account'} />
      <Table.Column title={'申请时间'} dataIndex={'serverGroupList'} render={this.serverGroupRender} />
      <Table.Column title={'最近登录时间'} dataIndex={'userNum'} />
      <Table.Column title={'状态'} dataIndex={'status'} render={this.statusRender} />
      <Table.Column title={'操作'} render={this.nestedActionRender} />
    </Table>
  }

  nestedActionRender = (record: ServerUserVO) => {
    return <Action>
      <Action.Confirm title={'禁用'} action={async () => {alert('TODO: 添加禁用动作')}} content={`确定禁用用户${record.username}(${record.id})`} />
      <Action.Confirm title={'启用'} action={async () => {alert('TODO: 添加启用动作')}} content={`确定启用用户${record.username}(${record.id})`} />
      <Action.Confirm title={'删除'} action={async () => {alert('TODO: 添加删除动作')}} content={`确定删除用户${record.username}(${record.id})`} />
    </Action>
  }

  statusRender = (text: string) => text ? <Tag color={randomColor(text)}> {text} </Tag> : null

  serverGroupRender = (serverGroupList: string[]) => serverGroupList.map(
    (group, index) => <Tag color={randomColor(group)} key={index}>{group}</Tag>
  )

  actionRender = (record: ServerVO) => {
    return <Action>
      <Action.Request title={'添加用户'} action={() => {alert('指定服务器')}} />
      <Action.Confirm
        title={'删除'}
        action={async () => {
          alert('TODO: 添加删除动作')
        }}
        content={`确定删除服务器${record.ip}(${record.id})`}
      />
    </Action>
  }

  render() {
    return (
      <Table
        rowKey={'id'}
        loading={store.isFetching}
        dataSource={store.records}
        expandedRowRender={this.expandedRowRender}
      >
        <Table.Column title={'主机名'} dataIndex={'hostname'} />
        <Table.Column title={'域名'} dataIndex={'domain'} />
        <Table.Column title={'IP地址'} dataIndex={'ip'} />
        <Table.Column title={'分组'} dataIndex={'serverGroupList'} render={this.serverGroupRender} />
        <Table.Column title={'使用人数'} dataIndex={'userNum'} />
        <Table.Column title={'状态'} dataIndex={'status'} render={this.statusRender} />
        <Table.Column title={'操作'} render={this.actionRender} />
      </Table>
    )
  }
}
