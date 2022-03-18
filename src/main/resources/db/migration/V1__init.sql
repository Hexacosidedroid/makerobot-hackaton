drop sequence if exists listing_seq;
create sequence listing_seq;
alter sequence listing_seq owner to postgres;

drop table if exists listing cascade;
create table listing
(
    id      bigint        not null
        constraint listing_pkey
            primary key,
    name    varchar(100)  not null,
    nominal numeric(8, 2) not null,
    prc     numeric(8, 2) not null,
    price   numeric(8, 2) not null,
    year    integer       not null
);

alter table listing
    owner to postgres;

INSERT INTO public.listing (id, name, nominal, prc, price, year)
VALUES (1, 'Облигация 1', 1000.00, 5.00, 900.00, 2025);
INSERT INTO public.listing (id, name, nominal, prc, price, year)
VALUES (2, 'Облигация 2', 1000.00, 6.00, 950.00, 2025);
INSERT INTO public.listing (id, name, nominal, prc, price, year)
VALUES (3, 'Облигация 3', 1000.00, 7.00, 1000.00, 2024);
INSERT INTO public.listing (id, name, nominal, prc, price, year)
VALUES (4, 'Облигация 4', 1000.00, 8.00, 1050.00, 2024);
INSERT INTO public.listing (id, name, nominal, prc, price, year)
VALUES (5, 'Облигация 5', 1000.00, 6.50, 950.00, 2023);
INSERT INTO public.listing (id, name, nominal, prc, price, year)
VALUES (6, 'Облигация 6', 1000.00, 10.00, 1200.00, 2023);
INSERT INTO public.listing (id, name, nominal, prc, price, year)
VALUES (7, 'Облигация 7', 1000.00, 3.00, 800.00, 2023);
INSERT INTO public.listing (id, name, nominal, prc, price, year)
VALUES (8, 'Облигация 8', 1000.00, 5.50, 900.00, 2022);
INSERT INTO public.listing (id, name, nominal, prc, price, year)
VALUES (9, 'Облигация 9', 1000.00, 2.00, 700.00, 2022);
INSERT INTO public.listing (id, name, nominal, prc, price, year)
VALUES (10, 'Облигация 10', 1000.00, 5.00, 900.00, 2022);

drop sequence if exists oper_seq;
create sequence oper_seq;
alter sequence oper_seq owner to postgres;

drop table if exists operation cascade;
create table operation
(
    id         bigint         not null
        constraint operation_pkey
            primary key,
    amount     integer        not null,
    summa      numeric(12, 2) not null,
    type       varchar(5)     not null
        check ( type in ('BUY', 'SELL', 'PRC', 'RED') ),
    listing_id bigint
        constraint listing_fk1
            references listing,
    year       integer        not null
);

alter table operation  owner to postgres;

drop table if exists account cascade;
create table account
(
    id    bigint         not null
        constraint account_pkey
            primary key,
    name  varchar(50)    not null,
    summa numeric(12, 2) not null,
    divprc   integer not null
);

alter table account owner to postgres;

INSERT INTO public.account (id, name, summa, divprc)
VALUES (1, 'Мой счет', 0.00, 25);

drop sequence if exists portf_seq;
create sequence portf_seq;
alter sequence portf_seq owner to postgres;

drop table if exists portfolio cascade;
create table portfolio
(
    id         bigint         not null
        constraint portfolio_pkey
            primary key,
    amount     integer,
    avgprice   numeric(8, 2)  not null,
    summa      numeric(12, 2) not null,
    account_id bigint         not null
        constraint fk1_portf references account,
    listing_id bigint         not null
        constraint fk2_portf references listing
);

alter table portfolio  owner to postgres;

