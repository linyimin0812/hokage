import React, { ReactText } from 'react'
import { message, Table } from 'antd'
import ApplyAndSearchServer from '../common/apply-and-search-server'
import { ServerSearchForm, ServerVO } from '../../axios/action/server/server-type'
import { column } from './column-definition'
import { ServerAction } from '../../axios/action/server/server-action'
import { Operation } from '../../axios/action/user/user-type'
import { getHokageRole, getHokageUid } from '../../libs'

type SshInfoProps = {
    addSshTerm: (record: ServerVO) => void
}

type SshInfoState = {
    selectedRowKeys: ReactText[],
    dataSource: ServerVO[],
    loading: boolean
}

export default class WebSshServer extends React.Component<SshInfoProps, SshInfoState> {

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
            operatorId: getHokageUid(),
            role: getHokageRole()
        }
        ServerAction.searchServer(form).then(result => {
            result = (result || []).map(serverVO => {
                serverVO.key = serverVO.id + ''
                const operationList: Operation[] = serverVO.operationList
                // TODO: 所有动作由后台传回，对于action类型，添加相关动作
                operationList.push(
                    {
                        operationType: 'action',
                        operationName: '登录',
                        operationAction: this.props.addSshTerm,
                    },
                )
                serverVO.operationList = operationList
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
