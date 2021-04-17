import React from 'react'
import { Modal, Form, Row, Col, Select, Button, message } from 'antd'
import { Option } from '../../../axios/action/server/server-type'
import { UserAction } from '../../../axios/action'
import { FormInstance } from 'antd/lib/form'

type AddUserProps = {
  onModalOk: (value: any) => void,
  onModalCancel: () => void,
  isModalVisible: boolean
}

type AddSubordinateState = {
  ordinaryUsers: Option[],
}

export default class AddUser extends React.Component<AddUserProps, AddSubordinateState> {

  state = {
    ordinaryUsers: []
  }

  componentDidMount() {
    this.listUserOptions()
  }

  listUserOptions = () => {
    UserAction.listAllSubordinate().then(userVOList => {
      const subordinateUserOptions: Option[] = userVOList.map(userVO => {
        return { label: `${userVO.username}(${userVO.email})`, value: userVO.id }
      })
      this.setState({ ordinaryUsers: subordinateUserOptions })

    }).catch(err => {
      message.error(err)
    })
  }

  onModalOk = (idList: number[]) => {
    this.props.onModalOk(idList)
    this.listUserOptions()
  }

  formRef = React.createRef<FormInstance>()

  reset = () => {
    this.props.onModalCancel()
    if (this.formRef.current) {
      this.formRef.current.resetFields()
    }
  }

  render() {
    const { isModalVisible } = this.props
    const { ordinaryUsers } = this.state
    return (
      <Modal
        title="批量添加用户"
        visible={isModalVisible}
        footer={null}
        onCancel={this.props.onModalCancel}
      >
        <Form
          name="operator-add"
          onFinish={this.onModalOk}
          ref={this.formRef}
        >
          <Row gutter={24}>
            <Col span={15}>
              <Form.Item name="userIds" initialValue={[]}>
                <Select
                  mode="multiple"
                  style={{ width: '100%' }}
                  placeholder={"请选择(支持多选)"}
                >
                  {
                    ordinaryUsers.map((option: Option, index) => {
                      return (
                        <Select.Option key={index} value={option.value}>{option.label}</Select.Option>
                      )
                    })
                  }
                </Select>
              </Form.Item>
            </Col>
            <Col span={9}>
              <Form.Item>
                <Button type="primary" htmlType="submit">
                  添加
                </Button>
                <Button
                  style={{
                    margin: '0 8px',
                  }}
                  onClick={this.reset}
                >
                  取消
                </Button>
              </Form.Item>
            </Col>
          </Row>
        </Form>
      </Modal>
    )
  }
}
