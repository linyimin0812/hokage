import React from 'react'
import { Button, message, Modal } from 'antd'
import { translate } from '../i18n'

export interface ActionModalProps {
    actionName: string,
    title: string,
    action: () => Promise<void>,
    content: string
}

export const ConfirmModal = (props: ActionModalProps) => {

    const onOk = (_: React.MouseEvent<HTMLElement>) => {
        const confirmAction = async () => {
            try {
                await props.action()
            } catch (e) {
                message.error(`${translate(props.actionName)} error.`)
                console.log(JSON.stringify(e))
            }
        }
        confirmAction()
    }
    const onClick = () => {
        Modal.confirm({
            onOk: onOk,
            okText: translate('confirm'),
            cancelText: translate('cancel'),
            closable: false,
            maskClosable: false,
            content: props.content,
            title: props.title
        })
    }
    return (
        <Button type="link" onClick={onClick}>{translate(props.actionName)}</Button>
    )
}
