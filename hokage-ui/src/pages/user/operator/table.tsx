import React from 'react'
import { observer } from 'mobx-react'
import { message, Table, Tag } from 'antd'
import store from './store'
import { UserServerOperateForm, UserVO } from '../../../axios/action/user/user-type'
import { getHokageUid, randomColor } from '../../../libs'
import { Action } from '../../../component/Action'
import { FormInstance } from 'antd/lib/form'
import { SelectServer } from '../common/select-server'
import { ServerVO } from '../../../axios/action/server/server-type'
import { UserAction } from '../../../axios/action'
import { UserSearchFormType } from '../common/search'
import serverSelectStore from '../store'

@observer
export default class OperatorTable extends React.Component {

  componentDidMount() {
    const form: UserSearchFormType = {
      operatorId: getHokageUid()
    }
    store.fetchRecords(form)
  }

  expandedRowRender = (userVO: UserVO) => {
    return <Table rowKey={'id'} dataSource={store.nestedRecords} pagination={false}>
      <Table.Column title={'id'} dataIndex={'id'} />
      <Table.Column title={'主机名'} dataIndex={'hostname'} />
      <Table.Column title={'域名'} dataIndex={'domain'} />
      <Table.Column title={'ip'} dataIndex={'ip'} />
      <Table.Column title={'port'} dataIndex={'sshPort'} />
      <Table.Column title={'管理账号'} dataIndex={'account'} />
      <Table.Column title={'服务器分组'} dataIndex={'serverGroupList'} render={this.serverGroupRender} />
      <Table.Column title={'使用人数'} dataIndex={'userNum'} />
      <Table.Column title={'状态'} dataIndex={'status'} render={this.serverStatusRender} />
      <Table.Column title={'操作'} render={(record: ServerVO) => this.nestedActionRender(userVO.id, record)} />
    </Table>
  }

  nestedActionRender = (supervisorId: number, record: ServerVO) => {
    return <Action>
      <Action.Confirm
        title={'回收'}
        action={() => {
          const form: UserServerOperateForm = {
            operatorId: getHokageUid(),
            serverIds: [record.id],
            userIds: [supervisorId]
          }
          this.recycleSupervisorServer(form)
        }}
        content={`确定回收服务器${record.account}@${record.ip}(${record.id})`}
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

  deleteSupervisor = (record: UserVO) => {
    store.isFetching = true
    const form: UserServerOperateForm = {
      operatorId: getHokageUid(),
      userIds: [record.id]
    }
    UserAction.deleteSupervisor(form).then(result => {
      if (result) {
        store.fetchRecords()
      } else {
        message.error("删除管理员失败")
      }
    }).catch(e => message.error(e))
      .finally(() => store.isFetching = false)
  }

  actionRender = (_: any, record: UserVO) => {
    return <Action>
      <Action.Form
        title={'添加服务器'}
        renderForm={(form: FormInstance) => {
          return <SelectServer form={form} />
        }}
        confirmAction={(form: UserServerOperateForm) => {
          form.userIds = [record.id]
          this.addServerToSupervisor(form)
        }}
        onClickAction={() => { serverSelectStore.fetchNotGrantedServerList(record.id) }}
      />
      <Action.Form
        title={'回收服务器'}
        renderForm={(form: FormInstance) => { return <SelectServer form={form} />}}
        confirmAction={(value: UserServerOperateForm) => {
          value.operatorId = getHokageUid()
          value.userIds = [record.id]
          this.recycleSupervisorServer(value)
        }}
        onClickAction={() => { serverSelectStore.fetchHasGrantedServerList(record.id) }}
      />
      <Action.Confirm
        title={'删除'}
        action={() => {this.deleteSupervisor(record)}}
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

  recycleSupervisorServer = (form: UserServerOperateForm) => {
    UserAction.recycleSupervisorServer(form).then(result => {
      if (result) {
        message.info('回收服务器成功');
        this.searchSupervisorServer(form.userIds![0])
      } else {
        message.error('回收服务器失败');
      }
    }).catch(e => message.error(e));
  }

  onExpend = (expanded: boolean, record: UserVO) => {
    if (expanded) {
      this.searchSupervisorServer(record.id)
    }
  }

  searchSupervisorServer = (supervisorId: number) => {
    store.isFetching = true
    const form: UserServerOperateForm = {
      operatorId: getHokageUid(),
      userIds: [supervisorId],
    }
    UserAction.searchSupervisorServer(form).then(value => {
      store.nestedRecords = value || []
    }).catch(e => message.error(e))
      .finally(() => store.isFetching = false)
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
