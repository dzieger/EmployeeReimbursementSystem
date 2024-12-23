import React, { useEffect, useState } from "react"
import { Button, Col, Container, Dropdown, Row } from "react-bootstrap"
import { useAuth } from "../../Contexts/AuthContext"
import { useNavigate } from "react-router-dom"
import { Form } from "react-bootstrap"
import SidebarMenu from "./SidebarMenu"
import { Reimbursement } from "../../Interfaces/Reimbursement"
import { User } from "../../Interfaces/User"
import axios from "axios"
import ReimbursementList from "./Lists/ReimbursementList"
import UserList from "./Lists/UserList"
import ActionsMenu from "./ActionsMenu"

type DashboardProps = {

}

const Dashboard: React.FC<DashboardProps> = ({}) => {
    // Base state variables
    const { userRole, isLoggedIn, userId, sessionId } = useAuth()
    const [currentUserId, setCurrentUserId] = useState<number>(localStorage.getItem("userId") ? Number(localStorage.getItem("userId")) : 0)
    const { setIsLoggedIn } = useAuth ()
    const navigate = useNavigate()
    const [error, setError] = useState<string>("")
    const endpointHead = `http://localhost:4444`

    // Viewport varibales
    const [showNewReimbursementForm, setShowNewReimbursementForm] = useState<boolean>(false)
    const [viewUserOrReimbursement, setViewUserOrReimbursement] = useState<"USER" | "REIMBURSEMENT">("REIMBURSEMENT")
    const [viewMode, setViewMode] = useState<"ALL" | "OWN">("ALL")
    const [showDeleteUser, setShowDeleteUser] = useState<boolean>(false)

    // Reimbursement variables
    const [reimbursements, setReimbursements] = useState<Reimbursement[]>([])
    const [selectedReimbursement, setSelectedReimbursement] = useState<Reimbursement | null>(null)
    const [selectedUserId, setSelectedUserId] = useState<number | null>(null)

    // Reimbursement modifier variable
    const [description, setDescription] = useState<string>("")
    const [amount, setAmount] = useState<number>(0)
    const [isEditing, setIsEditing] = useState<boolean>(false)

    // Other variables related to Reimbursements
    const [filterStatus, setFilterStatus] = useState<string>("PENDING")
    const [isLoading, setIsLoading] = useState<boolean>(false)

    // User varibles
    const [users, setUsers] = useState<User[]>([])
    const [selectedUser, setSelectedUser] = useState<User | null>(null)

    // Login / Logout / Navigate Functions
    // Do I really need it if I'm not doing a whole lot of navigating
    useEffect(() => {
        if (!isLoggedIn) {
            navigate("/")
        }
    }, [isLoggedIn, navigate])

    const headHome = () => {
        if (!isLoggedIn) {
            navigate("/")
        }
    }


    const handleLogout = async () => {
        await axios.post("http://localhost:4444/auth/logout", {
            sessionId
        })
        localStorage.removeItem("userId")
        localStorage.removeItem("firstName")
        localStorage.removeItem("lastName")
        localStorage.removeItem("username")
        localStorage.removeItem("useRole")
        localStorage.setItem("isLoggedIn", "false")
        setIsLoggedIn(false)
    }

    // useEffect to better handle state changes through button clicks
    useEffect(() => {
        if (viewUserOrReimbursement === "REIMBURSEMENT") {
            handleGetReimbursements(filterStatus)
        } else {
            handleGetAllUsers()
        }
    }, [filterStatus, viewMode, viewUserOrReimbursement])

    // Reimbursement Functions
    const handleGetReimbursements = async (status: string) => {
        setIsLoading(true)
        setViewUserOrReimbursement("REIMBURSEMENT")
        setSelectedReimbursement(null)
        // console.log("Starting handle get reimbursements for selectedUser", selectedUser)
        // console.log("Starting handle get reimbursements for current User", currentUserId)
        try {
            let endpoint = ""
            if (viewMode === "ALL" && userRole === "MANAGER" && !selectedUser) {
                endpoint = status === "All"
                    ? endpointHead + `/reimbursements/all`
                    : endpointHead + `/reimbursements/all/status/${status}`
            } else if (userRole === "MANAGER" && selectedUser && selectedUser?.userId !== userId) {
                console.log("Inside else if statement: ", selectedUser?.userId)
                endpoint = status === "All"
                    ? endpointHead + `/reimbursements/userId/${selectedUser?.userId}/all`
                    : endpointHead + `/reimbursements/userId/${selectedUser?.userId}/status/${status}`
            } else {
                endpoint = status === "All"
                    ? endpointHead + `/reimbursements/userId/${currentUserId}/all`
                    : endpointHead + `/reimbursements/userId/${currentUserId}/status/${status}`
            }

            const response = await axios.get(endpoint, {
                withCredentials: true
            })
            const data = Array.isArray(response.data) ? response.data : []
            setReimbursements(data)
        } catch (error : any) {
            setError("No Reimbursement" + error.response.data)
            setReimbursements([])
        } finally {
            setIsLoading(false)
        }

    }

    // Row click for a Reimbursement
    const handleRowClick = (reimbursement: Reimbursement) => {
        setSelectedReimbursement(reimbursement)
        setShowNewReimbursementForm(false)
        setIsEditing(false)
        setShowDeleteUser(false)
    }

    // User function
    const handleGetAllUsers = async () => {
        try {
            const params = { userId }
            const response = await axios.get(endpointHead + `/users/all`, { params })
            const data = Array.isArray(response.data) ? response.data : []
            setUsers(data)
            setViewUserOrReimbursement("USER")
            setSelectedReimbursement(null)
        } catch (error: any) {
            setError("No Users" + error.response.data)
        }
    }

    // Row click for a User
    const handleUserRowClick = (user: User) => {
        setSelectedUser(user)
        setShowNewReimbursementForm(false)
        setIsEditing(false)
        setShowDeleteUser(false)
    }

    // Action Menu functions
    // Used to delete a reimbursement or a User
    const handleDelete = async () => {
        if (viewUserOrReimbursement === "USER") {
            try {
                await axios.delete(`http://localhost:4444/users/userId/${selectedUser?.userId}/delete`)

                handleGetAllUsers()
                setSelectedUser(null)
                setShowDeleteUser(false)
            } catch (error: any) {
                alert("Failed to delete user: " + error.response.data)
                setError("Unable to delete" + error.response.data)
            }
        }

    }


    //  Used to resolve a riembursement
    const handleResolve = async(status: string) => {
        try {
            await axios.patch(`http://localhost:4444/reimbursements/reimbId/${selectedReimbursement?.reimbId}/resolve`,
                { status }
            )
            handleGetReimbursements(filterStatus)
            setSelectedReimbursement(null)
        } catch (error: any) {
            setError("Failed to resolve reimbursement" + error.response.data)
        }

    }

    // Update the contents of a reimbursement
    const handleUpdate = async (updatedReimbursement:Partial<Reimbursement>) => {
        try {
            await axios.patch(`http://localhost:4444/reimbursements/reimbId/${selectedReimbursement?.reimbId}/update`,
                updatedReimbursement
            )
            setSelectedReimbursement(null)
            handleGetReimbursements(filterStatus)
        } catch (error: any) {
            setError("Failed to update reimbursement" + error.response.data)
        }
    }

    // View the reimbursements of a selected user
    const handleViewReimbursements = async () => {
        if (selectedUser) {
            setViewMode("ALL")
            setFilterStatus("All")
            handleGetReimbursements("All")
        }
    }

    // Handle submit for new reimbursement form
    const handleSubmit = async () => {
        console.log(description)
        console.log(amount)
        try {
            const newReimbursement = { description, amount}
            console.log(newReimbursement)
            await axios.post(endpointHead + `/reimbursements/userId/${userId}/create`,
                newReimbursement)
            handleGetReimbursements("All")
            setShowNewReimbursementForm(false)
            setIsEditing(false)
            setDescription("")
            setAmount(0)
        } catch (error: any) {
            setError("Failed to create Reimbursement" + error.response.data)
        }
    }
    
    // Handle cancel for reimbursement form
    const handleCancel = () => {
        setShowNewReimbursementForm(false)
        setIsEditing(false)
        setDescription("")
        setAmount(0)
        setShowDeleteUser(false)
    }

    const handleShowUpdateReimbursementsOptions = () => {
        setIsEditing(true)
    }

    // Promotes the user to manager
    const handlePromoteUser = async () => {
        try {
            await axios.patch(`http://localhost:4444/users/userId/${selectedUser?.userId}/promote`)
            setIsEditing(false)
            setSelectedUser(null)
            handleGetAllUsers
        } catch (error: any) {
            setError("Unable to promote user" + error.response.data)
        }
    }

    const populate = async () => {
        try {
            await axios.post(`http://localhost:4444/data/populate`)
            handleGetReimbursements("All")
        } catch (error: any) {
            setError("Failed to populate data" + error.response.data)
        }
    }

    return (
        <Container>
            <Row className="dashboard-header">
                <Col xs={3} className="text-start">
                    <button
                        className="dashboard-title"
                        onClick={headHome}
                    >
                        <h3>Dashboard</h3>
                    </button>
                </Col>
                <Col xs={6}></Col>
                <Col xs={3} className="text-end">
                    <Dropdown align="end">
                        <Dropdown.Toggle variant="outlinePrimary" id="dropdown-basic">View/Edit Account</Dropdown.Toggle>

                        <Dropdown.Menu>
                            <Dropdown.Item href="/edit-account">Edit Account</Dropdown.Item>
                            <Dropdown.Item onClick={handleLogout}>Logout</Dropdown.Item>
                        </Dropdown.Menu>
                    </Dropdown>
                </Col>
            </Row>

            <Row className="dashboard-main">
                <Col xs={2} className="dashboard-sidebar">
                {isLoading ? <p>Loading...</p> : 
                    <SidebarMenu
                        onAllReimbursements={() => {
                            setSelectedUser(null)
                            setSelectedReimbursement(null)
                            setViewMode("ALL")
                            setFilterStatus("All")
                            setViewUserOrReimbursement("REIMBURSEMENT")
                        }}
                        onMyReimbursements={() => {
                            setSelectedUser(null)
                            setSelectedReimbursement(null)
                            setViewMode("OWN")
                            setFilterStatus("PENDING")
                            setViewUserOrReimbursement("REIMBURSEMENT")
                        }}
                        onNewReimbursement={() => setShowNewReimbursementForm(true)}
                        onAllUsers={() => {
                            setViewUserOrReimbursement("USER")
                        }}
                    />
                }
                </Col>
                <Col xs={7} className="dashboard-center">
                    <div>
                        {viewUserOrReimbursement === "REIMBURSEMENT" ? (
                            <ReimbursementList 
                                reimbursements={reimbursements} 
                                onRowClick={handleRowClick}
                                onFilterChange={setFilterStatus}
                                selectedReimbursement={selectedReimbursement}
                                currentFilter={filterStatus}
                            />
                        ) : (
                            <UserList
                                users={users}
                                onRowClick={handleUserRowClick}
                                selectedUser={selectedUser}
                            />
                        )}
                    </div>
                </Col>
                <Col xs={3} className="dashboard-actions">
                    {showNewReimbursementForm ? (
                        <div>
                            <h5>Create New Reimbursement</h5>
                            <Form.Group className="mb-2">
                                <Form.Label>Description</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder="Description"
                                    value={description}
                                    onChange={(e) => setDescription(e.target.value)}
                                />
                            </Form.Group>
                            <Form.Group className="mb-2">
                                <Form.Label>Amount</Form.Label>
                                <Form.Control
                                    type="number"
                                    placeholder="Amount"
                                    value={amount}
                                    onChange={(e) => setAmount(Number(e.target.value))}
                                />
                            </Form.Group>
                            <Button variant="primary" className="mb-2" onClick={handleSubmit}>Submit</Button>
                            <Button variant="warning" className="mb-2" onClick={handleCancel}>Cancel</Button>
                        </div>
                    ) : (
                        <ActionsMenu
                            selectedReimbursement={selectedReimbursement}
                            selectedUser={selectedUser}
                            viewUserOrReimbursement={viewUserOrReimbursement}
                            isEditing={isEditing}
                            showDeleteUser={showDeleteUser}
                            onResolve={handleResolve}
                            onDelete={handleDelete}
                            onUpdate={handleUpdate}
                            onViewReimbursements={handleViewReimbursements}
                            onCancel={handleCancel}
                            onUpdateReimbursementStatus={handleShowUpdateReimbursementsOptions}
                            onEditUser={() => setShowDeleteUser(true)}
                            onPromote={handlePromoteUser}
                            onDemote={() => console.log("Not yet implemented")}
                        />
                    )}
                </Col>
            </Row>
            <Row className="dashboard-footer">
                <Col xs={2}>
                    <Button variant = "secondary" onClick={populate}>Populate</Button>
                </Col>
            </Row>
        </Container>
    )
}

export default Dashboard