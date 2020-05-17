import BreadcrumbCustom, { BreadcrumbPrpos } from "../BreadcrumbCustom";
import React from 'react'
import FileManagement from "./FileManagement";
import { Tabs } from "antd";
import MyServer from "../server/MyServer";
interface PanesType {
  title: string,
  content: JSX.Element,
  key: string,
  closable?: boolean
}

const panes = [
  {
    key: '1',
    content: <MyServer />,
    title: '我的服务器',
    closable: false
  },
  {
    key: '2',
    content: <FileManagement />,
    title: '文件管理'
  }
]

const breadcrumbProps: BreadcrumbPrpos[] = [
  {
    name: '首页',
    link: '/app/index'
  },
  {
    name: '文件管理'
  }
]

interface FileManagementState {
  panes: PanesType[],
  activeKey: string
}

export default class FileManagementHome extends React.Component<any, FileManagementState> {
  constructor(props: any) {
    super(props)
    this.state = {
      panes: panes,
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
   * TODO: 作为属性传给FileManagement组件进行触发
   */
  addPane = () => {
    let key = new Date().toLocaleTimeString()
    const { panes } = this.state
    const pane: PanesType = {
      key: key,
      content: <FileManagement />,
      title: key
    }
    panes.push(pane)
    
    this.setState({
      panes,
      activeKey: key
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