import React from 'react'
import { Button, Col, Row } from 'antd'
import { InfoCircleOutlined, UsergroupDeleteOutlined as UserGroupDeleteOutlined } from '@ant-design/icons'
import store from '../store'
import { observer } from 'mobx-react'

@observer
export default class Toolbar extends React.Component {
  render() {
    return (
      <Row gutter={24} style={{ backgroundColor: '#e6f7ff', border: '#91d5ff', margin: '0 0' }}>
        <Col span={12} style={{ display: 'flex', alignItems: 'center' }}>
          <span>
            <InfoCircleOutlined translate="true" style={{ color: '#1890ff' }} />
            已选择{<span style={{ color: 'blue' }}>{store.selectedRowKeys.length}</span>}项
          </span>
        </Col>
        <Col span={12} style={{padding: '0 0'}}>
          <span style={{ float: 'right' }}>
            {store.selectedRowKeys.length > 0 ? (
              <span>
                <Button icon={<UserGroupDeleteOutlined translate="true" />} onClick={() => {alert(JSON.stringify(store.selectedRowKeys))}}>
                  批量删除
                </Button>
              </span>
            ) : null}
          </span>
        </Col>
      </Row>
    )
  }
}
