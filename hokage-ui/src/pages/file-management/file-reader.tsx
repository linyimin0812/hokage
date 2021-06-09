import React from 'react'
import { Modal } from 'antd'
import Editor from 'react-monaco-editor'
import { FileContentVO } from '../../axios/action/file-management/file-management-type'

export interface FileReaderPropsType {
  visible: boolean,
  contentVO: FileContentVO,
  close: () => void
}

export class FileReader extends React.Component<FileReaderPropsType> {

  renderModalTitle = (contentVO: FileContentVO) => {
    const { name, curLine, totalLine } = contentVO
    if (curLine < totalLine) {
      return <span>{name}<span style={{color: 'red'}}>(此文件过大, 共{totalLine}行, 只显示前{curLine}行)</span></span>
    } else {
      return <span>{name}</span>
    }
  }


  render() {
    const { visible, contentVO, close } = this.props
    if (!contentVO.name) {
      return null
    }
    return (
      <Modal width={'1200px'} title={this.renderModalTitle(contentVO)} visible={visible} footer={null} onCancel={close}>
        <Editor height={'500px'} options={{readOnly: true}} value={contentVO.content} />
      </Modal>
    )
  }
}
