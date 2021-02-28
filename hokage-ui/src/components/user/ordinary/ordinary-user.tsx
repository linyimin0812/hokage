import React, { ReactNode, ReactText } from 'react'
import { Table, Row, Col, Button, message } from 'antd'
import BreadcrumbCustom from '../../bread-crumb-custom'
import Search from './search'
import {
	UserAddOutlined,
	InfoCircleOutlined,
	SyncOutlined,
	UsergroupDeleteOutlined,
} from '@ant-design/icons';
import AddUser from './add-user';
import { breadcrumProps, columns, nestedColumn } from './column-definition'
import { ServerVO } from '../../../axios/action/server/server-type'
import { UserVO } from '../../../axios/action/user/user-type'
import { UserAction } from '../../../axios/action'

interface Expandable {
	expandedRowKeys: ReactText[],
	expandedRowRender: () => ReactNode,
	onExpand: (expanded: boolean, record: any) => void
}


type OrdinaryUserState = {
	expandable: Expandable,
	nestedTableDataSource: ServerVO[],
	selectedRowKeys: ReactText[],
	isModalVisible: boolean,
	dataSource: UserVO[],
	loading: boolean,
}

export default class OrdinaryUser extends React.Component<any, OrdinaryUserState> {

	state: OrdinaryUserState = {
		expandable: {
			expandedRowKeys: [],
			expandedRowRender: () => {
				return <Table columns={nestedColumn} dataSource={this.state.nestedTableDataSource} pagination={false} />;
			},
			onExpand: (expanded: boolean, record: any) => {
				if (expanded) {
					const serverVOList: ServerVO[] = record.serverVOList || []
					const expandedRowKeys: string[] = [record.key];

					const expandable: Expandable = this.state.expandable;
					expandable.expandedRowKeys = expandedRowKeys;

					this.setState({ ...this.state, nestedTableDataSource: serverVOList, expandable });
				} else {
					const expandable: Expandable = this.state.expandable;
					expandable.expandedRowKeys = [];

					this.setState({ ...this.state, expandable });
				}
			},
		},
		nestedTableDataSource: [],
		selectedRowKeys: [],
		isModalVisible: false,
		dataSource: [],
		loading: false
	}

	componentDidMount() {
		this.listSubordinate(this.hokageUid)
	}

	// @ts-ignore
	hokageUid: number = window.localStorage.getItem('hokageUid') || 0

	listSubordinate = (supervisorId: number) => {
		this.setState({loading: true})
		UserAction.listSubordinate(supervisorId).then(userList => {
			this.setState({dataSource: userList, loading: false})
		}).catch(err => {
			message.error(err)
		})
	}

	onFinish = (value: any) => {
		console.log(value);
	};

	resetFields = () => {
		console.log('reset');
	};

	onSelectChange = (selectedRowKeys: ReactText[], selectedRows: any[]) => {
		this.setState({ selectedRowKeys });
		// TODO: 从selectRows中获取选择的目标数据,然后进行相关操作
	};

	add = () => {
		this.setState({ ...this.state, isModalVisible: true });
	};

	delete = () => {
		alert('delete operators bat');
	};

	sync = () => {
		alert('sync operator');
	};

	onModalOk = (value: {userIds: number[]}) => {
		UserAction.addSubordinate({
			id: this.hokageUid,
			serverIds: [],
			userIds: (value && value.userIds) || []
		}).then(value => {
			if (value) {
				this.setState({ ...this.state, isModalVisible: false })
				this.listSubordinate(this.hokageUid)
			} else {
				message.error('添加管理员失败')
			}
		}).catch((err) => {
			message.error(err)
		})
	};

	onModalCancel = () => {
		this.setState({ ...this.state, isModalVisible: false });
	};

	render() {
		const { selectedRowKeys, isModalVisible, dataSource, loading } = this.state;
		const rowSelection = {
			selectedRowKeys,
			onChange: this.onSelectChange,
			selections: [
				Table.SELECTION_ALL,
				Table.SELECTION_INVERT,
			],
		};

		return (
			<div>
				<BreadcrumbCustom breadcrumProps={breadcrumProps} />
				<Search onFinish={this.onFinish} clear={this.resetFields} />
				<div style={{ backgroundColor: '#FFFFFF' }}>
					<Row
						gutter={24}
						style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', margin: '0 0' }}
					>
						<Col span={12} style={{ display: 'flex', alignItems: 'center' }}>
							<span>
								<InfoCircleOutlined translate="true" style={{ color: '#1890ff' }} />
								已选择{<span style={{ color: 'blue' }}>{selectedRowKeys.length}</span>}项
							</span>
						</Col>
						<Col span={12}>
              				<span style={{ float: 'right' }}>
								{
									selectedRowKeys.length > 0 ? (
										<span style={{ paddingRight: '64px' }}>
											<Button icon={<UsergroupDeleteOutlined translate="true" />} onClick={this.delete}>
												批量删除
											</Button>
										</span>
									) : null
								}
						  		<Button icon={<UserAddOutlined translate="true" />} onClick={this.add}>
						  			添加
								</Button>
								<AddUser onModalOk={this.onModalOk} onModalCancel={this.onModalCancel} isModalVisible={isModalVisible} />
								<span style={{ paddingLeft: '64px' }}>
									<SyncOutlined translate="true" onClick={this.sync} />
								</span>
              				</span>
						</Col>
					</Row>
					<Table
						rowSelection={rowSelection}
						columns={columns}
						dataSource={dataSource}
						expandable={this.state.expandable}
						loading={loading}
					/>
				</div>
			</div>
		);
	}
}