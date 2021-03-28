import React, { ReactText } from 'react'
import { message, Table, Row, Col, Button, Divider } from 'antd'
import BreadcrumbCustom from '../../bread-crumb-custom'
import { InfoCircleOutlined, SyncOutlined, PlusOutlined, MinusOutlined } from '@ant-design/icons'
import { AllServerSearch } from './search'
import AddServer from '../add-server'
import { breadcrumbProps, columns } from './column-definition'
import { ServerForm, ServerSearchForm, ServerVO } from '../../../axios/action/server/server-type'
import { ServerAction } from '../../../axios/action/server/server-action'
import { getHokageUid } from '../../../utils';
import { ServiceResult } from '../../../axios/common'
import { searchServer } from '../util'

type AllServerState = {
    selectedRowKeys: ReactText[],
    isModalVisible: boolean,
    dataSource: ServerVO[],
    loading: boolean
}

export default class AllServer extends React.Component<{}, AllServerState> {

    state: AllServerState = {
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

    onModalOk = (value: ServerForm) => {
        value.operatorId = getHokageUid()
        if (value.passwd && !(typeof value.passwd === 'string')) {
            const uploadResponse = value.passwd.file.response as ServiceResult<string>
            if (uploadResponse.success) {
                value.passwd = uploadResponse.data!
            } else {
                message.error('密钥文件上传失败, 请重试！')
            }
        }
        if (!value.loginType) {
            value.loginType = 0
        }
        ServerAction.saveServer(value).then(() => {
            this.setState({ isModalVisible: false })
            searchServer(this)
        }).catch(e => message.error(e))
    }

    onModalCancel = () => {
        this.setState({ ...this.state, isModalVisible: false })
        message.warning({ content: '添加用户已经取消!', key: 'addUser', duration: 2 });
    }

    render() {
        const { selectedRowKeys, isModalVisible, dataSource, loading } = this.state
        const rowSelection = {
            selectedRowKeys,
            onChange: this.onSelectChange
        };

        return (
            <div>
                <BreadcrumbCustom breadcrumProps={breadcrumbProps} />
                <AllServerSearch onFinish={this.onFinish} />
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
                                        <Button icon={<MinusOutlined translate="true" />} onClick={this.delete}>
                                            批量删除
                                        </Button>,
                                        <Divider type="vertical" />
                                    ]) : null
                                }
                                <Button icon={<PlusOutlined translate="true" />} onClick={this.add}>
                                    添加
                                </Button>
                                <AddServer onModalOk={this.onModalOk} onModalCancel={this.onModalCancel} isModalVisible={isModalVisible} />
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
                        loading={loading}
                    />
                </div>
            </div>
        )
    }

} 