import React from 'react';
import {Link} from 'react-router-dom';
import {AppBar, Button, Toolbar, Typography} from "@mui/material";

const Navigation: React.FC = () => {
    const [isLogged, setIsLogged] = React.useState(true)

    return (
        <AppBar position="static">
            <Toolbar variant="regular">
                <Typography variant="h6" noWrap>
                    Bookkeeper
                </Typography>
                <div>
                    <Button color="inherit" component={Link} to="/">Home</Button>
                    <Button color="inherit" component={Link} to="/accounts">Accounts</Button>
                    <Button color="inherit" component={Link} to="/income">Income</Button>
                    <Button color="inherit" component={Link} to="/expenses">Expenses</Button>
                    <Button color="inherit" component={Link} to="/investments">Investments</Button>
                </div>
                <div style={{marginLeft: 'auto'}}>
                    {!isLogged
                        ? (<Button color="inherit">Login</Button>)
                        : (<Button color="inherit">Logout</Button>)
                    }
                </div>
            </Toolbar>
        </AppBar>
    );
}

export default Navigation;
