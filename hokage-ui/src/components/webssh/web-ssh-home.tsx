import BreadcrumbCustom from "../bread-crumb-custom"
import React from 'react'
import { Tabs } from "antd"
import WebSshServer from "./web-ssh-server"
import Xterm from "./xterm"
import { breadcrumbProps } from './column-definition'

interface PanesType {
    title: string,
    content: JSX.Element,
    key: string,
    closable?: boolean
}

interface WebSshState {
    panes: PanesType[],
    activeKey: string
}

export default class WebSshHome extends React.Component<any, WebSshState> {
    constructor(props: any) {
        super(props)
        this.state = {
            panes: [{
                key: '1',
                content: <WebSshServer addSshTerm={this.addPane} />,
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
    /**
     * 需要传入一个服务器的唯一标识,用于连接服务器获取文件信息
     */
    addPane = (id: string) => {
        const { panes } = this.state

        if (!panes.some(pane => pane.key === id)) {
            const pane: PanesType = {
                key: id,
                content: <Xterm id={id} />,
                title: id
            }
            panes.push(pane)
        }
        this.setState({
            panes,
            activeKey: id
        })
    }

    onEdit = (targetKey: any, action: 'add' | 'remove'): void => {
        let { activeKey, panes } = this.state
        switch(action) {
            case 'remove':
                let lastKeyIndex: number = 0
                panes.forEach((pane, i) => {
                    if (pane.key === targetKey) {
                        lastKeyIndex = i -1
                    }
                })

                const newPanes: PanesType[] = panes.filter(pane => pane.key !== targetKey)

                if (targetKey === activeKey && newPanes.length) {
                    lastKeyIndex = lastKeyIndex >=0 ? lastKeyIndex : 0
                    activeKey = newPanes[lastKeyIndex].key
                }

                this.setState({
                    panes: newPanes,
                    activeKey
                })
                break
            case 'add':
                break
        }

    }

    render() {
        const { activeKey, panes } = this.state
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
                        panes.map(pane => {
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