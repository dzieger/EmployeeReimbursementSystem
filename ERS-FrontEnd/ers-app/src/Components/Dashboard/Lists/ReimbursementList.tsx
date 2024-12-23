import React, { useState } from "react";
import { Reimbursement } from "../../../Interfaces/Reimbursement";
import { Table } from "react-bootstrap";
import { useAuth } from "../../../Contexts/AuthContext";
import Filter from "./Filter";
import "../Dashboard.css"

interface ReimbursementListProps {
    reimbursements: Reimbursement[]
    onRowClick: (reimbursement: Reimbursement) => void
    onFilterChange: (status: string) => void
    selectedReimbursement: Reimbursement | null
    currentFilter: string
}

const ReimbursementList: React.FC<ReimbursementListProps> = ({ reimbursements, onRowClick, onFilterChange, selectedReimbursement, currentFilter }) => {
    const [filterStatus, setFilterStatus] = useState<string>("All")
    const { userRole }= useAuth()

    const handleFilterChange = (status: string) => {
        setFilterStatus(status)
        onFilterChange(status)
    }



    return (
        <div>
            <div className="reimbursements-header">
                <h2>Reimbursements</h2>
                {/* Status Dropdown */}
                <Filter currentFilter={currentFilter} onFilterChange={handleFilterChange} />
            </div>
            <Table bordered hover>
                <thead className="table-light">
                    <tr>
                        <th>Id</th>
                        <th>Description</th>
                        <th>Amount</th>
                        <th>Status</th>
                        {userRole === "MANAGER" && (
                            <th>Employee Name</th>
                        )}
                    </tr>
                </thead>
                <tbody>
                    {reimbursements
                        .filter((reimbursement) => reimbursement?.user)
                        .map((reimbursement, index) => (
                            <tr key={reimbursement.reimbId} 
                                onClick={() => onRowClick(reimbursement)}
                                style={{ cursor: "pointer"}}
                                className={selectedReimbursement?.reimbId === reimbursement.reimbId ? "selected-row" : ""}
                                >
                                    <td>{reimbursement.reimbId}</td>
                                    <td>{reimbursement.description}</td>
                                    <td>${reimbursement.amount.toFixed(2)}</td>
                                    <td>{reimbursement.status}</td>
                                    {userRole === "MANAGER" && (
                                        <td>{reimbursement.user.firstName} {reimbursement.user.lastName}</td>
                                    )}
                            </tr>
                    ))}
                </tbody>
            </Table>
        </div>
    )
}
export default ReimbursementList