CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

SET search_path TO public;

CREATE TABLE IF NOT EXISTS headquarters (
    id uuid not null,
    galaxy_name varchar(50) not null,
    latitude double not null,
    longitude double not null,

    primary key (id)
);

CREATE TABLE IF NOT EXISTS rebel (
    id uuid not null,
    name varchar(100) not null,
    nick_name varchar(100) not null,
    birth_date date not null,
    age int,
    gender varchar(100) not null,
    headquarters_id uuid,
    active boolean default true,
    is_traitor boolean default false,
    service_started_at date not null,
    service_end_at date,

    primary key(id),

    constraint rebel_headquarters_fk
        foreign key (headquarters_id)
            references headquarters (id)
);

CREATE TABLE IF NOT EXISTS inventory (
    id uuid not null,
    rebel_soldier_id uuid not null,
    is_negotiable boolean default true,

    primary key(id),
    constraint inventory_rebel_soldier_fk
        foreign key (rebel_id)
            references rebel (id)
);

CREATE TABLE IF NOT EXISTS item (
    id uuid not null,
    type varchar(100) not null,
    amount int not null,
    inventory_id uuid not null,

    primary key (id),
    constraint item_inventory_fk
        foreign key (inventory_id)
            references inventory (id)
);

CREATE TABLE IF NOT EXISTS traitor_report(
    id uuid not null,
    rebel_soldier_id uuid not null,
    traitor_id uuid not null,

    primary key(id),
    constraint traitor_report_rebel_soldier_fk
        foreign key (rebel_soldier_id)
            references rebel(id)

    constraint traitor_report_rebel_soldier_traitor_fk
        foreign key (traitor_id)
            references rebel(id)
);