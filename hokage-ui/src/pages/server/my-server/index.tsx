import React from 'react'
import BreadCrumb, { BreadcrumbPrpos } from '../../../layout/bread-crumb'
import { MyServerSearch } from './search'
import { ServerSearchForm } from '../../../axios/action/server/server-type'
import Toolbar from './toolbar';
import MyServerTable from './table'
import { observer } from 'mobx-react'
import store from './store'

const breadcrumbProps: BreadcrumbPrpos[] = [
  { name: '首页', link: '/app/index' },
  { name: '我的服务器' },
  { name: '我使用的服务器' }
]

@observer
export default class MyServer extends React.Component {

  onFinish = (value: ServerSearchForm) => {
    store.fetchRecords(value)
  }

  render() {
    return (
      <>
        <BreadCrumb breadcrumbProps={breadcrumbProps} />
        <MyServerSearch onFinish={this.onFinish} />
        <div style={{ backgroundColor: '#FFFFFF' }}>
          <Toolbar />
          <MyServerTable />
        </div>
      </>
    )
  }
}
