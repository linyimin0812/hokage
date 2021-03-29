import React, { useState } from 'react'
import { Button, message, Modal } from 'antd'
import { translate } from '../../i18n'

export interface ActionModalProps {
    actionName: string,
    title: string,
    action: () => Promise<void>,
    content: string
}

export const ConfirmModal = (props: ActionModalProps) => {
    const [visible, setVisible] = useState(false)
    const [confirmLoading, setConfirmLoading] = useState(false)

    const onOk = (_: React.MouseEvent<HTMLElement>) => {
        setConfirmLoading(true)
        const confirmAction = async () => {
            try {
                await props.action()
            } catch (e) {
                message.error(`${translate(props.actionName)} error.`)
                console.log(JSON.stringify(e))
            } finally {
                setConfirmLoading(false)
                setVisible(false)
            }
        }
        confirmAction()
    }
    const onClick = () => setVisible(true)
    return (
        <>
            <Button type="link" onClick={onClick}>{translate(props.actionName)}</Button>
            <Modal
                title={translate(props.title)}
                visible={visible}
                onOk={onOk}
                onCancel={() => setVisible(false)}
                okText={translate('confirm')}
                cancelText={translate('cancel')}
                confirmLoading={confirmLoading}
                closable={false}
                maskClosable={false}
            >
                <p>{props.content}</p>
            </Modal>
        </>
    )
}
