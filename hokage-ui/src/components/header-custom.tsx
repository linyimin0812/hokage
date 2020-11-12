import React, { Component } from 'react'
import screenfull from 'screenfull'
import { Menu, Layout, Avatar, Row, Col, message } from 'antd'
import {
	FullscreenOutlined,
	FullscreenExitOutlined,
} from '@ant-design/icons'
import { withRouter, RouteComponentProps } from 'react-router-dom'
import { Icon } from '@ant-design/compatible'

import { Models } from '../utils/model'
import { UserAction } from '../axios/action'


const { Header } = Layout
const SubMenu = Menu.SubMenu

type HeaderCustomProps = RouteComponentProps<any> & {
	toggle: () => void;
	collapsed: boolean;
	user: any;
	responsive?: any;
	path?: string;
};
type HeaderCustomState = {
	visible: boolean,
	isFullScreen: boolean,
};

class HeaderCustom extends Component<HeaderCustomProps, HeaderCustomState> {
	state = {
		visible: false,
		isFullScreen: false
	};
	screenFull = () => {
		if (screenfull.isEnabled) {
			screenfull.request();
			this.setState({
				isFullScreen: true
			})
		}
	};
	exitFullScreen = () => {
		const { isFullScreen } = this.state
		if (screenfull.isEnabled && isFullScreen) {
			screenfull.exit()
			this.setState({
				isFullScreen: false
			})
		}
	}
	logout = () => {
		const userInfo = Models.get('userInfo')

		UserAction.logout({email: userInfo.email}).then(value => {
			if (value) {
				// 跳转到登录页
				this.props.history.push('/app/login')
				Models.remove('userInfo')
				message.success(`${userInfo.username}已退出`)
				return
			}
		}).catch(err => {
			message.error("退出失败，请稍后重试", 5)
			console.log("Service.register catch: ", JSON.stringify(err))
		})
	}
	popoverHide = () => {
		this.setState({
			visible: false,
		})
	}
	handleVisibleChange = (visible: boolean) => {
		this.setState({ visible });
	}
	
	render() {
		const { isFullScreen } = this.state
		return (
			<Header className="custom-theme header" style={{backgroundColor: '#e9e9e9'}}>
				<Row>
					<Col span={1} style={{height: '65px'}}>
						<Icon
							className="header_trigger custom-trigger"
							type={this.props.collapsed ? 'menu-unfold' : 'menu-fold'}
							onClick={this.props.toggle}
							style={{color: 'black'}}
						/>
					</Col>

					<Col span={17} style={{ height: '65px'}}>
						{/* <Carousel dots={false} autoplay autoplaySpeed={10 * 1000} speed={3000} style={{textAlign: 'center', marginTop: '9px'}}>
							<div>
								<span style={{
									height: '40px',
									color: '#000000',
									lineHeight: '40px',
									textAlign: 'center',
									background: '#e9e9e9',
								}}>
									服务器node1.pcncad.com的IP发生了变化,由原来的`10.108.210.194`变成了`10.108.211.136`, 由于域名存在缓存,可能会短暂不可用.
								</span>
							</div>
							<div style={{textAlign: 'center'}}>
								<span style={{
									height: '40px',
									color: '#000000',
									lineHeight: '40px',
									textAlign: 'center',
									background: '#e9e9e9',
								}}>
									斑蛰向你申请服务器node1.pcncad.com的使用权限,请及时处理.
								</span>
							</div>
						</Carousel> */}
					</Col>
					<Col span={3}>
						{
							isFullScreen ? (
								<FullscreenExitOutlined translate="true" onClick={this.exitFullScreen} style={{paddingRight: '64px', color: 'black'}} />
							) : (
								<FullscreenOutlined translate="true" onClick={this.screenFull} style={{paddingRight: '64px', color: 'black'}} />
							)
						}
					</Col>
					<Col span={3}>
						<Menu
							mode="horizontal"
							style={{textAlign: 'center'}}
						>
							<SubMenu
								title={
									<Avatar
										style={{
											backgroundColor: "#f56a00",
											verticalAlign: 'middle'
										}}
										size="large"
									>
										{"banzhe"}
									</Avatar>
								}
								popupOffset={[-60,1]}
							>
								<Menu.Item
									key="logout"
									style={{
										backgroundColor: '#FFFFFF',
										textAlign: 'center'
									}}
								>
									<span onClick={this.logout}>退出登录</span>
								</Menu.Item>
							</SubMenu>
						</Menu>
					</Col>
				</Row>
			</Header>
		);
	}
}

// 重新设置连接之后组件的关联类型
const HeaderCustomConnect: React.ComponentClass<
	HeaderCustomProps,
	HeaderCustomState
	> = HeaderCustom

export default withRouter(HeaderCustomConnect)