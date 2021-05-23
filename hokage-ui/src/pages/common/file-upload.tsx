import React from 'react'
import { Button, Upload } from 'antd'
import { UploadOutlined } from '@ant-design/icons'
import { UploadChangeParam } from 'antd/lib/upload'

type FileUploadPropsType = {
  onChange: (uploadChangeParam: UploadChangeParam) => void
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
    return (
      <Upload
        name={'sshKeyFile'}
        accept={'file'}
        action={'/app/file/upload'}
        onChange={this.props.onChange}
        progress={this.showProgress()}
        multiple={false}
      >
        <Button icon={<UploadOutlined translate />}>点击上传密钥文件</Button>
      </Upload>
    )
  }

}
