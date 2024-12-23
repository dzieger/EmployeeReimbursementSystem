import { Button, Col, Container, Dropdown, Form, Row, Toast } from "react-bootstrap"
import "../Components/Dashboard/Dashboard.css"
import { Link, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { useAuth } from "../Contexts/AuthContext";
import axios from "axios";
import { error } from "console";

interface EditAccountProps {

}

const EditAccount: React.FC<EditAccountProps> = () => {
    const [showChangePassword, setShowChangePassword] = useState<boolean>(false)
    const navigate = useNavigate()

    const [oldPassword, setOldPassword] = useState<string>("")
    const [newPassword, setNewPassword] = useState<string>("")
    const [newPasswordCompare, setNewPasswordCompare] = useState<string>("")

    const [error, setError] = useState<string>("")

    const headHome = () => {
        navigate("/dashboard")
    }

    const handleChangePassword = async () => {
        if (newPassword === newPasswordCompare) {
            try {
                await axios.patch("http://localhost:4444/auth/change-password", {
                    oldPassword,
                    newPassword
                }, {
                    headers: {
                        "Authorization": localStorage.getItem("sessionId")
                    }
                })

                setShowChangePassword(false)
                navigate("/dashboard")
            } catch (error) {
                setError("Password change failed")
            }
        }
    }

  return (
    <Container>
        <Row className="dashboard-header">
            <Col xs={3} className="text-start">
                <Button variant="outline-primary" onClick={headHome}>Back</Button>
            </Col>
            <Col xs={6}></Col>
            <Col xs={3} className="text-end">
            </Col>
        </Row>

        <Row className="dashboard-main">
            <Col xs={2} className="dashboard-sidebar">
                <Button className="edit-account-sidebar-buttons" onClick={() => setShowChangePassword(true)}>Change Password</Button>
            </Col>
            <Col xs={10} className="dashboard-content">
                {showChangePassword && (
                    <div>
                        <h2>Change Password</h2>
                        <Form>
                            <Form.Group className="mb-3" controlId="formBasicPassword">
                                <Form.Label>Old Password</Form.Label>
                                <Form.Control 
                                    type="password" 
                                    placeholder="Old Password"
                                    value={oldPassword}
                                    onChange={(e) => setOldPassword(e.target.value)} />
                            </Form.Group>
                            <Form.Group>
                                <Form.Label>New Password</Form.Label>
                                <Form.Control 
                                    type="password" 
                                    placeholder="New Password" 
                                    value={newPassword}
                                    onChange={(e) => setNewPassword(e.target.value)}/>
                            </Form.Group>
                            <Form.Group>
                                <Form.Label>Retype New Password</Form.Label>
                                <Form.Control 
                                    type="password" 
                                    placeholder="Retype New Password"
                                    value={newPasswordCompare}
                                    onChange={(e) => setNewPasswordCompare(e.target.value)} />
                            </Form.Group>
                            <Button variant="primary" onClick={handleChangePassword}>
                                Submit
                            </Button>
                        </Form>
                        {error && <p className="error-message">{error}</p>}
                    </div>
                )}
            </Col>
            <Col xs={3} className="dashboard-actions">
                <p></p>
            </Col>
        </Row>
    </Container>
  );
}
export default EditAccount