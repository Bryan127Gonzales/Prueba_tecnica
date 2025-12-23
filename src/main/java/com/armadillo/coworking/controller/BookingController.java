package com.armadillo.coworking.controller;

import com.armadillo.coworking.dto.request.BookingRequest;
import com.armadillo.coworking.dto.response.BookingResponse;
import com.armadillo.coworking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    //Crear reserva
    @PostMapping
    public ResponseEntity<BookingResponse> create(
            @Valid @RequestBody BookingRequest request) {

        return new ResponseEntity<>(
                bookingService.create(request),
                HttpStatus.CREATED
        );
    }

    //Listar todas las reservas
    @GetMapping
    public ResponseEntity<List<BookingResponse>> findAll(
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) String responsible,
            @RequestParam(required = false) String date
    ) {
        return ResponseEntity.ok(
                bookingService.findAll(roomId, responsible, date)
        );
    }

    //Obtener reserva por ID
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.findById(id));
    }

    //Cancelar reserva
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
