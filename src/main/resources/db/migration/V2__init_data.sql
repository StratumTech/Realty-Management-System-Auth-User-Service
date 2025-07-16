-- Инициализация ролей
INSERT INTO roles (role_id, name) VALUES
(1, 'ADMIN'),
(2, 'REGIONAL_ADMIN'),
(3, 'AGENT');

-- Инициализация регионов
INSERT INTO regions (region_id, name, created_at) VALUES
(1, 'North Region', CURRENT_TIMESTAMP),
(2, 'South Region', CURRENT_TIMESTAMP);

-- Инициализация глобального администратора
INSERT INTO administrators (admin_uuid, role_id, region_id, name, surname, email, phone, password, telegram_tag, prefer_channel, referral, created_at, updated_at, is_blocked)
VALUES (
    gen_random_uuid(),
    1, -- ADMIN
    NULL, -- Нет региона для глобального администратора
    'Admin',
    'Global',
    'admin.global@example.com',
    '+49123456789',
    'password', -- Какой-то пароль
    '@globaladmin',
    'email',
    NULL, -- Глобальному администратору не нужна реферальная ссылка
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    false
); 