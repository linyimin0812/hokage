import React from 'react'
import { Table } from 'antd'
import './index.less'
import { BreadcrumbPrpos } from '../../layout/bread-crumb'
import MenuContext from './menu-context'
import { FileOperation } from './file-operation'
// import { fileDataList } from './mock-data'
import { observer } from 'mobx-react'
import store from './store'
import { ServerVO } from '../../axios/action/server/server-type'
import { FileOperateForm, FileVO } from '../../axios/action/file-management/file-management-type';
import { getHokageUid } from '../../libs';

type FileTablePropsType = {
  serverVO: ServerVO
}

@observer
export default class FileTable extends React.Component<FileTablePropsType> {

  componentWillMount = () => {
    // 左键按下时
    window.addEventListener("mousedown", this.onMouseDown)
    const { serverVO } = this.props
    const form: FileOperateForm = {
      operatorId: getHokageUid(),
      ip: serverVO.ip,
      sshPort: serverVO.sshPort,
      account: serverVO.account,
      curDir: '~'
    }
    store.listDir(form)
  }

  onMouseDown = (event: MouseEvent) => {
    if (event.button === 0 && store.actionProps.left !== undefined) {
      store.actionProps = {
        left: undefined,
        top: undefined,
        record: undefined
      }
    }
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
    const breadcrumbProps: BreadcrumbPrpos[] = []
    store.currentDir.split('/').forEach((name: string) => {
      const prop: BreadcrumbPrpos = {
        name: name
      }
      breadcrumbProps.push(prop)
    })
    return breadcrumbProps
  }

  onDoubleClick = (record: FileVO) => {
    // TODO: 文件夹则打开文件夹
    // TODO: 普通文本文件直接打开
  }

  onContextMenu = (record: FileVO, event: React.MouseEvent<HTMLElement, MouseEvent>) => {
    event.preventDefault()
    const { left, top } = this.getActionPosition(event)
    store.actionProps = { left, top, record }
  }

  render() {
    return (
      <div>
        { store.actionProps.left !== undefined ? <MenuContext {...store.actionProps} /> : null }
        <FileOperation currentDir={store.currentDir} />
        <Table
          style={{ cursor: 'pointer' }}
          dataSource={store.records}
          pagination={false}
          loading={store.loading}
          onRow={(record: FileVO) => {
            return {
              onDoubleClick: _ => this.onDoubleClick(record),
              onContextMenu: event => this.onContextMenu(record, event)
            }
          }}
        >
          <Table.Column title={'文件名'} dataIndex={'name'} />
          <Table.Column title={'大小'} dataIndex={'size'} />
          <Table.Column title={'权限'} dataIndex={'typeAndPermission'} />
          <Table.Column title={'所有者'} dataIndex={'owner'} />
          <Table.Column title={'修改时间'} dataIndex={'lastAccessTime'} />
        </Table>
      </div>
    )
  }
}
