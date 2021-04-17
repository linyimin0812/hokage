import React from 'react'
import BreadCrumb, { BreadcrumbPrpos } from '../../../layout/bread-crumb'
import { UserSearch, UserSearchFormType } from '../search'
import { TableExtendable } from '../../common/table-extendable'
import { UserVO } from '../../../axios/action/user/user-type'
import { ServerVO } from '../../../axios/action/server/server-type'
import { Toolbar } from './toolbar'
import store from './store'
import { observer } from 'mobx-react'
import OperatorTable from './table'

type OperatorState = {
  dataSource: UserVO[],
  expandable: TableExtendable,
  nestedTableDataSource: ServerVO[],
  loading: boolean,
}

const breadcrumbProps: BreadcrumbPrpos[] = [
  {
    name: '首页',
    link: '/app/index'
  },
  {
    name: '用户管理'
  },
  {
    name: '服务器管理员'
  }
]

@observer
export default class Operator extends React.Component<any, OperatorState> {

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
