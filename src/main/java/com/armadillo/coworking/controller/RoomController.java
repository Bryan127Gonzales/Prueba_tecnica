package com.armadillo.coworking.controller;

import com.armadillo.coworking.dto.request.RoomRequest;
import com.armadillo.coworking.dto.response.RoomResponse;
import com.armadillo.coworking.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    //Crear sala
    @PostMapping
    public ResponseEntity<RoomResponse> create(@Valid @RequestBody RoomRequest request) {
        return new ResponseEntity<>(roomService.create(request), HttpStatus.CREATED);
    }

    //Listar todas
    @GetMapping
    public ResponseEntity<List<RoomResponse>> findAll(
            @RequestParam(required = false) Integer minCapacity,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String dateTime
    ) {
        return ResponseEntity.ok(
                roomService.findAll(minCapacity, status, dateTime)
        );
    }

    //Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.findById(id));
    }

    //Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.update(id, request));
    }

    //Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //Consultar disponibilidad
    @GetMapping("/{roomId}/availability")
    public ResponseEntity<Boolean> checkAvailability(
            @PathVariable Long roomId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end
    ) {
        return ResponseEntity.ok(
                roomService.checkAvailability(roomId, start, end)
        );
    }
}
