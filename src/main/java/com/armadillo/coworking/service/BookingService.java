package com.armadillo.coworking.service;

import com.armadillo.coworking.dto.request.BookingRequest;
import com.armadillo.coworking.dto.response.BookingResponse;
import com.armadillo.coworking.exception.ConflictException;
import com.armadillo.coworking.exception.ResourceNotFoundException;
import com.armadillo.coworking.model.entity.Booking;
import com.armadillo.coworking.model.entity.Room;
import com.armadillo.coworking.repository.IBookingRepository;
import com.armadillo.coworking.repository.IRoomRepository;

import org.springframework.stereotype.Service;
import com.armadillo.coworking.exception.BadRequestException;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class BookingService {
    private final IBookingRepository bookingRepository;
    private final IRoomRepository roomRepository;

    public BookingService(IBookingRepository bookingRepository,
                          IRoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    //Crear reserva
    public BookingResponse create(BookingRequest request) {
        //Verificar que la sala exista
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada"));
        //Validar que la fecha fin sea posterior a inicio
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new BadRequestException("La fecha de fin debe ser posterior a la de inicio");
        }

        //No permitir reservas en el pasado
        if (request.getStartTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("No se pueden crear reservas en el pasado");
        }

        //Duración mínima 30 minutos
        long minutos = Duration.between(
                request.getStartTime(),
                request.getEndTime()
        ).toMinutes();

        if (minutos < 30) {
            throw new BadRequestException("La reserva debe durar al menos 30 minutos");
        }

        //Validación de horario
        if (bookingRepository.existsOverlappingBooking(
                room.getId(),
                request.getStartTime(),
                request.getEndTime()
        )) {
            throw new ConflictException("La sala ya está reservada en ese horario");
        }

        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setResponsibleName(request.getResponsibleName());
        booking.setContactEmail(request.getContactEmail());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setPurpose(request.getPurpose());

        Booking saved = bookingRepository.save(booking);
        return mapToResponse(saved);
    }

    //Listar todas
    public List<BookingResponse> findAll(
            Long roomId,
            String responsible,
            String date
    ) {

        List<Booking> bookings = bookingRepository.findAll();

        // 🔹 Filtro por sala
        if (roomId != null) {
            bookings = bookings.stream()
                    .filter(b -> b.getRoom().getId().equals(roomId))
                    .toList();
        }

        // 🔹 Filtro por responsable
        if (responsible != null && !responsible.isBlank()) {
            bookings = bookings.stream()
                    .filter(b ->
                            b.getResponsibleName() != null &&
                                    b.getResponsibleName().toLowerCase()
                                            .contains(responsible.toLowerCase())
                    )
                    .toList();
        }

        // 🔹 Filtro por fecha (YYYY-MM-DD)
        if (date != null && !date.isBlank()) {
            LocalDate filterDate = LocalDate.parse(date);

            bookings = bookings.stream()
                    .filter(b ->
                            b.getStartTime().toLocalDate().equals(filterDate)
                    )
                    .toList();
        }

        return bookings.stream()
                .map(this::mapToResponse)
                .toList();
    }

    //Buscar por ID
    public BookingResponse findById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        return mapToResponse(booking);
    }

    //Eliminar
    public void delete(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        bookingRepository.delete(booking);
    }

    //Mapper
    private BookingResponse mapToResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setRoomId(booking.getRoom().getId());
        response.setRoomName(booking.getRoom().getName());
        response.setResponsibleName(booking.getResponsibleName());
        response.setContactEmail(booking.getContactEmail());
        response.setStartTime(booking.getStartTime());
        response.setEndTime(booking.getEndTime());
        response.setPurpose(booking.getPurpose());
        return response;
    }
}
