import React from "react";
import { useAuth } from "../../Contexts/AuthContext";
import { Button } from "react-bootstrap";

interface SidebarMenuProps {
    onAllReimbursements: () => void
    onMyReimbursements: () => void
    onNewReimbursement: () => void
    onAllUsers: () => void
}

const SidebarMenu: React.FC<SidebarMenuProps> = ({
        onAllReimbursements,
        onMyReimbursements,
        onNewReimbursement,
        onAllUsers
    }) => {
        const { userRole } = useAuth()

        return (
            <div>
                <div className="d-grid gap-1">
                    <h4>Welcome, {localStorage.getItem("firstName")}</h4>
                    {userRole === "MANAGER" && (
                        <Button variant="outline-primary" className="mt-2" onClick={onAllReimbursements}>All Reimbursements</Button>
                    )}
                    <Button variant="outline-primary" className="mt-2" onClick={onMyReimbursements}>My Reimbursements</Button>
                    <Button variant="outline-primary" className="mt-2" onClick={onNewReimbursement}>New Reimbursement</Button>
                </div>
                <br />
                <div>
                    {userRole === "MANAGER" && (
                        <div className="d-grid gap-1">
                            <h5>Users</h5>
                            <Button variant="outline-primary" className="mt-2" onClick={onAllUsers}>All Users</Button>
                        </div>
                    )}
                </div>
            </div>
        )

}

export default SidebarMenu