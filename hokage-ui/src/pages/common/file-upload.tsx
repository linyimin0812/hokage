import React from 'react'
import { Button, Upload } from 'antd'
import { UploadOutlined } from '@ant-design/icons'
import { UploadChangeParam } from 'antd/lib/upload'

type FileUploadPropsType = {
  name: string,
  action: string,
  prompt: string,
  onChange?: (uploadChangeParam: UploadChangeParam) => void,
  multiple?: boolean
}

export class FileUpload extends React.Component<FileUploadPropsType> {
  showProgress = () => {
    return {
      strokeColor: { '0%': '#108ee9', '100%': '#87d068' },
      strokeWidth: 3,
      format: (percent: number | undefined) => `${percent ? parseFloat(percent.toFixed(2)) : 0}%`,
    }
  }

  render() {
    const { name, action, prompt, multiple } = this.props
    return (
      <Upload
        name={name}
        action={action}
        onChange={this.props.onChange}
        progress={this.showProgress()}
        multiple={multiple}
      >
        <Button icon={<UploadOutlined translate />}>{prompt}</Button>
      </Upload>
    )
  }

}
