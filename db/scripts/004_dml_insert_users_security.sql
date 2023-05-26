INSERT INTO users (username, password, email, authority_id)
    VALUES (
            'root',
            '$2a$10$WLaVVeqJgiIh6hl1M9cvU.cxXkZZnLYKG7scr1xoCFdhZM47eoVHm',
            'test@test.com',
            (SELECT id FROM authorities WHERE authority = 'ROLE_ADMIN')
    );