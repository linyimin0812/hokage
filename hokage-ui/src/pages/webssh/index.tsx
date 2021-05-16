import BreadCrumb, { BreadcrumbPrpos } from '../../layout/bread-crumb'
import React from 'react'
import { Tabs } from "antd"
import { observer } from 'mobx-react'
import store from './store'
import { ServerVO } from '../../axios/action/server/server-type'
import Xterm from './xterm'
import WebSshServer from './table'
import { Terminal } from 'xterm';

const breadcrumbProps: BreadcrumbPrpos[] = [
  { name: '首页', link: '/app/index' },
  { name: 'Web终端' }
]

export interface PanesType {
  title: string,
  content: JSX.Element,
  key: string,
  terminal?: Terminal,
  closable?: boolean
}
@observer
export default class WebSshHome extends React.Component {

  constructor(props: any) {
    super(props)
    if (!store.panes || store.panes.length === 0) {
      store.panes = [{
        key: '1',
        content: <WebSshServer addSshTerm={this.addPane} />,
        title: '我的服务器',
        closable: false
      }]
    }
  }

  onChange = (activeKey: string) => {
    store.activeKey = activeKey
  }

  /**
   * 需要传入一个服务器的唯一标识,用于连接服务器获取文件信息
   */
  addPane = (record: ServerVO) => {
    const id = `${record.ip} (${record.account})`

    if (!store.panes.some(pane => pane.key === id)) {
      const pane: PanesType = {
        key: id,
        content: <Xterm id={id} server={record} />,
        title: id
      }
      store.panes.push(pane)
    }
    store.activeKey = id
  }

  onEdit = (targetKey: any, action: 'add' | 'remove'): void => {
    switch(action) {
      case 'remove':
        let lastKeyIndex: number = 0
        store.panes.forEach((pane, i) => {
          if (pane.key === targetKey) {
            lastKeyIndex = i -1
          }
        })
        const newPanes: PanesType[] = store.panes.filter(pane => pane.key !== targetKey)
        if (targetKey === store.activeKey && newPanes.length) {
          lastKeyIndex = lastKeyIndex >=0 ? lastKeyIndex : 0
          store.activeKey = newPanes[lastKeyIndex].key
        }
        store.panes = newPanes
        break
      default:
        break
    }
  }

  render() {
    return (
      <>
        <BreadCrumb breadcrumbProps={breadcrumbProps} />
        <Tabs onChange={this.onChange} activeKey={store.activeKey} type="editable-card" hideAdd onEdit={this.onEdit}>
          {store.panes.map(pane => {
              return <Tabs.TabPane tab={pane.title} key={pane.key} closable={pane.closable}>
                  {pane.content}
                </Tabs.TabPane>
            })}
        </Tabs>
      </>
    )
  }
}
