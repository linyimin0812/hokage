import React from 'react'
import { observer } from 'mobx-react'
import { Table, Tag } from 'antd';
import store from './store'
import { UserServerOperateForm, UserVO } from '../../../axios/action/user/user-type'
import { randomColor } from '../../../libs';
import { Action } from '../../../component/Action'
import { FormInstance } from 'antd/lib/form'
import { SelectServer } from '../../server/select-server'

@observer
export default class OperatorTable extends React.Component {

  componentDidMount() {
    store.fetchRecords()
  }

  expandedRowRender = (record: UserVO) => {
    const nestedRecords = record.serverVOList || []
    return <Table rowKey={'id'} dataSource={nestedRecords} pagination={false}>
      <Table.Column title={'id'} dataIndex={'id'} />
      <Table.Column title={'主机名'} dataIndex={'hostname'} />
      <Table.Column title={'域名'} dataIndex={'domain'} />
      <Table.Column title={'服务器分组'} dataIndex={'serverGroupList'} render={this.serverGroupRender} />
      <Table.Column title={'使用人数'} dataIndex={'userNum'} />
      <Table.Column title={'状态'} dataIndex={'status'} render={this.serverStatusRender} />
      <Table.Column title={'操作'} render={this.actionRender} />
    </Table>
  }

  serverStatusRender = (status: string) => {
    return <Tag color = {randomColor(status)}> { status } </Tag>
  }

  serverGroupRender = (groupList: string[]) => {
    return groupList.map(
      (tag: string)=> <Tag color={randomColor(tag)} key={tag}>{tag}</Tag>
    )
  }

  actionRender = (_: any, record: UserVO) => {
    return <Action>
      <Action.Form
        title={'添加服务器'}
        renderForm={(form: FormInstance) => { return <SelectServer form={form} />}}
        action={(value: UserServerOperateForm) => {alert(JSON.stringify(value))}}
      />
      <Action.Form
        title={'回收服务器'}
        renderForm={(form: FormInstance) => { return <SelectServer form={form} />}}
        action={(value: UserServerOperateForm) => {alert(JSON.stringify(value))}}
      />
      <Action.Confirm
        title={'删除'}
        action={async () => {alert('TODO: 添加删除动作')}}
        content={`确定删除管理员${record.username}(${record.id})`}
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
        <Table.Column title={'id'} dataIndex={'id'} />
        <Table.Column title={'姓名'} dataIndex={'username'} />
        <Table.Column title={'服务器数量'} dataIndex={'serverNum'} />
        <Table.Column title={'服务器分组'} dataIndex={'serverGroupList'} render={this.serverGroupRender} />
        <Table.Column title={'操作'} render={this.actionRender} />
      </Table>
    )
  }
}
