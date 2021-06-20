import React from 'react'
import { Card, Progress, Table, Tooltip } from 'antd';

export interface DiskInfoVO {
  name: string,
  size: string,
  used: string,
  capacity: number,
  mounted: string
}

type DiskPartitionProp = {
  dataSource: DiskInfoVO[]
}

export default class DiskPartition extends React.Component<DiskPartitionProp> {


  renderStatus = (record: DiskInfoVO) => {
    return <Tooltip title={`${record.used}/${record.size}`}>
      <Progress type={'circle'} size={'small'} width={50} percent={record.capacity} />
    </Tooltip>
  }

  render() {
    const { dataSource } = this.props
    return (
      <Card title="磁盘使用率">
        <Table dataSource={dataSource} pagination={false} scroll={{y: 350}} >
          <Table.Column title="NAME" dataIndex="name" />
          <Table.Column
            title="STATUS"
            align={'center'} width={90}
            render={this.renderStatus} sorter={(a1: DiskInfoVO, a2: DiskInfoVO) => a1.capacity > a2.capacity ? 1 : -1}
          />
          <Table.Column title="MOUNT" dataIndex="mounted" ellipsis />
        </Table>
      </Card>
    )
  }
}
