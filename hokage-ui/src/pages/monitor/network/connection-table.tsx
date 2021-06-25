import React from 'react'
import { Card, Table } from 'antd'
import tableSearch from '../../common/TableSearch'

export interface ConnectionInfoVO {
  protocol: string,
  localIp: string,
  foreignIp: string,
  status: string,
  process: string
}

type ConnectionTableProp = {
  dataSource: ConnectionInfoVO[]
}

export default class ConnectionTable extends React.Component<ConnectionTableProp> {

  renderLocalIp = (_: any, record: ConnectionInfoVO, __: number) => {
    return tableSearch.renderHighLight(record.localIp, record.localIp)
  }

  renderForeignIp = (_: any, record: ConnectionInfoVO, __: number) => {
    return tableSearch.renderHighLight(record.foreignIp, record.foreignIp)
  }

  renderStatus = (_: any, record: ConnectionInfoVO, __: number) => {
    return tableSearch.renderHighLight(record.status, record.status)
  }

  renderProcess = (_: any, record: ConnectionInfoVO, __: number) => {
    return tableSearch.renderHighLight(record.process, record.process)
  }

  onLocalIpFilter = (value: string | number | boolean, record: ConnectionInfoVO) => record.localIp.includes(value.toString())

  onForeignIpFilter = (value: string | number | boolean, record: ConnectionInfoVO) => record.foreignIp.includes(value.toString())

  onStatusFilter = (value: string | number | boolean, record: ConnectionInfoVO) => record.status.includes(value.toString())

  onProcessFilter = (value: string | number | boolean, record: ConnectionInfoVO) => record.process.includes(value.toString())

  render() {
    const { dataSource } = this.props
    return (
      <Card title="网络连接信息表">
        <Table dataSource={dataSource} pagination={false} scroll={{y: "350px"}}>
          <Table.Column title="协议" dataIndex="protocol" width={'10%'} />
          <Table.Column
            title="本地地址" dataIndex="localIp"
            render={this.renderLocalIp}
            filterDropdown={tableSearch.filterDropdown}
            filterIcon={tableSearch.filterIcon}
            onFilter={this.onLocalIpFilter}
            onFilterDropdownVisibleChange={tableSearch.onFilterDropdownVisibleChange}
          />
          <Table.Column
            title="连接地址" dataIndex="foreignIp"
            render={this.renderForeignIp}
            filterDropdown={tableSearch.filterDropdown}
            filterIcon={tableSearch.filterIcon}
            onFilter={this.onForeignIpFilter}
            onFilterDropdownVisibleChange={tableSearch.onFilterDropdownVisibleChange}
          />
          <Table.Column
            title="连接状态" dataIndex="status"
            render={this.renderStatus}
            filterDropdown={tableSearch.filterDropdown}
            filterIcon={tableSearch.filterIcon}
            onFilter={this.onStatusFilter}
            onFilterDropdownVisibleChange={tableSearch.onFilterDropdownVisibleChange}
          />
          <Table.Column
            title="进程" dataIndex="process"
            render={this.renderProcess}
            filterDropdown={tableSearch.filterDropdown}
            filterIcon={tableSearch.filterIcon}
            onFilter={this.onProcessFilter}
            onFilterDropdownVisibleChange={tableSearch.onFilterDropdownVisibleChange}
          />
        </Table>
      </Card>
    )
  }

}
