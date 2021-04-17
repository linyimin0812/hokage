import React from 'react'
import { Button, Form, Input, message, Switch } from 'antd'
import { UserAction } from '../../axios/action'
import { UserLoginForm, UserRegisterForm } from '../../axios/action/user/user-type'
import { setHokageUserInfo } from '../../libs'

const formItemLayout = {
	labelCol: { xs: { span: 24 }, sm: { span: 6 } },
	wrapperCol: { xs: { span: 24 }, sm: { span: 18, } },
}

const tailFormItemLayout = {
	wrapperCol: { xs: { span: 24, offset: 0 }, sm: { span: 16, offset: 8 } }
}

interface LoginStateType {
	isLogin: boolean,
	formData: UserLoginForm | UserRegisterForm,
	subscribed: number	// 不知道为什么不能通过表单获取switch的值, 所以一个字段单独标识
}

export default class Login extends React.Component<any, LoginStateType>{

	state = {
		isLogin: true, // 默认是登录
		formData: {} as UserLoginForm,
		subscribed: 0
	}

	register = () => {
		this.setState({ isLogin: false })
	}

	login = () => {
		this.setState({isLogin: true})
	}

	save = () => {
		const { isLogin, formData, subscribed } = this.state
		if (isLogin) {
			this.doLogin(formData)
		} else {
			this.doRegister(formData as UserRegisterForm, subscribed)
		}
	}

	doLogin = (formData: UserLoginForm) => {
		// 用户登录
		UserAction.login(formData).then(value => {
			if (value.success) {
				// 存储用户信息
				setHokageUserInfo(JSON.stringify(value.data))
				// 跳转到首页
				window.location.href = '/app/index'
				return
			}
		}).catch(err => {
			message.error("登录失败，请稍后重试", 5)
			console.log("Service.login catch: ", JSON.stringify(err))
		})
	}

	doRegister = (formData: UserRegisterForm, subscribed: number) => {
		formData.subscribed = subscribed
		// 用户注册
		console.log(formData)
		UserAction.register(formData).then(value => {
			if (value.success) {
				// 存储用户信息
				setHokageUserInfo(JSON.stringify(value.data))
				// 跳转到首页
				window.location.href = '/app/index'
				return
			}
			message.error(value.msg, 5)
			console.log("Service.register: " + JSON.stringify(value))
		}).catch(err => {
			message.error("注册失败，请稍后重试", 5)
			console.log("Service.register catch: ", JSON.stringify(err))
		})
	}

	render() {
		const { isLogin, subscribed } = this.state
		return (
			<div className="login">
				<div className="login-form">
					<div className="login-logo"><span>Hokage UI</span></div>
					<Form
						{ ...formItemLayout }
						onFinish={this.save}
						onValuesChange={(_: any, allValues: any) => this.setState({ formData: allValues })}
					>
						{ isLogin ? null :
							<Form.Item name="username" label="昵称" rules={[{ required: true, message: '请输入昵称', }]}>
								<Input placeholder="昵称不能为空" />
							</Form.Item>
						}

						<Form.Item name="email" label="邮箱" rules={[{required: true, message: '邮箱不能为空'}]}>
							<Input placeholder={"请输入邮箱"} />
						</Form.Item>
						{ isLogin ? null : <Form.Item name="subscribed" label="是否订阅">
							<Switch onChange={ value => this.setState({ subscribed: value ? 1 : 0 }) } checked={subscribed === 1} />
								<span>&nbsp;<span style={{color: "red"}}>*</span>&nbsp;开启订阅,服务器IP变化时会发送邮件通知</span>
							</Form.Item>
						}
						<Form.Item name="passwd" label="密码" rules={[{required: true,message: '请输入密码',}]} hasFeedback>
							<Input.Password placeholder="请输入密码" />
						</Form.Item>
						{ isLogin ? null :
							<Form.Item name="confirm" label="确认密码" dependencies={['passwd']} hasFeedback
								rules={[{required: true, message: '请输入确认密码'},
									({ getFieldValue }) => ({
										validator(rule, value) {
											if (!value || getFieldValue('passwd') === value) {
												return Promise.resolve();
											}
											return Promise.reject('密码不一致');
										},
									}),
								]}
							>
							<Input.Password placeholder="请输入确认密码" />
						</Form.Item>}
						<Form.Item {...tailFormItemLayout}>
							{ isLogin ? <>
								<Button type="primary" htmlType="submit">登录</Button>
								<span style={{color:"#5072D1", cursor: "pointer"}} onClick={this.register}>&nbsp;还没有账号?</span>
							</> : <>
								<Button type="primary" htmlType="submit">
									注册
								</Button>
								<span style={{color:"#5072D1", cursor: "pointer"}} onClick={this.login}>&nbsp;已有账号?</span>
							</>
							}
						</Form.Item>
					</Form>
				</div>
			</div>
		)
	}
}
