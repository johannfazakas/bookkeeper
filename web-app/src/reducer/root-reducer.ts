import {combineReducers} from 'redux';
import accountReducer from "./account-reducer";

const rootReducer = combineReducers({
    accounts: accountReducer,
});

export default rootReducer;
