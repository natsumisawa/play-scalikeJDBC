create table applications (
  id serial not null primary key,
  name nvarchar(64) not null,
  created_at timestamp not null
)