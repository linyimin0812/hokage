import React from 'react'
import { Button, Col, Divider, Row } from 'antd'
import BreadCrumb, { BreadcrumbProps } from '../../layout/bread-crumb'
import Search from 'antd/lib/input/Search'
import store from './store';
import { observer } from 'mobx-react';

type FileOperationPropsType = {
  curDir: string,
  fileNum: number,
  directoryNum: number,
  totalSize: string
}
@observer
export class FileOperation extends React.Component<FileOperationPropsType> {

  retrieveBreadcrumbProps = () => {
    const { curDir } = this.props
    const breadcrumbProps: BreadcrumbProps[] = new Array<BreadcrumbProps>()
    curDir.split('/').forEach((name: string) => {
      const prop: BreadcrumbProps = {
        name: name
      }
      breadcrumbProps.push(prop)
    })
    return breadcrumbProps
  }

  clickBreadcrumb = (pathIndex: number) => {
    const paths: string[] = store.currentDir.split('/').filter((path, index) => index <= pathIndex)
    store.currentDir = paths.join('/')
  }

  render() {
    const { fileNum, directoryNum, totalSize } = this.props
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
