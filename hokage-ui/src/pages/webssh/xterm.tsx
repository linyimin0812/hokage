import React from 'react'
import 'xterm/css/xterm.css'
import { Terminal } from 'xterm'
import { FitAddon } from 'xterm-addon-fit'
import { ICloseEvent, IMessageEvent, w3cwebsocket as W3cWebsocket } from 'websocket'
import { ServerVO } from '../../axios/action/server/server-type'
import { xtermSpinner } from '../../libs'
import { observable, toJS } from 'mobx'
import store from './store'
import { observer } from 'mobx-react'
import { CheckCircleTwoTone, CloseCircleTwoTone } from '@ant-design/icons'

interface XtermPropsType {
  id: string,
  server: ServerVO,
  titleContent: string
}

interface XtermStateType {
  spinner: NodeJS.Timeout
}
@observer
export default class Xterm extends React.Component<XtermPropsType, XtermStateType> {

  componentDidMount = ()=> {
    const { id } = this.props
    const terminal = new Terminal({cursorBlink: true})
    const fitAddon = new FitAddon();
    terminal.loadAddon(fitAddon)
    terminal.open(document.getElementById(id)!)
    fitAddon.fit()
    terminal.focus()
    this.setState({ spinner: xtermSpinner(terminal) })
    const client = this.initClient(terminal)
    terminal.onData((text: string, _: void) => {
      client.send(JSON.stringify({
        type: 'xtermSshData',
        data: text
      }))
    })
    terminal.onResize(({ cols, rows }) => {
      client.send(JSON.stringify({
        type: 'xtermSshResize',
        data: { cols: cols, rows: rows }
      }))
    })
    window.onresize = () => fitAddon.fit()
  }

  renderPaneTitle = (success: boolean) => {
    let panes = toJS(store.panes)
    for (const pane of panes) {
      if (pane.key !== this.props.id) {
        continue
      }
      if (success) {
        if (pane.status === 1) {
          return
        }
        pane.title = <span><CheckCircleTwoTone translate />{this.props.titleContent}</span>
        pane.status = 1
        break
      }

      if (pane.status !== 2) {
        return
      }
      pane.title = <span><CloseCircleTwoTone translate />{this.props.titleContent}</span>
      pane.status = 2
      break
    }
    store.panes = observable.array(panes)
    clearInterval(this.state.spinner)
  }

  initClient = (terminal: Terminal): W3cWebsocket => {
    let protocol = 'ws://'
    if (window.location.protocol === 'https') {
      protocol = 'wss://'
    }
    const endpoint = protocol + '127.0.0.1:8080/ws/ssh'
    const client: W3cWebsocket = new W3cWebsocket(endpoint)

    client.onopen = () => {
      const { server } = this.props
      client.send(JSON.stringify({
        type: 'xtermSshInit',
        data: {
          id: server.id,
          ip: server.ip,
          sshPort: server.sshPort,
          account: server.account,
          size: { cols: terminal.cols, rows: terminal.rows }
        }
      }))
    }

    client.onmessage = (message: IMessageEvent) => {
      this.renderPaneTitle(true)
      terminal.write(message.data.toString())
    }

    client.onerror = (error: Error) => {
      this.renderPaneTitle(false)
      terminal.writeln('\rError: ' + JSON.stringify(error))
    }

    client.onclose = (_: ICloseEvent) => {
      this.renderPaneTitle(false)
      terminal.writeln('\rwebsocket connection closed.')
    }

    return client
  }

  render () {
    return <div id={this.props.id} style={{ padding: '0 0', height: '100%' }} />
  }

}
