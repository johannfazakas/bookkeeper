import { createStore, applyMiddleware } from 'redux';
import thunk from 'redux-thunk';
import rootReducer from '../reducer/root-reducer'; // Import your root reducer

export type RootState = ReturnType<typeof rootReducer>;

const store  = createStore(rootReducer, applyMiddleware(thunk));

export default store;
