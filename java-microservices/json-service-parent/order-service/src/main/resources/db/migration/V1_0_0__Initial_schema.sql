create table ORDER_
(
    ID           UUID        not null,
    CUSTOMER_ID  UUID        not null,
    PRODUCT_ID   UUID        not null,
    QUANTITY     numeric(4)  not null,
    VERSION      numeric(4)  not null,
    CREATED_DATE date        not null,
    STATUS       varchar(16) not null,
    primary key (ID)
);

create table ORDER_LINE
(
    ID         UUID       not null,
    VERSION    numeric(4) not null,
    PRODUCT_ID UUID       not null,
    QUANTITY   numeric(4) not null,
    primary key (id)
);
