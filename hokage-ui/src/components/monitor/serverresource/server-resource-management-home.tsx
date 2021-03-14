import React from 'react'
import ServerCardPanel from '../../common/server-card-panel'
import ServerResourceManagement from './server-resource-management'
import { ActionPanesType } from '../../common/server-card'
import BreadcrumbCustom, { BreadcrumbPrpos } from '../../bread-crumb-custom'
import { Tabs } from 'antd';

const breadcrumbProps: BreadcrumbPrpos[] = [
    {
        name: '首页',
        link: '/app/index'
    },
    {
        name: '资源监控'
    }
]

type StateType = {
    actionPanes: ActionPanesType[],
    activeKey: string
}

export default class ServerResourceManagementHome extends React.Component<{}, StateType> {

    constructor(props: any) {
        super(props)
        this.state = {
            actionPanes: [{
                title: "我的服务器",
                content: <ServerCardPanel actionName={'资源管理'} action={this.addPane} />,
                key: '1',
            }],
            activeKey: '1'
        }
    }

    addPane = (id: string) => {
        const { actionPanes } = this.state
        if (!actionPanes.some(pane => pane.key === id)) {
            const pane: ActionPanesType = {
                key: id,
                content: <ServerResourceManagement />,
                title: id
            }
            actionPanes.push(pane)
        }

        this.setState({
            actionPanes: actionPanes,
            activeKey: id
        })
    }

    onChange = (activeKey: string) => {
        this.setState({
            activeKey: activeKey
        })
    }

    onEdit = (targetKey: any, action: string): void => {
        let { activeKey, actionPanes } = this.state
        switch(action) {
            case 'remove':
                let lastKeyIndex: number = 0
                actionPanes.forEach((pane, i) => {
                    if (pane.key === targetKey) {
                        lastKeyIndex = i - 1
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
            default:
                return
        }

    }

    render() {
        const { activeKey, actionPanes } = this.state
        return (
            <>
                <BreadcrumbCustom breadcrumProps={breadcrumbProps} />
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