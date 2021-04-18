import React from 'react'
import BreadCrumb, { BreadcrumbPrpos } from '../../../layout/bread-crumb'
import { MyOperatorServerSearch } from './search'
import { ServerSearchForm } from '../../../axios/action/server/server-type'
import OperatorServerTable from './table'
import store from './store'
import Toolbar from './toolbar'
import { observer } from 'mobx-react'

const breadcrumbProps: BreadcrumbPrpos[] = [
  { name: '首页', link: '/app/index' },
  { name: '我的服务器' },
  { name: '我管理的服务器' }
]

@observer
export default class OperatorServer extends React.Component {

  onFinish = (value: ServerSearchForm) => {
    store.fetchRecords(value)
  }

  render() {
    return (
      <div>
        <BreadCrumb breadcrumbProps={breadcrumbProps} />
        <MyOperatorServerSearch onFinish={this.onFinish} />
        <div style={{ backgroundColor: '#FFFFFF' }}>
          <Toolbar />
          <OperatorServerTable />
        </div>
      </div>
    )
  }
}
