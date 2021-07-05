import React, { ReactText } from 'react'
import { Button, Col, Row, Table } from 'antd';
import { ServerVO } from '../../axios/action/server/server-type'
import { Action } from '../../component/Action'
import { observer } from 'mobx-react'
import store from './store'
import { InfoCircleOutlined, MinusOutlined } from '@ant-design/icons'

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

  renderSelection = () => {
    if (!store.selectedRowKeys || store.selectedRowKeys.length === 0) {
      return null
    }
    return (
      <Row gutter={24} style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', padding: '4px 0px' }}>
        <Col span={8} style={{ display: 'flex', alignItems: 'center' }}>
          <span>
            <InfoCircleOutlined translate="true" style={{ color: '#1890ff' }} />已选择{<span style={{ color: 'blue' }}>{store.selectedRowKeys.length}</span>}项
          </span>
        </Col>
        <Col span={16}>
          <span style={{ float: 'right' }}>
            <Button icon={<MinusOutlined translate="true" />} onClick={() => alert('批量登录')}>批量登录</Button>
          </span>
        </Col>
      </Row>
    )
  }

  render() {
    const rowSelection = {
      selectedRowKeys: store.selectedRowKeys,
      onChange: this.onSelectChange,
    };
    return (
      <>
        <div style={{ backgroundColor: '#FFFFFF' }}>
          {this.renderSelection()}
          <Table
            dataSource={store.records}
            rowSelection={rowSelection}
            loading={store.isFetching}
            pagination={false}
          >
            <Table.Column title={'id'} dataIndex={'id'} />
            <Table.Column title={'ip'} dataIndex={'ip'} />
            <Table.Column title={'port'} dataIndex={'SshPort'} />
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
