import React from 'react'
import { Menu } from 'antd'
import { Record } from './table'
export interface ActionProps {
  left: string | undefined,
  top: string | undefined,
  record: Record | undefined
}

export default class Action extends React.Component<ActionProps> {
  render() {
    const { left, top } = this.props
    return (
      <Menu
        mode="vertical"
        className="file-action-menu"
        style={{
          position: 'fixed',
          width: '100px',
          height: '300px',
          top: top,
          left: left,
          backgroundColor: '#dcdcdc',
          border: '1px solid #778899',
          borderRadius: 8,
          overflow: 'auto',
          zIndex: 1,
        }}
      >
        <Menu.Item className="file-action-menu-item" key="open">打开</Menu.Item>
        <Menu.Item className="file-action-menu-item" key="copy">复制</Menu.Item>
        <Menu.Item className="file-action-menu-item" key="cut">剪切</Menu.Item>
        <Menu.Item className="file-action-menu-item" key="paste">粘贴</Menu.Item>
        <Menu.Item className="file-action-menu-item" key="rename">重命名</Menu.Item>
        <Menu.Item className="file-action-menu-item" key="share">分享</Menu.Item>
        <Menu.Item className="file-action-menu-item" key="permission">权限</Menu.Item>
        <Menu.Item className="file-action-menu-item" key="compress">压缩</Menu.Item>
        <Menu.Item className="file-action-menu-item" key="download">下载</Menu.Item>
        <Menu.Item className="file-action-menu-item" key="delete">删除</Menu.Item>
      </Menu>
    )
  }
}
