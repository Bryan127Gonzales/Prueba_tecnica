package com.armadillo.coworking.service;


import com.armadillo.coworking.dto.request.RoomRequest;
import com.armadillo.coworking.dto.response.RoomResponse;
import com.armadillo.coworking.exception.ResourceNotFoundException;
import com.armadillo.coworking.model.entity.Room;
import com.armadillo.coworking.model.enums.RoomStatus;
import com.armadillo.coworking.repository.IBookingRepository;
import com.armadillo.coworking.repository.IRoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    private final IRoomRepository roomRepository;
    private final IBookingRepository bookingRepository;

    public RoomService(IRoomRepository roomRepository,IBookingRepository bookingRepository) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    //Crear sala
    public RoomResponse create(RoomRequest request) {

        Room room = new Room();
        room.setName(request.getName());
        room.setCapacity(request.getCapacity());
        room.setLocation(request.getLocation());
        room.setEquipment(request.getEquipment());
        room.setStatus(RoomStatus.valueOf(request.getStatus()));

        Room saved = roomRepository.save(room);
        return mapToResponse(saved);
    }

    //Listar con filtros opcionales
    public List<RoomResponse> findAll(
            Integer minCapacity,
            String status,
            String dateTime
    ) {

        List<Room> rooms = roomRepository.findAll();

        // Filtro por capacidad mínima
        if (minCapacity != null) {
            rooms = rooms.stream()
                    .filter(r -> r.getCapacity() >= minCapacity)
                    .toList();
        }

        // Filtro por estado
        if (status != null && !status.isBlank()) {
            RoomStatus roomStatus = RoomStatus.valueOf(status.toUpperCase());

            rooms = rooms.stream()
                    .filter(r -> r.getStatus() == roomStatus)
                    .toList();
        }
        

        return rooms.stream()
                .map(this::mapToResponse)
                .toList();
    }

    //Consultar disponibilidad
    public boolean checkAvailability(
            Long roomId,
            LocalDateTime start,
            LocalDateTime end
    ) {
        // Verificar que la sala exista
        roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada"));

        // Si hay cruce → NO disponible
        boolean hasConflict = bookingRepository
                .existsOverlappingBooking(roomId, start, end);

        return !hasConflict;
    }


    //Buscar por ID
    public RoomResponse findById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada"));

        return mapToResponse(room);
    }

    //Actualizar
    public RoomResponse update(Long id, RoomRequest request) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada"));

        room.setName(request.getName());
        room.setCapacity(request.getCapacity());
        room.setLocation(request.getLocation());
        room.setEquipment(request.getEquipment());
        room.setStatus(RoomStatus.valueOf(request.getStatus()));

        return mapToResponse(roomRepository.save(room));
    }

    //Eliminar
    public void delete(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada"));

        roomRepository.delete(room);
    }

    // Mapper
    private RoomResponse mapToResponse(Room room) {
        RoomResponse response = new RoomResponse();
        response.setId(room.getId());
        response.setName(room.getName());
        response.setCapacity(room.getCapacity());
        response.setLocation(room.getLocation());
        response.setEquipment(room.getEquipment());
        response.setStatus(room.getStatus().name());
        return response;
    }
}
