create table IF NOT EXISTS applications (
  id serial not null primary key,
  application_name varchar(64) not null,
  created_at timestamp not null
)