import React from 'react';
import { Breadcrumb } from 'antd';
import { Link } from 'react-router-dom';

export interface BreadcrumbPrpos {
  name: string,
  link?: string
}

export interface BreadcrumbCustomProps {
  breadcrumProps?: BreadcrumbPrpos[]
};
class BreadcrumbCustom extends React.Component<BreadcrumbCustomProps> {

  subBreadcrumb = () => {
    const { breadcrumProps } = this.props
    if (breadcrumProps === undefined) {
      return null
    }
    return breadcrumProps.map(prop => {
      return (
        <Breadcrumb.Item>
          {prop.link === undefined ? prop.name : <Link to={prop.link}>{prop.name}</Link>}
        </Breadcrumb.Item>
      )
      
    })
  }
  
  render() {
    
    return (
      <span>
        <Breadcrumb style={{ margin: '12px 0' }}>
          <Breadcrumb.Item>
            <Link to={'/app/index'}>首页</Link>
          </Breadcrumb.Item>
          {this.subBreadcrumb()}
        </Breadcrumb>
      </span>
    );
  }
}

export default BreadcrumbCustom;
