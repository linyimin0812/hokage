import React, { ReactText } from 'react'
import { Table, Result, Button, Tag, Row, Col, message, Divider } from 'antd';
import BreadcrumbCustom, { BreadcrumbPrpos } from '../../BreadcrumbCustom';
import Search from './Search';
import { InfoCircleOutlined, MinusOutlined, PlusOutlined, SyncOutlined } from '@ant-design/icons';
import AddServer from '../AddServer';

const renderStatus = (text: string, _: any, __: any) => {
    let color: string = ''
    switch (text) {
        case '在线':
        case '登录':
            color = 'green'
            break;
        case '掉线':
        case '退出':
            color = 'red'
            break
        default:
            color = 'red'
            break
    }
    return <Tag color={color}> {text} </Tag>
}

const columns = [
    {
        title: "id",
        dataIndex: "id",
        key: "id"
    },
    {
        title: "主机名",
        dataIndex: "hostname",
        key: "hostname"
    },
    {
        title: "域名",
        dataIndex: "domainName",
        key: "domainName"
    },
    {
        title: "登录账号",
        dataIndex: "account",
        key: "account"
    },
    {
        title: '服务器状态',
        dataIndex: 'serverStatus',
        key: 'serverStatue',
        render: renderStatus
    },
    {
        title: '我的状态',
        dataIndex: 'myStatus',
        key: 'myStatus',
        render: renderStatus
    },
    {
        title: "备注",
        dataIndex: "remark",
        key: "remark"
    },
    {
        title: "操作",
        dataIndex: "action",
        key: "action"
    },
]
const datas: any[] = []
for (let i = 0; i < 5; i++) {
    const data = {
        key: i + 1,
        id: 'id_' + i,
        hostname: 'master_' + i + ".pcncad.club",
        domainName: 'name_' + i + ".pcncad.club",
        account: "banzhe_" + i,
        serverStatus: i % 2 === 0 ? '掉线' : '在线',
        myStatus: i % 2 === 0 ? '退出' : '登录',
        action: 'Web SSH | 文件管理 | 远程命令 | 更多'
    }
    datas.push(data)
}



const breadcrumProps: BreadcrumbPrpos[] = [
    {
        name: '首页',
        link: '/app/index'
    },
    {
        name: '我的服务器'
    },
    {
        name: '我使用的服务器'
    }
]

type MyServerState = {
    selectedRowKeys: ReactText[],
    isModalVisible: boolean
}

export default class MyServer extends React.Component<any, MyServerState> {

    state = {
        selectedRowKeys: [],
        isModalVisible: false
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

        const { selectedRowKeys, isModalVisible } = this.state
        const rowSelection = {
            selectedRowKeys,
            onChange: this.onSelectChange,

        };

        return (
            <>
                <BreadcrumbCustom breadcrumProps={breadcrumProps} />
                {
                    (datas === undefined || datas.length === 0)
                        ?
                        <Result
                            title="你还没有可用服务器哦,请点击申请按钮进行申请"
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
                          selectedRowKeys.length > 0 ? ([
                              <Button
                                  icon={<MinusOutlined translate="true" />}
                                  onClick={this.delete}
                              >
                                  批量删除
                              </Button>,
                              <Divider type="vertical" />
                          ]) : (
                              null
                          )
                      }
                        <Button
                            icon={<PlusOutlined translate="true" />}
                            onClick={this.applyServer}
                        >
                        申请服务器
                      </Button>
                      <AddServer onModalOk={this.onModalOk} onModalCancel={this.onModalCancel} isModalVisible={isModalVisible} />                      
                      <Divider type="vertical" />
                      <Button
                          icon={<PlusOutlined translate="true" />}
                          onClick={this.add}
                      >
                        添加服务器
                      </Button>
                      <span style={{ paddingLeft: '64px' }} >
                        <SyncOutlined
                            translate="true" onClick={this.sync}
                        />
                      </span>
                    </span>
                                    </Col>
                                </Row>
                                <Table
                                    columns={columns}
                                    dataSource={datas}
                                    rowSelection={rowSelection}
                                />
                            </div>
                        </>
                }
            </>
        )
    }
}