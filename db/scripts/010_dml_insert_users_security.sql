INSERT INTO users (username, password, authority_id)
    VALUES (
            'root',
            '$2a$10$WLaVVeqJgiIh6hl1M9cvU.cxXkZZnLYKG7scr1xoCFdhZM47eoVHm',
            (SELECT id FROM authorities WHERE authority = 'ROLE_ADMIN')
    );