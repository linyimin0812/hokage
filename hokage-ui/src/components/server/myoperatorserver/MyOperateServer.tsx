import React, { ReactText } from 'react'
import { Tag, message, Table, Row, Col, Button, Result, Divider } from 'antd'
import BreadcrumbCustom, { BreadcrumbPrpos } from '../../BreadcrumbCustom'
import { InfoCircleOutlined, SyncOutlined, PlusOutlined, MinusOutlined } from '@ant-design/icons'
import { TableExtendable } from '../../common/TableExtendable'
import Search from './Search'
import OperatorApplyServer from '../OperatorApplyServer'

// 嵌套表 
const nestedColumns = [
    {
        title: 'id',
        dataIndex: 'id',
        key: 'id'
    },
    {
        title: '姓名',
        dataIndex: 'name',
        key: 'name'
    },
    {
        title: '用户名', // 服务器登录用户名
        dataIndex: 'loginName',
        key: 'loginName'
    },
    {
        title: '申请时间',
        dataIndex: 'applyTime',
        key: 'applyTime'
    },
    {
        title: '最近登录时间',
        dataIndex: 'lastLoginTime',
        key: 'lastLoginTime'
    },
    {
        title: '操作',
        dataIndex: 'action',
        key: 'action'
    }
]

const columns = [
    {
        title: 'id',
        dataIndex: 'id',
        key: 'id'
    },
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
            return (
                <span>
          {
              serverTags.map((tag: any) => {
                  let color = ''
                  let name = ''
                  switch (tag) {
                      case 'ordinaryServer':
                          color = 'magenta'
                          name = 'X86'
                          break
                      case 'gpuServer':
                          color = 'red'
                          name = 'GPU'
                          break
                      case 'intranetServer':
                          color = 'green'
                          name = '内网'
                          break
                      case 'publicNetworkServer':
                          color = 'purple'
                          name = '公网'
                          break
                      default:
                          color = '#f50'
                          name = '未知'
                  }
                  return (
                      <Tag color={color} key={tag}>
                          {name}
                      </Tag>
                  );
              })
          }
        </span>)
        }
    },
    {
        title: '使用人数',
        dataIndex: 'numOfUser',
        key: 'numOfUser'
    },
    {
        title: '状态',
        dataIndex: 'status',
        key: 'status',
        render: (text: string, _: any, __: any) => {
            let color: string = ''
            switch (text) {
                case '在线':
                    color = 'green'
                    break;
                case '掉线':
                    color = 'red'
                    break
                default:
                    color = 'red'
                    break
            }
            return (
                <Tag color={color}> {text} </Tag>
            )
        }
    },
    {
        title: '操作',
        dataIndex: 'action',
        key: 'action'
    }
]

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

const breadcrumProps: BreadcrumbPrpos[] = [
    {
        name: '首页',
        link: '/app/index'
    },
    {
        name: '我的服务器'
    },
    {
        name: '我管理的服务器'
    }
]

export default class MyOperateServer extends React.Component {

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
                id: 'id_' + i,
                hostname: 'master_' + i + ".pcncad.club",
                domainName: 'name_' + i + ".pcncad.club",
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
                <BreadcrumbCustom breadcrumProps={breadcrumProps} />
                {
                    (data === undefined || data.length === 0)
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
                            onClick={this.add}
                        >
                        申请
                      </Button>
                      <OperatorApplyServer onModalOk={this.onModalOk} onModalCancel={this.onModalCancel} isModalVisible={isModalVisible} />
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