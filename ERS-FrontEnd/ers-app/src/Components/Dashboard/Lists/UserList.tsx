import React from "react";
import { User } from "../../../Interfaces/User";
import { Table } from "react-bootstrap";

interface UserListProps {
    users: User[]
    onRowClick: (user: User) => void
    selectedUser: User | null
}

const UserList: React.FC<UserListProps> = ({ users, onRowClick, selectedUser }) => {
    return (
        <div>
            <div className="users-header">
                <h2>Users</h2>
            </div>
            <Table bordered hover>
                <thead className="table-light">
                    <tr>
                        <th>Id</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Username</th>
                        <th>Total Reimbursements</th>
                    </tr>
                </thead>
                <tbody>
                    {users.map((user, index) => (
                        <tr key={user.userId}
                            onClick={() => onRowClick(user)}
                            style={{ cursor: "pointer" }}
                            className={selectedUser?.userId === user.userId ? "selected-row" : ""}
                        >
                        <td>{index+1}</td>
                        <td>{user.firstName}</td>
                        <td>{user.lastName}</td>
                        <td>{user.username}</td>
                        <td>{user.reimbursements ? user.reimbursements.length : 0}</td>
                        </tr>
                    ))}
                </tbody>
            </Table>
        </div>
    )
}
export default UserList