import React from 'react'
import { Card, Table, Tooltip } from 'antd'
import { Action } from '../../../component/Action'
import { StopOutlined } from '@ant-design/icons'

export interface ProcessInfoVO {
  pid: number,
  account: string,
  cpu: number,
  mem: number,
  rss: number,
  vsz: number,
  started: string,
  status: string,
  comm: string,
  command: string,
}

type ProcessProp = {
  dataSource: ProcessInfoVO[]
}

export default class Process extends React.Component<ProcessProp> {

  renderCommand = (_: any, record: ProcessInfoVO, __: number) => {
    return (
      <Tooltip placement={'topLeft'} title={record.command}>
        {record.comm}
      </Tooltip>
    )
  }

  renderAction = (record: ProcessInfoVO) => {
    return <Action>
      <Action.Confirm
        title={<span><StopOutlined translate style={{color: 'red'}} /></span>}
        action={() => {alert('hhhhhh')}}
        content={`确定终止进程${record.pid}, 进程启动命令: ${record.command}`}
      />
    </Action>
  }

  render() {
    const { dataSource } = this.props
    return (
      <Card title="进程信息">
        <Table dataSource={dataSource} pagination={false} scroll={{y: 350}} >
          <Table.Column title="pid" dataIndex="pid" />
          <Table.Column title="account" dataIndex="account" />
          <Table.Column title="cpu%" dataIndex="cpu" sorter={(a: ProcessInfoVO, b: ProcessInfoVO) => a.cpu > b.cpu ? 1 : -1} />
          <Table.Column title="mem%" dataIndex="mem" sorter={(a: ProcessInfoVO, b: ProcessInfoVO) => a.mem > b.mem ? 1 : -1} />
          <Table.Column title="RSS" dataIndex="rss" />
          <Table.Column title="VSZ" dataIndex="vsz" />
          <Table.Column title="started" dataIndex="started" sorter={(a: ProcessInfoVO, b: ProcessInfoVO) => a.started > b.started ? 1 : -1} />
          <Table.Column title="command" dataIndex="comm" render={this.renderCommand} />
          <Table.Column title="操作" render={this.renderAction} align={'center'} />
        </Table>
      </Card>
    )
  }
}
