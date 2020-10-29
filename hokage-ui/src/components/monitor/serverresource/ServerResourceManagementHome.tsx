import React from 'react'
import { Tabs } from 'antd'
import BreadcrumbCustom, { BreadcrumbPrpos } from '../../BreadcrumbCustom'
import FileServer from '../../filemanagement/fiile-server'
import ServerResourceManagement from './ServerResourceManagement'

const breadcrumbProps: BreadcrumbPrpos[] = [
    {
        name: '首页',
        link: '/app/index'
    },
    {
        name: '资源监控'
    }
]
// TODO: 将FileServer封装成基本组件
const initPanes = [
    {
        title: "我的服务器",
        content: <FileServer action="资源监控" />,
        key: "1",
    },
    {
        title: "资源监控",
        content: <ServerResourceManagement />,
        key: "2"
    }
]

export default class ServerResourceManagementHome extends React.Component {
    render() {
        return (
            <>
                <BreadcrumbCustom breadcrumProps={breadcrumbProps} />
                <Tabs>
                    {
                        initPanes.map(pane => {
                            return (
                                <Tabs.TabPane
                                    tab={pane.title}
                                    key={pane.key}
                                    closable
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