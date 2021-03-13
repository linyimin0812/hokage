import React, { ReactText } from 'react'
import { Table, Result, Button, Tag, Row, Col, message, Divider } from 'antd'
import BreadcrumbCustom, { BreadcrumbPrpos } from '../../bread-crumb-custom'
import Search from './search'
import { InfoCircleOutlined, MinusOutlined, PlusOutlined, SyncOutlined } from '@ant-design/icons'
import AddServer from '../add-server'
import { breadcrumbProps, columns } from './column-definition'
import { ServerSearchForm, ServerVO } from '../../../axios/action/server/server-type';
import { ServerAction } from '../../../axios/action/server/server-action';

type MyServerState = {
    selectedRowKeys: ReactText[],
    isModalVisible: boolean,
    dataSource: ServerVO[],
    loading: boolean
}

const hokageUid: number = parseInt(window.localStorage.getItem('hokageUid') || '0')

export default class MyServer extends React.Component<any, MyServerState> {

    state = {
        selectedRowKeys: [],
        isModalVisible: false,
        dataSource: [],
        loading: false
    }

    componentDidMount() {
        this.listServer()
    }

    listServer = () => {
        this.setState({loading: true})
        const form: ServerSearchForm = {
            operatorId: hokageUid
        }
        ServerAction.searchServer(form).then(result => {
            result = (result || []).map(serverVO => {
                serverVO.key = serverVO.id + ''
                return serverVO
            })
            this.setState({dataSource: result})
        }).catch(err => message.error(err)).finally(() => this.setState({loading: false}))
    }

    applyServer = () => {
        window.location.href = "/#/app/server/all"
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



    render() {

        const { selectedRowKeys, isModalVisible, dataSource, loading } = this.state
        const rowSelection = {
            selectedRowKeys,
            onChange: this.onSelectChange,
        };

        return (
            <>
                <BreadcrumbCustom breadcrumProps={breadcrumbProps} />
                <Search onFinish={this.onFinish} clear={this.resetFields} />
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
                                <Button icon={<PlusOutlined translate="true" />} onClick={this.applyServer}>
                                    申请服务器
                                </Button>
                                <AddServer onModalOk={this.onModalOk} onModalCancel={this.onModalCancel} isModalVisible={isModalVisible} />
                                <Divider type="vertical" />
                                <Button icon={<PlusOutlined translate="true" />} onClick={this.add}>
                                    添加服务器
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