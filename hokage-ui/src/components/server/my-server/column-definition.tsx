import { Button, Tag } from 'antd';
import { randomColor } from '../../../utils';
import React from 'react';
import { Operation } from '../../../axios/action/user/user-type';
import { BreadcrumbPrpos } from '../../bread-crumb-custom';

/**
 * @author linyimin
 * @date 2021/3/13 4:48 下午
 * @email linyimin520812@gmail.com
 * @description
 */
export const columns = [
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
        title: 'ip地址',
        dataIndex: 'ip',
        key: 'ip'
    },
    {
        title: "登录账号",
        dataIndex: "account",
        key: "account"
    },
    {
        title: '服务器状态',
        dataIndex: 'status',
        key: 'status',
        render: (text: string) => text ? <Tag color={randomColor(text)}>{text}</Tag> : null
    },
    {
        title: '我的状态',
        dataIndex: 'myStatus',
        key: 'myStatus',
        render: (text: string) => text ? <Tag color={randomColor(text)}>{text}</Tag> : null
    },
    {
        title: "备注",
        dataIndex: "description",
        key: "description"
    },
    {
        title: '操作',
        dataIndex: 'operationList',
        key: 'operationList',
        render: (operationList: Operation[]) => operationList.map(operation => <Button type="link" href={operation.operationLink}>{operation.operationName}</Button>)
    }
]

export const breadcrumbProps: BreadcrumbPrpos[] = [
    {
        name: '首页',
        link: '/app/index'
    },
    {
        name: '我的服务器'
    },
    {
        name: '我使用的服务器'
    }
]