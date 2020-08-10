drop table if exists user;
drop table if exists user_roles;

create table user (
        username varchar(255) not null,
        password varchar(255) not null,
        primary key (username)
);

create table user_roles (
        user_username varchar(255) not null,
        roles integer
);
