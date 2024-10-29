create sequence users_id_seq start with 1 increment by 1;

create table users
(
    user_id   bigint       not null default nextval('users_id_seq'),
    email     varchar(100) not null,
    password  varchar(255) not null,
    full_name varchar(255) not null,
    primary key (user_id),
    constraint unique_user_email unique (email)
);

-- password = $2a$12$lfSZu/Q5dDO0cj7bjmTHeuQc8qvTT5FK4xO6n.aNbxLKiRTQoQ1PS
insert into users(full_name, email, password)
values ('SpringFramework User', 'info@spring.com', '$2a$12$lfSZu/Q5dDO0cj7bjmTHeuQc8qvTT5FK4xO6n.aNbxLKiRTQoQ1PS'),
       ('Angular User', 'info@angular.com', '$2a$12$lfSZu/Q5dDO0cj7bjmTHeuQc8qvTT5FK4xO6n.aNbxLKiRTQoQ1PS');