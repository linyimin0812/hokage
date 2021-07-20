import React from 'react'
import store from './store'
import { message, Table, Tag } from 'antd';
import { getHokageUid, randomColor } from '../../../libs';
import { ServerStatusEnum, ServerUserVO, ServerVO } from '../../../axios/action/server/server-type'
import { Action } from '../../../component/Action'
import { observer } from 'mobx-react'
import { UserServerOperateForm } from '../../../axios/action/user/user-type';
import { ServerAction } from '../../../axios/action/server/server-action';

@observer
export default class OperatorServerTable extends React.Component {

  componentDidMount() {
    store.fetchRecords()
  }

  expandedRowRender = (record: ServerVO) => {
    const nestedRecords = store.accountList || []
    return <Table rowKey={'id'} dataSource={nestedRecords} pagination={false}>
      <Table.Column title={'id'} dataIndex={'id'} />
      <Table.Column title={'姓名'} dataIndex={'username'} />
      <Table.Column title={'账号'} dataIndex={'account'} />
      <Table.Column title={'创建时间'} dataIndex={'createdTime'} />
      <Table.Column title={'最近登录时间'} dataIndex={'latestLoginTime'} />
      <Table.Column title={'操作'} render={this.nestedActionRender} />
    </Table>
  }

  nestedActionRender = (record: ServerUserVO) => {
    return <Action>
      <Action.Confirm title={'删除'} action={async () => {alert('TODO: 添加删除动作')}} content={`确定删除用户${record.username}(${record.id})`} />
    </Action>
  }

  statusRender = (status: number) => {
    if (status === ServerStatusEnum.offline) {
      return <Tag color={'red'}>offline</Tag>
    }
    if (status === ServerStatusEnum.online) {
      return <Tag color={'green'}>online</Tag>
    }
    return <Tag color={'magenta'}>unknown</Tag>
  }

  serverGroupRender = (serverGroupList: string[]) => {
    if (!serverGroupList || serverGroupList.length === 0) {
      return <span>-</span>
    }
    return serverGroupList.map(
      (group, index) => <Tag color={randomColor(group)} key={index}>{group}</Tag>
    )
  }

  serverOperatorRender = (supervisorList: string[]) => {
    if (!supervisorList || supervisorList.length === 0) {
      return <span>-</span>
    }
    return supervisorList.map(
      (tag: string)=> <Tag color={randomColor(tag)} key={tag}>{tag}</Tag>
    )
  }

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

  deleteServer = (record: ServerVO) => {
    const form: UserServerOperateForm = {
      operatorId: getHokageUid(),
      userIds: [getHokageUid()],
      serverIds: [record.id]
    }
    store.isFetching = true
    ServerAction.deleteSupervisorServer(form).then(result => {
      if (result) {
        message.info("删除管理员服务器成功")
      } else {
        message.error("删除管理员服务器失败")
      }
    }).catch(e => message.error(e))
      .finally(() => store.isFetching = false)
  }

  onExpand = (expanded: boolean, record: ServerVO) => {
    if (!expanded) {
      return
    }
    store.fetchAccountList(record.id)
  }
  render() {
    return (
      <Table
        rowKey={'id'}
        loading={store.isFetching}
        dataSource={store.records}
        onExpand={this.onExpand}
        expandedRowRender={this.expandedRowRender}
        pagination={false}
      >
        <Table.Column title={'id'} dataIndex={'id'} />
        <Table.Column title={'主机名'} dataIndex={'hostname'} />
        <Table.Column title={'域名'} dataIndex={'domain'} />
        <Table.Column title={'IP地址'} dataIndex={'ip'} />
        <Table.Column title={'分组'} dataIndex={'serverGroupList'} render={this.serverGroupRender} />
        {
          <Table.Column title={'管理员'} dataIndex={'supervisorList'} render={this.serverOperatorRender} />
        }
        <Table.Column title={'使用人数'} dataIndex={'userNum'} />
        <Table.Column title={'状态'} dataIndex={'status'} render={this.statusRender} />
        <Table.Column title={'描述'} dataIndex={'description'} ellipsis />
        <Table.Column title={'操作'} render={this.actionRender} />
      </Table>
    )
  }
}
