-- Test data for H2 in-memory database
-- This will only be used during testing

-- Data for Client table
INSERT INTO client (nombre, apellido, dni, fecha_nacimiento) 
VALUES ('Test', 'User', '12345678Z', '1990-01-01');

INSERT INTO client (nombre, apellido, dni, fecha_nacimiento) 
VALUES ('Jane', 'Smith', '87654321X', '1985-06-15');