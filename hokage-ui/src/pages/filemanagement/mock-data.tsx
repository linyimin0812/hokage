import { Record } from './file-management'

export const fileDataList: Record[] = [
  {
    key: '/home/linyimin/./',
    fileName: './',
    size: '4096',
    owner: 'linyimin',
    permission: '-rw-r--r--',
    modifiedTime: '20200421 04:21'
  },
  {
    key: '/home/linyimin/../',
    fileName: '../',
    size: '4096',
    owner: 'linyimin',
    permission: '-rw-r--r--',
    modifiedTime: '20200421 04:21'
  },
  {
    key: '/home/linyimin/CHANGELOG.md',
    fileName: 'CHANGELOG.md',
    size: '5441',
    owner: 'root',
    permission: '-rw-r--r--',
    modifiedTime: '20200421 04:21'
  },
  {
    key: '/home/linyimin/public/',
    fileName: 'public/',
    size: '4096',
    owner: 'linyimin',
    permission: '-rw-r--r--',
    modifiedTime: '20200421 04:21',
    children: [
      {
        key: '/home/linyimin/public/images/',
        fileName: 'images/',
        size: '807',
        owner: 'linyimin',
        permission: '-rw-r--r--',
        modifiedTime: '20200421 04:27',
        children: [
          {
            key: '/home/linyimin/public/images/index.html',
            fileName: 'index.html',
            size: '807',
            owner: 'linyimin',
            permission: '-rw-r--r--',
            modifiedTime: '20200421 04:27'
          },
          {
            key: '/home/linyimin/public/images/theme.less',
            fileName: 'theme.less',
            size: '234663',
            owner: 'linyimin',
            permission: '-rw-rw-r--',
            modifiedTime: '20200421 04:21'
          },
        ]
      },
      {
        key: '/home/linyimin/theme.less',
        fileName: 'theme.less',
        size: '234663',
        owner: 'linyimin',
        permission: '-rw-rw-r--',
        modifiedTime: '20200421 04:21'
      },
    ]
  },
]
