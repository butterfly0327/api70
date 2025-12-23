USE yumyumcoach;

CREATE TABLE post_likes (
    post_id BIGINT UNSIGNED NOT NULL,
    email VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL,
    PRIMARY KEY (post_id, email),
    KEY idx_post_likes_email_created_at (email, created_at),
    CONSTRAINT fk_post_likes_post
        FOREIGN KEY (post_id) REFERENCES posts(id),
    CONSTRAINT fk_post_likes_account
        FOREIGN KEY (email) REFERENCES accounts(email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;