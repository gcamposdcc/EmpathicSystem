CREATE TABLE empathy_sources
(
  id serial NOT NULL,
  source_name character varying(50) NOT NULL,
  description character varying(150) NOT NULL,
  CONSTRAINT pkey_empathy_sources_id PRIMARY KEY (id)
)
CREATE TABLE empathy_types
(
  id serial NOT NULL,
  type_name character varying(50) NOT NULL,
  description character varying(150) NOT NULL,
  CONSTRAINT pkey_empathy_types_id PRIMARY KEY (id)
)

CREATE TABLE empathy
(
  id serial NOT NULL,
  created_at timestamp without time zone NOT NULL,
  idsource integer NOT NULL DEFAULT 1,
  idtype integer NOT NULL DEFAULT 1,
  evaluated boolean NOT NULL DEFAULT false,
  answered boolean NOT NULL DEFAULT false,
  liked boolean NOT NULL DEFAULT false,
  CONSTRAINT pkey_empathy_id PRIMARY KEY (id),
  CONSTRAINT chk_answered_evaluated CHECK (evaluated OR NOT evaluated AND NOT answered)
)
CREATE TABLE empathy_info_message
(
  idempathy integer NOT NULL DEFAULT 0,
  message character varying(150) NOT NULL,
  uses_text boolean NOT NULL DEFAULT true,
  uses_audio boolean NOT NULL DEFAULT false,
  uses_image boolean NOT NULL DEFAULT false,
  uses_video boolean NOT NULL DEFAULT false,
  CONSTRAINT fkey_empathy_info_message_idempathy FOREIGN KEY (idempathy)
      REFERENCES empathy (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
CREATE TABLE empathy_info_sagde
(
  idempathy integer NOT NULL DEFAULT 0,
  idsector integer NOT NULL DEFAULT 0,
  ideje integer NOT NULL DEFAULT 0,
  idcmo integer NOT NULL DEFAULT 0,
  idobjcon integer NOT NULL DEFAULT 0,
  idnivel integer NOT NULL DEFAULT 0,
  CONSTRAINT fkey_empinfosagde_emp_id FOREIGN KEY (idempathy)
      REFERENCES empathy (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
CREATE TABLE empathy_info_sagdeuser
(
  idestablishment integer NOT NULL DEFAULT 0,
  idcourse integer NOT NULL DEFAULT 0,
  iduser integer NOT NULL DEFAULT 0,
  idempathy integer NOT NULL DEFAULT 0,
  CONSTRAINT fkey_info_sagdeuser_idempathy FOREIGN KEY (idempathy)
      REFERENCES empathy (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT unique_info_sagdeuser_idempathy UNIQUE (idempathy)
)
;