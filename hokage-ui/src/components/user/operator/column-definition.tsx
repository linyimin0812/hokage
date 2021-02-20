import { Button, Tag } from 'antd';
import { hashCode } from '../../../utils'
import React from 'react'
import { Models } from '../../../utils/model'
import { BreadcrumbPrpos } from '../../bread-crumb-custom'
import { Operation } from '../../../axios/action/user/user-type';

/**
 * @author linyimin
 * @date 2021/2/19 12:34 am
 * @email linyimin520812@gmail.com
 * @description
 */

const serverLabelColors = Models.get('serverLabelColor')

export const nestedColumn = [
    {
        title: '主机名',
        dataIndex: 'hostname',
        key: 'hostname'
    },
    {
        title: '域名',
        dataIndex: 'domainName',
        key: 'domainName'
    },
    {
        title: '标签',
        dataIndex: 'serverTags',
        key: 'serverTags',
        render: (serverTags: any, _: any, __: any) => {
            return serverTags.map((tag: any )=> <Tag color={serverLabelColors[hashCode(tag) % serverTags.length]} key={tag}>{tag}</Tag>)
        }
    },
    {
        title: '使用人数',
        dataIndex: 'numberOfUser',
        key: 'numOfUser'
    },
    {
        title: '状态',
        dataIndex: 'status',
        key: 'status',
        render: (text: string, _: any, __: any) => <Tag color = {serverLabelColors[hashCode(text) % serverLabelColors.length]}> { text } </Tag>
    },
    {
        title: '操作',
        dataIndex: 'action',
        key: 'action'
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
        dataIndex: 'serverLabel',
        key: 'serverLabel',
        render: (serverLabel: string[]) => serverLabel.map(
            (tag: string)=> <Tag color={serverLabelColors[hashCode(tag) % serverLabel.length]} key={tag}>{tag}</Tag>
        )
    },
    {
        title: '操作',
        dataIndex: 'operationList',
        key: 'action',
        render: (operationList: Operation[]) => operationList.map(operation => <Button type="link" href={operation.operationLink}>{operation.operationName}</Button>)
    }
]

export const breadcrumProps: BreadcrumbPrpos[] = [
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