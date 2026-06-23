SET search_path = public, pg_catalog;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE auth_user_role (
    id   bigserial PRIMARY KEY,
    code varchar(128) NOT NULL,
    name text         NOT NULL
);

INSERT INTO auth_user_role (code, "name")
VALUES ('USER', 'User');
INSERT INTO auth_user_role (code, "name")
VALUES ('ADMIN', 'Admin');

CREATE TABLE auth_user (
    id         bigserial PRIMARY KEY ,
    email      varchar(255) NOT NULL,
    password   varchar(255) NOT NULL,
    first_name varchar(128) NOT NULL DEFAULT '',
    last_name  varchar(128) NOT NULL DEFAULT '',
    created_at timestamp    NULL,
    created_by bigint       NULL,
    CONSTRAINT auth_user_email_uqc UNIQUE (email)
);

CREATE INDEX auth_user_email_idx ON auth_user USING btree(email);

-- Create the link table
CREATE TABLE auth_user_roles (
    id      bigserial PRIMARY KEY,
    user_id bigint NOT NULL,
    role_id bigint NOT NULL,
    CONSTRAINT auth_user_role_link_user_id_fk FOREIGN KEY (user_id) REFERENCES auth_user(id) ON DELETE CASCADE,
    CONSTRAINT auth_user_role_link_role_id_fk FOREIGN KEY (role_id) REFERENCES auth_user_role(id),
    CONSTRAINT auth_user_role_link_user_role_uq UNIQUE (user_id, role_id)
);
CREATE INDEX auth_user_role_link_user_id_idx ON auth_user_roles USING btree(user_id);
CREATE INDEX auth_user_role_link_role_id_idx ON auth_user_roles USING btree(role_id);