import React, { ReactText } from 'react'
import { Table, Row, Col, Divider, Button } from 'antd'
import './index.less'
import { FolderOutlined, FileOutlined } from '@ant-design/icons';
import BreadcrumbCustom, { BreadcrumbPrpos } from '../BreadcrumbCustom';
import Search from 'antd/lib/input/Search';
interface FileManagementState {
  expandable: TableExtendable,
  currentDir: string, // 当前目录

}

interface Record {
  key: string,
  fileName: string,
  size: string | number,
  owner: string,
  permission: string,
  modifiedTime: string,
  children?: Record[]
}

const columns = [
  {
    key: 'fileName',
    title: '文件名',
    dataIndex: 'fileName',
    render: (fileName: string, _: any, __: any) => {
      let icon;
      if (fileName.includes('/')) {
        icon = <FolderOutlined translate="false" />
      } else {
        icon = <FileOutlined translate="false" />
      }
      return (
        <span>
          {icon}
          {`  ${fileName}`}
        </span>
      )
    }
  },
  {
    key: 'size',
    title: '大小',
    dataIndex: 'size'
  },
  {
    key: 'permission',
    title: '权限',
    dataIndex: 'permission'
  },
  {
    key: 'owner',
    title: '所有者',
    dataIndex: 'owner'
  },
  {
    key: 'modifiedTime',
    title: '修改时间',
    dataIndex: 'modifiedTime'
  },
]

const datas: Record[] = [
  {
    key: '/home/linyimin/',
    fileName: './',
    size: '4096',
    owner: 'linyimin',
    permission: '-rw-r--r--',
    modifiedTime: '20200421 04:21'
  },
  {
    key: '/home/linyimin/',
    fileName: '../',
    size: '4096',
    owner: 'linyimin',
    permission: '-rw-r--r--',
    modifiedTime: '20200421 04:21'
  },
  {
    key: '/home/linyimin/',
    fileName: 'CHANGELOG.md',
    size: '5441',
    owner: 'root',
    permission: '-rw-r--r--',
    modifiedTime: '20200421 04:21'
  },
  {
    key: '/home/linyimin/',
    fileName: 'public/',
    size: '4096',
    owner: 'linyimin',
    permission: '-rw-r--r--',
    modifiedTime: '20200421 04:21',
    children: [
      {
        key: '/home/linyimin/public/',
        fileName: 'images/',
        size: '807',
        owner: 'linyimin',
        permission: '-rw-r--r--',
        modifiedTime: '20200421 04:27',
        children: [
          {
            key: '7',
            fileName: 'index.html',
            size: '807',
            owner: 'linyimin',
            permission: '-rw-r--r--',
            modifiedTime: '20200421 04:27'
          },
          {
            key: '/home/linyimin/public/',
            fileName: 'theme.less',
            size: '234663',
            owner: 'linyimin',
            permission: '-rw-rw-r--',
            modifiedTime: '20200421 04:21'
          },
        ]
      },
      {
        key: '/home/linyimin',
        fileName: 'theme.less',
        size: '234663',
        owner: 'linyimin',
        permission: '-rw-rw-r--',
        modifiedTime: '20200421 04:21'
      },
    ]
  },
]


interface TableExtendable {
  expandedRowKeys: Array<ReactText>,
}

const breadcrumProps: BreadcrumbPrpos[] = [
  {
    name: '首页',
    link: '/app/index'
  },
  {
    name: '文件管理'
  }
]

export default class FileManagement extends React.Component<any, FileManagementState> {

  state = {
    expandable: {
      expandedRowKeys: new Array<ReactText>()
    },
    currentDir: '/home/linyimin/'
  }

  retriveBreadcrumbProps = () => {
    const { currentDir } = this.state
    const breadcrumProps: BreadcrumbPrpos[] = new Array<BreadcrumbPrpos>()
    currentDir.split('/').forEach((name: string) => {
      const prop: BreadcrumbPrpos = {
        name: name
      }
      breadcrumProps.push(prop)
    })
    return breadcrumProps
  }

  render() {

    const { expandable } = this.state

    return (
      <div>
        <BreadcrumbCustom breadcrumProps={breadcrumProps} />

        <Row
          gutter={24}
          align="middle"
          style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', margin: '0px 0px'}}
        >
          {/* //TODO: 添加一个输入框,当点击这个时,显示,可以指定路径打开 */}
          <Col span={12} style={{display:'inline-block'}}>
            <BreadcrumbCustom breadcrumProps={this.retriveBreadcrumbProps()} />
            <span>
              <Divider type="vertical" />
              共 
              {<span style={{ color: "blue" }}>{8}</span>} 
              个目录与
              {<span style={{ color: "blue" }}>{10}</span>}
              个文件, 大小
              {<span style={{ color: "blue" }}>100.00</span>}
              MB
            </span>
          </Col>
          <Col span={12}>
              <span style={{ float: 'right' }}>
                <Search 
                  placeholder="查找文件"
                  onSearch={value => console.log(value)}
                  enterButton
                  style={{ width: 400 }}
                />
              </span>
          </Col>
        </Row>
        <Row
          gutter={24}
          align="middle"
          style={{ backgroundColor: '#FFFFFF', border: '#FFFFFF', margin: '0px 0px', padding: '8px 8px' }}
        >
          <Col span={16}>
            <span style={{paddingRight: '8px'}}>
              <Button>
                上传
              </Button>
            </span>
            <span style={{paddingRight: '8px'}}>
              <Button>
                新建
              </Button>
            </span>
            <span style={{paddingRight: '8px'}}>
              <Button>
                上一步
              </Button>
            </span>
            <span style={{paddingRight: '8px'}}>
              <Button>
                分享
              </Button>
            </span>
            <span style={{paddingRight: '8px'}}>
              <Button>
                工作目录
              </Button>
            </span>
          </Col>
          <Col span={8}>
            <span style={{ float: 'right' }}>
              <Button>
                回收站
              </Button>
            </span>
          </Col>
        </Row>
        <Table
          style={{ cursor: 'pointer' }}
          columns={columns}
          dataSource={datas}
          pagination={false}
          expandable={expandable}
          onRow={
            (record: Record) => {
              const { expandable } = this.state
              return {
                onDoubleClick: _ => {

                  const keys = new Array<ReactText>()
                  const index: number = expandable.expandedRowKeys.indexOf(record.key)

                  if (record.fileName.includes('/')) {
                    if (index > -1) {
                      expandable.expandedRowKeys.splice(index, 1)
                    } else {
                      // TODO: 加载新的目录数据
                      keys.push(record.key)
                    }

                    keys.push(...expandable.expandedRowKeys)
                    expandable.expandedRowKeys = keys
                    this.setState({ ...this.state, expandable, currentDir: record.key })
                  } else {
                    // TODO: 打开文件
                  }
                }
              }
            }
          }
        />
      </div>
    )
  }
}