import React from 'react'
import BreadCrumb, { BreadcrumbProps } from '../../../layout/bread-crumb'
import { UserSearch, UserSearchFormType } from '../common/search'
import { Toolbar } from './toolbar'
import store from './store'
import { observer } from 'mobx-react'
import OperatorTable from './table'
import { getHokageRole } from '../../../libs'
import { UserRoleEnum } from '../../../axios/action/user/user-type'


const breadcrumbProps: BreadcrumbProps[] = [
  { name: '首页', link: '/app/index' },
  { name: '用户管理' },
  { name: '服务器管理员' }
]

@observer
export default class Operator extends React.Component {

  onFinish = (value: UserSearchFormType) => {
    store.fetchRecords(value)
  }

  render() {

    return (
      <div>
        <BreadCrumb breadcrumbProps= {breadcrumbProps} />
        <UserSearch onFinish={this.onFinish} usernameType={'operator'} />
        <div style={{ backgroundColor: '#FFFFFF' }}>
          {
            UserRoleEnum.super_operator === getHokageRole() ? <Toolbar /> : null
          }
          <OperatorTable />
        </div>
      </div>
    )
  }
}
