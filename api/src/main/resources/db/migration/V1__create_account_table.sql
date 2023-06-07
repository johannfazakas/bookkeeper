create table if not exists account
(
    id uuid primary key,
    name varchar(255) not null,
    currency varchar(10) not null
);
