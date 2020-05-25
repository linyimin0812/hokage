import React from 'react'
import 'xterm/css/xterm.css'
import { Terminal } from 'xterm'
import { FitAddon } from 'xterm-addon-fit'
import { w3cwebsocket as W3cWebsocket, IMessageEvent, ICloseEvent } from 'websocket'

interface XtermPropsType {
  id: string
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
    const endpoint = protocol + window.location.host + '/ssh'
    const client: W3cWebsocket = new W3cWebsocket(endpoint)
    
    client.onopen = () => {
      client.send(JSON.stringify({
        type: 'xtermSshInit',
        data: {
          host: "47.111.74.22",
          port: 22,
          username: 'root',
          passwd: '******'
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
    return <div id={this.props.id} style={{height: "75vh"}} />  
  }
  
}