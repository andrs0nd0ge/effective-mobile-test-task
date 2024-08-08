create schema if not exists w;

create table if not exists w.statuses
(
    id   bigserial
        constraint statuses_pk
            primary key,
    name text not null
);

alter table w.statuses
    owner to postgres;

create unique index if not exists statuses_id_uindex
    on w.statuses (id);

create table if not exists w.priorities
(
    id   bigserial
        constraint priorities_pk
            primary key,
    name text not null
);

alter table w.priorities
    owner to postgres;

create unique index if not exists priorities_id_uindex
    on w.priorities (id);

create table if not exists w.roles
(
    id   bigserial
        constraint roles_pk
            primary key,
    name text not null
);

alter table w.roles
    owner to postgres;

create table if not exists w.users
(
    id       bigserial
        constraint users_pk
            primary key,
    username text not null,
    email    text not null,
    password text not null,
    role_id  bigint
        constraint users_roles_fk
            references w.roles
            on update cascade on delete set null
);

alter table w.users
    owner to postgres;

create unique index if not exists users_id_uindex
    on w.users (id);

create unique index if not exists users_email_uindex
    on w.users (email);

create table if not exists w.tasks
(
    id          bigserial
        constraint tasks_pk
            primary key,
    header      text             not null,
    description text             not null,
    status_id   bigint default 1 not null
        constraint tasks_status_fk
            references w.statuses
            on update cascade on delete set default,
    priority_id bigint default 1 not null
        constraint tasks_priority_fk
            references w.priorities
            on update cascade on delete set default,
    author_id   bigint
        constraint tasks_author_fk
            references w.users
            on update cascade on delete set null,
    executor_id bigint
        constraint tasks_executor_fk
            references w.users
            on update cascade on delete set null
);

alter table w.tasks
    owner to postgres;

create unique index if not exists tasks_id_uindex
    on w.tasks (id);

create table if not exists w.comments
(
    id      bigserial
        constraint comments_pk
            primary key,
    comment text   not null,
    task_id bigint not null
        constraint comments_task_fk
            references w.tasks
            on update cascade on delete cascade
);

alter table w.comments
    owner to postgres;

create unique index if not exists comments_id_uindex
    on w.comments (id);

create unique index if not exists roles_id_uindex
    on w.roles (id);