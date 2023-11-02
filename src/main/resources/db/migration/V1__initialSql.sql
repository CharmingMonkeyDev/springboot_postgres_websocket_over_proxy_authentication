CREATE TABLE core.apl_language (
                                   id uuid NOT NULL,
                                   created_date timestamp NULL,
                                   created_by uuid NULL,
                                   status int4 NULL,
                                   last_modified_date timestamp NULL,
                                   last_modified_by uuid NULL,
                                   name varchar(255) NOT NULL,
                                   short_code varchar(20) NOT NULL,
                                   CONSTRAINT apl_language_pkey PRIMARY KEY (id),
                                   CONSTRAINT apl_language_name UNIQUE (name),
                                   constraint apl_language_short_code UNIQUE (short_code)
);

INSERT INTO core.apl_language
(id, created_date, created_by, status, last_modified_date, last_modified_by, "name", short_code)
VALUES('ce2f8b20-fe6a-11eb-888a-0242ac160002'::uuid, '2021-05-05 13:35:50.493', 'cb94aa80-ac80-11eb-b342-0242ac160002'::uuid, 1, NULL, NULL, 'English', 'en');
INSERT INTO core.apl_language
(id, created_date, created_by, status, last_modified_date, last_modified_by, "name", short_code)
VALUES('dbf6787c-fe6a-11eb-888a-0242ac160002'::uuid, '2021-05-05 13:35:50.493', 'cb94aa80-ac80-11eb-b342-0242ac160002'::uuid, 1, NULL, NULL, 'Spanish', 'es');


CREATE TABLE core.apl_translations (
                                       id uuid NOT NULL,
                                       created_date timestamp NULL,
                                       created_by uuid NULL,
                                       status int4 NULL,
                                       last_modified_date timestamp NULL,
                                       last_modified_by uuid NULL,
                                       language_id uuid not null,
                                       keyword  varchar(255)  NULL,
                                       value varchar(255)  NULL,
                                       CONSTRAINT apl_translations_pkey PRIMARY KEY (id),
                                       CONSTRAINT apl_translations_keyword UNIQUE (keyword,language_id),
                                       CONSTRAINT fk_apl_translations_language_id FOREIGN KEY (language_id) REFERENCES core.apl_language(id)

);

CREATE TABLE core.apl_user_type (
                                    id uuid NOT NULL,
                                    created_date timestamp NULL,
                                    created_by uuid NULL,
                                    status int4 NULL,
                                    last_modified_date timestamp NULL,
                                    last_modified_by uuid NULL,
                                    name varchar(255) NOT NULL,
                                    short_code varchar(20) NOT NULL,
                                    CONSTRAINT apl_user_type_pkey PRIMARY KEY (id),
                                    CONSTRAINT apl_user_type_name UNIQUE (name),
                                    CONSTRAINT apl_user_type_short_code UNIQUE (short_code)
);

INSERT INTO core.apl_user_type
(id, created_date, created_by, status, last_modified_date, last_modified_by, "name", short_code)
VALUES('4fc57982-c10e-11eb-be64-0242ac160002'::uuid, '2021-05-05 13:35:50.493', 'cb94aa80-ac80-11eb-b342-0242ac160002'::uuid, 1, NULL, NULL, 'USER', 'USER');
INSERT INTO core.apl_user_type
(id, created_date, created_by, status, last_modified_date, last_modified_by, "name", short_code)
VALUES('4fc58134-c10e-11eb-be64-0242ac160002'::uuid, '2021-05-05 13:35:50.493', 'cb94aa80-ac80-11eb-b342-0242ac160002'::uuid, 1, NULL, NULL, 'DOCTOR', 'DOCTOR');

CREATE TABLE core.apl_user (
                               id uuid NOT NULL,
                               created_date timestamp NULL,
                               created_by uuid NULL,
                               status int4 NULL,
                               last_modified_date timestamp NULL,
                               last_modified_by uuid NULL,
                               email varchar(255) NOT NULL,
                               prefix_phone_number varchar(4) NOT NULL,
                               phone_number varchar(10) NOT NULL,
                               activation_key varchar(20) NULL,
                               lang_key varchar(10) NULL,
                               user_type_id uuid NOT NULL,
                               ext_id int4 null,
                               is_allowed_sms bool NULL DEFAULT false,
                               CONSTRAINT apl_user_pkey PRIMARY KEY (id),
                               CONSTRAINT fk_user_user_type_id FOREIGN KEY (user_type_id) REFERENCES core.apl_user_type(id)
);

CREATE TABLE core.otp_code (
                            id uuid NOT NULL,
                            code varchar NOT NULL,
                            expired_date timestamp NULL,
                            phone_number varchar  NULL,
                            email varchar  NULL,
                            user_id uuid NULL,
                            created_date timestamp NULL,
                            created_by uuid NULL,
                            status int4 NULL,
                            last_modified_date timestamp NULL,
                            last_modified_by uuid NULL,
                            CONSTRAINT otp_code_pkey PRIMARY KEY (id)
);
