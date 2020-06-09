import React from 'react'
import BreadcrumbCustom, { BreadcrumbPrpos } from '../BreadcrumbCustom';
import EnterSecurity from './EnterSecurity';

const breadcrumProps: BreadcrumbPrpos[] = [
  {
    name: '首页',
    link: '/app/index'
  },
  {
    name: '安全组'
  }
]

export default class SecurityGroupHome extends React.Component {
  render() {
    return (
        <div>
          <BreadcrumbCustom breadcrumProps={breadcrumProps} />
          <EnterSecurity />
        </div>
    )
  }
}