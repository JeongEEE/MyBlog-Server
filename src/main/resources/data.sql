INSERT INTO article (title, content, author, created_at, updated_at) VALUES ('제목 1', '내용 1', 'test@test.com', NOW(), NOW());
INSERT INTO article (title, content, author, created_at, updated_at) VALUES ('제목 2', '내용 2', 'test2@test.com', NOW(), NOW());
INSERT INTO article (title, content, author, created_at, updated_at) VALUES ('제목 3', '내용 3', 'test@test.com', NOW(), NOW());

INSERT INTO members (ID, USERNAME, PASSWORD, ROLE) VALUES (0, 'test@test.com', '$2a$10$65WUr4PQRnVE56snADiXr.s33.ob8Dve42q8Xrwm6eLypi.EW2rfK', 0);