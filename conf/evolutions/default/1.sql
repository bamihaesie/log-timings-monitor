# Users schema

# --- !Ups

CREATE TABLE log_entry (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    service_name varchar(255) NOT NULL,
    duration double NOT NULL,
    leg varchar(255) NOT NULL,
    server varchar(255) NOT NULL,
    created_on timestamp NOT NULL,
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE log_entry;