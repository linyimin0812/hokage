export interface FileVO {
  curDir: string,
  filePropertyList: FileProperty[],
  directoryNum: number,
  fileNum: number,
  totalSize: string
}

export interface FileProperty {
  key: string,
  name: string,
  owner: string,
  group: string,
  type: 'directory' | 'file',
  permission: string,
  size: string,
  lastAccessTime: string,
  curDir: string
}

export interface FileOperateForm {
  operatorId: number,
  ip: string,
  sshPort: string,
  account: string,
  path: string,
  page?: number,
  dst?: string,
  permission?: string
}

export interface FileContentVO {
  name: string,
  curDir: string,
  extension?: string,
  content?: string,
  perPageLine?: number,
  totalLine?: number,
}
