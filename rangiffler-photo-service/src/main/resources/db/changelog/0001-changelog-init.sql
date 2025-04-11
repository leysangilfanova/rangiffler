create table if not exists `photo`
(
    id                      UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    user_id                 varchar(255)        not null,
    country_code            varchar(255)        not null,
    description             varchar(255),
    src                     longblob           not null,
    creation_date            datetime not null
);

create table if not exists `likes`
(
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    user_id                 varchar(255)        not null,
    photo_id                varchar(255)        not null,
    creation_date            datetime not null,
    foreign key (photo_id) references `photo` (id)
);