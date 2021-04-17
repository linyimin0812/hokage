import React, { ReactNode, ReactText } from 'react'
import { Table, Row, Col, Button, message } from 'antd'
import BreadCrumb from '../../../layout/bread-crumb'
import { UserSearch, UserSearchFormType } from '../search'
import {
  InfoCircleOutlined,
  UsergroupDeleteOutlined,
} from '@ant-design/icons'
import { breadcrumbProps, columns, nestedColumn } from './column-definition'
import { ServerVO } from '../../../axios/action/server/server-type'
import { UserVO } from '../../../axios/action/user/user-type'
import { UserAction } from '../../../axios/action'
import { getHokageUid } from '../../../libs'

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
    this.searchSubordinate({operatorId: getHokageUid()})
  }

  searchSubordinate = (value: UserSearchFormType) => {
    this.setState({loading: true})
    UserAction.searchSubOrdinate(value).then(userList => {
      this.setState({dataSource: userList, loading: false})
    }).catch(err => {
      message.error(err)
    })
  }

  onFinish = (value: UserSearchFormType) => {
    value.operatorId = getHokageUid()
    this.searchSubordinate(value)
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

  onModalOk = (value: {userIds: number[]}) => {
    UserAction.addSubordinate({
      operatorId: getHokageUid(),
      serverIds: [],
      userIds: (value && value.userIds) || []
    }).then(value => {
      if (value) {
        this.setState({ ...this.state, isModalVisible: false })
        this.searchSubordinate({operatorId: getHokageUid()})
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
    const { selectedRowKeys, dataSource, loading } = this.state;
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
        <BreadCrumb breadcrumbProps={breadcrumbProps} />
        <UserSearch onFinish={this.onFinish} usernameType={'ordinary'} />
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
            <Col span={12} style={{padding: '0 0'}}>
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
    )
  }
}
