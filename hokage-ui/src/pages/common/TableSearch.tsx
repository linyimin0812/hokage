import { FilterConfirmProps, FilterDropdownProps } from 'antd/lib/table/interface'
import { Button, Input, Space, Tooltip } from 'antd'
import { SearchOutlined } from '@ant-design/icons'
import React from 'react'
import Highlighter from 'react-highlight-words'

class TableSearch {

  private searchInput: Input | null = null
  public searchText: React.Key = ''
  filterDropdown = (prop: FilterDropdownProps) => (
    <div style={{ padding: 8 }}>
      <Input
        ref={(node) => this.searchInput = node}
        placeholder={`Search name`}
        value={prop.selectedKeys[0]}
        onChange={(e) =>
          prop.setSelectedKeys(e.target.value ? [e.target.value] : [])
        }
        onPressEnter={() =>
          this.handleSearch(prop.selectedKeys, prop.confirm)
        }
        style={{ marginBottom: 8, display: "block" }}
      />
      <Space>
        <Button type="primary" onClick={() => this.handleSearch(prop.selectedKeys, prop.confirm)} icon={<SearchOutlined translate />} size="small" style={{ width: 90 }}>
          Search
        </Button>
        <Button id={'file-search-reset'} onClick={() => this.handleReset(prop.clearFilters)} size="small" style={{ width: 90 }}>
          Reset
        </Button>
      </Space>
    </div>
  )

  filterIcon = (filtered: boolean) => (
    <SearchOutlined translate style={{ color: filtered ? '#1890ff' : '#000000' }} />
  )

  onFilterDropdownVisibleChange = (visible: boolean) => {
    if (visible) {
      setTimeout(() => this.searchInput!.select(), 100)
    }
  }

  handleSearch = (selectedKeys: React.Key[], confirm: (param?: FilterConfirmProps | undefined) => void) => {
    confirm();
    this.searchText = selectedKeys[0]
  }

  handleReset = (clearFilters: (() => void) | undefined) => {
    if (clearFilters) {
      clearFilters()
    }
    this.searchText = ''
  }

  renderHighLight = (textHighLight: string, text: string) => {
    const highLightComponent = (
      <Highlighter
        highlightStyle={{ backgroundColor: "#ffc069", padding: 0 }}
        searchWords={[this.searchText as string]}
        autoEscape
        textToHighlight={textHighLight}
      />
    )
    return (
      <Tooltip placement={'topLeft'} title={text}>
        <span>{highLightComponent}</span>
      </Tooltip>
    )

  }
}

export default new TableSearch()
