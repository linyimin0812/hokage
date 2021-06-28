
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
  operatorId: number,
  taskId?: number
}

export interface TaskResultVO {
  batchId: string,
  taskId: number,
  taskName: string,
  taskStatus: number,
  triggerType: number,
  startTime: string,
  endTime: string,
  cost: number,
  resultDetailVOList: TaskResultDetailVO[]
}

export interface TaskResultDetailVO {
  taskId: number,
  serverIp: string,
  startTime: string,
  endTime: string,
  taskStatus: number,
  cost: number,
}
