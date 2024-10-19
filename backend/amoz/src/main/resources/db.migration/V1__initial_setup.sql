CREATE TABLE User (
                      UserId CHAR(21) PRIMARY KEY,
                      Name VARCHAR(30) NOT NULL,
                      Surname VARCHAR(30) NOT NULL,
                      Email VARCHAR(30) NOT NULL UNIQUE
);