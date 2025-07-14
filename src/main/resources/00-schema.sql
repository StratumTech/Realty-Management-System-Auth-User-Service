CREATE TABLE "roles" (
    "role_id" INTEGER NOT NULL UNIQUE,
    "name" VARCHAR(255) NOT NULL,
    PRIMARY KEY("role_id")
);

CREATE TABLE "regions" (
    "region_id" INTEGER NOT NULL UNIQUE,
    "name" VARCHAR(255) NOT NULL,
    "created_at" TIMESTAMP NOT NULL,
    PRIMARY KEY("region_id")
);

CREATE TABLE "agents" (
    "agent_uuid" UUID NOT NULL UNIQUE,
    "role_id" INTEGER NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "patronymic" VARCHAR(255),
    "surname" VARCHAR(255) NOT NULL,
    "email" VARCHAR(255) NOT NULL UNIQUE,
    "phone" VARCHAR(255) NOT NULL,
    "password" VARCHAR(255) NOT NULL,
    "telegram_tag" VARCHAR(255) NOT NULL,
    "prefer_channel" VARCHAR(255) NOT NULL,
    "image_url" VARCHAR(255),
    "created_at" TIMESTAMP NOT NULL,
    "updated_at" TIMESTAMP NOT NULL,
    "is_blocked" BOOLEAN NOT NULL,
    PRIMARY KEY("agent_uuid")
);

CREATE TABLE "administrators" (
    "admin_uuid" UUID NOT NULL UNIQUE,
    "role_id" INTEGER NOT NULL,
    "region_id" INTEGER,
    "name" VARCHAR(255) NOT NULL,
    "patronymic" VARCHAR(255),
    "surname" VARCHAR(255) NOT NULL,
    "email" VARCHAR(255) NOT NULL,
    "phone" VARCHAR(255) NOT NULL,
    "password" VARCHAR(255) NOT NULL,
    "telegram_tag" VARCHAR(255) NOT NULL,
    "prefer_channel" VARCHAR(255) NOT NULL,
    "referal" VARCHAR(255) UNIQUE,
    "image_url" VARCHAR(255),
    "created_at" TIMESTAMP NOT NULL,
    "updated_at" TIMESTAMP NOT NULL,
    "is_blocked" BOOLEAN NOT NULL,
    PRIMARY KEY("admin_uuid")
);

CREATE TABLE "administrators_to_agents" (
    "agent_uuid" UUID NOT NULL UNIQUE,
    "administrator_uuid" UUID,
    PRIMARY KEY("agent_uuid")
);