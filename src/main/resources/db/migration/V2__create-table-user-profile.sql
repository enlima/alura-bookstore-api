create table user (
    id bigint not null auto_increment,
    name varchar(100) not null,
    login varchar(100) not null,
    password varchar(100) not null,
    primary key(id)
);

create table profile (
    id bigint not null auto_increment primary key,
    name varchar(100) not null
);

create table profiles_users (
    user_id bigint not null,
    profile_id bigint not null,
    primary key (user_id, profile_id),
    foreign key (user_id) references user(id),
    foreign key (profile_id) references profile(id)
);

insert into profile values (1, 'ROLE_ADMIN');
insert into profile values (2, 'ROLE_COMMON');