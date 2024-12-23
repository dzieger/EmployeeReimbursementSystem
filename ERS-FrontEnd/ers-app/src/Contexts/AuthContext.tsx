import axios from "axios"
import React, { createContext, useContext, useEffect, useState } from "react"

type AuthContextType = {
    isLoggedIn: boolean | undefined
    userRole: string | null
    userId: number | null
    sessionId: string | null
    setIsLoggedIn: (loggedIn: boolean) => void
    setUserRole: (role: string | null) => void
    setUserId: (userId: number | null) => void
    setSessionId: (sessionId: string) => void
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [isLoggedIn, setIsLoggedIn] = useState<boolean | undefined>(undefined)
    const [userRole, setUserRole] = useState<string | null>(null)
    const [userId, setUserId] = useState<number | null>(null)
    const [sessionId, setSessionId] = useState<string | null>(null)
    const [loading, setLoading] = useState<boolean>(true)

    useEffect(() => {
        const loggedIn = localStorage.getItem("isLoggedIn") === "true"
        const role = localStorage.getItem("userRole")
        const userId = localStorage.getItem("userId")
        const session = localStorage.getItem("sessionId")

        console.log("Restoring auth state from localStorage: ")
        console.log("isLoggedIn: ", loggedIn)
        console.log("role: ", role)

        if (loggedIn) {
            setIsLoggedIn(true)
            setUserRole(role)
            setUserId(Number(userId))
            setSessionId(session)
        } else {
            setIsLoggedIn(false)
        }
        setLoading(false)
    }, [])

    if (loading) {
        return <div>Loading...</div>
    }

    return (
        <AuthContext.Provider value={{ isLoggedIn, userRole, userId, sessionId, setIsLoggedIn, setUserRole, setUserId, setSessionId}}>
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => {
    const context = useContext(AuthContext)
    if (!context) {
        throw new Error("useAuth must be within an AuthProvider")
    }
    return context
}