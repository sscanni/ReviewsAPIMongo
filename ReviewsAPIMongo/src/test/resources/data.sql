INSERT INTO product(prodid, name) VALUES (1,'Dell XPS Desktop Computer');
INSERT INTO product(prodid, name) VALUES (2,'Dell XPS 13 Laptop Computer');
INSERT INTO product(prodid, name) VALUES (3,'Lenovo Laptop Computer');
INSERT INTO product(prodid, name) VALUES (4,'Dell 27 inch Monitor');
INSERT INTO product(prodid, name) VALUES (5,'HP Laptop');

INSERT INTO reviews(reviewid, prodid) VALUES (1,1);

INSERT INTO comments(id, reviewid, comment) VALUES (1,1, 'This is a new review inserted from sql.');
INSERT INTO comments(id, reviewid, comment) VALUES (2,1, 'This is the second comment for review 1.');

