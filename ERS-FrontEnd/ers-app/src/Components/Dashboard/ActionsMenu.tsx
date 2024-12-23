import React, { useState } from "react";
import { useAuth } from "../../Contexts/AuthContext";
import { Reimbursement } from "../../Interfaces/Reimbursement";
import { User } from "../../Interfaces/User";
import { Button, Col, Form, Row } from "react-bootstrap";

interface ActionsMenuProps {
    selectedReimbursement: Reimbursement | null
    selectedUser: User | null
    viewUserOrReimbursement: "REIMBURSEMENT" | "USER"
    isEditing: boolean
    showDeleteUser: boolean
    onResolve: (status: string) => void
    onDelete: () => void
    onUpdate: (updatedReimbursement: Partial<Reimbursement>) => void
    onViewReimbursements: () => void
    onCancel: () => void
    onUpdateReimbursementStatus: () => void
    onEditUser: () => void
    onPromote: () => void
    onDemote: () => void
}

const ActionsMenu: React.FC<ActionsMenuProps> = ({ 
        selectedReimbursement,
        selectedUser,
        viewUserOrReimbursement,
        isEditing,
        showDeleteUser,
        onResolve,
        onDelete,
        onUpdate,
        onViewReimbursements,
        onCancel,
        onUpdateReimbursementStatus,
        onEditUser,
        onPromote,
        onDemote
    }) => {
    const { userRole } = useAuth()
    const [updatedDescription, setUpdatedDescription] = useState<string>(selectedReimbursement?.description || "")
    // const [updatedAmount, setUpdatedAmount] = useState<number>(selectedReimbursement?.amount || 0)
    const { userId } = useAuth()

    const saveValue = () => {
        if (updatedDescription) {
            const updatedReimbursement = {
                description: updatedDescription,
                // amount: Number(updatedAmount)
            }
            onUpdate(updatedReimbursement)
        } else {
            alert("Description cannot be empty")
        }
    }

    return (

        <div>
            {viewUserOrReimbursement === "REIMBURSEMENT" && !selectedReimbursement && (
                <p>Select a reimbursement to view details.</p>
            )}

            {viewUserOrReimbursement === "USER" && !selectedUser && (
                <p>Select a user for more information</p>
            )}
            {/* Reimbursement Details */}
            {selectedReimbursement && viewUserOrReimbursement === "REIMBURSEMENT" && (
                <div>
                    <h5>Reimbursement Details</h5>
                    {isEditing && selectedReimbursement.user.userId === userId ? (
                        <>
                            <Form.Group className="mb-2">
                                <Form.Label>Description</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder={selectedReimbursement.description}
                                    value={updatedDescription}
                                    onChange={(e) => setUpdatedDescription(e.target.value)}
                                />
                            </Form.Group>
                            {/* <Form.Group className="mb-2">
                                <Form.Label>Amount</Form.Label>
                                <Form.Control
                                    type="number"
                                    placeholder={selectedReimbursement.amount.toString()}
                                    value={updatedAmount}
                                    onChange={(e) => setUpdatedAmount(Number(e.target.value))}
                                />
                            </Form.Group> */}
                            <div className="d-grid gap-1">
                                <Button variant="primary" className="me-2" onClick={saveValue}>Save</Button>
                                <Button variant="secondary" className="me-2" onClick={onCancel}>Cancel</Button>
                            </div>
                        </>
                    ) : (
                        <>
                            <p><strong>Description:</strong> {selectedReimbursement.description}</p>
                            <p><strong>Amount:</strong> ${selectedReimbursement.amount.toFixed(2)}</p>
                            <p><strong>Status:</strong> {selectedReimbursement.status}</p>
                            <p><strong>Employee:</strong> {selectedReimbursement.user.firstName} {selectedReimbursement.user.lastName}</p>
                            <p></p>
                            {selectedReimbursement.status === "PENDING" && (
                                <div className="d-grid gap-1">
                                    <Button variant="primary" className="me-2" onClick={onUpdateReimbursementStatus}>Update Reimbursement</Button>
                                    {isEditing && (
                                        <Button variant="secondary" className="me-2" onClick={onCancel}>Cancel</Button>
                                    )}

                                </div>
                            )}
                        </>
                    )}
                </div>
            )}

            {selectedUser && viewUserOrReimbursement === "USER" && (
                <div>
                    <div>
                        <h5>User Details</h5>
                        <div>
                            <p><strong>Name:</strong> {selectedUser.firstName} {selectedUser.lastName}</p>
                            <p><strong>Username:</strong> {selectedUser.username}</p>
                            <p><strong>Role: </strong> {selectedUser.role}</p>
                        </div>
                        <div className="d-grid gap-2">
                            <Button variant="primary" className="me-1" onClick={onViewReimbursements}>View Reimbursements</Button>
                            <Button variant="secondary" className="me-1" onClick={onEditUser}>Edit User Account</Button>
                            {showDeleteUser && (
                                <div>
                                    <Row className="mb-2">
                                        <Col xs={6} className="d-flex justify-content-between">
                                            <Button variant="success" className="w-100" onClick={onPromote}>Promote User</Button>

                                        </Col>
                                        <Col xs={6} className="d-flex justify-content-between">
                                            <Button variant="danger" className="w-100" onClick={onDelete}>Delete User</Button>
                                        </Col>
                                    </Row>
                                    <Row className="mb-2">
                                        <Col xs ={6} className="d-flex justify-content-between">
                                            {/* <Button variant="warning" className="w-100" onClick={onDemote}>Demote User</Button> */}
                                        </Col>
                                        <Col xs={6} className="d-flex justify-content-between">
                                        <Button variant="secondary" className="w-100" onClick={onCancel}>Cancel</Button>
                                        </Col>
                                    </Row>
                                </div>
                            )}
                        </div>
                    </div>
                </div>

            )}

            {/* Buttons */}
            {selectedReimbursement && userRole === "MANAGER" && isEditing && viewUserOrReimbursement === "REIMBURSEMENT" && (
                <div>
                    <br />
                    <h5>Resolve Reimbursement</h5>
                    <div className="d-grid gap-1">
                        {userRole === "MANAGER" && selectedReimbursement.status === "PENDING" && (
                            <div className="d-grid gap-2">
                                <Button variant="success" className="me-2 mt-1" onClick={() => onResolve("APPROVED")}>Approve</Button>
                                <Button variant="danger" className="me-2" onClick={() => onResolve("DENIED")}>Deny</Button>
                            </div>
                        )}
                    </div>
                </div>
            )}
            <br />
            {selectedReimbursement && viewUserOrReimbursement === "REIMBURSEMENT" && (() => {
                const dateCreated = new Date(selectedReimbursement.dateCreated)
                const formattedDateCreated = dateCreated.toLocaleDateString()
                const formattedTimeCreated = dateCreated.toLocaleTimeString([], {hour: '2-digit', minute: '2-digit', second: '2-digit'})

                return (
                    <div>
                    <h5>History</h5>

                    
                    <div>
                        {selectedReimbursement?.reimbursementHistory && (
                            <>
                                {selectedReimbursement.reimbursementHistory
                                    ?.sort((a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime())
                                    .map((history, index) => {
                                    const date = new Date(history.timestamp)
                                    const formattedDate = date.toLocaleDateString()
                                    const formattedTime = date.toLocaleTimeString([], {hour: '2-digit', minute: '2-digit', second: '2-digit'})

                                    return (
                                        <div key={index}>
                                            <p>{formattedDate} at {formattedTime}: {history.updatedBy} updated {history.fieldChanged} from "{history.oldValue}" to "{history.newValue}".</p>
                                            <hr />
                                        </div>
                                    )
                                })}
                                
                            </>
                        )}
                        <p>{formattedDateCreated} at {formattedTimeCreated}: {selectedReimbursement.user.firstName} {selectedReimbursement.user.lastName} created a new reimbursement.</p>
                    </div>
                </div>
                )
            })()}

            
        </div>        
    )
}

export default ActionsMenu