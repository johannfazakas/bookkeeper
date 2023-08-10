import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../store/store';
import { addAccount, removeAccount } from '../action/account-actions';
import { Account } from '../model/account';

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
        <div>
            <h1>Accounts</h1>
            <div>
                <input
                    type="text"
                    value={newAccountName}
                    onChange={(e) => setNewAccountName(e.target.value)}
                />
                <button onClick={handleAddAccount}>Add Account</button>
            </div>
            <ul>
                {accounts.map((account) => (
                    <li key={account.id}>
                        {account.name}
                        <button onClick={() => handleRemoveAccount(account.id)}>Remove</button>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default App;
