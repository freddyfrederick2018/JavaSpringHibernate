CREATE TABLE reply (
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    name VARCHAR(255) NOT NULL,
    body VARCHAR(255) NOT NULL,
    ticket_id INTEGER DEFAULT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (ticket_id) REFERENCES ticket(id)
);



insert into reply(name, body, ticket_id) VALUES ('fred', 'Ohayo gozaimasu.', 4);
insert into reply(name, body, ticket_id) VALUES ('keith', 'Ohayo gozaimasu.', 4);
insert into reply(name, body, ticket_id) VALUES ('fred', 'Ohayo gozaimasu.', 1);
insert into reply(name, body, ticket_id) VALUES ('fred', 'Ohayo gozaimasu.', 2);