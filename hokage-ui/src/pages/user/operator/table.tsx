import React from 'react'
import { observer } from 'mobx-react'
import { message, Table, Tag } from 'antd';
import store from './store'
import { UserServerOperateForm, UserVO } from '../../../axios/action/user/user-type'
import { getHokageRole, randomColor } from '../../../libs';
import { Action } from '../../../component/Action'
import { FormInstance } from 'antd/lib/form'
import { SelectServer } from '../../server/common/select-server'
import { ServerSearchForm, ServerVO } from '../../../axios/action/server/server-type';
import { UserAction } from '../../../axios/action';
import { ServerAction } from '../../../axios/action/server/server-action';

@observer
export default class OperatorTable extends React.Component {

  componentDidMount() {
    store.fetchRecords()
  }

  expandedRowRender = () => {
    return <Table rowKey={'id'} dataSource={store.nestedRecords} pagination={false}>
      <Table.Column title={'id'} dataIndex={'id'} />
      <Table.Column title={'主机名'} dataIndex={'hostname'} />
      <Table.Column title={'域名'} dataIndex={'domain'} />
      <Table.Column title={'服务器分组'} dataIndex={'serverGroupList'} render={this.serverGroupRender} />
      <Table.Column title={'使用人数'} dataIndex={'userNum'} />
      <Table.Column title={'状态'} dataIndex={'status'} render={this.serverStatusRender} />
      <Table.Column title={'操作'} render={this.nestedActionRender} />
    </Table>
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

  serverStatusRender = (status: string) => {
    if (!status) {
      return <span>-</span>
    }
    return <Tag color = {randomColor(status)}> { status } </Tag>
  }

  serverGroupRender = (groupList: string[]) => {
    if (!groupList || groupList.length === 0) {
      return <span>-</span>
    }
    return groupList.map(
      (tag: string)=> <Tag color={randomColor(tag)} key={tag}>{tag}</Tag>
    )
  }

  actionRender = (_: any, record: UserVO) => {
    return <Action>
      <Action.Form
        title={'添加服务器'}
        renderForm={(form: FormInstance) => { return <SelectServer form={form} />}}
        action={(form: UserServerOperateForm) => {
          form.userIds = [record.id]
          this.addServerToSupervisor(form)
        }}
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

  addServerToSupervisor = (form: UserServerOperateForm) => {
    store.isFetching = true
    UserAction.grantSupervisorServer(form).then(result => {
      if (result) {
        message.info('添加成功')
        store.fetchRecords()
      } else {
        message.error('添加失败')
      }
    }).catch(e => message.error(e))
      .finally(() => store.isFetching = false)
  }

  onExpend = (expanded: boolean, record: UserVO) => {
    if (expanded) {
      store.isFetching = true
      const form: ServerSearchForm = {
        operatorId: record.id,
        role: getHokageRole()
      }
      ServerAction.searchServer(form).then(value => {
        store.nestedRecords = value || []
      }).catch(e => message.error(e))
        .finally(() => store.isFetching = false)
    }
  }

  render() {
    return (
      <Table
        rowKey={'id'}
        loading={store.isFetching}
        dataSource={store.records}
        expandedRowRender={this.expandedRowRender}
        onExpand={this.onExpend}
        pagination={false}
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
