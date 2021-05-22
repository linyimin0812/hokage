import React, { ReactText } from 'react'
import { Table } from 'antd'
import './index.less'
import { FolderOutlined, FolderOpenOutlined } from '@ant-design/icons'
import { BreadcrumbPrpos } from '../../layout/bread-crumb'
import Action, { ActionProps } from './action'
import { FileOperation } from './file-operation';
interface FileManagementState {
  expandable: TableExtendable,
  currentDir: string, // 当前目录
  isActionVisible: boolean, // 是否需要显示右键菜单
  actionProps: ActionProps
}

export interface Record {
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
    key: '/home/linyimin/./',
    fileName: './',
    size: '4096',
    owner: 'linyimin',
    permission: '-rw-r--r--',
    modifiedTime: '20200421 04:21'
  },
  {
    key: '/home/linyimin/../',
    fileName: '../',
    size: '4096',
    owner: 'linyimin',
    permission: '-rw-r--r--',
    modifiedTime: '20200421 04:21'
  },
  {
    key: '/home/linyimin/CHANGELOG.md',
    fileName: 'CHANGELOG.md',
    size: '5441',
    owner: 'root',
    permission: '-rw-r--r--',
    modifiedTime: '20200421 04:21'
  },
  {
    key: '/home/linyimin/public/',
    fileName: 'public/',
    size: '4096',
    owner: 'linyimin',
    permission: '-rw-r--r--',
    modifiedTime: '20200421 04:21',
    children: [
      {
        key: '/home/linyimin/public/images/',
        fileName: 'images/',
        size: '807',
        owner: 'linyimin',
        permission: '-rw-r--r--',
        modifiedTime: '20200421 04:27',
        children: [
          {
            key: '/home/linyimin/public/images/index.html',
            fileName: 'index.html',
            size: '807',
            owner: 'linyimin',
            permission: '-rw-r--r--',
            modifiedTime: '20200421 04:27'
          },
          {
            key: '/home/linyimin/public/images/theme.less',
            fileName: 'theme.less',
            size: '234663',
            owner: 'linyimin',
            permission: '-rw-rw-r--',
            modifiedTime: '20200421 04:21'
          },
        ]
      },
      {
        key: '/home/linyimin/theme.less',
        fileName: 'theme.less',
        size: '234663',
        owner: 'linyimin',
        permission: '-rw-rw-r--',
        modifiedTime: '20200421 04:21'
      },
    ]
  },
]

interface ExpandIconProps {
  expanded: boolean,
  onExpand: Function,
  record: Record
}

interface TableExtendable {
  expandedRowKeys: Array<ReactText>,
  expandIcon: (iconProps: ExpandIconProps) => JSX.Element
}

export default class FileManagement extends React.Component<any, FileManagementState> {

  state = {
    expandable: {
      expandedRowKeys: new Array<ReactText>(),
      expandIcon: (iconProps: ExpandIconProps) => {
        const { expanded, onExpand, record } = iconProps
        return expanded ? (
          <span>
            <FolderOpenOutlined
              translate="true"
              onClick={(e: React.MouseEvent) => onExpand(record, e)}
            />
            &nbsp;&nbsp;
          </span>
        ) : (
          <span>
            <FolderOutlined
              translate="true"
              onClick={(e: React.MouseEvent) => onExpand(record, e)}
            />
            &nbsp;&nbsp;
          </span>
        )
      }
    },
    currentDir: '/home/linyimin/',
    isActionVisible: false,
    actionProps: {
      left: undefined,
      top: undefined,
      record: undefined
    }
  }

  componentWillMount = () => {
    // 左键按下时
    window.addEventListener("mousedown", (event) => {
      let { actionProps } = this.state
      if (event.which === 1 && actionProps.left !== undefined) {
        actionProps = {
          left: undefined,
          top: undefined,
          record: undefined
        }
        this.setState({
          actionProps
        })
      }
    })
  }

  getActionPosition = (event: React.MouseEvent<HTMLElement, MouseEvent>) => {
    const left = event.clientX
    const top = event.clientY
    const ww = document.documentElement.clientWidth
    const wh = document.documentElement.clientHeight

    return {
      top: ((wh - top > 300) ? top : (top - 330)) + 'px',
      left: ((ww - left > 100) ? left : (left - 100)) + 'px'
    }
  }

  retrieveBreadcrumbProps = () => {
    const { currentDir } = this.state
    const breadcrumProps: BreadcrumbPrpos[] = []
    currentDir.split('/').forEach((name: string) => {
      const prop: BreadcrumbPrpos = {
        name: name
      }
      breadcrumProps.push(prop)
    })
    return breadcrumProps
  }

  render() {

    const { expandable, actionProps } = this.state

    return (
      <div>
        {
          actionProps.left !== undefined
            ?
            <Action left={actionProps.left} top={actionProps.top} record={actionProps.record} />
            :
            null
        }
        <FileOperation currentDir={this.state.currentDir} />
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
                onClick: _ => {

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
                },
                onContextMenu: event => {
                  event.preventDefault()
                  const { left, top } = this.getActionPosition(event)
                  const actionProps: ActionProps = { left, top, record }
                  this.setState({ actionProps })
                }
              }
            }
          }
        />
      </div>
    )
  }
}
