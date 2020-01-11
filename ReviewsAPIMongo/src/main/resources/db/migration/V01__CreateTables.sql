CREATE TABLE `product` (
  `prodid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`prodid`),
  UNIQUE KEY `prodid_UNIQUE` (`prodid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `reviews` (
  `reviewid` int(11) NOT NULL AUTO_INCREMENT,
  `prodid` int(11) NOT NULL,
  PRIMARY KEY (`reviewid`),
  UNIQUE KEY `id_UNIQUE` (`reviewid`),
  CONSTRAINT `prodid` FOREIGN KEY (`prodid`) REFERENCES `product` (`prodid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `reviewid` int(11) NOT NULL,
  `comment` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  CONSTRAINT `reviewid` FOREIGN KEY (`reviewid`) REFERENCES `reviews` (`reviewid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO product(prodid, name) VALUES (1,"Dell XPS Desktop Computer");
INSERT INTO product(prodid, name) VALUES (2,"Dell XPS 13 Laptop Computer");
INSERT INTO product(prodid, name) VALUES (3,"Lenovo Laptop Computer");
INSERT INTO product(prodid, name) VALUES (4,"Dell 27 inch Monitor");
INSERT INTO product(prodid, name) VALUES (5,"HP Laptop");

