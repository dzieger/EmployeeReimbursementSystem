import { useEffect, useState } from "react"
import { Dropdown, DropdownButton } from "react-bootstrap"

interface FilterProps {
    currentFilter: string
    onFilterChange: (status: string) => void
}

const Filter: React.FC<FilterProps> = ({ currentFilter, onFilterChange }) => {
    const [selectedFilter, setSelectedFilter] = useState<string>(currentFilter)

    useEffect(() => {
        setSelectedFilter(currentFilter)
        
    }, [currentFilter])

    const handleSelect = (status: string) => {
        setSelectedFilter(status)
        onFilterChange(status)
    }

    return (
        <div className="filter-menu">
            {/* Status Dropdown */}
            <div className="dropdown-container">
                <DropdownButton
                    id="status-filter"
                    title={`Status: ${selectedFilter}`}
                    onSelect={(status) => handleSelect(status!)}
                >
                    <Dropdown.Item eventKey={"PENDING"}>Pending</Dropdown.Item>
                    <Dropdown.Item eventKey={"APPROVED"}>Approved</Dropdown.Item>
                    <Dropdown.Item eventKey={"DENIED"}>Denied</Dropdown.Item>
                    <Dropdown.Item eventKey={"All"}>All</Dropdown.Item>
                </DropdownButton>
            </div>
        </div>
    )
}

export default Filter