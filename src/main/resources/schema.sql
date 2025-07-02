-- Database Schema for MovieLens
CREATE TABLE IF NOT EXISTS movies
(
    movie_id INT PRIMARY KEY,
    title    VARCHAR(255),
    genres   VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS users
(
    user_id    INT PRIMARY KEY,
    gender     CHAR(1),
    age        INT,
    occupation INT,
    zip_code   VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS ratings
(
    user_id   INT,
    movie_id  INT,
    rating    INT,
    timestamp BIGINT,
    PRIMARY KEY (user_id, movie_id, timestamp)
);
