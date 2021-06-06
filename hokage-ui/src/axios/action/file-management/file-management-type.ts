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
  lastAccessTime: string
}

export interface FileOperateForm {
  operatorId: number,
  ip: string,
  sshPort: string,
  account: string,
  curDir: string,
}

export interface FileContentVO {
  name: string,
  type: string,
  content: string,
  curLine: number,
  totalLine: number,
}
