import React from 'react'
import {
	Row,
	Col,
	Card,
	Comment,
	Tooltip,
	List,
	PageHeader,
	Descriptions,
	Divider,
	Avatar
} from 'antd'
import moment from 'moment'
import BreadcrumbCustom, { BreadcrumbPrpos } from '../BreadcrumbCustom'
import ServerInfo from './ServerInfo'

const data = [
	{
		actions: [<span key="comment-list-reply-to-0">Reply to</span>],
		author: 'Han Solo',
		avatar:
			<Avatar
				style={{ backgroundColor: '#F56A00', verticalAlign: 'middle' }}
				size="large"
			>
				{'banzhe'}
			</Avatar>,
		content: (
			<p>
				We supply a series of design principles, practical patterns and high quality design
				resources (Sketch and Axure), to help people create their product prototypes beautifully and
				efficiently.
			</p>
		),
		datetime: (
			<Tooltip
				title={moment()
					.subtract(1, 'days')
					.format('YYYY-MM-DD HH:mm:ss')}
			>
        <span>
          {moment()
			  .subtract(1, 'days')
			  .fromNow()}
        </span>
			</Tooltip>
		),
	},
	{
		actions: [<span key="comment-list-reply-to-0">Reply to</span>],
		author: 'Han Solo',
		avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
		content: (
			<p>
				We supply a series of design principles, practical patterns and high quality design
				resources (Sketch and Axure), to help people create their product prototypes beautifully and
				efficiently.
			</p>
		),
		datetime: (
			<Tooltip
				title={moment()
					.subtract(2, 'days')
					.format('YYYY-MM-DD HH:mm:ss')}
			>
        <span>
          {moment()
			  .subtract(2, 'days')
			  .fromNow()}
        </span>
			</Tooltip>
		),
	},
]

const breadcrumProps: BreadcrumbPrpos[] = [
	{
		name: '首页',
		link: '/app/index'
	}
]

class Home extends React.Component {
	render() {
		return (
			<div className="gutter-example button-demo">
				<BreadcrumbCustom breadcrumProps={breadcrumProps} />
				<div style={{ backgroundColor: '#FFFFFF', padding: '8px 8px' }}>
					<Divider orientation="left">所有服务器信息概览</Divider>
					<Row gutter={16} >
						<Col span={6}>
							<Card>
								<PageHeader
									className="site-page-header"
									title="主机总数"
									subTitle={18}
								>
									<Descriptions size="default" column={2}>
										<Descriptions.Item label="普通服务器">10</Descriptions.Item>
										<Descriptions.Item label="GPU服务器">8</Descriptions.Item>
										<Descriptions.Item label="内网服务器">10</Descriptions.Item>
										<Descriptions.Item label="公网服务器">8</Descriptions.Item>
									</Descriptions>
								</PageHeader>
							</Card>
						</Col>

						<Col span={6}>
							<Card>
								<PageHeader
									className="site-page-header"
									title="可用主机总数"
									subTitle={18}
								>
									<Descriptions size="default" column={2}>
										<Descriptions.Item label="普通服务器">10</Descriptions.Item>
										<Descriptions.Item label="GPU服务器">8</Descriptions.Item>
										<Descriptions.Item label="内网服务器">10</Descriptions.Item>
										<Descriptions.Item label="公网服务器">8</Descriptions.Item>
									</Descriptions>
								</PageHeader>
							</Card>
						</Col>

						<Col span={6}>
							<Card>
								<PageHeader
									className="site-page-header"
									title="全部用户数"
									subTitle={18}
								>
									<Descriptions size="default" column={2}>
										<Descriptions.Item label="普通服务器">10</Descriptions.Item>
										<Descriptions.Item label="GPU服务器">8</Descriptions.Item>
										<Descriptions.Item label="内网服务器">10</Descriptions.Item>
										<Descriptions.Item label="公网服务器">8</Descriptions.Item>
									</Descriptions>
								</PageHeader>
							</Card>
						</Col>

						<Col span={6}>
							<Card>
								<PageHeader
									className="site-page-header"
									title="在线用户数"
									subTitle={18}
								>
									<Descriptions size="default" column={2}>
										<Descriptions.Item label="普通服务器">10</Descriptions.Item>
										<Descriptions.Item label="GPU服务器">8</Descriptions.Item>
										<Descriptions.Item label="内网服务器">10</Descriptions.Item>
										<Descriptions.Item label="公网服务器">8</Descriptions.Item>
									</Descriptions>
								</PageHeader>
							</Card>
						</Col>
					</Row>
				</div>
				<Divider style={{margin: '4px 0px'}} />
				<ServerInfo />
				<div style={{ backgroundColor: '#FFFFFF', padding: '8px 8px' }}>
					<Divider orientation="left">我的服务器信息概览</Divider>
					<Row gutter={16} >
						<Col span={6}>
							<Card>
								<PageHeader
									className="site-page-header"
									title="主机总数"
									subTitle={18}
								>
									<Descriptions size="default" column={2}>
										<Descriptions.Item label="普通服务器">10</Descriptions.Item>
										<Descriptions.Item label="GPU服务器">8</Descriptions.Item>
										<Descriptions.Item label="内网服务器">10</Descriptions.Item>
										<Descriptions.Item label="公网服务器">8</Descriptions.Item>
									</Descriptions>
								</PageHeader>
							</Card>
						</Col>

						<Col span={6}>
							<Card>
								<PageHeader
									className="site-page-header"
									title="可用主机总数"
									subTitle={18}
								>
									<Descriptions size="default" column={2}>
										<Descriptions.Item label="普通服务器">10</Descriptions.Item>
										<Descriptions.Item label="GPU服务器">8</Descriptions.Item>
										<Descriptions.Item label="内网服务器">10</Descriptions.Item>
										<Descriptions.Item label="公网服务器">8</Descriptions.Item>
									</Descriptions>
								</PageHeader>
							</Card>
						</Col>

						<Col span={6}>
							<Card>
								<PageHeader
									className="site-page-header"
									title="全部用户数"
									subTitle={18}
								>
									<Descriptions size="default" column={2}>
										<Descriptions.Item label="普通服务器">10</Descriptions.Item>
										<Descriptions.Item label="GPU服务器">8</Descriptions.Item>
										<Descriptions.Item label="内网服务器">10</Descriptions.Item>
										<Descriptions.Item label="公网服务器">8</Descriptions.Item>
									</Descriptions>
								</PageHeader>
							</Card>
						</Col>

						<Col span={6}>
							<Card>
								<PageHeader
									className="site-page-header"
									title="连接数"
									subTitle={18}
								>
									<Descriptions size="default" column={2}>
										<Descriptions.Item label="普通服务器">10</Descriptions.Item>
										<Descriptions.Item label="GPU服务器">8</Descriptions.Item>
										<Descriptions.Item label="内网服务器">10</Descriptions.Item>
										<Descriptions.Item label="公网服务器">8</Descriptions.Item>
									</Descriptions>
								</PageHeader>
							</Card>
						</Col>
					</Row>
				</div>

				<Divider style={{margin: '4px 0px'}} />
				<ServerInfo />

				<div style={{ backgroundColor: '#FFFFFF', padding: '8px 8px' }}>
					<Divider orientation="left">我的消息</Divider>
					<Row>
						<Col className="gutter-row">
							<Card bordered={false}>
								<List
									className="comment-list"
									itemLayout="horizontal"
									dataSource={data}
									renderItem={item => (
										<li>
											<Comment
												actions={item.actions}
												author={item.author}
												avatar={item.avatar}
												content={item.content}
												datetime={item.datetime}
											/>
										</li>
									)}
								/>
							</Card>
						</Col>
					</Row>
				</div>
			</div>
		)
	}
}

export default Home;