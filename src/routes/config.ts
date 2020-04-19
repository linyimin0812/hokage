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
        { key: '/app/index', title: '首页', icon: 'mobile', component: 'Dashboard' },
        {
            key: '/app/user',
            title: '用户管理',
            icon: 'user',
            subs: [
                { key: '/app/user/operator', title: '服务器管理员', component: 'Buttons' },
                { key: '/app/user/ordinary', title: '服务器使用者', component: 'Icons' }
            ],
        },
        {
            key: '/app/server',
            title: '我的服务器',
            icon: 'wallet',
            subs: [
                {
                    key: '/app/server/manage',
                    title: '我管理的服务器',
                    component: 'BasicAnimations',
                },
                {
                    key: '/app/server/use',
                    title: '我使用的服务器',
                    component: 'ExampleAnimations',
                },
            ],
        },
        {
            key: '/app/file',
            title: '文件管理',
            icon: 'copy',
        },
        {
            key: '/app/monitor',
            title: '资源监控',
            icon: 'edit',
            subs: [
              { 
                key: '/app/monitor/server', 
                title: '服务器资源监控', 
                component: 'BasicForm' 
              },
              { 
                key: '/app/monitor/process', 
                title: '进程监控', 
                component: 'BasicForm' 
              },
            ],
        },
    ],
    others: [], // 非菜单相关路由
};

export default menus;
