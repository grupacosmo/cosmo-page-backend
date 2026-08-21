-- Baseline schema generated from the JPA entity mappings.
-- Matches the schema previously created by Hibernate `ddl-auto: update`.

create table facebook_images (
    id varchar(255) not null,
    height integer,
    width integer,
    src varchar(250000),
    primary key (id)
);

create table images (
    id varchar(255) not null,
    name varchar(255),
    type varchar(255),
    data oid,
    primary key (id)
);

create table mail_history (
    id varchar(255) not null,
    recipient_email varchar(255),
    template_name varchar(255),
    timestamp timestamp(6) with time zone,
    primary key (id)
);

create table posts (
    id varchar(255) not null,
    description varchar(250000),
    provider_id varchar(255),
    title varchar(255),
    primary key (id)
);

create table posts_facebook_images (
    post_id varchar(255) not null,
    facebook_images_id varchar(255) not null unique
);

create table posts_images (
    post_id varchar(255) not null,
    images_id varchar(255) not null unique
);

create table token (
    id varchar(255) not null,
    added_by_id varchar(255),
    page_id varchar(255),
    validity_period varchar(255),
    "value" varchar(255),
    primary key (id)
);

create table users (
    id varchar(255) not null,
    creation_date timestamp(6),
    email varchar(255) unique,
    name varchar(255),
    role smallint check (role between 0 and 2),
    surname varchar(255),
    primary key (id)
);

alter table if exists posts_facebook_images
    add constraint fk_posts_facebook_images_facebook_image
    foreign key (facebook_images_id) references facebook_images;

alter table if exists posts_facebook_images
    add constraint fk_posts_facebook_images_post
    foreign key (post_id) references posts;

alter table if exists posts_images
    add constraint fk_posts_images_image
    foreign key (images_id) references images;

alter table if exists posts_images
    add constraint fk_posts_images_post
    foreign key (post_id) references posts;

alter table if exists token
    add constraint fk_token_added_by
    foreign key (added_by_id) references users;