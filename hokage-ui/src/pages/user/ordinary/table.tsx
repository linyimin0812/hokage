import React, { ReactText } from 'react'
import { Divider, message, Table, Tag } from 'antd';
import store from './store'
import { getHokageUid, randomColor } from '../../../libs';
import { UserServerOperateForm, UserVO } from '../../../axios/action/user/user-type'
import { ServerVO } from '../../../axios/action/server/server-type'
import { Action } from '../../../component/Action'
import { FormInstance } from 'antd/lib/form'
import { observer } from 'mobx-react'
import { SelectSupervisor } from '../common/select-supervisor'
import { UserAction } from '../../../axios/action';


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
              renderForm={(form: FormInstance) => { return <SelectSupervisor form={form} />}}
              confirmAction={(value: UserServerOperateForm) => {alert(JSON.stringify(value))}}
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
