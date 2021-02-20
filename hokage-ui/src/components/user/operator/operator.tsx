import React, { ReactText } from 'react'
import { Table, Row, Col, Button, message } from 'antd'
import BreadcrumbCustom, { BreadcrumbPrpos } from '../../bread-crumb-custom'
import Search from './search'
import { UserAddOutlined, InfoCircleOutlined, SyncOutlined, UsergroupDeleteOutlined } from '@ant-design/icons'
import AddOperator from './add-operator'
import { TableExtendable } from '../../common/table-extendable'
import { UserAction } from '../../../axios/action'
import { Operation, UserVO } from '../../../axios/action/user/user-form';
import { breadcrumProps, columns, nestedColumn } from './column-definition';

interface NestedTableDataSource {
    key: string,
    hostname: string,
    domainName: string,
    serverTags: string[],
    numberOfUser: number,
    status: string,
    action: string
}

type OperatorState = {
    dataSource: UserVO[],
    expandable: TableExtendable,
    nestedTableDataSource: NestedTableDataSource[],
    selectedRowKeys: ReactText[],
    isModalVisible: boolean
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
                if (expanded) {
                    // TODO: 这里替换成接口,请求真实的数据
                    const expandedRowKeys: string[] = [record.key]
                    const datasources: NestedTableDataSource[] = []
                    const colors = ['ordinaryServer', 'gpuServer', "intranetServer", "publicNetworkServer"]
                    for (let i = 0; i < 3; i++) {
                        const data: NestedTableDataSource = {
                            key: record.key + '_' + i,
                            hostname: record.username,
                            domainName: record.username,
                            serverTags: [colors[i], colors[i+1]],
                            numberOfUser: 3,
                            status: '在线',
                            action: '回收'
                        }
                        datasources.push(data)
                    }
                    const expandable: TableExtendable = this.state.expandable
                    expandable.expandedRowKeys = expandedRowKeys

                    this.setState({ ...this.state, nestedTableDataSource: datasources, expandable })
                } else {
                    const expandable: TableExtendable = this.state.expandable
                    expandable.expandedRowKeys = []

                    this.setState({ ...this.state, expandable })
                }
            }
        },
        nestedTableDataSource: [],
        selectedRowKeys: [],
        isModalVisible: false
    }

    componentDidMount() {
        // TODO: 获取管理员信息
        UserAction.listSupervisor().then(supervisorList => {
            this.setState({dataSource: supervisorList})
        }).catch(err => {
            message.error(err)
        })
    }

    // @ts-ignore
    hokageUid: number = window.hokageUid || 0

    onFinish = (value: any) => {
        console.log(value)
    }

    resetFields = () => {
        console.log("reset")
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
            id: this.hokageUid,
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

        const { selectedRowKeys, isModalVisible, dataSource } = this.state
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
                <Search onFinish={this.onFinish} clear={this.resetFields} />
                <div style={{ backgroundColor: '#FFFFFF' }}>
                    <Row
                        gutter={24}
                        style={{ backgroundColor: '#e6f7ff', border: '#91d5ff' }}
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
                        expandable={this.state.expandable}
                    />
                </div>
            </div>
        )
    }
}