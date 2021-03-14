import React from 'react'
import { message, Row, Col } from 'antd'
import ServerCard from './server-card'
import ApplyServerPrompt from './apply-server-prompt'
import ApplyAndSearchServer from './apply-and-search-server'
import { ServerSearchForm, ServerVO } from '../../axios/action/server/server-type'
import { ServerAction } from '../../axios/action/server/server-action'

type FileServerProps = {
    actionName: string
    action: (id: string) => void
}

type FileServerState = {
    isModalVisible: boolean,
    dataSource: ServerVO[]
}

const hokageUid: number = parseInt(window.localStorage.getItem('hokageUid') || '0')

export default class ServerCardPanel extends React.Component<FileServerProps, FileServerState> {

    state = {
        isModalVisible: false,
        dataSource: []
    }

    componentDidMount() {
        this.listServer()
    }

    listServer = () => {
        const form: ServerSearchForm = {
            operatorId: hokageUid
        }
        ServerAction.searchServer(form).then(result => {
            result = (result || []).map(serverVO => {
                serverVO.key = serverVO.id + ''
                return serverVO
            })
            this.setState({dataSource: result})
        }).catch(err => message.error(err))
    }

    applyServer = () => {
        window.location.href = "/app/server/all"
    }

    onFinish = (value: any) => {
        console.log(value)
    }

    resetFields = () => {
        console.log("reset")
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
        }, 2000)
    }

    onModalCancel = () => {
        this.setState({ ...this.state, isModalVisible: false })
        message.warning({ content: '添加用户已经取消!', key: 'addUser', duration: 2 });
    }

    enterFileManagement = () => {
        alert('enter file management')
    }

    renderServerCards = (dataSource: ServerVO[]) => {
        return dataSource.map(serverVO => {
            return (
                <Col span={8}>
                    <ServerCard
                        account={serverVO.account}
                        serverIp={serverVO.ip}
                        description={serverVO.description}
                        actionName={this.props.actionName}
                        action={this.props.action}
                    />

                </Col>
            )
        })
    }

    render() {

        const { dataSource } = this.state

        return (
            <>
                {
                    (dataSource === undefined || dataSource.length === 0)
                        ?
                        <ApplyServerPrompt />
                        :
                        <div style={{ backgroundColor: '#FFFFFF' }}>
                            <ApplyAndSearchServer />
                            <Row gutter={24} style={{ margin: '0 0' }}>
                                {this.renderServerCards(dataSource)}
                            </Row>
                        </div>
                }

            </>
        )
    }
}