CREATE TABLE event (
    event_id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    state TEXT NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 1
);


CREATE TABLE market (
    market_id SERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL REFERENCES event (event_id) ON DELETE CASCADE,
    name TEXT NOT NULL,
    state TEXT NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 1
);

CREATE INDEX market_event_idx ON market (event_id);


CREATE TABLE selection (
    selection_id SERIAL PRIMARY KEY,
    market_id BIGINT NOT NULL REFERENCES market (market_id) ON DELETE CASCADE,
    index INTEGER NOT NULL,
    name TEXT NOT NULL,
    price NUMERIC,
    state TEXT NOT NULL,
    result TEXT,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 1,
    UNIQUE (market_id, index)
);

CREATE INDEX selection_market_idx ON selection (market_id);


CREATE TABLE bet (
    bet_id SERIAL PRIMARY KEY,
    bet_type TEXT NOT NULL,
    stake NUMERIC NOT NULL,
    max_return NUMERIC NOT NULL,
    return NUMERIC,
    state TEXT NOT NULL,
    player_id NUMERIC NOT NULL,
    stake_tx_ref TEXT,
    return_tx_refs TEXT,
    attempted TIMESTAMP,
    placed TIMESTAMP,
    settled TIMESTAMP,
    version INTEGER DEFAULT 1
);

CREATE INDEX bet_player_idx ON bet (player_id);


CREATE TABLE bet_leg (
    bet_id BIGINT NOT NULL REFERENCES bet (bet_id) ON DELETE CASCADE,
    index INTEGER NOT NULL,
    market_id BIGINT NOT NULL REFERENCES market (market_id),
    selection_id BIGINT NOT NULL REFERENCES selection (selection_id),
    price NUMERIC NOT NULL,
    result TEXT,
    PRIMARY KEY (bet_id, index)
);

CREATE INDEX bet_leg_market_idx ON bet_leg (market_id);
CREATE INDEX bet_leg_selection_idx ON bet_leg (selection_id);
