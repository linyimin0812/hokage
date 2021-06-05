import React from 'react'
import BreadCrumb, { BreadcrumbProps } from '../../../layout/bread-crumb'
import { UserSearch, UserSearchFormType } from '../common/search'
import { Toolbar } from './toolbar'
import store from './store'
import { observer } from 'mobx-react'
import OperatorTable from './table'


const breadcrumbProps: BreadcrumbProps[] = [
  { name: '首页', link: '/app/index' },
  { name: '用户管理' },
  { name: '服务器管理员' }
]

@observer
export default class Operator extends React.Component {

  componentDidMount() {
    store.fetchRecords()
  }

  onFinish = (value: UserSearchFormType) => {
    store.fetchRecords(value)
  }

  render() {

    return (
      <div>
        <BreadCrumb breadcrumbProps= {breadcrumbProps} />
        <UserSearch onFinish={this.onFinish} usernameType={'operator'} />
        <div style={{ backgroundColor: '#FFFFFF' }}>
          <Toolbar />
          <OperatorTable />
        </div>
      </div>
    )
  }
}
