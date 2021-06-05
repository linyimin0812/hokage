import BreadCrumb, { BreadcrumbProps } from '../../layout/bread-crumb'
import React from 'react'
import { Modal, Tabs } from 'antd';
import { observer } from 'mobx-react'
import store, { PanesType } from './store'
import { ServerVO } from '../../axios/action/server/server-type'
import Xterm from './xterm'
import WebSshServer from './table'
import { LoadingOutlined } from '@ant-design/icons'
import { v4 as uuid } from 'uuid'
import './index.less'
import { toJS } from 'mobx'

const breadcrumbProps: BreadcrumbProps[] = [
  { name: '首页', link: '/app/index' },
  { name: 'Web终端' }
]

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

  componentWillUnmount() {
    window.removeEventListener('beforeunload', this.onbeforeunload)
  }


  onbeforeunload = (ev: BeforeUnloadEvent) => {
    ev.returnValue = true
  }

  onChange = (activeKey: string) => {
    store.activeKey = activeKey
  }

  /**
   * 需要传入一个服务器的唯一标识,用于连接服务器获取文件信息
   */
  addPane = (record: ServerVO) => {
    const titleContent = `${record.ip} (${record.account})`
    const id = uuid()

    if (!store.panes.some(pane => pane.key === id)) {
      const pane: PanesType = {
        key: id,
        content: <Xterm id={id} server={record} titleContent={titleContent} />,
        server: `${record.account}@${record.ip}`,
        title: <span><LoadingOutlined translate />{titleContent}</span>,
      }
      store.panes.push(pane)
    }
    store.activeKey = id
    window.removeEventListener('beforeunload', this.onbeforeunload)
    window.addEventListener('beforeunload', this.onbeforeunload)
  }

  onEdit = (targetKey: any, action: 'add' | 'remove'): void => {
    switch(action) {
      case 'remove':
        const pane = toJS(store.panes).find(pane => pane.key === targetKey);
        if (!pane) {
          break
        }
        Modal.confirm({
          title: '关闭ssh连接确认',
          content: `确认关闭ssh连接: ${pane.server} ?`,
          onOk: () => this.closeTerminal(targetKey, pane)
        })
        break
      default:
        break
    }
  }
  closeTerminal = (key: any, pane: PanesType) => {
    let lastKeyIndex: number = 0
    store.panes.forEach((pane, i) => {
      if (pane.key === key) {
        lastKeyIndex = i -1
      }
    })
    const newPanes: PanesType[] = store.panes.filter(pane => pane.key !== key)
    if (key === store.activeKey && newPanes.length) {
      lastKeyIndex = lastKeyIndex >=0 ? lastKeyIndex : 0
      store.activeKey = newPanes[lastKeyIndex].key
    }
    store.panes = newPanes
    if (newPanes.length === 0) {
      window.removeEventListener('beforeunload', this.onbeforeunload)
    }
    pane.websocket?.close()
  }

  render() {
    return (
      <div className={'ant-layout-content'} style={{ height: '100%' }}>
        <BreadCrumb breadcrumbProps={breadcrumbProps} />
        <div className={'antd-tabs-content'} >
          <Tabs onChange={this.onChange} activeKey={store.activeKey} type="editable-card" hideAdd onEdit={this.onEdit} style={{ height: '100%' }}>
            {store.panes.map(pane => {
              return <Tabs.TabPane tab={pane.title} key={pane.key} closable={pane.closable}>
                {pane.content}
              </Tabs.TabPane>
            })}
          </Tabs>
        </div>
      </div>
    )
  }
}
