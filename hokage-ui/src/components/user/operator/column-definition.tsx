import { Button, Tag } from 'antd'
import { randomColor } from '../../../utils'
import React from 'react'
import { Operation, UserServerOperateForm } from '../../../axios/action/user/user-type'
import { translate } from '../../../i18n'
import { ConfirmModal } from '../../common/confirm-modal'
import { BreadcrumbPrpos } from '../../bread-crumb-custom'
import { FormModal } from '../../common/form-modal'
import { FormInstance } from 'antd/lib/form'
import { SelectServer } from '../../server/select-server'

/**
 * @author linyimin
 * @date 2021/2/19 12:34 am
 * @email linyimin520812@gmail.com
 * @description
 */


export const nestedColumn = [
    {
        title: 'id',
        dataIndex: 'id',
        key: 'id'
    },
    {
        title: '主机名',
        dataIndex: 'hostname',
        key: 'hostname'
    },
    {
        title: '域名',
        dataIndex: 'domain',
        key: 'domain'
    },
    {
        title: '标签',
        dataIndex: 'labels',
        key: 'labels',
        render: (labels: string[]) => {
            return labels.map((tag: any )=> <Tag color={randomColor(tag)} key={tag}>{tag}</Tag>)
        }
    },
    {
        title: '使用人数',
        dataIndex: 'userNum',
        key: 'userNum'
    },
    {
        title: '状态',
        dataIndex: 'status',
        key: 'status',
        render: (text: string) => <Tag color = {randomColor(text)}> { text } </Tag>
    },
    {
        title: '操作',
        dataIndex: 'operationList',
        key: 'operationList',
        render: (operationList: Operation[]) => operationList.map(operation => <Button type="link" href={operation.operationLink}>{operation.operationName}</Button>)
    }
]

export const columns = [
    {
        title: 'id',
        dataIndex: 'id',
        key: 'id'
    },
    {
        title: '姓名',
        dataIndex: 'username',
        key: 'username'
    },
    {
        title: '负责服务器数量',
        dataIndex: 'serverNum',
        key: 'serverNum'
    },
    {
        title: '服务器标签',
        dataIndex: 'serverLabelList',
        key: 'serverLabelList',
        render: (serverLabelList: string[]) => serverLabelList.map(
            (tag: string)=> <Tag color={randomColor(tag)} key={tag}>{tag}</Tag>
        )
    },
    {
        title: '操作',
        dataIndex: 'operationList',
        key: 'action',
        render: (operationList: Operation[]) => operationList.map(operation => {
            if (operation.operationType === 'link') {
                return <Button type="link" href={operation.operationLink}>{translate(operation.operationName)}</Button>
            }

            if (operation.operationType === 'confirm') {
                return <ConfirmModal
                            actionName={operation.operationName}
                            title={operation.operationName}
                            action={ async () => {alert('test')}}
                            content={operation.description!}
                       />
            }
            if (operation.operationType === 'modal') {
                if (operation.operationName === 'recycleServer') {
                    return (
                        <FormModal
                            actionName={operation.operationName}
                            renderForm={(form: FormInstance) => { return <SelectServer form={form} /> }}
                            action={(value: UserServerOperateForm) => alert('回收：' + JSON.stringify(value))}
                        />
                    )
                }
                if (operation.operationName === 'addServer') {
                    return (
                        <FormModal
                            actionName={operation.operationName}
                            renderForm={(form: FormInstance) => { return <SelectServer form={form} /> }}
                            action={(value: UserServerOperateForm) => alert('添加：' + JSON.stringify(value))}
                        />
                    )
                }
            }
            return null

        })
    }
]

export const breadcrumbProps: BreadcrumbPrpos[] = [
    {
        name: '首页',
        link: '/app/index'
    },
    {
        name: '用户管理'
    },
    {
        name: '服务器管理员'
    }
]