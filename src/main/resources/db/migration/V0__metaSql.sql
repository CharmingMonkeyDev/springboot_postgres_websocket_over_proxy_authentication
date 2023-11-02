CREATE TABLE meta.currency (
                                   id uuid NOT NULL,
                                   created_date timestamp NULL,
                                   created_by uuid NULL,
                                   status int4 NULL,
                                   last_modified_date timestamp NULL,
                                   last_modified_by uuid NULL,
                                   name varchar(255) NOT NULL,
                                   short_code varchar(20) NOT NULL,
                                   symbol varchar(3) NOT NULL,
                                   CONSTRAINT currency_pkey PRIMARY KEY (id),
                                   CONSTRAINT currency_name UNIQUE (name),
                                   constraint currency_short_code UNIQUE (short_code),
                                   constraint currency_symbol UNIQUE (symbol)
);

INSERT INTO meta.currency
(id, created_date, created_by, status, last_modified_date, last_modified_by, "name", short_code,symbol)
VALUES('ce2f8b20-fe6a-11eb-888a-0242ac160002'::uuid, '2021-05-05 13:35:50.493', 'cb94aa80-ac80-11eb-b342-0242ac160002'::uuid, 1, NULL, NULL, 'American Dollar', 'USD','$');
INSERT INTO meta.currency
(id, created_date, created_by, status, last_modified_date, last_modified_by, "name", short_code,symbol)
VALUES('dbf6787c-fe6a-11eb-888a-0242ac160002'::uuid, '2021-05-05 13:35:50.493', 'cb94aa80-ac80-11eb-b342-0242ac160002'::uuid, 1, NULL, NULL, 'Euro', 'EUR','€');
