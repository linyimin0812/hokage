import React, { ReactText } from 'react'
import { Table } from 'antd'
import ApplyAndSearchServer from '../common/apply-and-search-server'
import { ServerVO } from '../../axios/action/server/server-type'
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

  resetFields = () => {
    console.log("reset")
  }

  onSelectChange = (selectedRowKeys: ReactText[]) => {
    store.selectedRowKeys = selectedRowKeys
    // TODO: 从selectRows中获取选择的目标数据,然后进行相关操作
  }
  sync = () => {
    alert("sync operator")
  }

  actionRender = (_: any, record: ServerVO) => {
    return <Action>
      <Action.Button
        title={'登录'}
        action={() => {this.props.addSshTerm(record)}}
      />
    </Action>
  }

  render() {
    const rowSelection = {
      selectedRowKeys: store.selectedRowKeys,
      onChange: this.onSelectChange,
    };
    return (
      <>
        <div style={{ backgroundColor: '#FFFFFF' }}>
          <ApplyAndSearchServer selectionKeys={store.selectedRowKeys} />
          <Table
            dataSource={store.records}
            rowSelection={rowSelection}
            loading={store.isFetching}
          >
            <Table.Column title={'ip'} dataIndex={'ip'} />
            <Table.Column title={'账号'} dataIndex={'account'} />
            <Table.Column title={'登录方式'} dataIndex={'loginType'} />
            <Table.Column title={'我的状态'} dataIndex={'status'} />
            <Table.Column title={'备注'} dataIndex={'description'} />
            <Table.Column title={'操作'} render={this.actionRender} />
          </Table>
        </div>
      </>
    )
  }
}
