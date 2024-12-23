import { Reimbursement } from "./Reimbursement"

export interface ReimbursementHistory {
    id: number
    reimbId: number
    timestamp: string
    updatedBy: string
    fieldChanged: string
    oldValue: string
    newValue: string
    reimbursement: Reimbursement
}