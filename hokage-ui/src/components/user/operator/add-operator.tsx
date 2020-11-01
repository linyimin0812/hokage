import React from 'react'
import { Modal, Form, Row, Col, Select, Button } from 'antd'

type AddOperatorPropTypes = {
    onModalOk: (value: any) => void,
    onModalCancel: () => void,
    isModalVisible: boolean
}

// TODO: 获取真实的所有用户
const ordianryUser: any[] = [];
for (let i = 10; i < 36; i++) {
    ordianryUser.push(<Select.Option key={i.toString(36) + i} value={i.toString(36) + i}>{i.toString(36) + i}</Select.Option>);
}

export default class AddOperator extends React.Component<AddOperatorPropTypes, {}> {

    state = {
        isModalVisible: false
    }

    render() {
        const { isModalVisible } = this.props
        return (
            <Modal
                title="批量添加管理员"
                visible={isModalVisible}
                footer={null}
                onCancel={this.props.onModalCancel}
            >
                <Form
                    name="operator-add"
                    onFinish={this.props.onModalOk}
                >
                    <Row gutter={24}>
                        <Col span={15}>
                            <Form.Item name="usernames" initialValue={[]}>
                                <Select
                                    mode="multiple"
                                    style={{ width: '100%' }}
                                    placeholder={"请选择(支持多选)"}
                                >
                                    {ordianryUser}
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
                                    onClick={() => {
                                        this.props.onModalCancel();
                                    }}
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