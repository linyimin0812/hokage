import { BreadcrumbPrpos } from '../../bread-crumb-custom';
import { Button, Tag } from 'antd';
import React from 'react';
import { randomColor } from '../../../utils';
import { Operation } from '../../../axios/action/user/user-type';

/**
 * @author linyimin
 * @date 2021/2/21 7:12 pm
 * @email linyimin520812@gmail.com
 * @description
 */

export const breadcrumProps: BreadcrumbPrpos[] = [
    {
        name: '首页',
        link: '/app/index',
    },
    {
        name: '用户管理',
    },
    {
        name: '服务器管理员',
    },
];

export const columns = [
    {
        title: 'id',
        dataIndex: 'id',
        key: 'id',
    },
    {
        title: '姓名',
        dataIndex: 'name',
        key: 'name',
    },
    {
        title: '使用服务器数量',
        dataIndex: 'serverNum',
        key: 'serverNum',
    },
    {
        title: '标签',
        dataIndex: 'serverLabelList',
        key: 'serverLabelList',
        render: (serverLabelList: string[]) => {
            return (
                <span>
          			{
                        serverLabelList.map((tag: string) => <Tag color={randomColor(tag)} key={tag}>{tag}</Tag>)
                    }
        </span>);
        },
    },
    {
        title: '操作',
        dataIndex: 'operationList',
        key: 'operationList',
        render: ((operationList: Operation[]) => {
            return (
                <span>
                    {
                        operationList.map((operation: Operation) => <Button type="link" href={operation.operationLink}>{operation.operationName}</Button>)
                    }
                </span>
            )
        })
    },
];

// 嵌套表
export const nestedColumn = [
    {
        title: '主机名',
        dataIndex: 'hostname',
        key: 'hostname',
    },
    {
        title: '域名',
        dataIndex: 'domainName',
        key: 'domainName',
    },
    {
        title: 'IP地址',
        dataIndex: 'IPAddress',
        key: 'IPAddress',
    },
    {
        title: '标签',
        dataIndex: 'serverTags',
        key: 'serverTags',
        render: (serverTags: any, _: any, __: any) => {
            return (
                <span>
        {
            serverTags.map((tag: any) => {
                let color = '';
                let name = '';
                switch (tag) {
                    case 'ordinaryServer':
                        color = 'magenta';
                        name = 'X86';
                        break;
                    case 'gpuServer':
                        color = 'red';
                        name = 'GPU';
                        break;
                    case 'intranetServer':
                        color = 'green';
                        name = '内网';
                        break;
                    case 'publicNetworkServer':
                        color = 'purple';
                        name = '公网';
                        break;
                    default:
                        color = '#f50';
                        name = '未知';
                }
                return (
                    <Tag color={color} key={tag}>
                        {name}
                    </Tag>
                );
            })
        }
      </span>);
        },
    },
    {
        title: '状态',
        dataIndex: 'status',
        key: 'status',
    },
    {
        title: '操作',
        dataIndex: 'action',
        key: 'action',
    },

];