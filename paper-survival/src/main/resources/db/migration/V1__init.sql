CREATE TABLE IF NOT EXISTS fcps_playerdata
(
    uuid             BLOB PRIMARY KEY,
    character_name   TEXT,
    character_height INTEGER
);

CREATE TABLE IF NOT EXISTS fcps_gatherings
(
    identifier   TEXT PRIMARY KEY,
    position_x   INTEGER NOT NULL,
    position_y   INTEGER NOT NULL,
    position_z   INTEGER NOT NULL,
    material     TEXT    NOT NULL,
    goal         INTEGER NOT NULL,
    collected    INTEGER NOT NULL DEFAULT 0,
    display_name TEXT
);