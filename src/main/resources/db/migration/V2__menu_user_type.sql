CREATE TABLE domain.menu (
                               id uuid NOT NULL,
                               short_code varchar NOT NULL,
                               name varchar  NULL,
                               url varchar NULL,
                               icon varchar NULL,
                               created_date timestamp NULL,
                               created_by uuid NULL,
                               status int4 NULL,
                               last_modified_date timestamp NULL,
                               last_modified_by uuid NULL,
                               CONSTRAINT menu_pkey PRIMARY KEY (id)
);

CREATE TABLE domain.menu_user_type (
                             id uuid NOT NULL,
                             menu_id uuid NOT NULL,
                             user_type_id uuid NOT NULL,
                             created_date timestamp NULL,
                             created_by uuid NULL,
                             status int4 NULL,
                             last_modified_date timestamp NULL,
                             last_modified_by uuid NULL,
                             view_index int4 NULL,
                             CONSTRAINT menu_user_type_pkey PRIMARY KEY (id),
                             CONSTRAINT fk_menu_user_type_user_type_id FOREIGN KEY (user_type_id) REFERENCES core.apl_user_type(id),
                             CONSTRAINT fk_menu_user_type_user_id FOREIGN KEY (menu_id) REFERENCES domain.menu(id)
);

INSERT INTO "domain".menu (id, short_code, name,url,icon, created_by, created_date, status) VALUES('d98c61a4-b37b-46a0-904b-9223e233eaef'::uuid, 'DSH', 'Dashboard','/index','fas fa-columns', '99999999-9999-9999-9999-999999999999'::uuid, '2023-02-18 13:35:50.493', 1);
INSERT INTO "domain".menu (id, short_code, name,url,icon, created_by, created_date, status) VALUES('79e76bdb-c2b5-4608-9cea-ea2132f29e71'::uuid, 'APT', 'Appointments','/appointments','fas fa-calendar-check', '99999999-9999-9999-9999-999999999999'::uuid, '2023-02-18 13:35:50.493', 1);
INSERT INTO "domain".menu (id, short_code, name,url,icon, created_by, created_date, status) VALUES('18b80976-23a7-4726-9afc-ae44bd847306'::uuid, 'PS', 'Profile Settings','/profile-settings','fas fa-user-cog', '99999999-9999-9999-9999-999999999999'::uuid, '2023-02-18 13:35:50.493', 1);
INSERT INTO "domain".menu (id, short_code, name,url,icon, created_by, created_date, status) VALUES('6c998a8f-de0f-40a4-b154-e8d50494a96c'::uuid, 'PS', 'Change Password','/change-password','fas fa-lock', '99999999-9999-9999-9999-999999999999'::uuid, '2023-02-18 13:35:50.493', 1);
INSERT INTO "domain".menu (id, short_code, name,url,icon, created_by, created_date, status) VALUES('b48c9c74-e2c0-466d-8c7d-45257408324b'::uuid, 'LO', 'Logout','/logout','fas fa-sign-out-alt', '99999999-9999-9999-9999-999999999999'::uuid, '2023-02-18 13:35:50.493', 1);
INSERT INTO "domain".menu (id, short_code, "name", url, icon, created_date, created_by, status, last_modified_date, last_modified_by) VALUES('b48c9c74-e2c0-466d-8c7d-45257408324e'::uuid, 'DPS', 'Doctor Profile Settings', '/professional', 'fas fa-stethoscope', '2023-02-18 13:35:50.493', '99999999-9999-9999-9999-999999999999'::uuid, 1, NULL, NULL);
INSERT INTO "domain".menu (id, short_code, "name", url, icon, created_date, created_by, status, last_modified_date, last_modified_by) VALUES('b47c9c74-e2c0-466d-8c7d-45257408324e'::uuid, 'SRVC', 'Services', '/services', 'fas fa-star-of-life', '2023-02-18 13:35:50.493', '99999999-9999-9999-9999-999999999999'::uuid, 1, NULL, NULL);
INSERT INTO "domain".menu (id, short_code, "name", url, icon, created_date, created_by, status, last_modified_date, last_modified_by) VALUES('b47c9c74-e3c0-466d-8c7d-45257408324e'::uuid, 'FMR', 'Family Medical Records', '/family', 'fas fa-users', '2023-02-18 13:35:50.493', '99999999-9999-9999-9999-999999999999'::uuid, 1, NULL, NULL);


INSERT INTO "domain".menu_user_type (id, menu_id, user_type_id, created_by, created_date, status, view_index ) VALUES('4c73acf2-4974-43ad-8a24-298e8aa8e775'::uuid, 'd98c61a4-b37b-46a0-904b-9223e233eaef'::uuid, '4fc57982-c10e-11eb-be64-0242ac160002'::uuid, '99999999-9999-9999-9999-999999999999'::uuid, '2023-02-18 13:35:50.493', 1,0);
INSERT INTO "domain".menu_user_type (id, menu_id, user_type_id, created_by, created_date, status, view_index  ) VALUES('4c73bcf2-4974-43ad-8a24-298e8aa8e775'::uuid, '79e76bdb-c2b5-4608-9cea-ea2132f29e71'::uuid, '4fc57982-c10e-11eb-be64-0242ac160002'::uuid, '99999999-9999-9999-9999-999999999999'::uuid, '2023-02-18 13:35:50.493', 1,10);
INSERT INTO "domain".menu_user_type (id, menu_id, user_type_id, created_by, created_date, status, view_index  ) VALUES('4c73ccf2-4974-43ad-8a24-298e8aa8e775'::uuid, '18b80976-23a7-4726-9afc-ae44bd847306'::uuid, '4fc57982-c10e-11eb-be64-0242ac160002'::uuid, '99999999-9999-9999-9999-999999999999'::uuid, '2023-02-18 13:35:50.493', 1,20);
INSERT INTO "domain".menu_user_type (id, menu_id, user_type_id, created_by, created_date, status, view_index  ) VALUES('4c73dcf2-4974-43ad-8a24-298e8aa8e775'::uuid, '6c998a8f-de0f-40a4-b154-e8d50494a96c'::uuid, '4fc57982-c10e-11eb-be64-0242ac160002'::uuid, '99999999-9999-9999-9999-999999999999'::uuid, '2023-02-18 13:35:50.493', 1,30);
INSERT INTO "domain".menu_user_type (id, menu_id, user_type_id, created_by, created_date, status, view_index  ) VALUES('4c73ecf2-4974-43ad-8a24-298e8aa8e775'::uuid, 'b48c9c74-e2c0-466d-8c7d-45257408324b'::uuid, '4fc57982-c10e-11eb-be64-0242ac160002'::uuid, '99999999-9999-9999-9999-999999999999'::uuid, '2023-02-18 13:35:50.493', 1,40);
INSERT INTO "domain".menu_user_type (id, menu_id, user_type_id, created_date, created_by, status, last_modified_date, last_modified_by, view_index) VALUES('d965c07e-1427-4e78-8ca5-ad32fff91770'::uuid, 'b47c9c74-e3c0-466d-8c7d-45257408324e'::uuid, '4fc57982-c10e-11eb-be64-0242ac160002'::uuid, '2023-02-18 13:35:50.493', '99999999-9999-9999-9999-999999999999'::uuid, 1, NULL, NULL, 35);


INSERT INTO "domain".menu_user_type (id, menu_id, user_type_id, created_by, created_date, status, view_index  ) VALUES('d963c07a-1427-4e78-8ca5-ad32fff91770'::uuid, 'd98c61a4-b37b-46a0-904b-9223e233eaef'::uuid, '4fc58134-c10e-11eb-be64-0242ac160002'::uuid, '99999999-9999-9999-9999-999999999999'::uuid, '2023-02-18 13:35:50.493', 1,0);
INSERT INTO "domain".menu_user_type (id, menu_id, user_type_id, created_by, created_date, status, view_index  ) VALUES('d963c07b-1427-4e78-8ca5-ad32fff91770'::uuid, '79e76bdb-c2b5-4608-9cea-ea2132f29e71'::uuid, '4fc58134-c10e-11eb-be64-0242ac160002'::uuid, '99999999-9999-9999-9999-999999999999'::uuid, '2023-02-18 13:35:50.493', 1,10);
INSERT INTO "domain".menu_user_type (id, menu_id, user_type_id, created_by, created_date, status, view_index  ) VALUES('d963c07c-1427-4e78-8ca5-ad32fff91770'::uuid, '18b80976-23a7-4726-9afc-ae44bd847306'::uuid, '4fc58134-c10e-11eb-be64-0242ac160002'::uuid, '99999999-9999-9999-9999-999999999999'::uuid, '2023-02-18 13:35:50.493', 1,20);
INSERT INTO "domain".menu_user_type (id, menu_id, user_type_id, created_date, created_by, status, last_modified_date, last_modified_by, view_index ) VALUES('d963c07f-1427-4e78-8ca5-ad32fff91770'::uuid, 'b48c9c74-e2c0-466d-8c7d-45257408324e'::uuid, '4fc58134-c10e-11eb-be64-0242ac160002'::uuid, '2023-02-18 13:35:50.493', '99999999-9999-9999-9999-999999999999'::uuid, 1, NULL, NULL,30);
INSERT INTO "domain".menu_user_type (id, menu_id, user_type_id, created_by, created_date, status, view_index  ) VALUES('d963c07d-1427-4e78-8ca5-ad32fff91770'::uuid, '6c998a8f-de0f-40a4-b154-e8d50494a96c'::uuid, '4fc58134-c10e-11eb-be64-0242ac160002'::uuid, '99999999-9999-9999-9999-999999999999'::uuid, '2023-02-18 13:35:50.493', 1,40);
INSERT INTO "domain".menu_user_type (id, menu_id, user_type_id, created_by, created_date, status, view_index  ) VALUES('d963c07e-1427-4e78-8ca5-ad32fff91770'::uuid, 'b48c9c74-e2c0-466d-8c7d-45257408324b'::uuid, '4fc58134-c10e-11eb-be64-0242ac160002'::uuid, '99999999-9999-9999-9999-999999999999'::uuid, '2023-02-18 13:35:50.493', 1,50);
INSERT INTO "domain".menu_user_type (id, menu_id, user_type_id, created_date, created_by, status, last_modified_date, last_modified_by, view_index) VALUES('d964c07e-1427-4e78-8ca5-ad32fff91770'::uuid, 'b47c9c74-e2c0-466d-8c7d-45257408324e'::uuid, '4fc58134-c10e-11eb-be64-0242ac160002'::uuid, '2023-02-18 13:35:50.493', '99999999-9999-9999-9999-999999999999'::uuid, 1, NULL, NULL, 35);
