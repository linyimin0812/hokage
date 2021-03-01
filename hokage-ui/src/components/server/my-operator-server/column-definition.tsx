import { Tag } from 'antd';
import React from 'react';
import { BreadcrumbPrpos } from '../../bread-crumb-custom';

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
        dataIndex: 'name',
        key: 'name'
    },
    {
        title: '用户名', // 服务器登录用户名
        dataIndex: 'loginName',
        key: 'loginName'
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
        dataIndex: 'action',
        key: 'action'
    }
]

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
        title: 'ip地址',
        dataIndex: 'ipAddress',
        key: 'ipAddress'
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
                  let color = ''
                  let name = ''
                  switch (tag) {
                      case 'ordinaryServer':
                          color = 'magenta'
                          name = 'X86'
                          break
                      case 'gpuServer':
                          color = 'red'
                          name = 'GPU'
                          break
                      case 'intranetServer':
                          color = 'green'
                          name = '内网'
                          break
                      case 'publicNetworkServer':
                          color = 'purple'
                          name = '公网'
                          break
                      default:
                          color = '#f50'
                          name = '未知'
                  }
                  return (
                      <Tag color={color} key={tag}>
                          {name}
                      </Tag>
                  );
              })
          }
        </span>)
        }
    },
    {
        title: '使用人数',
        dataIndex: 'numOfUser',
        key: 'numOfUser'
    },
    {
        title: '状态',
        dataIndex: 'status',
        key: 'status',
        render: (text: string, _: any, __: any) => {
            let color: string = ''
            switch (text) {
                case '在线':
                    color = 'green'
                    break;
                case '掉线':
                    color = 'red'
                    break
                default:
                    color = 'red'
                    break
            }
            return (
                <Tag color={color}> {text} </Tag>
            )
        }
    },
    {
        title: '操作',
        dataIndex: 'action',
        key: 'action'
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