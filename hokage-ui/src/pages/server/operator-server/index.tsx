import React from 'react'
import BreadCrumb, { BreadcrumbProps } from '../../../layout/bread-crumb'
import { MyOperatorServerSearch } from './search'
import { ServerSearchForm } from '../../../axios/action/server/server-type'
import OperatorServerTable from './table'
import store from './store'
import { observer } from 'mobx-react'
import { getHokageRole, getHokageUid } from '../../../libs'

const breadcrumbProps: BreadcrumbProps[] = [
  { name: '首页', link: '/app/index' },
  { name: '我的服务器' },
  { name: '我管理的服务器' }
]

@observer
export default class OperatorServer extends React.Component {

  onFinish = (value: ServerSearchForm) => {
    value.operatorId = getHokageUid()
    value.role = getHokageRole()
    store.fetchRecords(value)
  }

  render() {
    return (
      <div>
        <BreadCrumb breadcrumbProps={breadcrumbProps} />
        <MyOperatorServerSearch onFinish={this.onFinish} />
        <div style={{ backgroundColor: '#FFFFFF' }}>
          <OperatorServerTable />
        </div>
      </div>
    )
  }
}
