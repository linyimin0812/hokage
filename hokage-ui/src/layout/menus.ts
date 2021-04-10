export interface MenusType {
    icon: string,
    title: string,
    auth: string,
    path?: string,
    child?: MenusType[]
}

export const menus: MenusType[] = [
    { icon: 'home', title: '首页', auth: 'home.view', path: '/app/index'},
    {
        icon: 'team', title: '用户管理', auth: 'app.user.operator|app.user.ordinary', child: [
            { icon: 'user', title: '服务器管理员', auth: 'app.user.operator', path: '/app/user/operator' },
            { icon: 'team', title: '服务器使用者', auth: 'app.user.ordinary', path: '/app/user/ordinary' },
        ]
    },
    {
        icon: 'hdd', title: '我的服务器', auth: 'app.server.all|app.server.operator|app.server.my', child: [
            { icon: 'laptop', title: '所有服务器', auth: 'app.server.all', path: '/app/server/all' },
            { icon: 'laptop', title: '我管理的服务器', auth: 'app.server.operator', path: '/app/server/operator' },
            { icon: 'laptop', title: '我使用的服务器', auth: 'app.server.my', path: '/app/server/my' },
        ]
    },
    { icon: 'code', title: 'Web终端', auth: 'app.web.ssh', path: '/app/web/ssh' },
    { icon: 'folder', title: '文件管理', auth: 'app.web.file', path: '/app/web/file' },
    { icon: 'code', title: '批量任务', auth: 'app.web.task', path: '/app/web/task' },
    { icon: 'safety', title: '安全组', auth: 'app.server.security', path: '/app/server/security' },
    { icon: 'picture', title: '资源监控', auth: 'app.server.monitor', path: '/app/server/monitor'},
    { icon: 'cloud', title: '内网穿透', auth: 'app.server.network.penetration', path: '/app/server/network/penetration' }

]
