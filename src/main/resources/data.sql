-- Datos de ejemplo para clientes
INSERT INTO client (nombre, apellido, dni, fecha_nacimiento) 
VALUES ('Juan', 'Perez', '11111111A', '1985-03-15');

INSERT INTO client (nombre, apellido, dni, fecha_nacimiento) 
VALUES ('Maria', 'Garcia', '22222222B', '1990-07-22');

INSERT INTO client (nombre, apellido, dni, fecha_nacimiento) 
VALUES ('Pedro', 'Rodriguez', '33333333C', '1978-12-05');

INSERT INTO client (nombre, apellido, dni, fecha_nacimiento) 
VALUES ('John', 'Doe', '12345678', '1990-01-01');


INSERT INTO ticket (cliente_id, evento, costo, fecha_vigencia) 
VALUES (1, 'Concierto de Rock', 5000.00, '2025-12-30');
INSERT INTO ticket (cliente_id, evento, costo, fecha_vigencia) 
VALUES (1, 'Festival del Choclo', 3000.00, '2025-11-30');
INSERT INTO ticket (cliente_id, evento, costo, fecha_vigencia) 
VALUES (2, 'Fiesta Electronica', 7500.00, '2025-07-15');