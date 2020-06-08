import React from 'react'
import { Button, Divider, Form, Input, Modal, Select, Switch } from 'antd';
import Editor from '@monaco-editor/react'
import { FormInstance } from 'antd/lib/form';

// 表单数据类型
interface FormDataType {
    name: string,
    type: string,
    execType: string,
    execTime: string,
    isUseCron: boolean,
    servers: string[],
    batCommand: string
}

const formDataInitValue: FormDataType = {
    name: '',
    type: 'shell',
    execType: 'period',
    execTime: '',
    isUseCron: false,
    servers: [],
    batCommand: ''
}

interface EditBatCommandPropsType {
    isVisible: boolean, // 显示弹窗
    onChange: Function, // 关闭弹窗
    isEdit: boolean, // 是否编辑
}

interface EditBatCommandStateType {
    formData: FormDataType,
    isTiming: boolean, // 定时
    isPeriod: boolean, // 周期
    isUseCron: boolean, // 是否使用crontab
    getEditorText: () => string , // 批量命令
}

// 表单样式
const formItemLayout = {
    labelCol: {
        xs: {
            span: 24,
        },
        sm: {
            span: 4,
        },
    },
    wrapperCol: {
        xs: {
            span: 24,
        },
        sm: {
            span: 20,
        },
    },
}

export default class EditBatCommand extends React.Component<EditBatCommandPropsType, EditBatCommandStateType> {

    state = {
        formData: formDataInitValue,
        isTiming: false,
        isPeriod: true,
        isUseCron: false,
        getEditorText: () => { return "" }
    }

    formRef = React.createRef<FormInstance>()

    onCancel = () => {
        this.setState({ formData: formDataInitValue }, () => { this.props.onChange(false) })
        console.log(this.formRef)
        this.formRef.current!.resetFields()
    }
    onValueChange = (_: any, allValues: any) => {
        let { isPeriod, isTiming } = this.state
        const { execType } = allValues
        // 定时
        if (execType === 'timing') {
            isPeriod = false
            isTiming = true
        }
        // 周期
        if (execType === 'period') {
            isPeriod = true
            isTiming = false
        }
        this.setState({ formData: allValues, isPeriod, isTiming })
    }

    onFinish = () => {
        const { formData, isUseCron, getEditorText } = this.state
        formData.isUseCron = isUseCron
        formData.batCommand = getEditorText()
        console.log(formData)
        // TODO: 保存数据
    }

    render() {
        const { isVisible, isEdit } = this.props
        const { isTiming, isPeriod } = this.state
        return (
            <Modal
                title="任务编辑"
                visible={isVisible}
                onCancel={this.onCancel}
                footer={null}
                width={800}
            >
                <Form
                    {...formItemLayout}
                    onValuesChange={this.onValueChange}
                    onFinish={this.onFinish}
                    ref={this.formRef}
                >
                    <Form.Item
                        name="name"
                        label="任务名称"

                        rules={[{ required: true, message: "任务名称不能为空" }]}

                    >
                        <Input style={{width: "50%"}} disabled={!isEdit} />
                    </Form.Item>
                    <Form.Item
                        name="type"
                        label="任务类型"
                    >
                        <Select style={{width: "50%"}} disabled={!isEdit} >
                            <Select.Option value="shell">shell</Select.Option>
                        </Select>
                    </Form.Item>
                    <Form.Item
                        name="execType"
                        label="执行类型"
                    >
                        <Select style={{width: "50%"}} disabled={!isEdit} >
                            <Select.Option value="timing">定时</Select.Option>
                            <Select.Option value="period">周期</Select.Option>
                        </Select>
                    </Form.Item>
                    {
                        isTiming ? (
                            <Form.Item
                                name="execTime"
                                label="执行时间"
                            >
                                <Input placeholder="时间单位为分钟" style={{width: "50%"}} disabled={!isEdit} />
                            </Form.Item>
                        ) : null
                    }
                    {
                        isPeriod ? ([
                            <Form.Item
                                name="execTime"
                                label="执行周期"
                            >
                                <Input placeholder="cron表达式" style={{width: "50%"}} disabled={!isEdit} />
                            </Form.Item>,
                            <Form.Item
                                name="useCron"
                                label="使用Cron"
                            >
                                <Switch onChange={(value: boolean) => { this.setState({ isUseCron: value }) }} disabled={!isEdit} />
                                <span>&nbsp;<span style={{color: "red"}}>*</span>&nbsp;开启Cron,会直接把命令放到crontab中进行周期控制, 不会返回任务结果</span>
                            </Form.Item>
                        ]) : null
                    }

                    <Form.Item
                        name="servers"
                        initialValue={[]}
                        label="执行机器"
                    >
                        <Select
                            mode="multiple"
                            style={{ width: '50%' }}
                            placeholder={"请选择服务器(支持多选)"}
                            disabled={!isEdit}
                        >
                            {[1,2,3,4,5,6].map(value => {return <Select.Option key={value} value={`10.108.210.2${value}`}>{`10.108.210.2${value}`}</Select.Option>})}
                        </Select>
                    </Form.Item>

                    <Form.Item
                        name="execCommand"
                        label="执行内容"
                    >
                        <Editor
                            language="shell"
                            theme="vs-dark"
                            height="200px"
                            editorDidMount={ (getEditorValue: () => string): void => { this.setState({ getEditorText: getEditorValue }) }}
                            options={{readOnly: !isEdit}}

                        />
                    </Form.Item>
                    {
                        isEdit ? (
                            <Form.Item style={{textAlign: "center"}} >
                                <Button type="primary" htmlType="submit">
                                    保存
                                </Button>
                                <Divider type="vertical" />
                                <Button type="primary" onClick={this.onCancel}>
                                    取消
                                </Button>
                            </Form.Item>
                        ) : null
                    }
                </Form>
            </Modal>
        )
    }
}