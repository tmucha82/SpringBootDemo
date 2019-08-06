CREATE PROCEDURE GET_SUM(IN first_num INT, IN second_num INT, OUT result INT, OUT rowCount INT)
BEGIN
    ATOMIC
    SET result = first_num + second_num;
    SET rowCount = 325;
END
/;


CREATE TABLE PNL
(
    id    INT         NOT NULL,
    title VARCHAR(50) NOT NULL,
    name  VARCHAR(20) NOT NULL
)
/;

INSERT INTO PNL VALUES (1, 'A', 'A1')/;
INSERT INTO PNL VALUES (2, 'B', 'B2')/;
INSERT INTO PNL VALUES (3, 'C', 'C3')/;
INSERT INTO PNL VALUES (4, 'D', 'D4')/;
INSERT INTO PNL VALUES (5, 'E', 'E5')/;

CREATE PROCEDURE FETCH_PNL()
    READS SQL DATA DYNAMIC RESULT SETS 1
BEGIN
    ATOMIC
    DECLARE pnl_cursor CURSOR FOR SELECT * FROM PNL;
    open pnl_cursor;
END
/;
