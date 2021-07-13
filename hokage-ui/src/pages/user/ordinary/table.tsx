import React from 'react'
import { Divider, message, Table, Tag } from 'antd'
import store from './store'
import { getHokageUid, randomColor } from '../../../libs'
import { UserServerOperateForm, UserVO } from '../../../axios/action/user/user-type'
import { ServerSearchForm, ServerVO } from '../../../axios/action/server/server-type';
import { Action } from '../../../component/Action'
import { FormInstance } from 'antd/lib/form'
import { observer } from 'mobx-react'
import { SelectSupervisor } from '../common/select-supervisor'
import { UserAction } from '../../../axios/action'
import { SelectServer } from '../common/select-server'
import serverSelectStore from '../store'
import { ServerAction } from '../../../axios/action/server/server-action';


@observer
export default class OrdinaryTable extends React.Component {

  serverGroupRender = (groupList: string[]) => {
    if (!groupList || groupList.length === 0) {
      return <span>-</span>
    }
    return groupList.map(
      (tag: string)=> <Tag color={randomColor(tag)} key={tag}>{tag}</Tag>
    )
  }

  serverStatusRender = (status: string) => {
    if (!status) {
      return <span>-</span>
    }
    return <Tag color = {randomColor(status)}> { status } </Tag>
  }

  nestedActionRender = (subordinateId: number, record: ServerVO) => {
    return <Action>
      <Action.Confirm
        title={'回收'}
        action={() => {
          const form: UserServerOperateForm = {
            operatorId: getHokageUid(),
            serverIds: [record.id],
            userIds: [subordinateId]
          }
          this.recycleSubordinateServer(form)
        }}
        content={`确定回收账号${record.account}@${record.ip}`}
      />
    </Action>
  }

  recycleSubordinateServer = (form: UserServerOperateForm) => {
    store.isFetching = true
    UserAction.recycleSubordinateServer(form).then(result => {
      if (result) {
        message.info("回收账号成功")
        const searchForm: ServerSearchForm = {
          operatorId: getHokageUid(),
          userId:form.userIds![0]
        }
        store.isFetching = true
        ServerAction.searchSubordinateServer(searchForm).then(value => {
          store.serverVOList = value || []
        }).catch(e => message.error(e))
          .finally(() => store.isFetching = false)
      } else {
        message.error("回收账号失败")
      }
    }).catch(e => message.error(e))
      .finally(() => store.isFetching = false)
  }

  expandedRowRender = (userVO: UserVO) => {
    return <Table rowKey={'id'} dataSource={store.serverVOList} pagination={false}>
      <Table.Column title={'主机名'} dataIndex={'hostname'} />
      <Table.Column title={'域名'} dataIndex={'domain'} />
      <Table.Column title={'ip地址'} dataIndex={'ip'} />
      <Table.Column title={'port'} dataIndex={'sshPort'} />
      <Table.Column title={'账号'} dataIndex={'account'} />
      <Table.Column title={'服务器分组'} dataIndex={'serverGroupList'} render={this.serverGroupRender} />
      <Table.Column title={'状态'} dataIndex={'status'} render={this.serverStatusRender} />
      <Table.Column title={'操作'} render={(record: ServerVO) => this.nestedActionRender(userVO.id, record)} />
    </Table>
  }

  addUserToSupervisor = (form: UserServerOperateForm) => {
    UserAction.addUserToSupervisor(form).then(result => {
      if (result) {
        message.info("添加管理成功")
        store.fetchRecords({operatorId: getHokageUid()})
      } else {
        message.error("添加管理员失败")
      }
    }).catch(e => message.error(e))
  }

  deleteUserSupervisor = (record: UserVO) => {
    const form: UserServerOperateForm = {
      operatorId: getHokageUid(),
      supervisorId: record.supervisorId,
      userIds: [record.id]
    }
    UserAction.deleteUserSupervisor(form).then(result => {
      if (result) {
        message.info("移除管理成功")
        store.fetchRecords({operatorId: getHokageUid()})
      } else {
        message.error("移除管理员失败")
      }
    }).catch(e => message.error(e))
  }

  addUserAccount =(form: UserServerOperateForm) => {
    store.isFetching = true
    UserAction.grantSubordinateServer(form).then(result => {
      if (result) {
        message.info("添加账号成功")
        store.fetchRecords({operatorId: getHokageUid()})
      } else {
        message.error("添加账号失败")
      }
    }).catch(e => message.error(e))
      .finally(() => store.isFetching = false)
  }

  actionRender = (_: any, record: UserVO) => {
    return <Action>
      {
        record.supervisorId ?
          <>
            <Action.Confirm
              title={'移除管理员'}
              action={() => this.deleteUserSupervisor(record)}
              content={`确定移除${record.supervisorName}(${record.supervisorId})`}
            />
            <Divider type={'vertical'} />
            <Action.Form
              title={'添加账号'}
              renderForm={(form: FormInstance) => {
                return <SelectServer form={form} />
              }}
              confirmAction={(form: UserServerOperateForm) => {
                form.operatorId = getHokageUid()
                form.userIds = [record.id]
                this.addUserAccount(form)
              }}
              onClickAction={() => { serverSelectStore.fetchHasGrantedServerList(record.supervisorId) }}
            />
          </> :
          <>
            <Action.Form
              title={'设置管理员'}
              renderForm={(form: FormInstance) => {
                return <SelectSupervisor form={form} />
              }}
              confirmAction={(form: UserServerOperateForm) => {
                form.userIds = [record.id]
                form.operatorId = getHokageUid()
                this.addUserToSupervisor(form)
              }}
              onClickAction={() => { store.fetchSupervisorList() }}
            />
          </>
      }
    </Action>
  }

  onExpend = (expanded: boolean, record: UserVO) => {
    if (expanded) {
      store.isFetching = true
      const form: ServerSearchForm = {
        operatorId: getHokageUid(),
        role: record.role,
        userId: record.id
      }
      ServerAction.searchSubordinateServer(form).then(value => {
        store.serverVOList = value || []
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
        onExpand={this.onExpend}
        expandedRowRender={this.expandedRowRender}
        pagination={false}
      >
        <Table.Column title={'id'} dataIndex={'id'} />
        <Table.Column title={'姓名'} dataIndex={'username'} />
        <Table.Column title={'管理员'} dataIndex={'supervisorName'} />
        <Table.Column title={'使用服务器数量'} dataIndex={'serverNum'} />
        <Table.Column title={'服务器分组'} dataIndex={'serverGroupList'} render={this.serverGroupRender} />
        <Table.Column title={'操作'} render={this.actionRender} />
      </Table>
    )
  }
}
