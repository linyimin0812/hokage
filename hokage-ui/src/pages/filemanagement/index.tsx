import BreadCrumb, { BreadcrumbPrpos } from "../../layout/bread-crumb"
import React from 'react'
import FileTable from "./table"
import { Tabs } from "antd"
import ServerCardPanel from "../common/server-card-panel"
import { ActionPanesType } from '../common/server-card'
import store from './store';
import { observer } from 'mobx-react'

const breadcrumbProps: BreadcrumbPrpos[] = [
  { name: '首页', link: '/app/index' },
  { name: '文件管理' }
]

interface HomePropsType {
  initActionPanes: ActionPanesType[],
  initActiveKey: string,
  addActionPanel: (id: string) => void
}
@observer
export default class FileManagementHome extends React.Component<HomePropsType> {
  constructor(props: HomePropsType) {
    super(props)
    store.actionPanes = [{
      key: '1',
      content: <ServerCardPanel actionName={'文件管理'} action={this.addPane} />,
      title: '我的服务器',
      closable: false
    }]
    store.activeKey = '1'
  }

  onChange = (activeKey: string) => {
    store.activeKey = activeKey
  }

  addPane = (id: string) => {
    if (!store.actionPanes.some(pane => pane.key === id)) {
      const pane: ActionPanesType = {
        key: id,
        content: <FileTable />,
        title: id
      }
      store.actionPanes.push(pane)
    }
    store.activeKey = id
  }

  onEdit = (targetKey: any, action: 'add' | 'remove'): void => {
    switch(action) {
      case 'remove':
        let lastKeyIndex: number = 0
        store.actionPanes.forEach((pane, i) => {
          if (pane.key === targetKey) {
            lastKeyIndex = i -1
          }
        })
        const newPanes: ActionPanesType[] = store.actionPanes.filter(pane => pane.key !== targetKey)
        if (targetKey === store.activeKey && newPanes.length) {
          lastKeyIndex = lastKeyIndex >=0 ? lastKeyIndex : 0
          store.activeKey = newPanes[lastKeyIndex].key
        }
        store.actionPanes = newPanes
        break
      case 'add':
        break
    }

  }

  renderActionPanel = () => {
    return store.actionPanes.map(pane => {
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
        <Tabs
          onChange={this.onChange}
          activeKey={store.activeKey}
          type="editable-card"
          hideAdd
          onEdit={this.onEdit}
        >
          { this.renderActionPanel() }
        </Tabs>
      </>
    )
  }
}
