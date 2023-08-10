import {Account} from '../model/account';
import {Action, ActionType} from "../action/account-actions";

const initialState: Account[] = [];

const accountReducer = (state = initialState, action: Action): Account[] => {
    switch (action.type) {
        case ActionType.ADD_ACCOUNT:
            return [...state, action.account];
        case ActionType.REMOVE_ACCOUNT:
            return state.filter((account) => account.id !== action.id);
        default:
            return state;
    }
};

export default accountReducer;