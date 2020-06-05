export interface IFMenuBase {
    key: string;
    title: string;
    icon?: string;
    component?: string;
    query?: string;
    requireAuth?: string;
    route?: string;
    /** 是否登录校验，true不进行校验（访客） */
    login?: boolean;
}

export interface IFMenu extends IFMenuBase {
    subs?: IFMenu[];
}

const menus: {
    menus: IFMenu[];
    others: IFMenu[] | [];
    [index: string]: any;
} = {
    menus: [
        // 菜单相关路由
        { key: '/app/index', title: '首页', icon: 'home', component: 'Home' },
        {
            key: '/app/user',
            title: '用户管理',
            icon: 'team',
            subs: [
                { key: '/app/user/operator', title: '服务器管理员', icon: 'user', component: 'Operator' },
                { key: '/app/user/ordinary', title: '服务器使用者', icon: 'team', component: 'OrdinaryUser' }
            ],
        },
        {
            key: '/app/server',
            title: '我的服务器',
            icon: 'hdd',
            subs: [
                {
                  key: '/app/server/all',
                    title: '所有的服务器',
                    icon: 'laptop',
                    component: 'AllServers',
                },
                {
                    key: '/app/server/manage',
                    title: '我管理的服务器',
                    icon: 'desktop',
                    component: 'MyOperateServer',
                },
                {
                    key: '/app/server/use',
                    title: '我使用的服务器',
                    icon: 'tablet',  
                    component: 'MyServer',
                },
            ],
        },
        {
          key: '/app/webssh',
          title: 'Web终端',
          icon: 'code',
          component: 'WebSshHome'
        },
        {
            key: '/app/file',
            title: '文件管理',
            icon: 'folder',
            component: 'FileManagementHome'
        },
        {
          key: '/app/bat',
          title: '批量命令',
          icon: 'code',
          component: 'BatCommandHome'
        },
        {
          key: '/app/security',
          title: '安全组',
          icon: 'safety',
          component: 'SecurityGroupHome'
        },
        {
            key: '/app/monitor',
            title: '资源监控',
            icon: 'picture',
            subs: [
              { 
                key: '/app/monitor/server', 
                title: '服务器资源监控', 
                icon: 'eye',
                component: 'ServerResourceManagement' 
              },
              { 
                key: '/app/monitor/process', 
                title: '进程监控', 
                icon: 'line-chart',
                component: 'ProcessManagement' 
              },
            ],
        },
        {
          key: '/app/penetration',
          title: '内网穿透',
          icon: 'cloud',
          component: 'InverseNetworkProxy',
        },
    ],
    others: [], // 非菜单相关路由
};

export default menus;
