--liquibase formatted sql

--changeset dezzzl:1
CREATE TABLE artist
(
    id       SERIAL PRIMARY KEY,
    image_filename VARCHAR(32),
    nickname VARCHAR(32) NOT NULL
);

--changeset dezzzl:2
CREATE TABLE producer
(
    id       SERIAL PRIMARY KEY,
    image_filename VARCHAR(32),
    nickname VARCHAR(32) NOT NULL
);

--changeset dezzzl:3
CREATE TABLE track_artist
(
    id        SERIAL PRIMARY KEY,
    track_id  INTEGER REFERENCES track (id),
    artist_id INTEGER REFERENCES artist (id)
);

--changeset dezzzl:4
CREATE TABLE track_producer
(
    id          SERIAL PRIMARY KEY,
    track_id    INTEGER REFERENCES track (id),
    producer_id INTEGER REFERENCES producer (id)
)