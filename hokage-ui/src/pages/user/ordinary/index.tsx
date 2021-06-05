import React from 'react'
import BreadCrumb, { BreadcrumbProps } from '../../../layout/bread-crumb'
import { UserSearch, UserSearchFormType } from '../common/search'
import { getHokageUid } from '../../../libs'
import Toolbar from './toolbar';
import { observer } from 'mobx-react'
import store from './store'
import OrdinaryTable from './table'

const breadcrumbProps: BreadcrumbProps[] = [
  { name: '首页', link: '/app/index' },
  { name: '用户管理' },
  { name: '服务器使用者' },
]

@observer
export default class OrdinaryUser extends React.Component {

  componentDidMount() {
    store.fetchRecords({operatorId: getHokageUid()})
  }

  onFinish = (value: UserSearchFormType) => {
    value.operatorId = getHokageUid()
    store.fetchRecords(value)
  }

  render() {
    return (
      <div>
        <BreadCrumb breadcrumbProps={breadcrumbProps} />
        <UserSearch onFinish={this.onFinish} usernameType={'ordinary'} />
        <div style={{ backgroundColor: '#FFFFFF' }}>
          <Toolbar />
          <OrdinaryTable />
        </div>
      </div>
    )
  }
}
