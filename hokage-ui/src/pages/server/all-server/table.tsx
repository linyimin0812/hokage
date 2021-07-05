import React, { ReactText } from 'react'
import { Table, Tag } from 'antd'
import store from './store'
import { randomColor } from '../../../libs'
import { ServerVO } from '../../../axios/action/server/server-type'
import { Action } from '../../../component/Action'
import { observer } from 'mobx-react'

@observer
export default class AllServerTable extends React.Component {

  componentDidMount() {
    store.fetchRecords({})
  }

  configRowSelection = () => {
    return {
      selectedRowKeys: store.selectedRowKeys,
      onChange: (selectedRowKeys: ReactText[], _: any): void => { store.selectedRowKeys = selectedRowKeys },
      selections: [
        Table.SELECTION_ALL,
        Table.SELECTION_INVERT,
      ],
    }
  }

  serverGroupRender = (groupList: string[]) => {
    return groupList.map(
      (tag: string)=> <Tag color={randomColor(tag)} key={tag}>{tag}</Tag>
    )
  }

  serverOperatorRender = (supervisorList: string[]) => {
    return supervisorList.map(
      (tag: string)=> <Tag color={randomColor(tag)} key={tag}>{tag}</Tag>
    )
  }

  actionRender = (record: ServerVO) => {
    return <Action>
      <Action.Request title={'指定服务器'} action={() => {alert('指定服务器')}} />
      <Action.Request title={'申请服务器'} action={() => {alert('申请服务器')}} />
      <Action.Request title={'申请管理员'} action={() => {alert('申请管理员')}} />
      <Action.Confirm
        title={'删除'}
        action={async () => {alert('TODO: 添加删除动作')}}
        content={`确定删除服务器${record.ip}(${record.id})`}
      />
    </Action>
  }

  render () {
    return (
      <Table
        rowKey={'id'}
        loading={store.isFetching}
        dataSource={store.records}
        rowSelection={this.configRowSelection()}
        pagination={false}
      >
        <Table.Column title={'主机名'} dataIndex={'hostname'} />
        <Table.Column title={'域名'} dataIndex={'domain'} />
        <Table.Column title={'IP地址'} dataIndex={'ip'} />
        <Table.Column title={'port'} dataIndex={'sshPort'} />
        <Table.Column title={'分组'} dataIndex={'serverGroupList'} render={this.serverGroupRender} />
        <Table.Column title={'管理员'} dataIndex={'supervisorList'} render={this.serverOperatorRender} />
        <Table.Column title={'使用人数'} dataIndex={'userNum'} />
        <Table.Column title={'状态'} dataIndex={'status'} />
        <Table.Column title={'操作'} render={this.actionRender} />
      </Table>
    )
  }
}
