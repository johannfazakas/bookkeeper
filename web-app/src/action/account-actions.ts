import { Account } from '../model/account';

export enum ActionType {
    ADD_ACCOUNT = 'ADD_ACCOUNT',
    REMOVE_ACCOUNT = 'REMOVE_ACCOUNT',
}

export interface AddAccountAction {
    type: ActionType.ADD_ACCOUNT;
    account: Account;
}

export interface RemoveAccountAction {
    type: ActionType.REMOVE_ACCOUNT;
    id: string;
}

export type Action = AddAccountAction | RemoveAccountAction;

export const addAccount = (account: Account): AddAccountAction => ({
    type: ActionType.ADD_ACCOUNT,
    account,
});

export const removeAccount = (id: string): RemoveAccountAction => ({
    type: ActionType.REMOVE_ACCOUNT,
    id,
});
