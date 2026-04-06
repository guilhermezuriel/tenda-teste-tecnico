CREATE TABLE coupons (
                         id VARCHAR(255) PRIMARY KEY,
                         code VARCHAR(20) NOT NULL,
                         description TEXT NOT NULL,
                         discount_value DECIMAL(10,2) NOT NULL,
                         expiration_date DATE NOT NULL,
                         status VARCHAR(20) NOT NULL,
                         published BOOLEAN NOT NULL DEFAULT FALSE,
                         redeemed BOOLEAN NOT NULL DEFAULT FALSE,
                         created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                         deleted_at TIMESTAMP NULL
);