import React, {useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {RootState} from '../store/store';
import {addAccount, removeAccount} from '../action/account-actions';
import {Account} from '../model/account';
import {Route, BrowserRouter as Router, Routes} from "react-router-dom";
import Home from "./Home";
import Accounts from "./Accounts";
import Income from "./Income";
import Expenses from "./Expenses";
import Investments from "./Investments";
import Navigation from "./Navigation";

const App: React.FC = () => {
    const accounts = useSelector((state: RootState) => state.accounts);
    const dispatch = useDispatch();

    const [newAccountName, setNewAccountName] = useState('');

    const handleAddAccount = () => {
        const newAccount: Account = {
            id: Date.now().toString(),
            name: newAccountName,
        };
        dispatch(addAccount(newAccount));
        setNewAccountName('');
    };

    const handleRemoveAccount = (id: string) => {
        dispatch(removeAccount(id));
    };
    return (
        <Router>
            <Navigation/>
            <Routes>
                <Route path="/" element={<Home/>}/>
                <Route path="/accounts" element={<Accounts/>}/>
                <Route path="/income" element={<Income/>}/>
                <Route path="/expenses" element={<Expenses/>}/>
                <Route path="/investments" element={<Investments/>}/>
            </Routes>
        </Router>
    );

    // return (
    //     <div>
    //         <h1>Accounts</h1>
    //         <div>
    //             <input
    //                 type="text"
    //                 value={newAccountName}
    //                 onChange={(e) => setNewAccountName(e.target.value)}
    //             />
    //             <button onClick={handleAddAccount}>Add Account</button>
    //         </div>
    //         <ul>
    //             {accounts.map((account) => (
    //                 <li key={account.id}>
    //                     {account.name}
    //                     <button onClick={() => handleRemoveAccount(account.id)}>Remove</button>
    //                 </li>
    //             ))}
    //         </ul>
    //     </div>
    // );
};

export default App;
