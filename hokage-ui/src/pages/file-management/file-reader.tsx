import React from 'react'
import { Modal, Pagination, Spin } from 'antd'
import Editor from 'react-monaco-editor'
import { FileContentVO, FileOperateForm } from '../../axios/action/file-management/file-management-type'
import { FullscreenExitOutlined, FullscreenOutlined } from '@ant-design/icons'
import { Link } from 'react-router-dom'
import { ServerVO } from '../../axios/action/server/server-type'
import { getHokageUid } from '../../libs'
import path from 'path'
import store from './store'
import { observer } from 'mobx-react'


export interface FileReaderPropsType {
  visible: boolean,
  isPlainText: boolean,
  contentVO: FileContentVO,
  close: () => void,
  openFile: (form: FileOperateForm) => void,
  serverVO: ServerVO
}

type FileReaderStateType = {
  fullScreen: boolean,
  currentPage: number,
}

@observer
export class FileReader extends React.Component<FileReaderPropsType, FileReaderStateType> {

  state = {
    fullScreen: false,
    currentPage: 1,
  }

  toggleFullScreen = () => {
    const { fullScreen } = this.state
    this.setState({
      fullScreen: !fullScreen,
    })
  }

  renderModalTitle = (contentVO: FileContentVO) => {
    const { name } = contentVO
    const { fullScreen } = this.state
    return (
      <>
        {name}
        <button type="button" className="ant-modal-close" style={{ right: 42 }} onClick={this.toggleFullScreen}>
            <span className="ant-modal-close-x">
              {fullScreen ? <FullscreenExitOutlined translate /> : <FullscreenOutlined translate />}
            </span>
        </button>
      </>
    )
  }

  itemRender = (_: number, type: 'page' | 'prev' | 'next' | 'jump-prev' | 'jump-next', originalElement: React.ReactElement<HTMLElement>): React.ReactNode => {
    if (type === 'prev') {
      return <Link to={'#'}>上一页</Link>
    }
    if (type === 'next') {
      return <Link to={'#'}>下一页</Link>
    }
    return originalElement
  }

  assembleFileOperateForm = (page: number): FileOperateForm => {
    const { serverVO, contentVO } = this.props
    const { name, curDir } = contentVO
    return {
      operatorId: getHokageUid(),
      ip: serverVO.ip,
      sshPort: serverVO.sshPort,
      account: serverVO.account,
      path: path.resolve(curDir, name),
      page: page ? page : 1
    }
  }

  onPageChange = (page: number, pageSize?: number): void => {
    const form = this.assembleFileOperateForm(page)
    this.setState({currentPage: page})
    this.props.openFile(form)
  }

  onModalCancel = () => {
    this.props.close()
    this.setState({currentPage: 1})
  }

  renderViewer = () => {
    const { visible, contentVO, isPlainText } = this.props
    const { fullScreen, currentPage } = this.state

    if (!visible) {
      return null
    }

    if (!isPlainText) {
      const { ip, sshPort, account } = this.props.serverVO
      const serverKey = `${ip}_${sshPort}_${account}`
      const url = `${process.env.REACT_APP_ENV === 'local' ? '/api' : ''}/server/file/view?serverKey=${serverKey}&file=${path.resolve(contentVO.curDir, contentVO.name)}`
      return <div style={{height: visible && fullScreen ? 'calc(100vh - 120px)' : '500px'}}>
        <iframe title={contentVO.name} width={'100%'} height={'100%)'} src={url} style={{border: '1px solid #e9e9e9'}} />
      </div>
    }

    const editorHeight = visible && fullScreen ? 'calc(100vh - 120px)' : '500px'

    return (
      <Spin spinning={store.loading}>
        <Editor height={editorHeight} options={{readOnly: true}} value={contentVO.content} />
        <div style={{width: '100%', height: '2px', background: '#d9d9d9', margin: '0 0 0 0'}} />
        <div style={{textAlign: 'center'}}>
          <Pagination
            style={{paddingTop: '5px'}}
            current={currentPage} total={contentVO.totalLine! / contentVO.perPageLine!}
            itemRender={this.itemRender}
            hideOnSinglePage showSizeChanger={false}
            onChange={this.onPageChange}
          />
        </div>
      </Spin>
    )
  }

  render() {
    const { visible, contentVO } = this.props
    const { fullScreen } = this.state
    if (!contentVO.name) {
      return null
    }
    return (
      <Modal
        width={fullScreen ? '100vw' : '1200px'}
        wrapClassName={fullScreen ? 'file-reader-wrapper' : undefined}
        title={this.renderModalTitle(contentVO)}
        visible={visible} footer={null} onCancel={this.onModalCancel}
      >
        {this.renderViewer()}
      </Modal>
    )
  }
}
