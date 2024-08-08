insert into w.roles (name) values ('AUTHOR'), ('EXECUTOR');

insert into w.users (username, email, password, role_id)
values ('one', 'one@test', '123', 1),
       ('two', 'two@test', '123', 2),
       ('three', 'three@test', '123', 1);

insert into w.priorities (name) values ('Low'), ('Medium'), ('High');

insert into w.statuses (name) values ('Created'), ('In Progress'), ('Finished');

insert into w.tasks (header, description, status_id, priority_id, author_id, executor_id)
values ('First Task', 'first task desc', 1, 1, 1, 2),
       ('Second Task', 'second task desc', 1, 1, 1, 2),
       ('Third Task', 'third task desc', 1, 1, 1, 1);

insert into w.comments (comment, task_id)
values ('test comment 1', 1),
       ('test comment 2', 3),
       ('test comment 3', 2),
       ('test comment 4', 2),
       ('test comment 5', 3);