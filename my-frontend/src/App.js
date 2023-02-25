import LogIn from './LogIn';
import './App.css';


import {
  BrowserRouter as Router,
  Route,
  Routes as Switch,
} from "react-router-dom";
import Home from './Home';
import PrivateRoute from './PrivateRoute';
import Register from './Register';
import UsersList from './UsersList';
import 'bootstrap/dist/css/bootstrap.css';
import ApproveList from './ApproveList';
import EditUser from './EditUser';
import AccountsList from './AccountsList';
import CreateAccount from './CreateAccount';
import ApproveAccount from './ApproveAccount';
import EditAccount from './EditAccount';
import ChangePassword from './ChangePassword';
import MakePayment from './MakePayment';
import PayHistoryList from './PayHistoryList';
import VerifyPayment from './VerifyPayment';
import BalancesList from './BalancesList';
import AccountBalance from './AccountBalance';
import PaymentList from './PaymentList';
function App() {

  

  return (
    
    <Router>
      <Switch>
        <Route exact path="/" element={<LogIn />} />
        <Route exact path='/' element={<PrivateRoute />}>
          <Route exact path='/accountsList' element={<AccountsList/>}/>
          <Route exact path='/usersList' element={<UsersList/>}/>
          <Route exact path='/home' element={<Home />} />
          <Route exact path="/register" element={<Register />} />
          <Route exact path="/approve" element={<ApproveList />} />
          <Route exact path="/edit" element={<EditUser />} />
          <Route exact path="/createAccount" element={<CreateAccount />} />
          <Route exact path="/approveAccount" element={<ApproveAccount />} />
          <Route exact path="/editAccounts" element={<EditAccount />} />
          <Route exact path="/changePass" element={<ChangePassword />} />
          <Route exact path="/pay" element={<MakePayment/>} />
          <Route exact path="/payList" element={<PayHistoryList />} />
          <Route exact path="/verify" element={<VerifyPayment />} />
          <Route exact path="/balances" element={<BalancesList />} />
          <Route exact path="/accountBalance" element={<AccountBalance />}/>
          <Route exact path="/payments" element={<PaymentList />}/>
      </Route>
      </Switch>
    </Router>    
  );
}

export default App;