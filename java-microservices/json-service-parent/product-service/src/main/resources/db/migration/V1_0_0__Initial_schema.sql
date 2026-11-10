create table PRODUCT
(
    ID                UUID not null,
    NAME              varchar(32) not null,
    CODE              varchar(32) not null,
    EAN               varchar(32) not null,
    PRODUCER          varchar(32) not null,
    COUNTRY_OF_ORIGIN varchar(32) not null,
    VERSION           numeric(4)    not null,
    primary key (id)
);
