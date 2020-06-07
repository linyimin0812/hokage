import React from 'react'
import BreadcrumbCustom, { BreadcrumbPrpos } from '../BreadcrumbCustom';
import MyBatCommand from './MyBatCommand';

const breadcrumProps: BreadcrumbPrpos[] = [
  {
    name: '首页',
    link: '/app/index'
  },
  {
    name: '批量任务'
  }
]

export default class BatCommand extends React.Component {
  render() {
    return(
      <div>
        <BreadcrumbCustom breadcrumProps={breadcrumProps} />
        <MyBatCommand />
      </div>
    )
  }
}