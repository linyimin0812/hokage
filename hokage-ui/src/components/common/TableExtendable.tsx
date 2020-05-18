import { ReactNode } from 'react'

// 表格嵌套子表配置

export interface TableExtendable {
  expandedRowKeys: string[],
  expandedRowRender: () => ReactNode,
  onExpand: (expanded: boolean, record: any) => void
}