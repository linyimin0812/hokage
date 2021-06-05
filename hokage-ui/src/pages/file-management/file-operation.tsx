import React from 'react'
import { Button, Col, Divider, Row } from 'antd'
import BreadCrumb, { BreadcrumbProps } from '../../layout/bread-crumb'
import Search from 'antd/lib/input/Search'
import store from './store'
import { observer } from 'mobx-react'
import { ServerVO } from '../../axios/action/server/server-type'
import { FileOperateForm, FileVO } from '../../axios/action/file-management/file-management-type';
import { getHokageUid } from '../../libs';

type FileOperationPropsType = {
  id: string,
  serverVO: ServerVO,
  fileVO: FileVO
}
@observer
export class FileOperation extends React.Component<FileOperationPropsType> {

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
      curDir: '/' + paths.join('/')
    }
    store.listDir(id, form)
  }

  render() {
    const { fileNum, directoryNum, totalSize } = this.props.fileVO
    if (!totalSize) {
      return null
    }
    return (
      <>
        <Row gutter={24} align="middle" style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', margin: '0px 0px'}}>
          {/* //TODO: 添加一个输入框,当点击这个时,显示,可以指定路径打开 */}
          <Col span={20} style={{display:'inline-block'}}>
            <BreadCrumb breadcrumbProps={this.retrieveBreadcrumbProps()} type={'path'} onClick={this.clickBreadcrumb} />
            <span>
              <Divider type="vertical" />
              共{<span style={{ color: "blue" }}>{directoryNum}</span>}
              个目录与{<span style={{ color: "blue" }}>{fileNum}</span>}
              个文件, 大小{<span style={{ color: "blue" }}>{totalSize}</span>}
          </span>
          </Col>
          <Col span={4}>
            <span style={{ float: 'right' }}>
              <Search placeholder="查找文件" onSearch={value => console.log(value)} enterButton />
            </span>
          </Col>
        </Row>
        <Row gutter={24} align="middle" style={{ backgroundColor: '#FFFFFF', border: '#FFFFFF', margin: '0px 0px', padding: '8px 8px' }}>
          <Col span={16} style={{padding: '0px 0px'}}>
            <span style={{paddingRight: '8px'}}><Button>工作目录</Button></span>
            <span style={{paddingRight: '8px'}}><Button>上一步</Button></span>
            <span style={{paddingRight: '8px'}}><Button>上传</Button></span>
            <span style={{paddingRight: '8px'}}><Button>新建</Button></span>
            <span style={{paddingRight: '8px'}}><Button>分享</Button></span>
          </Col>
          {/*<Col span={8} style={{padding: '0px 0px'}}>*/}
          {/*  <span style={{ float: 'right' }}><Button>回收站</Button></span>*/}
          {/*</Col>*/}
        </Row>
      </>
    )
  }
}
