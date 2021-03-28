import React, { ReactText } from 'react'
import { message, Table, Row, Col, Button, Divider } from 'antd'
import BreadcrumbCustom from '../../bread-crumb-custom'
import { InfoCircleOutlined, SyncOutlined, PlusOutlined, MinusOutlined } from '@ant-design/icons'
import { TableExtendable } from '../../common/table-extendable'
import { MyOperatorServerSearch } from './search'
import ApplyServer from '../apply-server'
import { breadcrumbProps, columns, nestedColumns } from './column-definition'
import { ServerSearchForm, ServerVO } from '../../../axios/action/server/server-type'
import { Operation } from '../../../axios/action/user/user-type'
import { searchServer } from '../util'

interface NestedTableDataSource {
    key: string,
    id: string,
    username: string,
    account: string,
    applyTime: string,
    lastLoginTime: string,
    operationList: Operation[]
}

type AllServerState = {
    expandable: TableExtendable,
    nestedTableDataSource: NestedTableDataSource[],
    selectedRowKeys: ReactText[],
    isModalVisible: boolean,
    dataSource: ServerVO[],
    loading: boolean
}

export default class MyOperateServer extends React.Component<{}, AllServerState> {

    state: AllServerState = {
        expandable: {
            expandedRowKeys: [],
            expandedRowRender: () => {
                return <Table columns={nestedColumns} dataSource={this.state.nestedTableDataSource} pagination={false} />;
            },
            onExpand: (expanded: boolean, record: ServerVO) => {
                if (expanded) {
                    // TODO: 这里替换成接口,请求真实的数据
                    const expandedRowKeys: string[] = [record.key!]
                    const datasources: NestedTableDataSource[] = []
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
        isModalVisible: false,
        dataSource: [],
        loading: false
    }

    componentDidMount() {
        searchServer(this)
    }

    onFinish = (value: ServerSearchForm) => {
        searchServer(this, value)
    }

    resetFields = () => {
        console.log("reset")
    }

    onSelectChange = (selectedRowKeys: ReactText[], selectedRows: any[]) => {
        this.setState({ selectedRowKeys })
        // TODO: 从selectRows中获取选择的目标数据,然后进行相关操作
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
        const { selectedRowKeys, isModalVisible, dataSource, loading } = this.state
        const rowSelection = {
            selectedRowKeys,
            onChange: this.onSelectChange,
        };

        return (
            <div>
                <BreadcrumbCustom breadcrumProps={breadcrumbProps} />
                <>
                    <MyOperatorServerSearch onFinish={this.onFinish} />
                    <div style={{ backgroundColor: '#FFFFFF' }}>
                        <Row
                            gutter={24}
                            style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', margin: '0 0' }}
                        >
                            <Col span={12} style={{ display: 'flex', alignItems: 'center' }}>
                                <span>
                                    <InfoCircleOutlined translate="true" style={{ color: "#1890ff" }} />
                                    已选择{<span style={{ color: "blue" }}>{selectedRowKeys.length}</span>}项
                                </span>
                            </Col>
                            <Col span={12} >
                                <span style={{ float: 'right' }}>
                                    {
                                        selectedRowKeys.length > 0 ? ([
                                            <Button icon={<MinusOutlined translate="true" />} onClick={this.delete}>
                                                批量删除
                                            </Button>,
                                            <Divider type="vertical" />
                                        ]) : null
                                    }
                                    <Button icon={<PlusOutlined translate="true" />} onClick={() => this.setState({ ...this.state, isModalVisible: true })} >
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
                            dataSource={dataSource}
                            expandable={this.state.expandable}
                            loading={loading}
                        />
                    </div>
                </>
            </div>
        )
    }

} 