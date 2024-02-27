CREATE TABLE artists
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY,
    nickname VARCHAR(64),
    PRIMARY KEY (id)
);

CREATE TABLE play_lists
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY,
    user_id BIGINT,
    PRIMARY KEY (id)
);

CREATE TABLE play_lists_music
(
    play_list_id BIGINT NOT NULL,
    song_id      BIGINT NOT NULL,
    PRIMARY KEY (play_list_id, song_id)
);

CREATE TABLE songs
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY,
    genre     VARCHAR(64),
    title     VARCHAR(64),
    url       TEXT,
    artist_id BIGINT,
    PRIMARY KEY (id)
);

CREATE TABLE user_roles
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY,
    role_name VARCHAR(20),
    PRIMARY KEY (id)
);

CREATE TABLE user_roles_users
(
    role_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE users
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY,
    email    VARCHAR(255),
    nickname VARCHAR(38),
    password TEXT,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS play_lists ADD CONSTRAINT play_lists_user_fk FOREIGN KEY (user_id) REFERENCES users;

ALTER TABLE IF EXISTS play_lists_music ADD CONSTRAINT play_lists_music_song_fk FOREIGN KEY (song_id) REFERENCES songs;

ALTER TABLE IF EXISTS play_lists_music ADD CONSTRAINT play_lists_music_play_list_fk FOREIGN KEY (play_list_id) REFERENCES play_lists;

ALTER TABLE IF EXISTS songs ADD CONSTRAINT songs_artist_fk FOREIGN KEY (artist_id) REFERENCES artists;

ALTER TABLE IF EXISTS user_roles_users ADD CONSTRAINT user_roles_user_fk FOREIGN KEY (user_id) REFERENCES users;

ALTER TABLE IF EXISTS user_roles_users ADD CONSTRAINT user_roles_role_fk FOREIGN KEY (role_id) REFERENCES user_roles;