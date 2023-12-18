CREATE TABLE IF NOT EXISTS transaction
(
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL,
    from_account_id UUID NOT NULL REFERENCES account(id),
    to_account_id UUID NOT NULL REFERENCES account(id),
    amount NUMERIC NOT NULL,
    description TEXT NOT NULL
);

CREATE INDEX IF NOT EXISTS transaction_user_id_index ON transaction(user_id);
