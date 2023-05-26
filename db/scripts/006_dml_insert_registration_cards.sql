INSERT INTO registration_cards (owner_name, owner_surname, car_plate, registration_certificate_number, vin_code, user_id)
    VALUES (
            'root',
            'root',
            'x000xx000',
            '00ЯЯ000000',
            'A1B2C3D4F5G6H7J8K',
            (SELECT id FROM users WHERE username = 'root')
    );