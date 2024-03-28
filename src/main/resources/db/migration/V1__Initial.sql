CREATE TABLE IF NOT EXISTS images (
                                      id VARCHAR(255) PRIMARY KEY DEFAULT gen_random_uuid(),
                                      name VARCHAR(255) NOT NULL,
                                      type VARCHAR(50) NOT NULL,
                                      data bytea NOT NULL
);

CREATE TABLE IF NOT EXISTS posts (
                                     id VARCHAR(255) PRIMARY KEY DEFAULT gen_random_uuid(),
                                     title VARCHAR(255) NOT NULL,
                                     description TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS post_images (
                                           post_id VARCHAR(255),
                                           image_id VARCHAR(255),
                                           FOREIGN KEY (post_id) REFERENCES posts(id),
                                           PRIMARY KEY (post_id, image_id)
);
