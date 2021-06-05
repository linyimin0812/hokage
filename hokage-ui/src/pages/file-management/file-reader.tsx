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

  render() {
    const { visible, contentVO, close } = this.props
    if (!contentVO.name) {
      return null
    }
    return (
      <Modal width={'1000px'} title={contentVO.name} visible={visible} footer={null} onCancel={close}>
        <Editor height={'500px'} options={{readOnly: true}} value={contentVO.content} />
      </Modal>
    )
  }
}
