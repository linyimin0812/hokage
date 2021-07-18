import React from 'react'
import { Card, message, Table } from 'antd'
import { Action } from '../../../component/Action'
import { StopOutlined } from '@ant-design/icons'
import store from '../store'
import { MonitorAction } from '../../../axios/action/monitor/monitor-action'
import { MonitorOperateForm } from '../../../axios/action/monitor/monitor-type'
import { getHokageUid } from '../../../libs'
import { ServerVO } from '../../../axios/action/server/server-type'
import tableSearch from '../../common/TableSearch'

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
  dataSource: ProcessInfoVO[],
  serverVO: ServerVO
}

export default class Process extends React.Component<ProcessProp> {

  renderCommand = (_: any, record: ProcessInfoVO, __: number) => {
    return tableSearch.renderHighLight(record.comm, record.command)
  }

  renderAccount = (_: any, record: ProcessInfoVO, __: number) => {
    return tableSearch.renderHighLight(record.account, record.account)
  }

  renderAction = (record: ProcessInfoVO) => {
    return <Action>
      <Action.Confirm
        title={<span><StopOutlined translate style={{color: 'red'}} /></span>}
        action={() => {this.killProcess(record)}}
        content={`确定终止进程${record.pid}, 进程启动命令: ${record.command}`}
      />
    </Action>
  }

  killProcess = (record: ProcessInfoVO) => {
    store.basicLoading = true
    MonitorAction.killProcess(this.assembleOperateForm(record)).then(result => {
      if (result) {
        message.info(`进程${record.pid}, 进程启动命令: ${record.command} 已关闭`)
      } else {
        message.info(`进程${record.pid}, 进程启动命令: ${record.command} 关闭失败`)
      }
    }).catch(e => message.error(e))
      .finally(() => store.basicLoading = false)
  }

  assembleOperateForm = (record: ProcessInfoVO) => {
    const { ip, sshPort, account, id } = this.props.serverVO
    const form: MonitorOperateForm = {
      operatorId: getHokageUid(),
      serverId: id,
      ip: ip,
      sshPort: sshPort,
      account: account,
      pid: record.pid
    }
    return form
  }

  onCommandFilter = (value: string | number | boolean, record: ProcessInfoVO) => record.command.includes(value.toString())

  onAccountFilter = (value: string | number | boolean, record: ProcessInfoVO) => record.account.includes(value.toString())

  render() {
    const { dataSource } = this.props
    return (
      <Card title="进程信息">
        <Table dataSource={dataSource} pagination={false} scroll={{y: 350}} >
          <Table.Column title="pid" dataIndex="pid" />
          <Table.Column
            title="account" dataIndex="account"
            render={this.renderAccount}
            filterDropdown={tableSearch.filterDropdown}
            filterIcon={tableSearch.filterIcon}
            onFilter={this.onAccountFilter}
            onFilterDropdownVisibleChange={tableSearch.onFilterDropdownVisibleChange}
          />
          <Table.Column title="cpu%" dataIndex="cpu" sorter={(a: ProcessInfoVO, b: ProcessInfoVO) => a.cpu > b.cpu ? 1 : -1} />
          <Table.Column title="mem%" dataIndex="mem" sorter={(a: ProcessInfoVO, b: ProcessInfoVO) => a.mem > b.mem ? 1 : -1} />
          <Table.Column title="RSS" dataIndex="rss" />
          {/*<Table.Column title="VSZ" dataIndex="vsz" />*/}
          <Table.Column title="started" dataIndex="started" sorter={(a: ProcessInfoVO, b: ProcessInfoVO) => a.started > b.started ? 1 : -1} />
          <Table.Column
            title={'command'} dataIndex={'comm'} render={this.renderCommand}
            filterDropdown={tableSearch.filterDropdown}
            filterIcon={tableSearch.filterIcon}
            onFilter={this.onCommandFilter}
            onFilterDropdownVisibleChange={tableSearch.onFilterDropdownVisibleChange}
          />
          <Table.Column title="操作" render={this.renderAction} align={'center'} />
        </Table>
      </Card>
    )
  }
}
