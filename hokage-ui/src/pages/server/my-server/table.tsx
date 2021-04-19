import React from 'react'
import { Table, Tag } from 'antd'
import store from './store'
import { randomColor } from '../../../libs'
import { ServerVO } from '../../../axios/action/server/server-type'
import { Action } from '../../../component/Action'
import { observer } from 'mobx-react'
@observer
export default class MyServerTable extends React.Component {

  componentDidMount() {
    store.fetchRecords()
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
      >
        <Table.Column title={'主机名'} dataIndex={'hostname'} />
        <Table.Column title={'域名'} dataIndex={'domain'} />
        <Table.Column title={'IP地址'} dataIndex={'ip'} />
        <Table.Column title={'分组'} dataIndex={'serverGroupList'} render={this.serverGroupRender} />
        <Table.Column title={'账号'} dataIndex={'account'} />
        <Table.Column title={'服务器状态'} dataIndex={'status'} render={this.statusRender} />
        <Table.Column title={'账号状态'} dataIndex={'accountStatus'} render={this.statusRender} />
        <Table.Column title={'备注'} dataIndex={'description'} />
        <Table.Column title={'操作'} render={this.actionRender} />
      </Table>
    )
  }
}
