export interface FileVO {
  key: string,
  name: string,
  owner: string,
  group: string,
  typeAndPermission: string,
  size: number,
  lastAccessTime: string
}

export interface FileOperateForm {
  operatorId: number,
  ip: string,
  sshPort: string,
  account: string,
  curDir: string,
}
