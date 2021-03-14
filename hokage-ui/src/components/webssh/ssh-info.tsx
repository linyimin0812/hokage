import React, { ReactText } from 'react'
import { message, Table } from 'antd';
import ApplyAndSearchServer from '../common/apply-and-search-server'
import { ServerSearchForm, ServerVO } from '../../axios/action/server/server-type';
import { column } from './column-definition'
import { ServerAction } from '../../axios/action/server/server-action';

// const datas: any[] = []
// for (let i = 0; i < 11; i++) {
//     const data = {
//         serverIp: '10.108.210' + (i + 5),
//         loginForm: (i % 2 === 0) ? '私钥' : '密码',
//         status: (i % 2 === 0) ? '退出' : '登录',
//         account: "banzhe_" + i,
//         remark: '测试',
//         action: '登录 | 退出 | 编辑 | 删除',
//         key: i
//     }
//     datas.push(data)
// }

type SshInfoState = {
    selectedRowKeys: ReactText[],
    dataSource: ServerVO[],
    loading: boolean
}

const hokageUid: number = parseInt(window.localStorage.getItem('hokageUid') || '0')

export default class SshInfo extends React.Component<{}, SshInfoState> {

    state = {
        selectedRowKeys: [],
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
    sync = () => {
        alert("sync operator")
    }

    render() {

        const { selectedRowKeys, dataSource, loading } = this.state

        const rowSelection = {
            selectedRowKeys,
            onChange: this.onSelectChange,
        };

        return (
            <>
                <div style={{ backgroundColor: '#FFFFFF' }}>
                    <ApplyAndSearchServer selectionKeys={selectedRowKeys} />
                    <Table
                        columns={column}
                        dataSource={dataSource}
                        rowSelection={rowSelection}
                        loading={loading}
                    />
                </div>
            </>
        )
    }
}