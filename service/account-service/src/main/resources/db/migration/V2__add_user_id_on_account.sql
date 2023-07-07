ALTER TABLE account ADD COLUMN user_id UUID;
UPDATE account SET user_id = '00000000-0000-0000-0000-000000000000';
ALTER TABLE account ALTER COLUMN user_id SET NOT NULL;
CREATE INDEX account_user_id_idx ON account(user_id);
