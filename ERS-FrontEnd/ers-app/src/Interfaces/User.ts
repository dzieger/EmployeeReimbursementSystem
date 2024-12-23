import { Reimbursement } from "./Reimbursement"

export interface User {
    userId: number
    firstName: string
    lastName: string
    username: string
    role: string
    reimbursements: Reimbursement[]
}