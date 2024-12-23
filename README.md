# EmployeeReimbursementSystem
Employee Reimbursement System project for Revature training

This full stack app allows for the creation and management of Reimbursements. Along with that is some user account management for managers such as promoting or deleting user accounts. This project utilizes Spring boot to manage the endpoints, create and interact with database, manage role verification with Spring AOP, and manage sessions with HttpSessions. This project utilizes React and bootstrap to build and render the front end web app.

## Employee User Stories
  -  Register for a new account with default role of "Employee"
  -  Login
  -  Change password
  -  Create Reimbursement
  -  Update a reimbursement desciption
  -  View all of their own reimbursements
  -  View all of thier own pending reimbursements (*can filter between all statuses)

## Manager User Stories (Including all employee user stories)
  -  View ALL reimbursements
  -  View All pending reimbursements (*can filter between all statuses)
  -  Resolve a reimbursement (Change status to "Approved" or "Denied"
  -  Promote a user to manager
  -  Delete a user

## Error Handling
All actions should require no empty fields and the app should display the message sent by either the front end or the backend, dependent upon where the error was caught.
Usernames CANNOT be the same as any other username in the DB
Passwords CANNOT contain duplicate characters
Passwords MUST contain at least 2 numbers and 2 symbols

## Authentication
### Backend
  -  Utilizes Spring AOP Annotation of @RequiresRole to set and verify the user's role before allowing certain actions.
  -  Utilize Spring AOP to check if the user is logged in before every method excluding those in the authentication controller
  -  Utilizes HttpSession to store and pass user information between the front and back end.

### Frontend
  -  Utilizes React Context API to configure and Authentication Contex to store various user data on the front end such as log in status, role, first name etc.

### Login Verification
  -  Login verification is achieved and verified on both ends. In the front end, React's useEffect hook is used to run on every change of the state {isLoggedIn} or the useNavigate hook is used. Anytime the logout method is called, it sets the {isLoggedIn} state to false. This means that even if the axios call to the backend to logout doesn't get set, it will still redirect you to the login page and attempt to prevent you from accessing the app. Logout on the backend is achieved by invalidating the HttpSession.

# GETTING STARTED
  Ensure you have configured your database, this project uses a Postgres DB created by Spring Data JPA. The default schema is set to "ers". All that should be required is to configure your DB credentials however you would like to in the application.properties. This project's credentials are stored via environement varibales configured in the IDE.

  There is an intializer that runs when the app starts and if there is no users in the DB, an ADMIN account with the Manager role is automatically created with the credentials: username: ADMIN, password: password.
