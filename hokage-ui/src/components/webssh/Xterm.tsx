import React from 'react'
import 'xterm/css/xterm.css'
import { Terminal } from 'xterm'
import { FitAddon } from 'xterm-addon-fit'

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
  
  processKeyEvent = (keyEvent: {key: string; domEvent: KeyboardEvent;}, terminal: Terminal) => {
    
    const { domEvent, key } = keyEvent
    console.log(domEvent)
    switch (domEvent.code) {
      case 'Enter': {
        console.log('command: ' + this.currentLine)
        terminal.writeln("")
        terminal.write(this.prompt, () => {
          this.currentLine = ""
          this.currentPos = -1
        })
        break
      }
      case 'ArrowUp':
      case 'ArrowDown':
        // TODO: 上下选择执行过的语句
        break;
      case 'ArrowLeft':
        if (this.currentPos > 0) {
          terminal.write(key)
          this.currentPos--
        }
        break
      case 'ArrowRight':
          if (this.currentPos < this.currentLine.length) {
            terminal.write(key)
            this.currentPos++
          }
          break
      case 'Delete':
      case 'Tab':
        // TODO: 自动补全
      case 'Home':
      case 'End':
      case 'Insert':
        break
      case 'Backspace':
        this.currentLine = this.currentLine.slice(0, -1)
        if (this.currentPos >= this.currentLine.length) {
          this.currentPos--
        }
        terminal.write('\x1b[2K\r')   
        terminal.write(this.prompt + this.currentLine)
        break
      default:
        this.currentPos++;
        this.currentLine = [this.currentLine.slice(0, this.currentPos), key, this.currentLine.slice(this.currentPos)].join('')
        // 清理最后一行
        terminal.write('\x1b[2K\r')   
        terminal.write(this.prompt + this.currentLine)
        // TODO: 指定光标
        //  ansi-escapes https://www.npmjs.com/package/ansi-escapes
        break
    }
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
    
    terminal.onKey((keyEvent: {key: string; domEvent: KeyboardEvent;}) => {
      this.processKeyEvent(keyEvent, terminal)
    })
  }
  
  render () {
    return <div id={this.props.id} />  
  }
  
}