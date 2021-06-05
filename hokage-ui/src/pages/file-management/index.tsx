import BreadCrumb, { BreadcrumbProps } from "../../layout/bread-crumb"
import React from 'react'
import FileTable from "./table"
import { Tabs } from "antd"
import ServerCardPanel from "../common/server-card-panel"
import store, { PanesType } from './store'
import { observer } from 'mobx-react'
import { ServerVO } from '../../axios/action/server/server-type'
import { v4 as uuid } from 'uuid'

const breadcrumbProps: BreadcrumbProps[] = [
  { name: '首页', link: '/app/index' },
  { name: '文件管理' }
]

interface HomePropsType {
  initActionPanes: PanesType[],
  initActiveKey: string,
  addActionPanel: (id: string) => void
}
@observer
export default class FileManagementHome extends React.Component<HomePropsType> {
  constructor(props: HomePropsType) {
    super(props)
    store.panes = [{
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

  addPane = (serverVO: ServerVO) => {
    const id = uuid()
    if (!store.panes.some(pane => pane.key === id)) {
      const pane: PanesType = {
        key: id,
        title: `${serverVO.account}@${serverVO.ip}`,
        content: <FileTable serverVO={serverVO} id={id} />,
        fileVO: { curDir: '~', filePropertyList: [], directoryNum: 0, fileNum: 0, totalSize: '' }
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
      case 'add':
        break
    }

  }

  renderActionPanel = () => {
    return store.panes.map(pane => {
      return <Tabs.TabPane tab={pane.title} key={pane.key} closable={pane.closable}>{pane.content}</Tabs.TabPane>
    })
  }

  render() {
    return (
      <>
        <BreadCrumb breadcrumbProps={breadcrumbProps} />
        <Tabs onChange={this.onChange} activeKey={store.activeKey} type="editable-card" hideAdd onEdit={this.onEdit}>
          { this.renderActionPanel() }
        </Tabs>
      </>
    )
  }
}
