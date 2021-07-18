import React from 'react'
import { Table, Tag } from 'antd'
import store from './store'
import { randomColor } from '../../../libs'
import { ServerStatusEnum, ServerVO } from '../../../axios/action/server/server-type';
import { Action } from '../../../component/Action'
import { observer } from 'mobx-react'
@observer
export default class MyServerTable extends React.Component {

  componentDidMount() {
    store.fetchRecords()
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

  serverGroupRender = (serverGroupList: string[]) => serverGroupList.map(
    (group, index) => <Tag color={randomColor(group)} key={index}>{group}</Tag>
  )

  actionRender = (record: ServerVO) => {
    return <Action>
      <Action.Request title={'Web终端'} action={() => {alert('打开终端')}} />
      <Action.Request title={'文件管理'} action={() => {alert('文件管理')}} />
      <Action.Request title={'资源监控'} action={() => {alert('资源监控')}} />
    </Action>
  }

  render() {
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
        <Table.Column title={'分组'} dataIndex={'serverGroupList'} render={this.serverGroupRender} />
        <Table.Column title={'账号'} dataIndex={'account'} />
        <Table.Column title={'状态'} dataIndex={'status'} render={this.statusRender} />
        <Table.Column title={'备注'} dataIndex={'description'} ellipsis />
        <Table.Column title={'操作'} width={'20%'} render={this.actionRender} />
      </Table>
    )
  }
}
