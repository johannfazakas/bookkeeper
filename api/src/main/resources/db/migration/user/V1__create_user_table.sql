create table if not exists "user"
(
    id uuid primary key,
    email varchar(255) not null
);
