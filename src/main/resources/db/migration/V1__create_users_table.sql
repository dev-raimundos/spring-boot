CREATE SCHEMA users;
GO

CREATE TABLE users.users (
    id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    email NVARCHAR(150) NOT NULL,
    password_hash NVARCHAR(MAX) NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    is_active BIT NOT NULL DEFAULT 1,
    is_email_verified BIT NOT NULL DEFAULT 0,
    failed_login_attempts INT NOT NULL DEFAULT 0,
    locked_until DATETIME2 NULL,
    created_at DATETIME2 NOT NULL,
    updated_at DATETIME2 NULL,
    last_login_at DATETIME2 NULL
);
GO

CREATE UNIQUE INDEX ix_users_email ON users.users (email);
GO
