import React from 'react'
import BreadCrumb, { BreadcrumbProps } from '../../../layout/bread-crumb'
import { AllServerSearch } from './search'
import { ServerSearchForm } from '../../../axios/action/server/server-type'
import Toolbar from './toolbar'
import AllServerTable from './table'
import { observer } from 'mobx-react'
import store from './store'
import AuthDiv from '../../../component/AuthDiv';
import { getHokageRole } from '../../../libs';
import { UserRoleEnum } from '../../../axios/action/user/user-type'

const breadcrumbProps: BreadcrumbProps[] = [
  { name: '首页', link: '/app/index' },
  { name: '我的服务器' },
  { name: '所有的服务器' }
]

@observer
export default class AllServer extends React.Component {

  onFinish = (value: ServerSearchForm) => {
    store.fetchRecords(value)
  }

  render() {
    const role = getHokageRole()
    return (
      <div>
        <BreadCrumb breadcrumbProps={breadcrumbProps} />
        <AllServerSearch onFinish={this.onFinish} />
        <div style={{ backgroundColor: '#FFFFFF' }}>
          <AuthDiv auth={role === UserRoleEnum.super_operator} >
            <Toolbar />
          </AuthDiv>
          <AllServerTable />
        </div>
      </div>
    )
  }

}
