alter table planet
add constraint UK_dhelj2sd5e5spyo2flmdhxo6o
unique (name);

alter table planet
add constraint FK1v7ifgd2txc9mkk4rmrwrc5o9
foreign key (galaxy_id)
references galaxy (id);