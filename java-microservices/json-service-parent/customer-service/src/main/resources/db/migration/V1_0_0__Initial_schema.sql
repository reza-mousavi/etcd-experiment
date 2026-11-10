create table CUSTOMER (
  ID UUID not null,
  NAME varchar(32) not null,
  FAMILY varchar(32) not null,
  EMAIL varchar(32) not null,
  CELLPHONE varchar(32),
  VERSION numeric(4) not null,
  primary key (id)
);
