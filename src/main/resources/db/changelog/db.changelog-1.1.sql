--liquibase formatted sql

--changeset dezzzl:1
ALTER TABLE artist
ALTER COLUMN image_filename TYPE VARCHAR(128);