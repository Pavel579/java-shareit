TRUNCATE TABLE requests RESTART IDENTITY cascade ;
TRUNCATE TABLE bookings RESTART IDENTITY cascade ;
TRUNCATE TABLE items RESTART IDENTITY cascade;
TRUNCATE TABLE users RESTART IDENTITY cascade;
TRUNCATE TABLE comments RESTART IDENTITY cascade;

create table if not exists users
(
    id    bigint GENERATED BY DEFAULT AS IDENTITY not null
        constraint users_pk
            primary key,
    name  varchar not null,
    email varchar unique not null
);

create table if not exists requests
(
    id    bigint GENERATED BY DEFAULT AS IDENTITY not null
        constraint requests_pk
            primary key,
    description  varchar,
    requester_id bigint not null,
    FOREIGN KEY (requester_id)
        REFERENCES users (id)
);


create table if not exists items
(
    id    bigint GENERATED BY DEFAULT AS IDENTITY not null
        constraint items_pk
            primary key,
    name        varchar not null,
    description varchar not null,
    is_available   boolean not null,
    owner_id       bigint  not null,
    request_id     bigint,
    FOREIGN KEY (owner_id)
        REFERENCES users (id)
    --FOREIGN KEY (request_id)
        --REFERENCES requests (id)
);

create table if not exists bookings
(
    id  bigint GENERATED BY DEFAULT AS IDENTITY not null
        constraint bookings_pk
            primary key,
    start_date     timestamp not null,
    end_date     timestamp not null,
    item_id   bigint    not null,
    booker_id bigint    not null,
    status    varchar   not null,
    FOREIGN KEY (item_id)
        REFERENCES items (id),
    FOREIGN KEY (booker_id)
        REFERENCES users (id)
);

create table if not exists comments
(
    id        bigint GENERATED BY DEFAULT AS IDENTITY not null
        constraint comments_pk
            primary key,
    text      varchar   not null,
    item_id   bigint    not null,
    author_id bigint    not null,
    created   timestamp not null,
        FOREIGN KEY (item_id)
        REFERENCES items (id),
    FOREIGN KEY (author_id)
        REFERENCES users (id)
);

