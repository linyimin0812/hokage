import React from 'react'
import ServerCardPanel from '../common/server-card-panel'
import BreadCrumb, { BreadcrumbProps } from '../../layout/bread-crumb'
import { Tabs } from 'antd'
import BasicInfo from './basic-info'
import SystemStatus from './system-status'
import Network from './network'
import { ServerVO } from '../../axios/action/server/server-type'
import store, { MonitorPanesType } from './store'
import { v4 as uuid } from 'uuid'
import { observer } from 'mobx-react'
import { RouteComponentProps, withRouter } from 'react-router-dom';

const breadcrumbProps: BreadcrumbProps[] = [
  { name: '首页', link: '/app/index' },
  { name: '资源监控' }
]

@observer
class ServerResourceManagementHome extends React.Component<RouteComponentProps> {

  constructor(props: RouteComponentProps) {
    super(props)
    if (!store.panes || store.panes.length === 0) {
      store.panes = [{
        title: "我的服务器",
        content: <ServerCardPanel actionName={'资源管理'} action={this.addPane} />,
        key: '1',
        closable: false
      }]
      store.activeKey = '1'
    }
  }

  componentDidMount() {
    const {state} = this.props.location
    if (!state) {
      return
    }
    // @ts-ignore
    const serverVO: ServerVO = JSON.parse(state.serverVO)
    this.addPane(serverVO)
  }

  renderMonitorPage = (serverVO: ServerVO) => {
    return (
      <Tabs defaultActiveKey="2" tabPosition="left" >
        <Tabs.TabPane tab={ <span>基本信息</span> } key="1">
          <BasicInfo serverVO={serverVO!} />
        </Tabs.TabPane>
        <Tabs.TabPane tab={ <span>系统状态</span> } key="2">
          <SystemStatus serverVO={serverVO!} />
        </Tabs.TabPane>
        <Tabs.TabPane tab={ <span>网络信息</span> } key="3">
          <Network serverVO={serverVO!} />
        </Tabs.TabPane>
      </Tabs>
    )
  }

  addPane = (serverVO: ServerVO) => {
    const id = uuid()
    if (!store.panes.some(pane => pane.key === id)) {
      const pane: MonitorPanesType = {
        key: id,
        content: this.renderMonitorPage(serverVO),
        title: `${serverVO.account}@${serverVO.ip}`,
        serverVO: serverVO
      }
      store.panes.push(pane)
    }
    store.activeKey = id
  }

  onChange = (activeKey: string) => {
    store.activeKey = activeKey
  }

  onEdit = (targetKey: any, action: 'add' | 'remove'): void => {
    if (action === 'remove') {
      let lastKeyIndex: number = 0
      store.panes.forEach((pane, i) => {
        if (pane.key === targetKey) {
          lastKeyIndex = i - 1
        }
      })
      const newPanes: MonitorPanesType[] = store.panes.filter(pane => pane.key !== targetKey)
      if (targetKey === store.activeKey && newPanes.length) {
        lastKeyIndex = lastKeyIndex >=0 ? lastKeyIndex : 0
        store.activeKey = newPanes[lastKeyIndex].key
      }
      store.panes = newPanes
    }
  }

  renderActionPanes = () => {
    return store.panes.map(pane => {
      return (
        <Tabs.TabPane tab={pane.title} key={pane.key} closable={pane.closable}>
          {pane.content}
        </Tabs.TabPane>
      )
    })
  }

  render() {
    return (
      <>
        <BreadCrumb breadcrumbProps={breadcrumbProps} />
        <Tabs onChange={this.onChange} activeKey={store.activeKey} type="editable-card" hideAdd onEdit={this.onEdit}>
          { this.renderActionPanes() }
        </Tabs>
      </>
    )
  }
}

export default withRouter(ServerResourceManagementHome)
