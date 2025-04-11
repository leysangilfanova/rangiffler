
create table if not exists "user"
(
    id                      UUID        not null default RANDOM_UUID() primary key,
    username                varchar(50) unique not null,
    password                varchar(255)       not null,
    enabled                 boolean            not null,
    account_non_expired     boolean            not null,
    account_non_locked      boolean            not null,
    credentials_non_expired boolean            not null
);

create table if not exists "authority"
(
    id        UUID not null default RANDOM_UUID() primary key,
    user_id   UUID        not null,
    authority varchar(50) not null,
    constraint fk_authority_user foreign key (user_id) references "user" (id)
);

create unique index if not exists ix_auth_username on "authority" (user_id, authority);