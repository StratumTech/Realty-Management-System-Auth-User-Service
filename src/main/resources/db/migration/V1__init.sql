
CREATE TABLE roles (
    role_id   BIGSERIAL PRIMARY KEY,
    name      VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE regions (
    region_id   BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL UNIQUE,
    created_at  TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE administrators (
    admin_uuid      UUID    PRIMARY KEY,
    role_id         BIGINT  NOT NULL,
    region_id       BIGINT,
    name            VARCHAR(255) NOT NULL,
    patronymic      VARCHAR(255),
    surname         VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL,
    phone           VARCHAR(255) NOT NULL,
    password        VARCHAR(255) NOT NULL,
    telegram_tag    VARCHAR(255) NOT NULL,
    prefer_channel  VARCHAR(255) NOT NULL,
    referral        VARCHAR(255) UNIQUE,
    image_url       VARCHAR(255),
    created_at      TIMESTAMP NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP NOT NULL DEFAULT now(),
    is_blocked      BOOLEAN   NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_admin_role   FOREIGN KEY(role_id)   REFERENCES roles(role_id),
    CONSTRAINT fk_admin_region FOREIGN KEY(region_id) REFERENCES regions(region_id)
);

CREATE TABLE agents (
    agent_uuid      UUID    PRIMARY KEY,
    role_id         BIGINT  NOT NULL,
    admin_uuid      UUID,
    name            VARCHAR(255) NOT NULL,
    patronymic      VARCHAR(255),
    surname         VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL,
    phone           VARCHAR(255) NOT NULL,
    password        VARCHAR(255) NOT NULL,
    telegram_tag    VARCHAR(255) NOT NULL,
    prefer_channel  VARCHAR(255) NOT NULL,
    image_url       VARCHAR(255),
    created_at      TIMESTAMP NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP NOT NULL DEFAULT now(),
    is_blocked      BOOLEAN   NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_agent_role    FOREIGN KEY(role_id)   REFERENCES roles(role_id),
    CONSTRAINT fk_agent_admin   FOREIGN KEY(admin_uuid) REFERENCES administrators(admin_uuid)
);
