import "bootstrap/dist/css/bootstrap.min.css"
import './App.css'
import { BrowserRouter, Route, Routes } from "react-router-dom"
import Dashboard from "./Components/Dashboard/Dashboard"
import LoginRegister from "./Components/LoginRegister/LoginRegister"
import { AuthProvider } from "./Contexts/AuthContext"
import EditAccount from "./Components/EditAccount"

function App() {


  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/dashboard" element={<Dashboard />}/>
          <Route path="/" element={<LoginRegister />} />
          <Route path="/edit-account" element={<EditAccount />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}

export default App
