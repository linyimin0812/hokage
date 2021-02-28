import { Button, Tag } from 'antd';
import React from 'react'
import { randomColor } from '../../../utils'
import { Operation } from '../../../axios/action/user/user-type'
import { BreadcrumbPrpos } from '../../bread-crumb-custom';

/**
 * @author linyimin
 * @date 2021/2/28 11:55 am
 * @email linyimin520812@gmail.com
 * @description
 */
export const columns = [
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
        title: 'IP地址',
        dataIndex: 'ip',
        key: 'ip'
    },
    {
        title: '分组',
        dataIndex: 'serverGroupList',
        key: 'serverGroupList',
        render: (serverGroupList: string[]) => serverGroupList.map((group, index) => <Tag color={randomColor(group)} key={index}>{group}</Tag>)
    },
    {
        title: '标签',
        dataIndex: 'labels',
        key: 'labels',
        render: (labels: string[]) => labels.map((label, index) => <Tag color={randomColor(label)} key={index}>{label}</Tag>)
    },
    {
        title: '管理员',
        dataIndex: 'supervisorList',
        key: 'supervisorList',
        render: (supervisorList: string[]) => supervisorList.map((label, index) => <Tag color={randomColor(label)} key={index}>{label}</Tag>)
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
        render: (text: string) => <Tag color = {text}> { text } </Tag>
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
        name: '所有的服务器'
    }
]