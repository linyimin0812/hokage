import React from 'react'
import { Drawer, Form, Tabs } from 'antd'
import Editor from '@monaco-editor/react'

interface ExecedBatCommandInfoPropsType {
    isVisible: boolean,
    onCloseDrawer: (value: boolean) => void
}

interface ExecedBatCommandInfoStateType {
    execType: string
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

export default class ExecedBatCommandInfo extends React.Component<ExecedBatCommandInfoPropsType, ExecedBatCommandInfoStateType> {

    state = {
        execType: 'period'
    }

    render() {

        const { isVisible } = this.props
        const { execType } = this.state
        return (
            <Drawer
                width={640}
                title="批量命令详情"
                placement="right"
                onClose={() => { this.props.onCloseDrawer(false) }}
                visible={isVisible}
            >
                <Form
                    {...formItemLayout}
                >
                    <Form.Item
                        name="name"
                        label="任务名称"

                        rules={[{ required: true, message: "任务名称不能为空" }]}

                    >
                        <span>备份数据库数据</span>
                    </Form.Item>
                    <Form.Item
                        name="type"
                        label="任务类型"
                    >
                        <span>shell</span>
                    </Form.Item>
                    <Form.Item
                        name="execType"
                        label="执行类型"
                    >
                        <span>period</span>
                    </Form.Item>
                    {
                        execType === 'timing' ? (
                            <Form.Item
                                name="execTime"
                                label="执行时间"
                            >
                                <span>2020-06-08 17:00</span>
                            </Form.Item>
                        ) : [
                            <Form.Item
                                name="execTime"
                                label="执行周期"
                            >
                                <span>* 1 * * *</span>
                            </Form.Item>,
                            <Form.Item
                                name="useCron"
                                label="使用Cron"
                            >
                                <span>否</span>
                            </Form.Item>
                        ]
                    }

                    <Form.Item
                        name="servers"
                        label="执行机器"
                    >
                        <span>10.108.210.194</span>
                    </Form.Item>

                    <Form.Item
                        name="startTime"
                        label="开始时间"
                    >
                        <span>2020-06-08 17:00</span>
                    </Form.Item>

                    <Form.Item
                        name="execTime"
                        label="执行时长"
                    >
                        <span>小于1秒</span>
                    </Form.Item>
                    <Form.Item
                        name="exitCode"
                        label="ExitCode"
                    >
                        <span>0</span>
                    </Form.Item>
                </Form>
                <Tabs type="card">
                    <Tabs.TabPane tab="命令脚本" key="bat_command">
                        <Editor
                            language="shell"
                            theme="vs-dark"
                            height="200px"
                            options={{readOnly: true}}
                            value="ls -lh"
                        />
                    </Tabs.TabPane>
                    <Tabs.TabPane tab="结果输出" key="bat_command_result">
                        <Editor
                            language="shell"
                            theme="vs-dark"
                            height="200px"
                            options={{readOnly: true}}
                            value={`jdk
                                jdk1.8.0_251
                                test.sh`
                            }
                        />
                    </Tabs.TabPane>
                </Tabs>
            </Drawer>
        )
    }

}