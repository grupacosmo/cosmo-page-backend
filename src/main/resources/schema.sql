CREATE TABLE IF NOT EXISTS book
(
    id     SERIAL PRIMARY KEY,
    title  TEXT NOT NULL,
    author TEXT NOT NULL
);