CREATE TABLE refresh_tokens (
    email VARCHAR(255) NOT NULL,
    token_hash CHAR(64) NOT NULL,
    expires_at DATETIME NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (email),
    UNIQUE KEY uk_refresh_email_hash (email, token_hash),
    KEY idx_refresh_expires (expires_at),
    CONSTRAINT fk_refresh_email
        FOREIGN KEY (email) REFERENCES accounts(email) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;