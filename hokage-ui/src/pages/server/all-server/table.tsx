import React from 'react'
import { message, Table, Tag } from 'antd';
import store from './store'
import { getHokageRole, getHokageUid, randomColor } from '../../../libs';
import { ServerVO } from '../../../axios/action/server/server-type'
import { Action } from '../../../component/Action'
import { observer } from 'mobx-react'
import { UserRoleEnum, UserServerOperateForm } from '../../../axios/action/user/user-type';
import { ServerAction } from '../../../axios/action/server/server-action';

@observer
export default class AllServerTable extends React.Component {

  componentDidMount() {
    store.fetchRecords({})
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
      <Action.Request title={'编辑'} action={() => {alert('编辑')}} />
      <Action.Confirm
        title={'删除'}
        action={async () => {this.deleteServer(record.id)}}
        content={`确定删除服务器${record.ip}(${record.id})`}
      />
    </Action>
  }

  deleteServer = (serverId: number) => {
    const form: UserServerOperateForm = {
      operatorId: getHokageUid(),
      serverIds: [serverId]
    }
    store.isFetching = true
    ServerAction.deleteServer(form).then(result => {
      if (result) {
        message.info("删除成功")
        store.fetchRecords()
      } else {
        message.error('删除失败')
      }
    }).catch(e => message.error(e))
      .finally(() => store.isFetching = false)

  }

  render () {
    const role = getHokageRole()
    return (
      <Table
        rowKey={'id'}
        loading={store.isFetching}
        dataSource={store.records}
        pagination={false}
      >
        <Table.Column title={'id'} dataIndex={'id'} />
        <Table.Column title={'主机名'} dataIndex={'hostname'} />
        <Table.Column title={'域名'} dataIndex={'domain'} />
        <Table.Column title={'IP地址'} dataIndex={'ip'} />
        <Table.Column title={'port'} dataIndex={'sshPort'} />
        <Table.Column title={'分组'} dataIndex={'serverGroupList'} render={this.serverGroupRender} />
        <Table.Column title={'管理员'} dataIndex={'supervisorList'} render={this.serverOperatorRender} />
        <Table.Column title={'使用人数'} dataIndex={'userNum'} />
        <Table.Column title={'状态'} dataIndex={'status'} />
        <Table.Column title={'描述'} dataIndex={'description'} ellipsis />
        {
          role === UserRoleEnum.super_operator ? <Table.Column title={'操作'} render={this.actionRender} /> : null
        }
      </Table>
    )
  }
}
