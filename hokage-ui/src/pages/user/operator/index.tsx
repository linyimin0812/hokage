import React, { ReactText } from 'react'
import { Table, message } from 'antd'
import BreadCrumb from '../../../layout/bread-crumb'
import { UserSearch, UserSearchFormType } from '../search'
import { TableExtendable } from '../../common/table-extendable'
import { UserAction } from '../../../axios/action'
import { UserVO } from '../../../axios/action/user/user-type'
import { breadcrumbProps, columns, nestedColumn } from './column-definition'
import { ServerVO } from '../../../axios/action/server/server-type'
import { Toolbar } from './toolbar'
import store from './store'
import { observer } from 'mobx-react'

type OperatorState = {
    dataSource: UserVO[],
    expandable: TableExtendable,
    nestedTableDataSource: ServerVO[],
    loading: boolean,
}

@observer
export default class Operator extends React.Component<any, OperatorState> {

    state: OperatorState = {
        dataSource: [],
        expandable: {
            expandedRowKeys: [],
            expandedRowRender: () => {
                return <Table columns={nestedColumn} dataSource={this.state.nestedTableDataSource} pagination={false} />;
            },
            onExpand: (expanded: boolean, record: UserVO) => {
                if (expanded) {
                    const expandedRowKeys: ReactText[] = [record.key!]

                    const expandable: TableExtendable = this.state.expandable
                    expandable.expandedRowKeys = expandedRowKeys

                    this.setState({ ...this.state, nestedTableDataSource: record.serverVOList, expandable })
                } else {
                    const expandable: TableExtendable = this.state.expandable
                    expandable.expandedRowKeys = []

                    this.setState({ ...this.state, expandable })
                }
            }
        },
        nestedTableDataSource: [],
        loading: false,
    }

    componentDidMount() {
        this.searchOperator({})
    }

    searchOperator = (value?: UserSearchFormType) => {
        this.setState({loading: true})
        UserAction.supervisorSearch(value ? value : {}).then(supervisorList => {
            supervisorList = (supervisorList || []).map(userVO => {
                userVO.key = userVO.id + ''
                return userVO
            })
            this.setState({dataSource: supervisorList, loading: false})
        }).catch(err => {
            message.error(err)
        })
    }

    onFinish = (value: UserSearchFormType) => {
        this.searchOperator(value)
    }

    onSelectChange = (selectedRowKeys: ReactText[], selectedRows: any[]) => {
        store.selectedRowKeys = selectedRowKeys
        // TODO: 从selectRows中获取选择的目标数据,然后进行相关操作
    }

    render() {

        const { dataSource, loading } = this.state
        const rowSelection = {
            selectedRowKeys: store.selectedRowKeys,
            onChange: this.onSelectChange,
            selections: [
                Table.SELECTION_ALL,
                Table.SELECTION_INVERT,
            ],
        };

        return (
            <div>
                <BreadCrumb breadcrumbProps= {breadcrumbProps} />
                <UserSearch onFinish={this.onFinish} usernameType={'operator'} />
                <div style={{ backgroundColor: '#FFFFFF' }}>
                    <Toolbar />
                    <Table
                        rowSelection={rowSelection}
                        columns={columns}
                        dataSource={dataSource}
                        loading={loading}
                        expandable={this.state.expandable}
                        pagination={false}
                    />
                </div>
            </div>
        )
    }
}
