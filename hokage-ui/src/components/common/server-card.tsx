import React from 'react'
import { Card, Row, Col, Avatar, Divider, Button } from 'antd'

export interface ActionPanesType {
	title: string,
	content: JSX.Element,
	key: string,
	closable?: boolean
}

interface ServerCardPropsType {
	account: string,
	serverIp: string, // 服务器IP或者服务器域名
	description: string, //服务器描述
	actionName: string, // 动作名称
	action: (id: string) => void, // 触发的操作
}

export default class ServerCard extends React.Component<ServerCardPropsType> {
	render() {
		const { account, serverIp, description, action, actionName } = this.props
		return (
			<div>
				<Card>
					<Row gutter={24}>
						<Col span={16}>
							<div>
								<Card.Meta
									avatar={
										<Avatar
											style={{
												backgroundColor: "#F56A00",
												verticalAlign: "middle"
											}}
											size="large"
										>
											{account}
										</Avatar>
									}
									title={serverIp}
									description={description}
								/>
							</div>
						</Col>
						<Col
							span={8}
							style={{
								display: "flex",
								alignItems: "center",
								justifyContent: "center"
							}}
						>
							<Button
								type={'link'}
								onClick={ () => { action(`${serverIp} (${account})`) } }
							>
								{actionName}
							</Button>
						</Col>
					</Row>
				</Card>
				<Divider style={{margin: '4px 0px'}} />
			</div>
		)
	}
}