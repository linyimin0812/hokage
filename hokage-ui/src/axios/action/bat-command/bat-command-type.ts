
export interface FixedDateTaskVO {
  id?: number,
  operatorId: number,
  taskType: number,
  taskName: string,
  execType: number,
  execTime: string,
  execServers: number[],
  execCommand: string
}

export type FixedDateTaskForm = FixedDateTaskVO
