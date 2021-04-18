import React, { ReactText } from 'react'
import { Table, Tag } from 'antd'
import store from './store'
import { randomColor } from '../../../libs'
import { UserServerOperateForm, UserVO } from '../../../axios/action/user/user-type'
import { ServerVO } from '../../../axios/action/server/server-type'
import { Action } from '../../../component/Action'
import { FormInstance } from 'antd/lib/form'
import { SelectServer } from '../../server/common/select-server'
import { observer } from 'mobx-react'
@observer
export default class OrdinaryTable extends React.Component {

  serverGroupRender = (groupList: string[]) => {
    return groupList.map(
      (tag: string)=> <Tag color={randomColor(tag)} key={tag}>{tag}</Tag>
    )
  }

  serverStatusRender = (status: string) => {
    return <Tag color = {randomColor(status)}> { status } </Tag>
  }

  nestedActionRender = (record: ServerVO) => {
    return <Action>
      <Action.Confirm
        title={'删除'}
        action={async () => {alert('TODO: 添加删除动作')}}
        content={`确定删除服务器${record.ip}(${record.id})`}
      />
    </Action>
  }

  expandedRowRender = (record: UserVO) => {
    const nestedRecords = record.serverVOList || []
    return <Table rowKey={'id'} dataSource={nestedRecords} pagination={false}>
      <Table.Column title={'主机名'} dataIndex={'hostname'} />
      <Table.Column title={'域名'} dataIndex={'domain'} />
      <Table.Column title={'ip地址'} dataIndex={'ip'} />
      <Table.Column title={'服务器分组'} dataIndex={'serverGroupList'} render={this.serverGroupRender} />
      <Table.Column title={'状态'} dataIndex={'status'} render={this.serverStatusRender} />
      <Table.Column title={'操作'} render={this.nestedActionRender} />
    </Table>
  }

  actionRender = (_: any, record: UserVO) => {
    return <Action>
      <Action.Form
        title={'添加服务器'}
        renderForm={(form: FormInstance) => { return <SelectServer form={form} />}}
        action={(value: UserServerOperateForm) => {alert(JSON.stringify(value))}}
      />
      <Action.Confirm
        title={'删除'}
        action={async () => {alert('TODO: 添加删除动作')}}
        content={`确定删除用户${record.username}(${record.id})`}
      />
    </Action>
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
  render() {
    return (
      <Table
        rowKey={'id'}
        loading={store.isFetching}
        dataSource={store.records}
        expandedRowRender={this.expandedRowRender}
        rowSelection={this.configRowSelection()}
      >
        <Table.Column title={'id'} dataIndex={'id'} />
        <Table.Column title={'姓名'} dataIndex={'username'} />
        <Table.Column title={'使用服务器数量'} dataIndex={'serverNum'} />
        <Table.Column title={'服务器分组'} dataIndex={'serverGroupList'} render={this.serverGroupRender} />
        <Table.Column title={'操作'} render={this.actionRender} />
      </Table>
    )
  }
}
