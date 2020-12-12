import React, { ReactNode, ReactText } from 'react'
import { Table, Row, Col, Button, Tag, message } from 'antd'
import BreadcrumbCustom, { BreadcrumbPrpos } from '../../bread-crumb-custom'
import Search from './search'
import {
	UserAddOutlined,
	InfoCircleOutlined,
	SyncOutlined,
	UsergroupDeleteOutlined,
} from '@ant-design/icons';
import AddUser from './add-user';

interface Expandable {
	expandedRowKeys: string[],
	expandedRowRender: () => ReactNode,
	onExpand: (expanded: boolean, record: any) => void
}

interface NestedTableDataSource {
	key: string,
	hostname: string,
	domainName: string,
	IPAddress: string,
	serverTags: string[],
	numberOfUser: number,
	status: string,
	action: string
}

// 嵌套表 
const columns = [
	{
		title: '主机名',
		dataIndex: 'hostname',
		key: 'hostname',
	},
	{
		title: '域名',
		dataIndex: 'domainName',
		key: 'domainName',
	},
	{
		title: 'IP地址',
		dataIndex: 'IPAddress',
		key: 'IPAddress',
	},
	{
		title: '标签',
		dataIndex: 'serverTags',
		key: 'serverTags',
		render: (serverTags: any, _: any, __: any) => {
			return (
				<span>
        {
			serverTags.map((tag: any) => {
				let color = '';
				let name = '';
				switch (tag) {
					case 'ordinaryServer':
						color = 'magenta';
						name = 'X86';
						break;
					case 'gpuServer':
						color = 'red';
						name = 'GPU';
						break;
					case 'intranetServer':
						color = 'green';
						name = '内网';
						break;
					case 'publicNetworkServer':
						color = 'purple';
						name = '公网';
						break;
					default:
						color = '#f50';
						name = '未知';
				}
				return (
					<Tag color={color} key={tag}>
						{name}
					</Tag>
				);
			})
		}
      </span>);
		},
	},
	{
		title: '状态',
		dataIndex: 'status',
		key: 'status',
	},
	{
		title: '操作',
		dataIndex: 'action',
		key: 'action',
	},

];

type OrdinaryUserState = {
	expandable: Expandable,
	nestedTableDataSource: NestedTableDataSource[],
	selectedRowKeys: ReactText[],
	isModalVisible: boolean,
}

const breadcrumProps: BreadcrumbPrpos[] = [
	{
		name: '首页',
		link: '/app/index',
	},
	{
		name: '用户管理',
	},
	{
		name: '服务器管理员',
	},
];

export default class OrdinaryUser extends React.Component<any, OrdinaryUserState> {

	state: OrdinaryUserState = {
		expandable: {
			expandedRowKeys: [],
			expandedRowRender: () => {
				return <Table columns={columns} dataSource={this.state.nestedTableDataSource} pagination={false} />;
			},
			onExpand: (expanded: boolean, record: any) => {
				if (expanded) {
					// TODO: 这里替换成接口,请求真实的数据
					const expandedRowKeys: string[] = [record.key];
					const datasources: NestedTableDataSource[] = [];
					const colors = ['ordinaryServer', 'gpuServer', 'intranetServer', 'publicNetworkServer'];
					for (let i = 0; i < 3; i++) {
						const data: NestedTableDataSource = {
							key: record.key + '_' + i,
							hostname: record.name,
							domainName: record.name,
							IPAddress: `10.108.210.21${i}`,
							serverTags: [colors[i], colors[i + 1]],
							numberOfUser: 3,
							status: 'online',
							action: '回收'
						};
						datasources.push(data);
					}
					const expandable: Expandable = this.state.expandable;
					expandable.expandedRowKeys = expandedRowKeys;

					this.setState({ ...this.state, nestedTableDataSource: datasources, expandable });
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
	};
	columns = [
		{
			title: 'id',
			dataIndex: 'id',
			key: 'id',
		},
		{
			title: '姓名',
			dataIndex: 'name',
			key: 'name',
		},
		{
			title: '使用服务器数量',
			dataIndex: 'numOfServer',
			key: 'numOfServer',
		},
		{
			title: '标签',
			dataIndex: 'serverTags',
			key: 'serverTags',
			render: (serverTags: any, _: any, __: any) => {
				return (
					<span>
          			{
						serverTags.map((tag: any) => {
							let color = '';
							let name = '';
							switch (tag) {
								case 'ordinaryServer':
									color = 'magenta';
									name = 'X86';
									break;
								case 'gpuServer':
									color = 'red';
									name = 'GPU';
									break;
								case 'intranetServer':
									color = 'green';
									name = '内网';
									break;
								case 'publicNetworkServer':
									color = 'purple';
									name = '公网';
									break;
								default:
									color = '#f50';
									name = '未知';
							}
							return (
								<Tag color={color} key={tag}>
									{name}
								</Tag>
							);
						})
					}
        </span>);
			},
		},
		{
			title: '操作',
			dataIndex: 'action',
			key: 'action',
		},
	];

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

	onModalOk = (value: any) => {
		console.log(value);
		this.setState({ ...this.state, isModalVisible: false });
		message.loading({ content: 'Loading...', key: 'addUser' });
		setTimeout(() => {
			message.success({ content: 'Loaded!', key: 'addUser', duration: 2 });
		}, 2000);
	};

	onModalCancel = () => {
		this.setState({ ...this.state, isModalVisible: false });
		message.warning({ content: '添加用户已经取消!', key: 'addUser', duration: 2 });
	};

	render() {
		const data: any = [];
		for (let i = 0; i < 5; i++) {
			const value = {
				key: i + 1,
				id: 'id_' + i,
				name: 'name_' + i + '.pcncad.club',
				serverTags: ['ordinaryServer', 'gpuServer', 'intranetServer', 'publicNetworkServer'],
				numOfServer: i + 1,
				action: '添加服务器 | 删除',
			};
			data.push(value);
		}
		const { selectedRowKeys, isModalVisible } = this.state;
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
						style={{ backgroundColor: '#e6f7ff', border: '#91d5ff' }}
					>
						<Col span={12} style={{ display: 'flex', alignItems: 'center' }}>
              <span>
                <InfoCircleOutlined
					translate="true"
					style={{ color: '#1890ff' }}
                />
                已选择{<span style={{ color: 'blue' }}>{selectedRowKeys.length}</span>}项
              </span>
						</Col>
						<Col span={12}>
              <span style={{ float: 'right' }}>
                {
					selectedRowKeys.length > 0 ? (
						<span style={{ paddingRight: '64px' }}>
                      <Button
						  icon={<UsergroupDeleteOutlined translate="true" />}
						  onClick={this.delete}
                      >
                        批量删除
                      </Button>
                    </span>
					) : (
						null
					)
				}
				  <Button
					  icon={<UserAddOutlined translate="true" />}
					  onClick={this.add}
				  >
                  添加
                </Button>
                <AddUser onModalOk={this.onModalOk} onModalCancel={this.onModalCancel} isModalVisible={isModalVisible} />
                <span style={{ paddingLeft: '64px' }}>
                  <SyncOutlined
					  translate="true" onClick={this.sync}
                  />
                </span>
              </span>
						</Col>
					</Row>
					<Table
						rowSelection={rowSelection}
						columns={this.columns}
						dataSource={data}
						expandable={this.state.expandable}
					/>
				</div>
			</div>
		);
	}
}