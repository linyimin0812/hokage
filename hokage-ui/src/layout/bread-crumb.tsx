import React from 'react'
import { Breadcrumb } from 'antd'
import { Link } from 'react-router-dom'
import { HomeOutlined } from '@ant-design/icons';

export interface BreadcrumbProps {
  name: string,
  link?: string,
}

export interface BreadcrumbPropsType {
  breadcrumbProps?: BreadcrumbProps[],
  type?: 'path',
  onClick?: (index: number) => void
}
class BreadCrumb extends React.Component<BreadcrumbPropsType> {

  subBreadcrumb = () => {
    const { breadcrumbProps, onClick } = this.props
    if (breadcrumbProps === undefined) {
      return null
    }
    return breadcrumbProps.map((prop, index) => {
      return (
        <Breadcrumb.Item href={'#'} onClick={() => { if (onClick) onClick(index)}}>
          {prop.link === undefined ? prop.name : <Link to={prop.link}>{prop.name}</Link>}
        </Breadcrumb.Item>
      )

    })
  }

  renderHomeIcon = () => {
    const { type, onClick } = this.props
    if (!type || type !== 'path') {
      return null
    }
    return (
      <Breadcrumb.Item href="#" onClick={() => { if (onClick) onClick(0)}}>
        <HomeOutlined translate />
      </Breadcrumb.Item>
    )
  }

  render() {
    return (
      <span>
        <Breadcrumb style={{ margin: '12px 0', display:'inline-block' }}>
          {this.renderHomeIcon()}
          {this.subBreadcrumb()}
        </Breadcrumb>
      </span>
    );
  }
}

export default BreadCrumb
