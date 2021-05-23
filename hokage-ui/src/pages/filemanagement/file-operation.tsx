import React from 'react'
import { Button, Col, Divider, Row } from 'antd'
import BreadCrumb, { BreadcrumbPrpos } from '../../layout/bread-crumb'
import Search from 'antd/lib/input/Search'

type FileOperationPropsType = {
  currentDir: string
}

export class FileOperation extends React.Component<FileOperationPropsType> {

  retrieveBreadcrumbProps = () => {
    const { currentDir } = this.props
    const breadcrumbProps: BreadcrumbPrpos[] = new Array<BreadcrumbPrpos>()
    currentDir.split('/').forEach((name: string) => {
      const prop: BreadcrumbPrpos = {
        name: name
      }
      breadcrumbProps.push(prop)
    })
    return breadcrumbProps
  }

  render() {
    return (
      <>
        <Row gutter={24} align="middle" style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', margin: '0px 0px'}}>
          {/* //TODO: 添加一个输入框,当点击这个时,显示,可以指定路径打开 */}
          <Col span={12} style={{display:'inline-block'}}>
            <BreadCrumb breadcrumbProps={this.retrieveBreadcrumbProps()} />
            <span>
              <Divider type="vertical" />
              共{<span style={{ color: "blue" }}>{8}</span>}
              个目录与{<span style={{ color: "blue" }}>{10}</span>}
              个文件, 大小{<span style={{ color: "blue" }}>100.00</span>}MB
            </span>
          </Col>
          <Col span={12}>
            <span style={{ float: 'right' }}>
              <Search placeholder="查找文件" onSearch={value => console.log(value)} enterButton style={{ width: 400 }} />
            </span>
          </Col>
        </Row>
        <Row gutter={24} align="middle" style={{ backgroundColor: '#FFFFFF', border: '#FFFFFF', margin: '0px 0px', padding: '8px 8px' }}>
          <Col span={16} style={{padding: '0px 0px'}}>
            <span style={{paddingRight: '8px'}}><Button>上传</Button></span>
            <span style={{paddingRight: '8px'}}><Button>新建</Button></span>
            <span style={{paddingRight: '8px'}}><Button>上一步</Button></span>
            <span style={{paddingRight: '8px'}}><Button>分享</Button></span>
            <span style={{paddingRight: '8px'}}><Button>工作目录</Button></span>
          </Col>
          <Col span={8} style={{padding: '0px 0px'}}>
            <span style={{ float: 'right' }}><Button>回收站</Button></span>
          </Col>
        </Row>
      </>
    )
  }
}
