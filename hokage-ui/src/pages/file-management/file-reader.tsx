import React from 'react'
import { Modal } from 'antd';
import Editor from 'react-monaco-editor'
import { FileContentVO } from '../../axios/action/file-management/file-management-type'
import { FullscreenExitOutlined, FullscreenOutlined } from '@ant-design/icons'

export interface FileReaderPropsType {
  visible: boolean,
  contentVO: FileContentVO,
  close: () => void
}

type FileReaderStateType = {
  fullScreen: boolean
}

export class FileReader extends React.Component<FileReaderPropsType, FileReaderStateType> {

  state = {
    fullScreen: false
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

  render() {
    const { visible, contentVO, close } = this.props
    const { fullScreen } = this.state
    if (!contentVO.name) {
      return null
    }
    return (
      <Modal
        width={fullScreen ? '100vw' : '1200px'}
        wrapClassName={fullScreen ? 'file-reader-wrapper' : undefined}
        title={this.renderModalTitle(contentVO)}
        visible={visible} footer={null} onCancel={close}
      >
        <Editor height={visible && fullScreen ? 'calc(100vh - 110px)' : '500px'} options={{readOnly: true}} value={contentVO.content} />
      </Modal>
    )
  }
}
