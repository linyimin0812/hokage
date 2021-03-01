import React, { ReactText } from 'react'
import { message, Table, Row, Col, Button, Result, Divider } from 'antd'
import BreadcrumbCustom from '../../bread-crumb-custom'
import { InfoCircleOutlined, SyncOutlined, PlusOutlined, MinusOutlined } from '@ant-design/icons'
import { TableExtendable } from '../../common/table-extendable'
import Search from './search'
import ApplyServer from '../apply-server'
import { breadcrumbProps, columns, nestedColumns } from './column-definition'

interface NestedTableDataSource {
    key: string,
    id: string,
    name: string,
    loginName: string,
    applyTime: string,
    lastLoginTime: string,
    action: string
}

type AllServerState = {
    expandable: TableExtendable,
    nestedTableDataSource: NestedTableDataSource[],
    selectedRowKeys: ReactText[],
    isModalVisible: boolean
}

export default class MyOperateServer extends React.Component<{}, AllServerState> {

    state: AllServerState = {
        expandable: {
            expandedRowKeys: [],
            expandedRowRender: () => {
                return <Table columns={nestedColumns} dataSource={this.state.nestedTableDataSource} pagination={false} />;
            },
            onExpand: (expanded: boolean, record: any) => {
                if (expanded) {
                    // TODO: 这里替换成接口,请求真实的数据
                    const expandedRowKeys: string[] = [record.key]
                    const datasources: NestedTableDataSource[] = []
                    // const colors = ['ordinaryServer', 'gpuServer', "intranetServer", "publicNetworkServer"]
                    for (let i = 0; i < 3; i++) {
                        const data: NestedTableDataSource = {
                            key: record.key + '_' + i,
                            id: i.toString(),
                            name: 'banzhe',
                            loginName: 'banzhe',
                            applyTime: new Date().toISOString(),
                            lastLoginTime: new Date().toISOString(),
                            action: '查看 | 删除'
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
        console.log(value)
        this.setState({ ...this.state, isModalVisible: false })
        message.loading({ content: 'Loading...', key: 'addUser' });
        setTimeout(() => {
            message.success({ content: 'Loaded!', key: 'addUser', duration: 2 });
        }, 2000);
    }

    onModalCancel = () => {
        this.setState({ ...this.state, isModalVisible: false })
        message.warning({ content: '添加用户已经取消!', key: 'addUser', duration: 2 });
    }

    applyServer = () => {
        window.location.href="/#/app/server/all"
    }

    render() {
        const data: any = []
        for (let i = 0; i < 5; i++) {
            const value = {
                key: i + 1,
                hostname: 'master_' + i + ".pcncad.club",
                domain: 'name_' + i + ".pcncad.club",
                ipAddress: `10.108.211.${i+1}`,
                serverTags: ['ordinaryServer', 'gpuServer', "intranetServer", "publicNetworkServer"],
                numOfUser: i + 1,
                status: "在线",
                action: '添加用户 | 删除服务器 | 服务器信息'
            }
            data.push(value)
        }
        const { selectedRowKeys, isModalVisible } = this.state
        const rowSelection = {
            selectedRowKeys,
            onChange: this.onSelectChange,
        };

        return (
            <div>
                <BreadcrumbCustom breadcrumProps={breadcrumbProps} />
                {
                    (data.length === 0)
                        ?
                        <Result
                            title="你还没有可管理的服务器哦,请点击申请按钮进行申请"
                            extra={
                                <Button type="primary" onClick={this.applyServer}>
                                    申请
                                </Button>
                            }
                        />
                        :
                        <>
                            <Search onFinish={this.onFinish} clear={this.resetFields} />
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
                                                selectedRowKeys.length > 0 ? ([
                                                    <Button
                                                        icon={<MinusOutlined translate="true" />}
                                                        onClick={this.delete}
                                                    >
                                                        批量删除
                                                    </Button>,
                                                    <Divider type="vertical" />
                                                ]) : null
                                            }
                                            <Button icon={<PlusOutlined translate="true" />} onClick={this.add} >
                                                申请
                                            </Button>
                                            <ApplyServer onModalOk={this.onModalOk} onModalCancel={this.onModalCancel} isModalVisible={isModalVisible} />
                                            <span style={{ paddingLeft: '64px' }} >
                                                <SyncOutlined translate="true" onClick={this.sync} />
                                            </span>
                                        </span>
                                    </Col>
                                </Row>
                                <Table
                                    rowSelection={rowSelection}
                                    columns={columns}
                                    dataSource={data}
                                    expandable={this.state.expandable}
                                />
                            </div>
                        </>
                }
            </div>
        )
    }

} 