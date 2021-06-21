import React from 'react'
import { Button, Col, Divider, Row } from 'antd'
import BreadCrumb, { BreadcrumbProps } from '../../layout/bread-crumb'
import store from './store'
import { observer } from 'mobx-react'
import { ServerVO } from '../../axios/action/server/server-type'
import { FileOperateForm, FileVO } from '../../axios/action/file-management/file-management-type'
import { getHokageUid } from '../../libs'
import { ReloadOutlined } from '@ant-design/icons'
import path from 'path'
import { FileUpload } from '../common/file-upload'
import { UploadChangeParam } from 'antd/lib/upload/interface'

type FileOperationPropsType = {
  id: string,
  serverVO: ServerVO,
  fileVO: FileVO
}

type FileOperationStateType = {
  showUploadList: boolean
}

@observer
export class FileOperation extends React.Component<FileOperationPropsType, FileOperationStateType> {

  state = {
    showUploadList: false
  }

  retrieveBreadcrumbProps = () => {
    const { fileVO } = this.props
    const breadcrumbProps: BreadcrumbProps[] = new Array<BreadcrumbProps>()
    fileVO.curDir.split('/').forEach((name: string) => {
      const prop: BreadcrumbProps = {
        name: name
      }
      breadcrumbProps.push(prop)
    })
    return breadcrumbProps
  }

  clickBreadcrumb = (pathIndex: number) => {
    const { id, serverVO } = this.props
    const pane = store.panes.find(pane => pane.key === this.props.id)!
    const paths: string[] = pane.fileVO?.curDir.split('/').filter((path, index) => index <= pathIndex)!
    const form: FileOperateForm = {
      operatorId: getHokageUid(),
      ip: serverVO.ip,
      sshPort: serverVO.sshPort,
      account: serverVO.account,
      path: '/' + paths.join('/')
    }
    store.listDir(id, form)
  }

  refresh = () => {
    const { id, serverVO, fileVO } = this.props
    const form: FileOperateForm = {
      operatorId: getHokageUid(),
      ip: serverVO.ip,
      sshPort: serverVO.sshPort,
      account: serverVO.account,
      path: fileVO.curDir
    }
    store.listDir(id, form)
    this.triggerFileSearchButtonClick()
  }

  triggerFileSearchButtonClick = () => {
    const fileSearchResetElement = document.getElementById('file-search-reset')
    if (fileSearchResetElement) {
      fileSearchResetElement.click()
    }
  }

  goPath = (path: string) => {
    const { id, serverVO} = this.props
    const form: FileOperateForm = {
      operatorId: getHokageUid(),
      ip: serverVO.ip,
      sshPort: serverVO.sshPort,
      account: serverVO.account,
      path: path
    }
    store.listDir(id, form)
  }

  goHome = () => {
    this.triggerFileSearchButtonClick()
    this.goPath('~')
  }

  goPrevious = () => {
    const { curDir } = this.props.fileVO
    this.goPath(path.resolve(curDir, '..'))
    this.triggerFileSearchButtonClick()
  }

  onUploadChange = (info: UploadChangeParam) => {
    const status = info.file.status
    if (status === 'done' || status === 'success') {
      this.setState({showUploadList: false})
      const { curDir } = this.props.fileVO
      this.goPath(curDir)
    } else {
      this.setState({showUploadList: true})
    }
  }

  render() {
    const { fileVO, serverVO } = this.props
    const { showUploadList } = this.state
    const { fileNum, directoryNum, totalSize, curDir } = fileVO
    if (!directoryNum) {
      return null
    }
    const url = process.env.REACT_APP_ENV === 'local' ? '/api/server/file/upload' : '/server/file/upload'
    const serverKey = `${serverVO.ip}_${serverVO.sshPort}_${serverVO.account}`
    const action = `${url}?curDir=${curDir}&serverKey=${serverKey}`
    return (
      <>
        <Row gutter={24} align="middle" style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', margin: '0px 0px'}}>
          {/* //TODO: 添加一个输入框,当点击这个时,显示,可以指定路径打开 */}
          <Col span={24} style={{display:'inline-block'}}>
            <BreadCrumb breadcrumbProps={this.retrieveBreadcrumbProps()} type={'path'} onClick={this.clickBreadcrumb} />
            <span>
              <Divider type="vertical" />
              共{<span style={{ color: "blue" }}>{directoryNum}</span>}
              个目录与{<span style={{ color: "blue" }}>{fileNum}</span>}
              个文件, 大小{<span style={{ color: "blue" }}>{totalSize}</span>}
          </span>
          </Col>
        </Row>
        <Row gutter={24} align="middle" style={{ backgroundColor: '#FFFFFF', border: '#FFFFFF', margin: '0px 0px', padding: '8px 8px' }}>
          <Col span={16} style={{padding: '0px 0px'}}>
            <span style={{paddingRight: '8px'}}><Button onClick={this.goHome}>工作目录</Button></span>
            <span style={{paddingRight: '8px'}}><Button onClick={this.goPrevious}>上一步</Button></span>
            <span style={{paddingRight: '8px', display: 'inline-flex'}}>
              <FileUpload showUploadList={showUploadList} name={'file'} action={action} prompt={'上传'} multiple onChange={this.onUploadChange} />
            </span>
            <span style={{paddingRight: '8px'}}><Button disabled>新建</Button></span>
            <span style={{paddingRight: '8px'}}><Button disabled>分享</Button></span>
          </Col>
          <Col span={8} style={{padding: '0px 0px'}}>
            <span style={{ float: 'right' }}><Button onClick={this.refresh}><ReloadOutlined translate />刷新</Button></span>
          </Col>
        </Row>
      </>
    )
  }
}
