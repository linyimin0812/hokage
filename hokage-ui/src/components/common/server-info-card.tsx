import React from 'react'
import { Card, Descriptions, Tag, Divider, Progress } from 'antd';
import { ArrowUpOutlined, ArrowDownOutlined, CodeOutlined } from '@ant-design/icons';

/**
 * 服务器基本信息卡片
 */
export default class ServerInfoCard extends React.Component {
	render() {
		return (
			<div>
				<Card>
					<Descriptions column={8}>
						<Descriptions.Item
							className="customItem"
							span={2}
							style={{padding: '0px 0px'}}
						>
							<div>
								<span>标签:&nbsp;</span>
								<Tag
									color="green"
									key="1"
									style={{padding: '0px 0px'}}
								>
									内网
								</Tag>
								<Tag
									color="red"
									style={{padding: '0px 0px'}}
								>
									X86
								</Tag>
							</div>
							<Divider style={{margin: '4px 0px'}} />

							<span>
                				地址:&nbsp; 10.108.210.194
              				</span>
							<Divider style={{margin: '4px 0px'}} />

							<div>
								<span>状态:&nbsp;</span>
								<Tag
									color={'red'}
								>
									在线
								</Tag>
							</div>
						</Descriptions.Item>

						<Descriptions.Item
							className="customItem"
							span={4}
							style={{padding: '0px 0px', paddingLeft: '12px'}}
						>
							<div>
								负载:&nbsp;
								<Progress
									percent={60}
									successPercent={30}
									style={{display: 'inline-block', width: '75%'}}
								/>
							</div>
							<Divider style={{margin: '4px 0px'}} />

							<div>
								CPU:&nbsp;
								<Progress
									percent={60}
									successPercent={30}
									style={{display: 'inline-block', width: '75%'}}
								/>
							</div>
							<Divider style={{margin: '4px 0px'}} />

							<div>
								<span>网络:&nbsp;</span>
								<span>
                  					<ArrowUpOutlined translate="true" style={{color: 'red'}} />
									{1024}KB&nbsp;&nbsp;&nbsp;&nbsp;
                				</span>
								<span>
                  					<ArrowDownOutlined translate="true" style={{color: 'blue'}} />
									{1024}KB
                				</span>
							</div>
							</Descriptions.Item>

						<Descriptions.Item
							span={2}
							className="customItem"
							style={{padding: '0px 0px', textAlign: 'center'}}
						>
							<div>
								<span>
								  <CodeOutlined translate="true" style={{color: 'green'}} />
								  <a href="/#/app/index">&nbsp;SSH链接</a>
								</span>
								<Divider style={{margin: '4px 0px'}} />
								<span>
								  	<CodeOutlined translate="true" style={{color: 'green'}} />
								  	<a href="/#/app/index">&nbsp;资源监控</a>
								</span>
								<Divider style={{margin: '4px 0px'}} />
								<span>
									<CodeOutlined translate="true" style={{color: 'green'}} />
									<a href="/#/app/index">&nbsp;文件管理</a>
								</span>
							</div>
						</Descriptions.Item>
					</Descriptions>
				</Card>
				<Divider style={{margin: '4px 0px'}} />
			</div>
		)
	}
}