export type MetaData = {
  total: number,
  groupInfo: {[key: string]: number}
}

export type HomeDetailVO = {
  allVO: MetaData,
  availableVO: MetaData,
  accountVO: MetaData
}
