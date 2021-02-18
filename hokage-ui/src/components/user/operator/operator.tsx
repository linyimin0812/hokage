import React, { ReactText } from 'react'
import { Table, Row, Col, Button, Tag, message } from 'antd'
import BreadcrumbCustom, { BreadcrumbPrpos } from '../../bread-crumb-custom'
import Search from './search'
import {
    UserAddOutlined,
    InfoCircleOutlined,
    SyncOutlined,
    UsergroupDeleteOutlined
} from '@ant-design/icons'
import AddOperator from './add-operator'

import { TableExtendable } from '../../common/table-extendable'
import { Models } from '../../../utils/model'
import { hashCode } from '../../../utils'
import { UserAction } from '../../../axios/action'

interface NestedTableDataSource {
    key: string,
    hostname: string,
    domainName: string,
    serverTags: string[],
    numberOfUser: number,
    status: string,
    action: string
}

const serverLabelColors: string[] = Models.get('serverLabelColor')

// 嵌套表 
const columns = [
    {
        title: '主机名',
        dataIndex: 'hostname',
        key: 'hostname'
    },
    {
        title: '域名',
        dataIndex: 'domainName',
        key: 'domainName'
    },
    {
        title: '标签',
        dataIndex: 'serverTags',
        key: 'serverTags',
        render: (serverTags: any, _: any, __: any) => {
            return serverTags.map((tag: any )=> <Tag color={serverLabelColors[hashCode(tag) % serverTags.length]} key={tag}>{tag}</Tag>)
        }
    },
    {
        title: '使用人数',
        dataIndex: 'numberOfUser',
        key: 'numOfUser'
    },
    {
        title: '状态',
        dataIndex: 'status',
        key: 'status',
        render: (text: string, _: any, __: any) => <Tag color = {serverLabelColors[hashCode(text) % serverLabelColors.length]}> { text } </Tag>
    },
    {
        title: '操作',
        dataIndex: 'action',
        key: 'action'
    }
]


type OperatorState = {
    expandable: TableExtendable,
    nestedTableDataSource: NestedTableDataSource[],
    selectedRowKeys: ReactText[],
    isModalVisible: boolean
}

const breadcrumProps: BreadcrumbPrpos[] = [
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

export default class Operator extends React.Component<any, OperatorState> {

    state: OperatorState = {
        expandable: {
            expandedRowKeys: [],
            expandedRowRender: () => {
                return <Table columns={columns} dataSource={this.state.nestedTableDataSource} pagination={false} />;
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

    // @ts-ignore
    hokageUid: number = window.hokageUid || 0

    columns = [
        {
            title: 'id',
            dataIndex: 'id',
            key: 'id'
        },
        {
            title: '姓名',
            dataIndex: 'username',
            key: 'username'
        },
        {
            title: '负责服务器数量',
            dataIndex: 'serverNum',
            key: 'serverNum'
        },
        {
            title: '服务器标签',
            dataIndex: 'serverLabel',
            key: 'serverLabel',
            render: (serverLabel: string[], _: any, __: any) => serverLabel.map(
                (tag: string)=> <Tag color={serverLabelColors[hashCode(tag) % serverLabel.length]} key={tag}>{tag}</Tag>
            )
        },
        {
            title: '操作',
            dataIndex: 'action',
            key: 'action'
        }
    ]

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
        const data: any = []
        for (let i = 0; i < 5; i++) {
            const value = {
                key: i + 1,
                id: 'id_' + i,
                username: 'name_' + i + ".pcncad.club",
                serverLabel: ['ordinaryServer', 'gpuServer', "intranetServer", "publicNetworkServer"],
                serverNum: i + 1,
                status: "online",
                action: '查看 | 修改 | 添加服务器 | 删除'
            }
            data.push(value)
        }
        const { selectedRowKeys, isModalVisible } = this.state
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
                        columns={this.columns}
                        dataSource={data}
                        expandable={this.state.expandable}
                    />
                </div>
            </div>
        )
    }
}