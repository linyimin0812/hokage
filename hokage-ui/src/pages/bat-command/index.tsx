import React from 'react'
import BreadCrumb, { BreadcrumbProps } from '../../layout/bread-crumb'
import MyBatCommand from './my-bat-command'
import { Tabs } from 'antd'
import ExecutedBatCommand from './executed-bat-command'

const breadcrumbProps: BreadcrumbProps[] = [
	{ name: '首页', link: '/app/index' },
	{ name: '批量任务' }
]

export default class BatCommand extends React.Component {
	render() {
		return(
			<div>
				<BreadCrumb breadcrumbProps={breadcrumbProps} />
				<Tabs type="card">
					<Tabs.TabPane tab="创建批量任务" key="create_bat_command">
						<MyBatCommand />
					</Tabs.TabPane>
					<Tabs.TabPane tab="已执行任务" key="view_bat_command">
						<ExecutedBatCommand />
					</Tabs.TabPane>
				</Tabs>
			</div>
		)
	}
}
