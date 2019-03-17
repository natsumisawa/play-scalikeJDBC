create table IF NOT EXISTS interviews (
  id serial not null primary key,
  application_id BIGINT UNSIGNED not null,
  created_at timestamp not null,
  FOREIGN KEY (application_id)
  REFERENCES applications(id)
)