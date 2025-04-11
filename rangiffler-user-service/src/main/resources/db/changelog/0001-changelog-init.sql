create table if not exists `users`
(
    id                      varchar(50) PRIMARY KEY,
    username                varchar(50) unique not null,
    first_name              varchar(255),
    last_name               varchar(255),
    avatar                  longblob,
    country_code            varchar(255)
);

create table if not exists `friendship`
(
    requester_id varchar(255) not null,
    addressee_id varchar(255) not null,
    created_date datetime not null,
    status varchar(50) not null,
    primary key (requester_id, addressee_id),
    constraint friend_are_distinct_ck check (requester_id <> addressee_id),
    constraint fk_requester_id foreign key (requester_id) references `users` (id),
    constraint fk_addressee_id foreign key (addressee_id) references `users` (id)
);