create table users
(
    id       uuid        default gen_random_uuid() not null,
    name     varchar(255)                          not null,
    email    varchar(255)                          not null,
    password varchar(255)                          not null,
    role     varchar(20) default 'USER'            not null
);

