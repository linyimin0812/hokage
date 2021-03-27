import React, { ReactText } from 'react'
import { Table, Row, Col, Button, message } from 'antd'
import BreadcrumbCustom from '../../bread-crumb-custom'
import { UserSearch, UserSearchFormType } from '../search'
import {
    UserAddOutlined,
    InfoCircleOutlined,
    SyncOutlined,
    UsergroupDeleteOutlined
} from '@ant-design/icons'
import AddOperator from './add-operator'
import { TableExtendable } from '../../common/table-extendable'
import { UserAction } from '../../../axios/action'
import { UserVO } from '../../../axios/action/user/user-type'
import { breadcrumProps, columns, nestedColumn } from './column-definition'
import { ServerVO } from '../../../axios/action/server/server-type'
import { getHokageUid } from '../../../utils'

type OperatorState = {
    dataSource: UserVO[],
    expandable: TableExtendable,
    nestedTableDataSource: ServerVO[],
    selectedRowKeys: ReactText[],
    isModalVisible: boolean,
    loading: boolean,
}

export default class Operator extends React.Component<any, OperatorState> {

    state: OperatorState = {
        dataSource: [],
        expandable: {
            expandedRowKeys: [],
            expandedRowRender: () => {
                return <Table columns={nestedColumn} dataSource={this.state.nestedTableDataSource} pagination={false} />;
            },
            onExpand: (expanded: boolean, record: any) => {
                const userVO: UserVO = record;

                if (expanded) {
                    // Why?
                    const expandedRowKeys: ReactText[] = [record.key]

                    const expandable: TableExtendable = this.state.expandable
                    expandable.expandedRowKeys = expandedRowKeys

                    this.setState({ ...this.state, nestedTableDataSource: userVO.serverVOList, expandable })
                } else {
                    const expandable: TableExtendable = this.state.expandable
                    expandable.expandedRowKeys = []

                    this.setState({ ...this.state, expandable })
                }
            }
        },
        nestedTableDataSource: [],
        selectedRowKeys: [],
        isModalVisible: false,
        loading: false,
    }

    componentDidMount() {
        this.searchOperator({})
    }

    searchOperator = (value?: UserSearchFormType) => {
        this.setState({loading: true})
        UserAction.supervisorSearch(value ? value : {}).then(supervisorList => {
            this.setState({dataSource: supervisorList, loading: false})
        }).catch(err => {
            message.error(err)
        })
    }

    onFinish = (value: UserSearchFormType) => {
        this.searchOperator(value)
    }

    onSelectChange = (selectedRowKeys: ReactText[], selectedRows: any[]) => {
        this.setState({ selectedRowKeys })
        // TODO: 从selectRows中获取选择的目标数据,然后进行相关操作
    }

    add = () => {
        this.setState({ ...this.state, isModalVisible: true })
    }

    delete = () => {
        alert("delete operators bat")
    }

    sync = () => {
        alert("sync operator")
    }

    onModalOk = (value: any) => {

        UserAction.addSupervisor({
            id: getHokageUid(),
            serverIds: [],
            userIds: value.userIds || []
        }).then(value => {
            if (value) {
                this.setState({ ...this.state, isModalVisible: false })
            } else {
                message.error('添加管理员失败')
            }
        }).catch((err) => {
            message.error(err)
        })
    }

    onModalCancel = () => {
        this.setState({ ...this.state, isModalVisible: false })
    }

    render() {

        const { selectedRowKeys, isModalVisible, dataSource, loading } = this.state
        const rowSelection = {
            selectedRowKeys,
            onChange: this.onSelectChange,
            selections: [
                Table.SELECTION_ALL,
                Table.SELECTION_INVERT,
            ],
        };

        return (
            <div>
                <BreadcrumbCustom breadcrumProps= {breadcrumProps} />
                <UserSearch onFinish={this.onFinish} usernameType={'operator'} />
                <div style={{ backgroundColor: '#FFFFFF' }}>
                    <Row
                        gutter={24}
                        style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', margin: '0 0' }}
                    >
                        <Col span={12} style={{ display: 'flex', alignItems: 'center' }}>
                            <span>
                                <InfoCircleOutlined
                                    translate="true"
                                    style={{ color: "#1890ff" }}
                                />
                                已选择{<span style={{ color: "blue" }}>{selectedRowKeys.length}</span>}项
                            </span>
                        </Col>
                        <Col span={12} >
                            <span style={{ float: 'right' }}>
                                {
                                    selectedRowKeys.length > 0 ? (
                                        <span style={{ paddingRight: '64px' }}>
                                            <Button
                                                icon={<UsergroupDeleteOutlined translate="true" />}
                                                onClick={this.delete}
                                            >
                                            批量删除
                                          </Button>
                                        </span>
                                    ) : null
                                }
                                <Button
                                    icon={<UserAddOutlined translate="true" />}
                                    onClick={this.add}
                                >
                                    添加
                                </Button>
                                <AddOperator onModalOk={this.onModalOk} onModalCancel={this.onModalCancel} isModalVisible={isModalVisible} />
                                <span style={{ paddingLeft: '64px' }} >
                                    <SyncOutlined
                                        translate="true" onClick={this.sync}
                                    />
                                </span>
                            </span>
                        </Col>
                    </Row>
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