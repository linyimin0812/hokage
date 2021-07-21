import BreadCrumb, { BreadcrumbProps } from "../../layout/bread-crumb"
import React from 'react'
import FileTable from "./table"
import { Tabs } from "antd"
import ServerCardPanel from "../common/server-card-panel"
import store, { PanesType } from './store'
import { observer } from 'mobx-react'
import { ServerVO } from '../../axios/action/server/server-type'
import { v4 as uuid } from 'uuid'
import { FileOperateForm } from '../../axios/action/file-management/file-management-type'
import { getHokageUid } from '../../libs'
import { RouteComponentProps, withRouter } from 'react-router-dom';

const breadcrumbProps: BreadcrumbProps[] = [
  { name: '首页', link: '/app/index' },
  { name: '文件管理' }
]

interface HomePropsType extends RouteComponentProps{
  initActionPanes: PanesType[],
  initActiveKey: string,
  addActionPanel: (id: string) => void
}
@observer
class FileManagementHome extends React.Component<HomePropsType> {
  constructor(props: HomePropsType) {
    super(props)
    if (!store.panes || store.panes.length === 0) {
      store.panes = [{
        key: 'my-server',
        content: <ServerCardPanel actionName={'文件管理'} action={this.addPane} />,
        title: '我的服务器',
        closable: false
      }]
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

  onChange = (activeKey: string) => {
    store.activeKey = activeKey
    const pane = store.panes.find(pane => pane.key === activeKey)
    if (pane && pane.serverVO) {
      const { ip, sshPort, account, id } = pane.serverVO
      const form: FileOperateForm = {
        operatorId: getHokageUid(),
        serverId: id,
        ip: ip,
        sshPort: sshPort,
        account: account,
        path: pane.fileVO?.curDir!,
      }
      store.listDir(activeKey, form)
    }
  }

  addPane = (serverVO: ServerVO) => {
    const id = uuid()
    if (!store.panes.some(pane => pane.key === id)) {
      const pane: PanesType = {
        key: id,
        title: `${serverVO.account}@${serverVO.ip}`,
        serverVO: serverVO,
        content: <FileTable serverVO={serverVO} id={id} />,
        fileVO: { curDir: '~', filePropertyList: [], directoryNum: 0, fileNum: 0, totalSize: '' }
      }
      store.panes.push(pane)
    }
    store.activeKey = id
  }

  onEdit = (targetKey: any, action: 'add' | 'remove'): void => {
    if (action === 'remove') {
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

export default withRouter(FileManagementHome)
