import React from 'react'
import { Button, Form, Input, Switch } from 'antd'
import { Service } from '../axios/service';

const formItemLayout = {
	labelCol: {
		xs: {
			span: 24,
		},
		sm: {
			span: 6,
		},
	},
	wrapperCol: {
		xs: {
			span: 24,
		},
		sm: {
			span: 18,
		},
	},
}

const tailFormItemLayout = {
	wrapperCol: {
		xs: {
			span: 24,
			offset: 0
		},
		sm: {
			span: 16,
			offset: 8
		}
	}
}

interface LoginStateType {
	isLogin: boolean,
	formData: {[key: string]: string}
}

export default class Login extends React.Component<any, LoginStateType>{

	state = {
		isLogin: true, // 默认是注册
		formData: {}
	}

	register = () => {
		this.setState({ isLogin: false })
	}

	login = () => {
		this.setState({isLogin: true})
	}

	onValuesChange = (changedValues: any, allValues: any) => {
		console.log(changedValues)
		console.log(allValues)
		this.setState({ formData: allValues })
	}

	save = () => {
		const { isLogin } = this.state
		if (isLogin) {
			Service.register()
		}
		// Service.
	}

	render() {

		const { isLogin } = this.state

		return (
			<div className="login">
				<div className="login-form">
					<div className="login-logo">
						<span>Hokage UI</span>
					</div>
					<Form
						{ ...formItemLayout }
						onFinish={this.save}
						onValuesChange={this.onValuesChange}
					>
						<Form.Item
							name="username"
							label={
								<span>
									用户名
								</span>
							}
							rules={[
								{
									required: true,
									message: '请输入用户名',
								},
							]}
						>
							<Input placeholder="请输入用户名" />
						</Form.Item>

						<Form.Item
							name="password"
							label="密码"
							rules={[
								{
									required: true,
									message: '请输入你好',
								},
							]}
							hasFeedback
						>
							<Input.Password placeholder="请输入密码" />
						</Form.Item>

						{ isLogin
							?
							null
							:
							<>
								<Form.Item
									name="confirm"
									label="确认密码"
									dependencies={['password']}
									hasFeedback
									rules={[
										{
											required: true,
											message: '请输入确认密码',
										},
										({ getFieldValue }) => ({
											validator(rule, value) {
												if (!value || getFieldValue('password') === value) {
													return Promise.resolve();
												}

												return Promise.reject('密码不一致');
											},
										}),
									]}
								>
									<Input.Password placeholder="请输入确认密码" />
								</Form.Item>
								<Form.Item name="email" label="邮箱">
									<Input placeholder={"请输入邮箱"} />
								</Form.Item>
								<Form.Item name="isSubScribed" label="是否订阅">
									<Switch />
									<span>&nbsp;<span style={{color: "red"}}>*</span>&nbsp;开启订阅,服务器IP变化时会发送邮件通知</span>
								</Form.Item>
							</>
						}

						<Form.Item {...tailFormItemLayout}>
							{ isLogin
								?
								[
									<Button type="primary" htmlType="submit">
										登录
									</Button>,
									<span style={{color:"#5072D1", cursor: "pointer"}} onClick={this.register}>&nbsp;还没有账号?</span>
								]
								:
								[
									<Button type="primary" htmlType="submit">
										注册
									</Button>,
									<span style={{color:"#5072D1", cursor: "pointer"}} onClick={this.login}>&nbsp;已有账号?</span>
								]
							}

						</Form.Item>
					</Form>
				</div>
			</div>
		)
	}

}