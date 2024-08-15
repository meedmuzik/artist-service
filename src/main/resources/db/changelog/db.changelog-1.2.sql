--liquibase formatted sql

--changeset dezzzl:1
INSERT INTO artist (image_filename, nickname)
VALUES ('194a6eab-df5c-40b5-9c0a-efd493fab021.jpg', 'DJ Thunder'),
       ('443b81ce-77e0-4db6-b4c5-e461c1e4bade.jpg', 'Melody Smith'),
       ('4e825280-4236-4f9d-a6a2-6213cb74cffb.jpg', 'Bass Hunter');

--changeset dezzzl:2
INSERT INTO album (image_id, title)
VALUES ('79dac124-f1e6-4bcd-9e30-5aaafac3f453.jpg','Thunder Beats'),
       ('7ad0aa49-9252-41cf-99ae-e0c1e61ab82b.jpg','Melody Journey'),
       ('84bec5d7-f8a0-42a7-8007-7519ec8bdce2.jpg', 'Bass Drop');

--changeset dezzzl:3
INSERT INTO track (image_id, title, release_date, album_id)
VALUES ('a9671c0d-4b4a-4bb5-91a5-5edca5255eae.jpg', 'Thunder Intro', '2024-01-01', 1),
       ('bedffa86-8806-4c6d-a3ac-44186ee6a92a.jpg', 'Melody Sunrise', '2024-02-01', 2),
       ('ed3aebf0-593f-4238-b9e4-e2e6660b2036.jpg', 'Bass Attack', '2024-03-01', 3);

--changeset dezzzl:4
INSERT INTO track_artist (track_id, artist_id)
VALUES (1, 1), -- DJ Thunder on Thunder Intro
       (2, 1),
       (3, 3); -- Bass Hunter on Bass Attack
