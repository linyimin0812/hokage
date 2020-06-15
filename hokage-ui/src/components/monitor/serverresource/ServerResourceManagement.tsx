import React from 'react'
import { Tabs } from 'antd';
import SystemStatus from './systemstatus/SystemStatus';

export default class ServerResourceManagement extends React.Component<any, any>{

    render() {
        return (
            <div>
                <Tabs defaultActiveKey="2" tabPosition="left" >
                    <Tabs.TabPane
                        tab={ <span>系统状态</span> }
                        key="1"
                    >
                        <SystemStatus />
                    </Tabs.TabPane>
                    <Tabs.TabPane
                        tab={ <span>基本信息</span> }
                        key="2"
                    >
                        Tab 2
                    </Tabs.TabPane>
                    <Tabs.TabPane
                        tab={ <span>网络信息</span> }
                        key="3"
                    >
                        Tab 3
                    </Tabs.TabPane>
                    <Tabs.TabPane
                        tab={ <span>账号信息</span> }
                        key="4"
                    >
                        Tab 4
                    </Tabs.TabPane>
                </Tabs>
            </div>
        )
    }

}