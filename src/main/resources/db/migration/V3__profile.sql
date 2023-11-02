CREATE TABLE domain.address (
                                id uuid NOT NULL,
                                created_date timestamp NULL,
                                created_by uuid NULL,
                                status int4 NULL,
                                last_modified_date timestamp NULL,
                                last_modified_by uuid NULL,
                                city varchar(100)  NULL,
                                country varchar(100)  NULL,
                                state varchar(100)  NULL,
                                postal_code varchar(10) NULL,
                                address_line1 varchar(250)  NULL,
                                address_line2 varchar(250)  NULL,
                                latitude float NULL,
                                longitude float NULL,
                                CONSTRAINT address_pkey PRIMARY KEY (id)
);
CREATE TABLE domain.user_profile (
                               id uuid NOT NULL,
                               created_date timestamp NULL,
                               created_by uuid NULL,
                               status int4 NULL,
                               last_modified_date timestamp NULL,
                               last_modified_by uuid NULL,
                               user_id uuid NOT NULL,
                               fullname varchar(255)  NULL,
                               birthday timestamp NULL,
                               photo varchar(500) NULL,
                               natId varchar(50)  NULL,
                               addressId uuid NULL,
                               gender varchar(10),
                               CONSTRAINT user_profile_pkey PRIMARY KEY (id),
                               CONSTRAINT fk_user_profile_user_id FOREIGN KEY (user_id) REFERENCES core.apl_user(id),
                               CONSTRAINT fk_user_profile_address_id FOREIGN KEY (addressId) REFERENCES domain.address(id)
);


