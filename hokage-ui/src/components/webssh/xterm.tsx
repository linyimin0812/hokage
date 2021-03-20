import React from 'react'
import 'xterm/css/xterm.css'
import { Terminal } from 'xterm'
import { FitAddon } from 'xterm-addon-fit'
import { w3cwebsocket as W3cWebsocket, IMessageEvent, ICloseEvent } from 'websocket'
import { ServerVO } from '../../axios/action/server/server-type'

interface XtermPropsType {
    id: string,
    server: ServerVO
}

interface XtermStateType {

}

export default class Xterm extends React.Component<XtermPropsType, XtermStateType> {

    componentDidMount = ()=> {
        const { id } = this.props
        const terminal = new Terminal({cursorBlink: true})
        const fitAddon = new FitAddon();
        terminal.loadAddon(fitAddon)
        terminal.open(document.getElementById(id)!)
        fitAddon.fit()
        terminal.focus()
        terminal.writeln('connecting ...')
        const client = this.initClient(terminal)
        terminal.onData((text: string, _: void) => {
            client.send(JSON.stringify({
                type: 'xtermSshData',
                data: text
            }))
        })
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
                }
            }))
        }

        client.onmessage = (message: IMessageEvent) => {
            terminal.write(message.data.toString())
        }

        client.onerror = (error: Error) => {
            terminal.writeln('\rError: ' + JSON.stringify(error))
        }

        client.onclose = (_: ICloseEvent) => {
            terminal.writeln('\rconnection closed.')
        }

        return client
    }

    render () {

        const layoutHeight = document.getElementsByClassName('ant-layout-content')[0].clientHeight
        const breadcrumbHeight = document.getElementsByClassName('ant-breadcrumb')[0].clientHeight
        const tabsNavHeight = document.getElementsByClassName('ant-tabs-nav')[0].clientHeight
        const footerHeight = document.getElementsByClassName('ant-layout-footer')[0].clientHeight || 0

        let height = '75vh';
        if (layoutHeight && breadcrumbHeight && tabsNavHeight) {
            height = layoutHeight - breadcrumbHeight - tabsNavHeight - 2 * footerHeight + 'px'
        }

        return <div id={this.props.id} style={{height: height, padding: '0 0'}} />
    }

}