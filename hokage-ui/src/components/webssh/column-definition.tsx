import { BreadcrumbPrpos } from '../bread-crumb-custom'
import { Button, Divider, Tag } from 'antd'
import React from 'react'
import { randomColor } from '../../utils'
import { Operation } from '../../axios/action/user/user-type'
import { ServerVO } from '../../axios/action/server/server-type'

/**
 * @author linyimin
 * @date 2021/3/14 1:51 pm
 * @email linyimin520812@gmail.com
 * @description
 */

export const breadcrumbProps: BreadcrumbPrpos[] = [
    {
        name: '首页',
        link: '/app/index'
    },
    {
        name: 'Web终端'
    }
]

export const column = [
    {
        title: 'ip',
        dataIndex: 'ip',
        key: 'ip'
    },
    {
        title: '账号',
        dataIndex: 'account',
        key: 'account'
    },
    {
        title: '登录方式',
        dataIndex: 'loginType',
        keyIndex: 'loginType'
    },
    {
        title: '我的状态',
        dataIndex: 'status',
        key: 'status',
        render: (text: string) => text ? <Tag color={randomColor(text)}> {text} </Tag> : null
    },
    {
        title: '备注',
        dataIndex: 'description',
        key: 'description'
    },
    {
        title: '操作',
        dataIndex: 'operationList',
        key: 'operationList',
        render: (operationList: Operation[], record: ServerVO) => operationList.map((operation, index) => {
            const components: JSX.Element[] = []
            if (operation.operationType === 'link') {
                components.push(<Button type="link" href={operation.operationLink}>{operation.operationName}</Button>)
            }

            if (operation.operationType === 'action') {
                components.push(
                    <Button
                        type="link"
                        onClick={
                            () => { operation.operationAction && operation.operationAction(record)}
                        }
                    >
                        {operation.operationName}
                    </Button>
                )
            }

            if (['modal', 'confirm'].includes(operation.operationType)) {
                components.push(<Button type="link" href={operation.operationLink}>{operation.operationName}</Button>)
            }

            if (index < operationList.length - 1) {
                components.push(<Divider type="vertical" style={{ color: '#000000' }} />)
            }

            return components.map(component => component);
        })
    }
]