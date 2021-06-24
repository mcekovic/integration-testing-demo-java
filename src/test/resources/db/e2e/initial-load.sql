INSERT INTO event (event_id, name, state)
VALUES (1, 'Tour de France', 'O');

INSERT INTO market (market_id, event_id, name, state)
VALUES (1, 1, 'General Classification Winner', 'O');

INSERT INTO selection (selection_id, market_id, index, name, price, state)
VALUES
(1, 1, 1, 'Tadej Pogacar', 2.62, 'O'),
(2, 1, 2, 'Primoz Roglic', 2.87, 'O'),
(3, 1, 3, 'Geraint Thomas', 5.5, 'O');

ALTER SEQUENCE event_event_id_seq RESTART WITH 2;
ALTER SEQUENCE market_market_id_seq RESTART WITH 2;
ALTER SEQUENCE selection_selection_id_seq RESTART WITH 4;

COMMIT;