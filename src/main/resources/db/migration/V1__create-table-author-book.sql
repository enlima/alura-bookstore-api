create table author (
    id bigint not null auto_increment,
    name varchar(100) not null,
    email varchar(50) not null,
    birthdate date not null,
    mini_resume varchar(240) not null,
    primary key(id)
);

create table book (
    id bigint not null auto_increment,
    title varchar(100) not null,
    publication_date date not null,
    pages int not null,
    author_id bigint not null,
    foreign key(author_id) references author(id),
    primary key(id)
);
