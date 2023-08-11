import React from 'react';
import {Link} from 'react-router-dom';

function Navigation() {
    return (
        <nav>
            <ul>
                <li><Link to="/">Home</Link></li>
                <li><Link to="/accounts">Accounts</Link></li>
                <li><Link to="/income">Income</Link></li>
                <li><Link to="/expenses">Expenses</Link></li>
                <li><Link to="/investments">Investments</Link></li>
            </ul>
        </nav>
    );
}

export default Navigation;
