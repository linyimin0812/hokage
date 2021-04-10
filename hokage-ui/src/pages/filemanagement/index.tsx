import BreadcrumbCustom, { BreadcrumbPrpos } from "../../layout/bread-crumb-custom"
import React from 'react'
import FileManagement from "./file-management"
import { Tabs } from "antd"
import ServerCardPanel from "../common/server-card-panel"
import { ActionPanesType } from '../common/server-card'

const breadcrumbProps: BreadcrumbPrpos[] = [
    {
        name: '首页',
        link: '/app/index'
    },
    {
        name: '文件管理'
    }
]

interface HomePropsType {
    initActionPanes: ActionPanesType[],
    initActiveKey: string,
    addActionPanel: (id: string) => void
}

interface FileManagementState {
    actionPanes: ActionPanesType[],
    activeKey: string
}

export default class FileManagementHome extends React.Component<HomePropsType, FileManagementState> {
    constructor(props: any) {
        super(props)
        this.state = {
            actionPanes: [{
                key: '1',
                content: <ServerCardPanel actionName={'文件管理'} action={this.addPane} />,
                title: '我的服务器',
                closable: false
            }],
            activeKey: '1'
        }
    }

    onChange = (activeKey: string) => {
        this.setState({
            activeKey: activeKey
        })
    }

    addPane = (id: string) => {
        const { actionPanes } = this.state
        if (!actionPanes.some(pane => pane.key === id)) {
            const pane: ActionPanesType = {
                key: id,
                content: <FileManagement />,
                title: id
            }
            actionPanes.push(pane)
        }

        this.setState({
            actionPanes: actionPanes,
            activeKey: id
        })
    }

    onEdit = (targetKey: any, action: 'add' | 'remove'): void => {
        let { activeKey, actionPanes } = this.state
        switch(action) {
            case 'remove':
                let lastKeyIndex: number = 0
                actionPanes.forEach((pane, i) => {
                    if (pane.key === targetKey) {
                        lastKeyIndex = i -1
                    }
                })

                const newPanes: ActionPanesType[] = actionPanes.filter(pane => pane.key !== targetKey)

                if (targetKey === activeKey && newPanes.length) {
                    lastKeyIndex = lastKeyIndex >=0 ? lastKeyIndex : 0
                    activeKey = newPanes[lastKeyIndex].key
                }

                this.setState({
                    actionPanes: newPanes,
                    activeKey
                })
                break
            case 'add':
                break
        }

    }

    render() {
        const { activeKey, actionPanes } = this.state
        return (
            <>
                <BreadcrumbCustom breadcrumbProps={breadcrumbProps} />
                <Tabs
                    onChange={this.onChange}
                    activeKey={activeKey}
                    type="editable-card"
                    hideAdd
                    onEdit={this.onEdit}
                >
                    {
                        actionPanes.map(pane => {
                            return (
                                <Tabs.TabPane
                                    tab={pane.title}
                                    key={pane.key}
                                    closable={pane.closable}
                                >
                                    {pane.content}
                                </Tabs.TabPane>
                            )
                        })
                    }
                </Tabs>
            </>
        )
    }
}
