import React, { ReactText } from 'react'
import { Table, Button, Row, Col, message, Divider } from 'antd'
import BreadcrumbCustom from '../../../components/bread-crumb-custom'
import { MyServerSearch } from './search'
import { InfoCircleOutlined, MinusOutlined, PlusOutlined, SyncOutlined } from '@ant-design/icons'
import AddServer from '../add-server'
import { breadcrumbProps, columns } from './column-definition'
import { ServerSearchForm, ServerVO } from '../../../axios/action/server/server-type'
import AddAccount from '../add-account'
import { searchServer } from '../util'

type MyServerState = {
    selectedRowKeys: ReactText[],
    isAddServerModalVisible: boolean,
    isAddAccountModalVisible: boolean,
    dataSource: ServerVO[],
    loading: boolean
}

export default class Index extends React.Component<any, MyServerState> {

    state = {
        selectedRowKeys: [],
        isAddServerModalVisible: false,
        isAddAccountModalVisible: false,
        dataSource: [],
        loading: false
    }

    componentDidMount() {
        searchServer(this)
    }
    applyServer = () => {
        window.location.href = "/app/server/all"
    }

    onFinish = (value: ServerSearchForm) => {
        searchServer(this, value)
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

    onAddServerModalOk = (value: any) => {
        console.log(value)
        this.setState({ ...this.state, isAddServerModalVisible: false })
        message.loading({ content: 'Loading...', key: 'addUser' });
        setTimeout(() => {
            message.success({ content: 'Loaded!', key: 'addUser', duration: 2 });
        }, 2000);
    }

    onAddServerModalCancel = () => {
        this.setState({ ...this.state, isAddServerModalVisible: false })
        message.warning({ content: '添加用户已经取消!', key: 'addUser', duration: 2 });
    }

    onAddAccountModalOk = (value: any) => {
        console.log(value)
    }

    onAddAccountModalCancel = () => {
        this.setState({ ...this.state, isAddAccountModalVisible: false })
    }



    render() {

        const { selectedRowKeys, isAddServerModalVisible, isAddAccountModalVisible, dataSource, loading } = this.state
        const rowSelection = {
            selectedRowKeys,
            onChange: this.onSelectChange,
        };

        return (
            <>
                <BreadcrumbCustom breadcrumbProps={breadcrumbProps} />
                <MyServerSearch onFinish={this.onFinish} />
                <div style={{ backgroundColor: '#FFFFFF' }}>
                    <Row
                        gutter={24}
                        style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', margin: '0 0' }}
                    >
                        <Col span={8} style={{ display: 'flex', alignItems: 'center' }}>
                            <span>
                            <InfoCircleOutlined translate="true" style={{ color: "#1890ff" }} />
                                已选择{<span style={{ color: "blue" }}>{selectedRowKeys.length}</span>}项
                            </span>
                        </Col>
                        <Col span={16} >
                            <span style={{ float: 'right' }}>
                                {
                                    selectedRowKeys.length > 0 ? ([
                                        <Button icon={<MinusOutlined translate="true" />} onClick={this.delete}>
                                            批量删除
                                        </Button>,
                                        <Divider type="vertical" />
                                    ]) : null
                                }
                                <Button icon={<PlusOutlined translate="true" />} onClick={this.applyServer}>
                                    申请服务器
                                </Button>
                                <AddServer onModalOk={this.onAddServerModalOk} onModalCancel={this.onAddServerModalCancel} isModalVisible={isAddServerModalVisible} />
                                <AddAccount onModalOk={this.onAddAccountModalOk} onModalCancel={this.onAddAccountModalCancel} isModalVisible={isAddAccountModalVisible} />
                                <Divider type="vertical" />
                                <Button icon={<PlusOutlined translate="true" />} onClick={() => this.setState({ ...this.state, isAddServerModalVisible: true })}>
                                    添加服务器
                                </Button>
                                <Divider type="vertical" />
                                <Button icon={<PlusOutlined translate="true" />} onClick={() => this.setState({ ...this.state, isAddAccountModalVisible: true })}>
                                    添加账号
                                </Button>
                                <span style={{ paddingLeft: '64px' }} >
                                    <SyncOutlined translate="true" onClick={this.sync} />
                                </span>
                            </span>
                        </Col>
                    </Row>
                    <Table
                        columns={columns}
                        dataSource={dataSource}
                        rowSelection={rowSelection}
                        loading={loading}
                    />
                </div>
            </>
        )
    }
}