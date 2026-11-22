package cl.ipss.SaborGourmet.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.ipss.SaborGourmet.model.Mesa;
import cl.ipss.SaborGourmet.service.MesaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {

    private final MesaService mesaService;

    // Inyeccion de dependencia por constructor
    public MesaController(MesaService mesaService) {
        this.mesaService = mesaService;
    }

    // Metodo GET para listar todas las mesas
    @GetMapping
    public ResponseEntity<List<Mesa>> listarMesas() {
        List<Mesa> mesas = mesaService.obtenerTodos();
        return ResponseEntity.ok(mesas);
    }

    // Metodo GET para buscar una mesa por numero
    @GetMapping("/{numeroMesa}")
    public ResponseEntity<?> obtenerMesa(@PathVariable int numeroMesa) {
        try {
            Mesa mesa = mesaService.buscarNumeroMesa(numeroMesa);
            return ResponseEntity.ok(mesa);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // Metodo POST para crear una mesa
    @PostMapping
    public ResponseEntity<?> crearMesa(@Valid @RequestBody Mesa mesa) {
        try {
            Mesa nuevaMesa = mesaService.crearMesa(mesa);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(nuevaMesa);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    // Metodo PUT para actualizar una mesa
    @PutMapping("/{numeroMesa}")
    public ResponseEntity<?> actualizarMesa(
            @PathVariable int numeroMesa,
            @Valid @RequestBody Mesa mesa) {

        try {
            Mesa mesaActualizada =
                    mesaService.actualizarMesa(numeroMesa, mesa);

            return ResponseEntity.ok(mesaActualizada);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // Metodo DELETE para eliminar una mesa
    @DeleteMapping("/{numeroMesa}")
    public ResponseEntity<?> eliminarMesa(@PathVariable int numeroMesa) {
        try {
            mesaService.eliminarMesa(numeroMesa);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}