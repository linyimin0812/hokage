import React from 'react'
import { Tabs } from 'antd'
import SystemStatus from './systemstatus/system-status'
import BasicInfoHome from './basicinfo/basic-info-home'
import NetworkHome from './network/network-home'

export default class ServerResourceManagement extends React.Component<any, any>{

    render() {
        return (
            <div>
                <Tabs defaultActiveKey="2" tabPosition="left" >
                    <Tabs.TabPane
                        tab={ <span>基本信息</span> }
                        key="1"
                    >
                        <BasicInfoHome />
                    </Tabs.TabPane>
                    <Tabs.TabPane
                        tab={ <span>系统状态</span> }
                        key="2"
                    >
                        <SystemStatus />
                    </Tabs.TabPane>
                    <Tabs.TabPane
                        tab={ <span>网络信息</span> }
                        key="3"
                    >
                        <NetworkHome />
                    </Tabs.TabPane>
                </Tabs>
            </div>
        )
    }

}