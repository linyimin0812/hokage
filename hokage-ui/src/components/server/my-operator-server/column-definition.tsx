import { Button, Tag } from 'antd';
import React from 'react';
import { BreadcrumbPrpos } from '../../bread-crumb-custom';
import { randomColor } from '../../../utils';
import { Operation } from '../../../axios/action/user/user-type';

/**
 * @author linyimin
 * @date 2021/3/1 10:01 下午
 * @email linyimin520812@gmail.com
 * @description
 */
// 嵌套表
export const nestedColumns = [
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
        title: '用户名', // 服务器登录用户名
        dataIndex: 'account',
        key: 'account'
    },
    {
        title: '申请时间',
        dataIndex: 'applyTime',
        key: 'applyTime'
    },
    {
        title: '最近登录时间',
        dataIndex: 'lastLoginTime',
        key: 'lastLoginTime'
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
        title: '分组',
        dataIndex: 'serverGroupList',
        key: 'serverGroupList',
        render: (serverGroupList: string[]) => serverGroupList.map((group, index) => <Tag color={randomColor(group)} key={index}>{group}</Tag>)
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
        render: (text: string) => text ? <Tag color={randomColor(text)}> {text} </Tag> : null
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
        name: '我管理的服务器'
    }
]