import React, { useEffect, useState } from "react";
import { useAuth } from "../../Contexts/AuthContext";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { Button, Col, Container, Form, Row } from "react-bootstrap";
import "../Dashboard/Dashboard.css"
import "./LoginRegister.css"
import useRecaptcha from "../../useRecaptcha";
import ReCAPTCHA from "react-google-recaptcha";

const LoginRegister: React.FC = () => {
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [passwordCompare, setPasswordCompare] = useState("")
    const [firstName, setFirstName] = useState("")
    const [lastName, setLastName] = useState("")
    const { setIsLoggedIn, setUserId, setUserRole, setSessionId } = useAuth()
    const [error, setError] = useState("")
    const navigate = useNavigate()
    const [renderRegister, setRenderRegister] = useState<boolean>(false)
    // const { captchaToken, recaptchaRef, handleRecaptcha } = useRecaptcha()

    useEffect(() => {
        setPassword("")
        setPasswordCompare("")
        setFirstName("")
        setLastName("")
        setError("")
    }, [renderRegister])



    const handleLogin = async () => {
        if (username && password) {
            try {
                const response = await axios.post("http://localhost:4444/auth/login", {
                    username,
                    password,
                })

                localStorage.setItem("sessionId", response.data.sessionId)
                localStorage.setItem("userId", response.data.userId)
                console.log(localStorage.getItem("userId"))
                localStorage.setItem("isLoggedIn", "true")
                localStorage.setItem("userRole", response.data.role)
                localStorage.setItem("firstName", response.data.firstName)
                setIsLoggedIn(true)
                setUserRole(response.data.role)
                setUserId(response.data.userId)
                setSessionId(response.data.sessionId)
    
                navigate("/dashboard")

            } catch (error: any) {
                setError(error.response.data)
            }
        }


    }

    const handleRegister = async () => {
        if (!(password === passwordCompare)) {
            setError("Passwords do not match")
            return;
        }
        if (!password || !passwordCompare || !firstName || !lastName || !username) {
            setError("All fields must bve filled in")
            return
        }

        try {
            const response = await axios.post("http://localhost:4444/auth/register", {
                firstName,
                lastName,
                username,
                password
            })

            setUsername(response.data.username)
            setPassword("")
            setRenderRegister(false)
            navigate("/")
        } catch (error: any) {
            setError(error.response.data)
        }
    }

    const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
        if (event.key === "Enter") {
            if (renderRegister) {
                handleRegister()
            } else {
                handleLogin()
            }
        }
    }

    return (

        <Container>
            <Row className="login-register-header">
                <Col xs={4} className="header-area">
                
                </Col>
                <Col xs={12}>
                    <h1 className="header-title">Employee Reimbursement System</h1>
                </Col>
                <Col xs={4}></Col>
            </Row>

            <Row className="login-register-center">
                <Col xs={4}></Col>
                <Col xs={4}>
                {renderRegister ? (
                <div>
                    <h2>Register</h2>
                    <Form.Group className="mb-2">
                        <Form.Label>First Name</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="John"
                            value={firstName}
                            onChange={(e) => setFirstName(e.target.value)}
                            onKeyDown={handleKeyDown}
                        />
                    </Form.Group>
                    <Form.Group className="mb-2">
                        <Form.Label>Last Name</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Doe"
                            value={lastName}
                            onChange={(e) => setLastName(e.target.value)}
                            onKeyDown={handleKeyDown}
                        />
                    </Form.Group>
                    <Form.Group className="mb-2">
                        <Form.Label>Username</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="jdoe12"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            onKeyDown={handleKeyDown}
                        />
                    </Form.Group>
                    <Form.Group className="mb-2">
                        <Form.Label>Password</Form.Label>
                        <Form.Control
                            type="password"
                            placeholder="Password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            onKeyDown={handleKeyDown}
                        />
                    </Form.Group>
                    <Form.Group className="mb-2">
                        <Form.Label>Retype Password</Form.Label>
                        <Form.Control
                            type="password"
                            placeholder="Retype Password"
                            value={passwordCompare}
                            onChange={(e) => setPasswordCompare(e.target.value)}
                            onKeyDown={handleKeyDown}
                        />
                    </Form.Group>
                    <Button variant="primary" className="mb-2" onClick={handleRegister}>Register</Button>
                    <p>Already have an account? <button className="login-register-button" onClick={() => setRenderRegister(false)}>Click Here</button></p>
                </div>
            ) : (
                <div>
                    <h2>Login</h2>
                    <Form.Group className="mb-2">
                        <Form.Label>Username</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder={username || "jdoe12"}
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            onKeyDown={handleKeyDown}
                        />
                    </Form.Group>
                    <Form.Group className="mb-2">
                        <Form.Label>Password</Form.Label>
                        <Form.Control
                            type="password"
                            placeholder="Password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            onKeyDown={handleKeyDown}
                        />
                    </Form.Group>
                    <Button variant="primary" className="mb-2" onClick={handleLogin}>Login</Button>
                    <p>Don't have an account? <button className="login-register-button" onClick={() => setRenderRegister(true)}>Click Here</button></p>
                </div>
            )}
            {error && <p className="error-message" style={{color: "red"}}>{error}</p>}
                </Col>
                <Col xs={4}></Col>
            </Row>

        </Container>

        // <Container className="d-flex align-items-canter flex-column">

        // </Container>
    )
}

export default LoginRegister