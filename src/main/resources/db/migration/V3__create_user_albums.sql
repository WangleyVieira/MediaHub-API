CREATE TABLE user_albums (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    album_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_album_user
        FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_album_album
        FOREIGN KEY (album_id)
        REFERENCES albums (id)
        ON DELETE CASCADE,

    CONSTRAINT unique_user_album UNIQUE (user_id, album_id)
)