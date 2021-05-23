import React, { ChangeEvent, ReactElement } from 'react'
import { Select, Table, Input, Tooltip } from 'antd'
import { QuestionCircleOutlined } from '@ant-design/icons/lib'
const data: any[] = [
  {id: "1", servers: "banzhe_test", authStrategy: "禁止", protocolType: "TCP", portRange: "8080", authType: "IPv4地址段访问", authObject: "0.0.0.0/0", description: "System created rule.", createTime: "", action: "保存 | 编辑 | 删除 | 启用 | 暂停" },
  {id: "2", servers: "banzhe_test", authStrategy: "禁止", protocolType: "TCP", portRange: "8080", authType: "IPv4地址段访问", authObject: "0.0.0.0/0", description: "System created rule.", createTime: "", action: "保存 | 编辑 | 删除" },
  {id: "3", servers: "banzhe_test", authStrategy: "禁止", protocolType: "TCP", portRange: "8080", authType: "IPv4地址段访问", authObject: "0.0.0.0/0", description: "System created rule.", createTime: "", action: "保存 | 编辑 | 删除" },
  {id: "4", servers: "banzhe_test", authStrategy: "禁止", protocolType: "TCP", portRange: "8080", authType: "IPv4地址段访问", authObject: "0.0.0.0/0", description: "System created rule.", createTime: "", action: "保存 | 编辑 | 删除" },
  {id: "5", servers: "banzhe_test", authStrategy: "禁止", protocolType: "TCP", portRange: "8080", authType: "IPv4地址段访问", authObject: "0.0.0.0/0", description: "System created rule.", createTime: "", action: "保存 | 编辑 | 删除" },
]

interface EnterSecurityPropsType {
  isAddNew: boolean,
  cancelAddNew: () => void
}

interface IsEditType {
  [id: string]: boolean,
}
interface EnterSecurityStateType {
  isEdit: IsEditType,
  servers: string[],
  authStrategy: string,
  protocolType: string,
  portRange: string,
  authObject: string,
  description: string,

  data: any[],
  allowToAdd: boolean
}
export default class EnterSecurity extends React.Component<EnterSecurityPropsType, EnterSecurityStateType> {

  state = {
    isEdit: {} as IsEditType, // 保存各行是否需要编辑的状态
    servers: [], // 选择的服务器(组)
    protocolType: "", // 协议类型: tcp, udp, ICMP(ipv4), 全部
    authStrategy: "", // 拒绝, 允许
    portRange: "", // 服务端口范围
    authObject: "", // 授权对象
    description: "", // 描述
    data: data,
    allowToAdd: true // 一次只能允许添加一行记录
  }

  shouldComponentUpdate(nextProps: Readonly<EnterSecurityPropsType>, nextState: Readonly<EnterSecurityStateType>, nextContext: any): boolean {
    if (nextProps.isAddNew && nextState.allowToAdd) {
      const newData = [{id: new Date().toISOString(), action: "保存"}] as any
      const isEdit: IsEditType = {}
      isEdit[newData[0].id] = true
      this.setState({ data: newData.concat(this.state.data), isEdit, allowToAdd: false }, () => {
        nextProps.cancelAddNew();
      })
      return true
    }
    return !(this.state === nextState && this.props === nextProps)
  }

  portPrompt: string = '取值范围从1到65535；设置格式例如“1/200”、“80/80”,其中“-1/-1”不能单独设置，代表不限制端口。支持输入多个端口范围，以","隔开'
  authObjectPrompt: string = '0.0.0.0/0或者掩码为0，代表允许或拒绝所有IP的访问，设置时请务必谨慎。支持输入多种授权对象，以","隔开。'

  onServersChange = (value: string[]) => {
    console.log(value)
    this.setState({ servers: value })
  }

  onProtocolTypeChange = (value: string) => {
    value.includes("all") ?
      this.setState({ protocolType: value, authObject: "-1/-1" }) :
      this.setState({ protocolType: value })
  }

  onAuthStrategyChange = (value: string) => {
    this.setState({ authStrategy: value })
  }

  onPortRangeChange = (event: ChangeEvent<HTMLInputElement>) => {
    this.setState({ portRange: event.target.value })
  }

  onAuthObjectChange = (event: ChangeEvent<HTMLInputElement>) => {
    this.setState({ authObject: event.target.value })
  }

  onDescriptionChange = (event: ChangeEvent<HTMLInputElement>) => {
    this.setState({ description: event.target.value })
  }

  saveSecurityConfig = () => {
    // TODO: 往后台发送表单数据
    // 允许下一次添加
    this.setState({allowToAdd: true})
  }

  onServerRender = (value: any, record: any, index: number): ReactElement => {
    const id = record.id as string
    return (
      this.state.isEdit[id] ? <div>
        <Select mode="multiple" style={{width: "150px"}} onChange={this.onServersChange} placeholder="请选择服务器(组)" value={this.state.servers} >
          <Select.Option value="10.108.210.194">10.108.210.194</Select.Option>
          <Select.Option value="daily">日常环境组</Select.Option>
        </Select>
      </div> : <span>{value}</span>
    )
  }

  onAuthStrategyRender = (value: any, record: any, index: number): ReactElement => {
    const id = record.id as string
    return (
      this.state.isEdit[id] ? <div>
        <Select style={{width: "150px"}} placeholder="协议类型" onChange={this.onAuthStrategyChange} value={this.state.authStrategy || undefined} >
          <Select.Option value="reject">拒绝</Select.Option>
          <Select.Option value="allow">允许</Select.Option>
        </Select>
      </div> : <span>{value}</span>
    )
  }

  onPortRangeRender = (value: any, record: any, index: number): ReactElement => {
    const id = record.id as string
    return (
      this.state.isEdit[id] ? <div>
        <Input onChange={this.onPortRangeChange} value={this.state.portRange || undefined} placeholder="端口范围" />
      </div> : <span>{value}</span>
    )
  }

  onPortRangeRenderTitle = () => {
    return (
      <span>
        端口范围&nbsp;
        <Tooltip placement="bottom" title={this.portPrompt}>
          <QuestionCircleOutlined translate="true" />
        </Tooltip>
      </span>
    )
  }

  onAuthObjectRender = (value: any, record: any, index: number): ReactElement => {
    const id = record.id as string
    return (
      this.state.isEdit[id] ? <div>
        <Input onChange={this.onAuthObjectChange} value={this.state.authObject || undefined} placeholder="授权对象" />
      </div> : <span>{value}</span>
    )
  }

  onAuthObjectRenderTitle = () => {
    return (
      <span>
        授权对象&nbsp;
        <Tooltip placement="bottom" title={this.authObjectPrompt}>
          <QuestionCircleOutlined translate="true" />
        </Tooltip>
      </span>
    )
  }

  onDescriptionRender = (value: any, record: any, index: number): ReactElement => {
    const id = record.id as string
    return (
      this.state.isEdit[id] ? <div>
        <Input style={{width: "150px"}} placeholder="描述" onChange={this.onDescriptionChange} value={this.state.description || undefined} />
      </div> : <span>{value}</span>
    )
  }

  onRenderAction = (value: any, record: any, index: number): ReactElement => {
    // TODO: 对于编辑的记录,需要先将记录的值赋值给对应的状态
    const isEdit = {} as any
    isEdit[record.id] = true
    return (
      <div>
        <span onClick={() => { this.setState({ isEdit: isEdit }) }} style={{color:"#5072D1", cursor: "pointer"}}>
          {value}
        </span>
      </div>
    )
  }

  onProtocolTypeRender = (value: any, record: any, index: number): ReactElement => {
    // TODO: 加一个header render,用于显示端口范围和授权对象的填写说明
    const id = record.id as string
    return (
      this.state.isEdit[id] ? <div>
        <Select style={{width: "150px"}} placeholder="协议类型" onChange={this.onProtocolTypeChange} value={this.state.protocolType || undefined} >
          <Select.Option value="tcp">自定义TCP</Select.Option>
          <Select.Option value="udp">自定义UDP</Select.Option>
          <Select.Option value="allICMP">全部ICMP</Select.Option>
          <Select.Option value="all">全部</Select.Option>
        </Select>
      </div> : <span>{value}</span>
    )
  }

  render() {
    // 一个可编辑的表格
    return (
      <div>
        <Table dataSource={this.state.data}>
          <Table.Column render={this.onServerRender} title="服务器(组)" dataIndex="servers" />
          <Table.Column render={this.onAuthStrategyRender} title="授权策略" dataIndex="authStrategy" />
          <Table.Column render={this.onProtocolTypeRender} title="协议类型" dataIndex="protocolType" />
          <Table.Column render={this.onPortRangeRender} title={this.onPortRangeRenderTitle()} dataIndex="portRange" />
          <Table.Column render={this.onAuthObjectRender} title={this.onAuthObjectRenderTitle()} dataIndex="authObject" />
          <Table.Column render={this.onDescriptionRender} title="描述" dataIndex="description" />
          <Table.Column title="创建时间" dataIndex="createTime" />
          <Table.Column render={this.onRenderAction} title="操作" dataIndex="action" />
        </Table>
      </div>
    )
  }
}
