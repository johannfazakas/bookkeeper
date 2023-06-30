create table if not exists "app_user"
(
    id uuid primary key,
    username varchar(255) not null
);
