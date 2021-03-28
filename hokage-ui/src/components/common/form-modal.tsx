import React, { useState } from 'react'
import { Button, Form, message, Modal } from 'antd'
import { translate } from '../../i18n'
import { FormInstance } from 'antd/lib/form'
import { UserServerOperateForm } from '../../axios/action/user/user-type'

interface FormModalProps {
    actionName: string,
    renderForm: (form: FormInstance) => React.ReactNode,
    action: (formValue: UserServerOperateForm) => void
}

export const FormModal = (props: FormModalProps) => {
    const [form] = Form.useForm()
    const [visible, setVisible] = useState(false)
    const [confirmLoading, setConfirmLoading] = useState(false)

    const onClick = () => { setVisible(true) }

    const onCancel = () => {
        form.resetFields()
        setVisible(false)
    }
    const onOk = (_: React.MouseEvent<HTMLElement>) => {
        setConfirmLoading(true)
        const confirmAction = async () => {
            let formValue: UserServerOperateForm = {}
            try {
                formValue = await form.validateFields()
                await props.action(formValue)
                setVisible(false)
            } catch (e) {
                if (!e.errorFields) {
                    message.error(`${translate(props.actionName)} error.`)
                }
                console.log(JSON.stringify(e))
            } finally {
                setConfirmLoading(false)
            }
        }
        confirmAction()
    }


    return (
        <>
            <Button type="link" onClick={onClick}>{translate(props.actionName)}</Button>
            <Modal
                visible={visible}
                title={props.actionName}
                okText={translate('confirm')}
                cancelText={translate('cancel')}
                onCancel={onCancel}
                confirmLoading={confirmLoading}
                onOk={onOk}
                closable={false}
                maskClosable={false}
            >
                {props.renderForm(form)}
            </Modal>
        </>
    )
}