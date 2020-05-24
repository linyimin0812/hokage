import React from 'react'
import 'xterm/css/xterm.css'
import { Terminal } from 'xterm'
import { FitAddon } from 'xterm-addon-fit'
import { string } from 'prop-types';

interface XtermPropsType {
  id: string
}

interface XtermStateType {
  
}

export default class Xterm extends React.Component<XtermPropsType, XtermStateType> {
  private currentLine: string
  private currentPos: number
  private history: string[]
  private prompt: string
  
  constructor (props: XtermPropsType) {
    super(props)
    this.currentLine = ""
    this.currentPos = -1
    this.history = []
    this.prompt = "Hello from \x1B[1;3;31mxterm.js\x1B[0m $ "
  }
  
  componentDidMount = ()=> {
    const { id } = this.props
    const terminal = new Terminal({cursorBlink: true})
    const fitAddon = new FitAddon();
    terminal.loadAddon(fitAddon)
    terminal.open(document.getElementById(id)!)
    fitAddon.fit()
    // TODO: 获取服务器prompt
    terminal.write(this.prompt)
    terminal.onData((text: string, _void) => {
      terminal.write(text)
    })
  }
  
  render () {
    return <div id={this.props.id} />  
  }
  
}