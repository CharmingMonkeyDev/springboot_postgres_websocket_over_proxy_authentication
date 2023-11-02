CREATE TABLE domain.user_family (
                                     id uuid NOT NULL,
                                     created_date timestamp NULL,
                                     created_by uuid NULL,
                                     status int4 NULL,
                                     last_modified_date timestamp NULL,
                                     last_modified_by uuid NULL,
                                     user_id uuid NOT NULL,
                                     firstname varchar(255)  NULL,
                                     lastname varchar(255)  NULL,
                                     birthday timestamp NULL,
                                     photo varchar(500) NULL,
                                     relation_type int4 NULL,
                                     isuser int4 NOT NULL ,
                                     CONSTRAINT user_family_pkey PRIMARY KEY (id),
                                     CONSTRAINT fk_user_family_user_id FOREIGN KEY (user_id) REFERENCES core.apl_user(id)
);

CREATE TABLE domain.service_category (
                                    id uuid NOT NULL,
                                    created_date timestamp NULL,
                                    created_by uuid NULL,
                                    status int4 NULL,
                                    last_modified_date timestamp NULL,
                                    last_modified_by uuid NULL,
                                    name varchar(255)  NULL,
                                    short_code varchar(50)  NULL,
                                    CONSTRAINT service_category_pkey PRIMARY KEY (id)
);

INSERT INTO domain.service_category
(id, created_date, created_by, status, last_modified_date, last_modified_by, "name", short_code)
VALUES('ce2f8b20-fe6a-11eb-888a-0242ac160002'::uuid, '2021-05-05 13:35:50.493', 'cb94aa80-ac80-11eb-b342-0242ac160002'::uuid, 1, NULL, NULL, 'Allergy and immunology', 'ALLERGY');
INSERT INTO domain.service_category
(id, created_date, created_by, status, last_modified_date, last_modified_by, "name", short_code)
VALUES('dbf6787c-fe6a-11eb-888a-0242ac160002'::uuid, '2021-05-05 13:35:50.493', 'cb94aa80-ac80-11eb-b342-0242ac160002'::uuid, 1, NULL, NULL, 'Dermatology', 'DERMATOLOGY');
INSERT INTO domain.service_category
(id, created_date, created_by, status, last_modified_date, last_modified_by, "name", short_code)
VALUES('dbf6787c-fe6a-11eb-888a-0242ac160102'::uuid, '2021-05-05 13:35:50.493', 'cb94aa80-ac80-11eb-b342-0242ac160002'::uuid, 1, NULL, NULL, 'Pediatrics', 'PEDIATRIC');


CREATE TABLE domain.doctor_service_detail (
                                         id uuid NOT NULL,
                                         created_date timestamp NULL,
                                         created_by uuid NULL,
                                         status int4 NULL,
                                         last_modified_date timestamp NULL,
                                         last_modified_by uuid NULL,
                                         user_id uuid NOT NULL,
                                         currency_id uuid null,
                                         service_category_id uuid NOT NULL,
                                         service_title varchar(255)  NULL,
                                         service_fee double precision  NULL,
                                         description varchar(1000)  NULL,
                                         CONSTRAINT doctor_service_detail_pkey PRIMARY KEY (id),
                                         CONSTRAINT fk_doctor_service_detail_user_id FOREIGN KEY (user_id) REFERENCES core.apl_user(id),
                                         CONSTRAINT fk_doctor_service_detail_service_category_id FOREIGN KEY (service_category_id) REFERENCES domain.service_category(id),
                                         CONSTRAINT fk_doctor_service_detail_service_currency_id FOREIGN KEY (currency_id) REFERENCES meta.currency(id)

);

CREATE TABLE domain.doctor_time_slots (
                                       id uuid NOT NULL,
                                       created_date timestamp NULL,
                                       created_by uuid NULL,
                                       status int4 NULL,
                                       last_modified_date timestamp NULL,
                                       last_modified_by uuid NULL,
                                       doctor_service_detail_id uuid NOT NULL,
                                       day_of_week INTEGER  NULL,
                                       start_time time without time zone NULL,
                                       end_time time without time zone NULL,
                                       CONSTRAINT doctor_time_slots_pkey PRIMARY KEY (id),
                                       CONSTRAINT fk_doctor_time_slots_doctor_service_detail_id FOREIGN KEY (doctor_service_detail_id) REFERENCES domain.doctor_service_detail(id)
);


CREATE TABLE domain.doctor_professional_profile (
                                          id uuid NOT NULL,
                                          created_date timestamp NULL,
                                          created_by uuid NULL,
                                          status int4 NULL,
                                          last_modified_date timestamp NULL,
                                          last_modified_by uuid NULL,
                                          user_id uuid NOT NULL,
                                          service_category_id uuid NULL,
                                          photo varchar(500) NULL,
                                          name varchar(255)  NULL,
                                          professional_details varchar(1000)  NULL,
                                          years_of_experience int4 NULL,
                                          location varchar(255)  NULL,
                                          licence_no varchar(50)  NULL,
                                          currency_id uuid null,
                                          consultation_fee double precision  NULL,
                                          CONSTRAINT doctor_professional_profile_pkey PRIMARY KEY (id),
                                          CONSTRAINT fk_doctor_professional_profile_user_id FOREIGN KEY (user_id) REFERENCES core.apl_user(id),
                                          CONSTRAINT fk_doctor_professional_profile_currency_id FOREIGN KEY (currency_id) REFERENCES meta.currency(id)
);
