package cl.ipss.SaborGourmet.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.ipss.SaborGourmet.model.Mesa;
import cl.ipss.SaborGourmet.model.Reserva;
import cl.ipss.SaborGourmet.service.ReservaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    // Inyeccion de dependencia por constructor
    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    // GET para listar todas las reservas
    @GetMapping
    public ResponseEntity<List<Reserva>> listarReservas() {
        return ResponseEntity.ok(reservaService.obtenerTodos());
    }

    // GET para buscar reserva por id
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerReserva(@PathVariable int id) {
        try {
            return ResponseEntity.ok(
                    reservaService.buscarIdReserva(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // POST para crear una reserva
    @PostMapping
    public ResponseEntity<?> crearReserva(
            @Valid @RequestBody Reserva reserva) {

        Reserva nueva = reservaService.crearReserva(reserva);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(nueva);
    }

    // PUT para modificar una reserva
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarReserva(
            @PathVariable int id,
            @Valid @RequestBody Reserva reserva) {

        return ResponseEntity.ok(
                reservaService.actualizarReserva(id, reserva));
    }

    // PATCH para cancelar una reserva
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarReserva(@PathVariable int id) {

        return ResponseEntity.ok(
                reservaService.cancelarReserva(id));
    }

    // GET para consultar mesas disponibles
    @GetMapping("/disponibles")
    public ResponseEntity<List<Mesa>> mesasDisponibles(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dia,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime hora,

            @RequestParam int personas) {

        return ResponseEntity.ok(
                reservaService.consultarDisponibles(
                        dia, hora, personas));
    }

    // Manejo de errores: reserva no encontrada
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> noEncontrada(
            NoSuchElementException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    // Manejo de errores: datos incorrectos
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> datosInvalidos(
            IllegalArgumentException e) {

        return ResponseEntity.badRequest().body(e.getMessage());
    }

    // Manejo de errores: conflicto de reservas
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> conflicto(
            IllegalStateException e) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }
}