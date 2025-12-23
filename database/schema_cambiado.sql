
-- Crear la base de datos
CREATE DATABASE armadillo_db;

CREATE TABLE rooms (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    capacity INTEGER NOT NULL,
    location VARCHAR(100),
    equipment TEXT,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE bookings (
    id SERIAL PRIMARY KEY,
    room_id INTEGER NOT NULL,
    responsible_name VARCHAR(100) NOT NULL,
    contact_email VARCHAR(100) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    purpose TEXT,

    CONSTRAINT fk_booking_room
        FOREIGN KEY (room_id)
        REFERENCES rooms(id)
);

-- Datos de ejemplo
INSERT INTO rooms (name, capacity, location, equipment, status) VALUES
('Sala Innovación', 10, 'Piso 1', 'Proyector, Pizarra', 'DISPONIBLE'),
('Sala Creatividad', 6, 'Piso 2', 'TV, Pizarra', 'DISPONIBLE');

Select * from rooms;
Select * from bookings;
