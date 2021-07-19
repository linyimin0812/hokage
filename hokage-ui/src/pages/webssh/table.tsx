import React from 'react'
import { Table, Tag } from 'antd';
import { ServerStatusEnum, ServerVO } from '../../axios/action/server/server-type';
import { Action } from '../../component/Action'
import { observer } from 'mobx-react'
import store from './store'

type SshInfoProps = {
  addSshTerm: (record: ServerVO) => void
}

@observer
export default class WebSshServer extends React.Component<SshInfoProps> {

  componentDidMount() {
    store.fetchRecords()
  }

  onFinish = (value: any) => {
    console.log(value)
  }

  actionRender = (_: any, record: ServerVO) => {
    return <Action>
      <Action.Button
        title={'登录'}
        action={() => {this.props.addSshTerm(record)}}
      />
    </Action>
  }

  renderLoginType = (loginType: String) => {
    if (loginType === '1') {
      return <span>密钥</span>
    }
    return <span>密码</span>
  }

  renderStatus = (status: number) => {
    if (status === ServerStatusEnum.offline) {
      return <Tag color={'red'}>offline</Tag>
    }
    if (status === ServerStatusEnum.online) {
      return <Tag color={'green'}>online</Tag>
    }
    return <Tag color={'magenta'}>unknown</Tag>
  }

  render() {

    return (
      <>
        <div style={{ backgroundColor: '#FFFFFF' }}>
          <Table
            dataSource={store.records}
            loading={store.isFetching}
            pagination={false}
          >
            <Table.Column title={'id'} dataIndex={'id'} />
            <Table.Column title={'主机名'} dataIndex={'hostname'} />
            <Table.Column title={'ip'} dataIndex={'ip'} />
            <Table.Column title={'port'} dataIndex={'sshPort'} />
            <Table.Column title={'账号'} dataIndex={'account'} />
            <Table.Column title={'登录方式'} dataIndex={'loginType'} render={this.renderLoginType} />
            <Table.Column title={'服务器状态'} dataIndex={'status'} render={this.renderStatus} />
            <Table.Column title={'描述'} dataIndex={'description'} ellipsis />
            <Table.Column title={'操作'} render={this.actionRender} />
          </Table>
        </div>
      </>
    )
  }
}
