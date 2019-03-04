insert into issue (title, description, place, status, created_at, updated_at) values ('issue1', 'description1', 'place1', 'NEW', CURRENT_TIMESTAMP(),  CURRENT_TIMESTAMP());
insert into issue (title, description, place, status, created_at, updated_at) values ('issue2', 'description2', 'place2', 'DOING', CURRENT_TIMESTAMP(),  CURRENT_TIMESTAMP());
insert into issue (title, description, place, status, created_at, updated_at) values ('issue3', 'description3', 'place3', 'DOING', CURRENT_TIMESTAMP(),  CURRENT_TIMESTAMP());
insert into issue (title, description, place, status, created_at, updated_at) values ('issue4', 'description4', 'place4', 'DONE', CURRENT_TIMESTAMP(),  CURRENT_TIMESTAMP());

insert into message (issue_id, text, created_at, updated_at) values (1, 'message1', CURRENT_TIMESTAMP(),  CURRENT_TIMESTAMP());
insert into message (issue_id, text, created_at, updated_at) values (1, 'message2', CURRENT_TIMESTAMP(),  CURRENT_TIMESTAMP());
insert into message (issue_id, text, created_at, updated_at) values (2, 'message3', CURRENT_TIMESTAMP(),  CURRENT_TIMESTAMP());
insert into message (issue_id, text, created_at, updated_at) values (3, 'message4', CURRENT_TIMESTAMP(),  CURRENT_TIMESTAMP());
