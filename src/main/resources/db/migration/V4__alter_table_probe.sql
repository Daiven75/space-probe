alter table probe
add constraint UK_ih3ehy9imwc01yrphyd4dwwvn
unique (name);

alter table probe
add constraint FKbvjy9i62mr11ymmqt07v7uk7q
foreign key (planet_id)
references planet (id);