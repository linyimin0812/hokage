import { FilterConfirmProps, FilterDropdownProps } from 'antd/lib/table/interface'
import { Button, Input, Space } from 'antd'
import { SearchOutlined } from '@ant-design/icons'
import React from 'react'

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
}

export default new TableSearch()
