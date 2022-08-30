create table probe (
    id varchar(255) not null,
    direction varchar(255),
    name varchar(100) not null,
    positionx integer,
    positiony integer,
    status varchar(255),
    planet_id varchar(255),
    primary key (id)
);