
export interface BatCommandVO {
  id?: number,
  operatorId: number,
  taskType: number,
  taskName: string,
  execType: number,
  execTime: string,
  execServers: number[],
  execServerList?: string []
  execCommand: string,
  status?: number
}

export type FixedDateTaskForm = BatCommandVO

export interface BatCommandOperateForm {
  operatorId: number
}
